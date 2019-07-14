package com.squareup.javapoet;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.squareup.javapoet.Util.*;

public class CodeBlockNode {
    private final List<FieldSpec> fieldSpecs = new ArrayList<>();
    private final CodeBlock.Builder staticBlock = CodeBlock.builder();
    private TypeSpec.Kind kind;
    private String name;

    public void init(TypeSpec.Kind kind, String name) {
        this.kind = kind;
        this.name = name;
    }

    public CodeBlockNode addFields(Iterable<FieldSpec> fieldSpecs) {
        checkArgument(fieldSpecs != null, "fieldSpecs == null");
        for (FieldSpec fieldSpec : fieldSpecs) {
            addField(fieldSpec);
        }
        return this;
    }

    public CodeBlockNode addField(FieldSpec fieldSpec) {
        checkState(kind != TypeSpec.Kind.ANNOTATION, "%s %s cannot have fields", kind, name);
        if (kind == TypeSpec.Kind.INTERFACE) {
            requireExactlyOneOf(fieldSpec.modifiers, Modifier.PUBLIC, Modifier.PRIVATE);
            Set<Modifier> check = EnumSet.of(Modifier.STATIC, Modifier.FINAL);
            checkState(fieldSpec.modifiers.containsAll(check), "%s %s.%s requires modifiers %s",
                    kind, name, fieldSpec.name, check);
        }
        fieldSpecs.add(fieldSpec);
        return this;
    }

    public CodeBlockNode addField(TypeName type, String name, Modifier... modifiers) {
        return addField(FieldSpec.builder(type, name, modifiers).build());
    }

    public CodeBlockNode addField(Type type, String name, Modifier... modifiers) {
        return addField(TypeName.get(type), name, modifiers);
    }

    public CodeBlockNode addStaticBlock(CodeBlock block) {
        staticBlock.beginControlFlow("static").add(block).endControlFlow();
        return this;
    }

    public boolean emit(CodeWriter codeWriter, String enumName, Set<Modifier> implicitModifiers, boolean firstMember)
            throws IOException {
        // Static fields.
        for (FieldSpec fieldSpec : fieldSpecs) {
            if (!fieldSpec.hasModifier(Modifier.STATIC)) continue;
            if (!firstMember) codeWriter.emit("\n");
            fieldSpec.emit(codeWriter, kind.implicitFieldModifiers);
            firstMember = false;
        }

        CodeBlock _staticBlock = staticBlock.build();
        if (!_staticBlock.isEmpty()) {
            if (!firstMember) codeWriter.emit("\n");
            codeWriter.emit(_staticBlock);
            firstMember = false;
        }

        // Non-static fields.
        for (FieldSpec fieldSpec : fieldSpecs) {
            if (fieldSpec.hasModifier(Modifier.STATIC)) continue;
            if (!firstMember) codeWriter.emit("\n");
            fieldSpec.emit(codeWriter, kind.implicitFieldModifiers);
            firstMember = false;
        }

        return firstMember;
    }
}
