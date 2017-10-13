package com.squareup.javapoet;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.squareup.javapoet.Util.checkArgument;
import static com.squareup.javapoet.Util.checkNotNull;

public class JavaFileTmp {
    private static final Appendable NULL_APPENDABLE = new Appendable() {
        @Override
        public Appendable append(CharSequence charSequence) {
            return this;
        }

        @Override
        public Appendable append(CharSequence charSequence, int start, int end) {
            return this;
        }

        @Override
        public Appendable append(char c) {
            return this;
        }
    };

    public final CodeBlock fileComment;
    public final String packageName;
    public final TypeSpec typeSpec;
    public final boolean skipJavaLangImports;
    private final String indent;

    private JavaFileTmp(Builder builder) {
        this.fileComment = builder.fileComment.build();
        this.packageName = builder.packageName;
        this.typeSpec = builder.typeSpec;
        this.skipJavaLangImports = builder.skipJavaLangImports;
        this.indent = builder.indent;
    }

    public void writeTo(Appendable out) throws IOException {
        // First pass: emit the entire class, just to collect the types we'll need to import.
        CodeWriter importsCollector = new CodeWriter(NULL_APPENDABLE, indent);
        emit(importsCollector);
        Map<ClassName, String> suggestedImports = importsCollector.suggestedImports();

        // Second pass: write the code, taking advantage of the imports.
        CodeWriter codeWriter = new CodeWriter(out, indent, suggestedImports);
        emit(codeWriter);
    }

    /**
     * Writes this to {@code directory} the standard directory structure.
     */
    public void writeTo(Path directory) throws IOException {
        checkArgument(Files.notExists(directory) || Files.isDirectory(directory), "path %s exists but is not a directory.", directory);
        Path outputDirectory = directory;
        if (!packageName.isEmpty()) {
            for (String packageComponent : packageName.split("\\.")) {
                outputDirectory = outputDirectory.resolve(packageComponent);
            }
            Files.createDirectories(outputDirectory);
        }

        Path outputPath = outputDirectory.resolve(typeSpec.name + ".java");
        try (Writer writer = new OutputStreamWriter(Files.newOutputStream(outputPath))) {
            writeTo(writer);
        }
    }

    /**
     * Writes this to {@code directory} the standard directory structure.
     */
    public void writeTo(Path directory, String encoding) throws IOException {
        checkArgument(Files.notExists(directory) || Files.isDirectory(directory), "path %s exists but is not a directory.", directory);
        Path outputDirectory = directory;
        if (!packageName.isEmpty()) {
            for (String packageComponent : packageName.split("\\.")) {
                outputDirectory = outputDirectory.resolve(packageComponent);
            }
            Files.createDirectories(outputDirectory);
        }

        Path outputPath = outputDirectory.resolve(typeSpec.name + ".java");
        try (Writer writer = new OutputStreamWriter(Files.newOutputStream(outputPath), encoding)) {
            writeTo(writer);
        }
    }

    /**
     * Writes this to {@code directory} the standard directory structure.
     */
    public void writeTo(File directory, String encoding) throws IOException {
        writeTo(directory.toPath(), encoding);
    }

    /**
     * Writes this to {@code directory} the standard directory structure.
     */
    public void writeTo(File directory) throws IOException {
        writeTo(directory.toPath());
    }

    /**
     * Writes this to {@code filer}.
     */
    public void writeTo(Filer filer) throws IOException {
        String fileName = packageName.isEmpty() ? typeSpec.name : packageName + "." + typeSpec.name;
        List<Element> originatingElements = typeSpec.originatingElements;
        JavaFileObject filerSourceFile = filer.createSourceFile(fileName, originatingElements.toArray(new Element[originatingElements.size()]));
        try (Writer writer = filerSourceFile.openWriter()) {
            writeTo(writer);
        } catch (Exception e) {
            try {
                filerSourceFile.delete();
            } catch (Exception ignored) {
            }
            throw e;
        }
    }

    private void emit(CodeWriter codeWriter) throws IOException {
        codeWriter.pushPackage(packageName);

        if (!fileComment.isEmpty()) {
            codeWriter.emitComment(fileComment);
        }

        if (!packageName.isEmpty()) {
            codeWriter.emit("package $L;\n", packageName);
            codeWriter.emit("\n");
        }

        int importedTypesCount = 0;
        for (ClassName className : codeWriter.importedTypes().keySet()) {
            if (skipJavaLangImports && className.packageName().equals("java.lang")) continue;
            codeWriter.emit("import $L;\n", className);
            importedTypesCount++;
        }

        if (importedTypesCount > 0) {
            codeWriter.emit("\n");
        }

        typeSpec.emit(codeWriter, null, Collections.<Modifier>emptySet());

        codeWriter.popPackage();
    }

    @Override
    public String toString() {
        try {
            StringBuilder result = new StringBuilder();
            writeTo(result);
            return result.toString();
        } catch (IOException e) {
            throw new AssertionError();
        }
    }

    public JavaFileObject toJavaFileObject() {
        URI uri = URI.create((packageName.isEmpty() ? typeSpec.name : packageName.replace('.', '/') + '/' + typeSpec.name) + Kind.SOURCE.extension);
        return new SimpleJavaFileObject(uri, Kind.SOURCE) {
            private final long lastModified = System.currentTimeMillis();

            @Override
            public String getCharContent(boolean ignoreEncodingErrors) {
                return JavaFileTmp.this.toString();
            }

            @Override
            public InputStream openInputStream() throws IOException {
                return new ByteArrayInputStream(getCharContent(true).getBytes());
            }

            @Override
            public long getLastModified() {
                return lastModified;
            }
        };
    }

    public static Builder builder(String packageName, TypeSpec typeSpec) {
        checkNotNull(packageName, "packageName == null");
        checkNotNull(typeSpec, "typeSpec == null");
        return new Builder(packageName, typeSpec);
    }

    public Builder toBuilder() {
        Builder builder = new Builder(packageName, typeSpec);
        builder.fileComment.add(fileComment);
        builder.skipJavaLangImports = skipJavaLangImports;
        builder.indent = indent;
        return builder;
    }

    public static final class Builder {
        private final String packageName;
        private final TypeSpec typeSpec;
        private CodeBlock.Builder fileComment = CodeBlock.builder();
        private boolean skipJavaLangImports;
        private String indent = "  ";

        private Builder(String packageName, TypeSpec typeSpec) {
            this.packageName = packageName;
            this.typeSpec = typeSpec;
        }

        public Builder addFileComment(String format, Object... args) {
            this.fileComment.add(format, args);
            return this;
        }

        /**
         * Call this to omit imports for classes in {@code java.lang}, such as {@code java.lang.String}.
         * <p>
         * <p>
         * By default, JavaPoet explicitly imports types in {@code java.lang} to defend against naming conflicts. Suppose an (ill-advised) class is named
         * {@code com.example.String}. When {@code java.lang} imports are skipped, generated code in {@code com.example} that references
         * {@code java.lang.String} will get {@code com.example.String} instead.
         */
        public Builder skipJavaLangImports(boolean skipJavaLangImports) {
            this.skipJavaLangImports = skipJavaLangImports;
            return this;
        }

        public Builder indent(String indent) {
            this.indent = indent;
            return this;
        }

        public JavaFileTmp build() {
            return new JavaFileTmp(this);
        }
    }
}
