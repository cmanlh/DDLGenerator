package com.lifeonwalden.codeGenerator;

import com.lifeonwalden.codeGenerator.bean.*;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.bean.config.ExtentionGenerator;
import com.lifeonwalden.codeGenerator.bean.config.Generator;
import com.lifeonwalden.codeGenerator.constant.ColumnConstraintEnum;
import com.lifeonwalden.codeGenerator.dll.TableGenerator;
import com.lifeonwalden.codeGenerator.javaClass.impl.DAOGeneratorImpl;
import com.lifeonwalden.codeGenerator.javaClass.impl.EnumGeneratorImpl;
import com.lifeonwalden.codeGenerator.javaClass.impl.JsEnumGeneratorImpl;
import com.lifeonwalden.codeGenerator.mybatis.impl.XMLMapperGenerator;
import com.thoughtworks.xstream.XStream;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateCodeMain {
    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
            return;
        }

        File configFileLocation = new File(args[0]);
        if (!configFileLocation.exists() && !configFileLocation.isDirectory()) {
            System.err.println("The directory is not exist.");
            printUsage();

            System.exit(1);
        }

        String globalOutputFileLocation = args[1];

        File[] xmlFiles = configFileLocation.listFiles((dir, fileName) -> fileName.endsWith("xml"));
        for (File templateFile : xmlFiles) {
            Generator generator = getGenerator(templateFile);

            init(generator, globalOutputFileLocation);

            Config config = generator.getConfig();
            if (null == config) {
                continue;
            }

            // constant generate
            generateConst(generator, config);

            Database database = generator.getDatabase();
            if (null == database) {
                continue;
            }

            // DDL generate
            generateSQL(generator, database);

            // other generate
            generatorMybatisORM(generator, config, database);
        }
    }

    private static void printUsage() {
        System.err.println("Usage: DDLGenerator <config dir> <output dir>");
    }

    private static Generator getGenerator(File templateFile) {
        XStream xStream = new XStream();
        xStream.processAnnotations(Generator.class);
        return (Generator) xStream.fromXML(templateFile);
    }

    private static void init(Generator generator, String globalOutputFileLocation) {
        if (generator.getDatabase() != null) {
            Database database = generator.getDatabase();
            Map<String, EnumConst> constMapping = new HashMap<String, EnumConst>();
            if (null != generator.getConstPool()) {
                for (EnumConst enumCost : generator.getConstPool()) {
                    constMapping.put(enumCost.getName(), enumCost);
                }
                database.setConstMapping(constMapping);
            }

            for (Table table : database.getTables()) {
                table.setDatabase(database);
                if (table.getAddDBFields()) {
                    List<Column> columnList = table.getColumns();
                    for (Column column : database.getDbFields()) {
                        columnList.add(column.copy());
                    }
                }

                Map<String, Column> columnMapping = new HashMap<String, Column>();
                for (Column column : table.getColumns()) {
                    column.setTable(table);
                    columnMapping.put(column.getName(), column);

                    if (null != column.getOptionRef()) {
                        column.setOptionRefObj(constMapping.get(column.getOptionRef()));
                    }
                }
                table.setColumnMapping(columnMapping);

                if (null != table.getConstraints()) {
                    List<Constraint> constraintList = table.getConstraints();
                    for (Constraint constraint : constraintList) {
                        constraint.setTable(table);
                        ColumnConstraintEnum constraintEnum = ColumnConstraintEnum.forAlias(constraint.getType().toUpperCase());

                        if (ColumnConstraintEnum.PRIMARY_KEY == constraintEnum) {
                            List<Column> primaryCols = new ArrayList<Column>();

                            for (IndexColumn indexColumn : constraint.getColumns()) {
                                Column column = columnMapping.get(indexColumn.getName());
                                primaryCols.add(column);
                                if (constraint.getColumns().size() > 0) {
                                    column.setConstraintType(ColumnConstraintEnum.UNION_PRIMARY_KEY);
                                } else {
                                    column.setConstraintType(ColumnConstraintEnum.PRIMARY_KEY);
                                }
                            }

                            table.setPrimaryColumns(primaryCols);
                        }
                    }
                }

                if (null != table.getIndexs()) {
                    for (Index index : table.getIndexs()) {
                        index.setTable(table);
                    }
                }
            }
        }

        Config config = generator.getConfig();
        if (null != config) {
            String outputLocation = config.getOutputLocation();
            if (null == outputLocation) {
                config.setOutputLocation(globalOutputFileLocation);
            }

            if (null != config.getExtentions()) {
                Map<String, ExtentionGenerator> extentionMapping = new HashMap<String, ExtentionGenerator>();

                for (ExtentionGenerator extention : config.getExtentions()) {
                    extentionMapping.put(extention.getGenerator(), extention);
                }

                config.setExtentionMapping(extentionMapping);
            }
        }
    }

    private static void generateConst(Generator generator, Config config) {
        if (null != generator.getConstPool() && null != config && null != config.getConstInfo()) {
            ConstBasedGenerator enumJavaClassGenerator = new EnumGeneratorImpl();
            ConstBasedGenerator jsEnumGenerator = new JsEnumGeneratorImpl();
            enumJavaClassGenerator.generate(generator.getConstPool(), config);
            jsEnumGenerator.generate(generator.getConstPool(), config);
        }
    }

    private static void generateSQL(Generator generator, Database database) {
        TableGenerator tableGenerator = null;
        try {
            tableGenerator = (TableGenerator) Class
                    .forName(null == database.getGenerator() ? "com.lifeonwalden.codeGenerator.dll.impl.MySQLTableGeneratorImpl" : database.getGenerator())
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            System.err.println("Not an illegal ddl generator.");
            return;
        }
        StringBuilder sqlBuilder = new StringBuilder();
        for (Table table : database.getTables()) {
            sqlBuilder.append(tableGenerator.generate(table, generator.getConfig()));
        }
        try {
            File folder = new File(generator.getConfig().getOutputLocation());
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String file = folder.getPath() + File.separator + database.getName() + ".sql";
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), generator.getConfig().getEncoding()));
            bw.write(sqlBuilder.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generatorMybatisORM(Generator generator, Config config, Database database) {
        List<TableBasedGenerator> generatorList = new ArrayList<>();

        // standard generate
        if (null != config.getBeanInfo()) {
            try {
                String beanGeneratorClass = config.getBeanInfo().getGenerator();

                generatorList.add((TableBasedGenerator) Class
                        .forName(null == beanGeneratorClass ? "com.lifeonwalden.codeGenerator.javaClass.impl.HashBeanGeneratorImpl" : beanGeneratorClass)
                        .newInstance());
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                System.err.println("Not an illegal Bean generator.");

                return;
            }
            if (null != config.getDaoInfo()) {
                generatorList.add(new DAOGeneratorImpl());
                if (null != config.getMybatisInfo()) {
                    generatorList.add(new XMLMapperGenerator());
                }
            }
        }

        if (null != config.getExtentions()) {
            for (ExtentionGenerator extention : config.getExtentions()) {
                try {
                    generatorList.add((TableBasedGenerator) Class.forName(extention.getGenerator()).newInstance());
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    System.err.println("Not an illegal generator : " + extention.getGenerator());
                }
            }
        }

        for (Table table : database.getTables()) {
            for (TableBasedGenerator _generator : generatorList) {
                _generator.generate(table, generator.getConfig());
            }
        }
    }
}
