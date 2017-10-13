package com.lifeonwalden.codeGenerator;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.DB;
import com.lifeonwalden.codeGenerator.bean.DBTable;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Config;
import com.lifeonwalden.codeGenerator.bean.config.DBSourceGenerator;
import com.lifeonwalden.codeGenerator.bean.config.ExtentionGenerator;
import com.lifeonwalden.codeGenerator.bean.config.JDBCConnectionConfiguration;
import com.lifeonwalden.codeGenerator.constant.ColumnConstraintEnum;
import com.lifeonwalden.codeGenerator.constant.JdbcTypeEnum;
import com.lifeonwalden.codeGenerator.javaClass.impl.DAOGeneratorImpl;
import com.lifeonwalden.codeGenerator.mybatis.constant.DatabaseUserTable;
import com.lifeonwalden.codeGenerator.mybatis.impl.XMLMapperGenerator;
import com.lifeonwalden.codeGenerator.util.ConnectionFactory;
import com.lifeonwalden.codeGenerator.util.DBInfoUtil;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBSourceGenerateCodeMain {
    public static void main(String[] args) {
        File configFileLocation = new File(args[0]);
        if (!configFileLocation.exists() && !configFileLocation.isDirectory()) {
            System.err.println("The directory is not exist.");

            System.exit(1);
        }

        String globalOutputFileLocation = null;
        if (args.length > 1) {
            globalOutputFileLocation = args[1];
        }

        for (File templateFile : configFileLocation.listFiles()) {
            if (!templateFile.getName().toLowerCase().endsWith("xml")) {
                continue;
            }
            XStream xStream = new XStream();
            xStream.processAnnotations(DBSourceGenerator.class);
            DBSourceGenerator generator = (DBSourceGenerator) xStream.fromXML(templateFile);
            List<Table> tableList = fetchTables(generator, globalOutputFileLocation);

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

                if (null == generator.getDb()) {
                    continue;
                }
                if (null != config) {
                    List<TableBasedGenerator> generatorList = new ArrayList<TableBasedGenerator>();

                    // standard generate
                    if (null != config.getBeanInfo()) {
                        try {
                            String beanGeneratorClass = generator.getConfig().getBeanInfo().getGenerator();
                            generatorList.add((TableBasedGenerator) Class.forName(
                                    null == beanGeneratorClass
                                            ? "com.lifeonwalden.codeGenerator.javaClass.impl.HashBeanGeneratorImpl"
                                            : beanGeneratorClass).newInstance());
                        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                            System.err.println("Not an illegal Bean generator.");

                            continue;
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

                    for (Table table : tableList) {
                        for (TableBasedGenerator _generator : generatorList) {
                            _generator.generate(table, generator.getConfig());
                        }
                    }
                }
            }
        }
    }

    private static List<Table> fetchTables(DBSourceGenerator generator, String globalOutputFileLocation) {
        if (generator.getDb() != null) {
            DB database = generator.getDb();

            Map<String, DBTable> tableCache = new HashMap<>();
            for (DBTable table : database.getTables()) {
                tableCache.put(table.getName().toUpperCase(), table);
            }

            JDBCConnectionConfiguration jdbcConfig = new JDBCConnectionConfiguration();
            jdbcConfig.setConnectionURL(database.getConnectionURL());
            jdbcConfig.setDriverClass(database.getDriverClass());
            jdbcConfig.setPassword(database.getPassword());
            jdbcConfig.setUserId(database.getUserId());

            ConnectionFactory connFactory = ConnectionFactory.getInstance();
            try (Connection connection = connFactory.getConnection(jdbcConfig);
                 PreparedStatement pps =
                         connection.prepareStatement(DatabaseUserTable.getDatabaseUserTable(DBInfoUtil.getDBType(jdbcConfig))
                                 .getUserTableRetrievalStatement())) {
                ResultSet tableResult = pps.executeQuery();
                List<Table> tableList = new ArrayList<Table>();
                while (tableResult.next()) {
                    String tableName = tableResult.getString(1);
                    if (!tableCache.containsKey(tableName.toUpperCase())) {
                        continue;
                    }

                    DatabaseMetaData metaData = connection.getMetaData();
                    ResultSet columnRS = metaData.getColumns(connection.getCatalog(), null, tableName, null);
                    DBTable dbTable = tableCache.get(tableName.toUpperCase());

                    Table table = new Table();
                    table.setName(tableName);
                    table.setNote(dbTable.getNote());
                    Map<String, Column> columnMapping = new HashMap<>();
                    List<Column> columnList = new ArrayList<Column>();
                    while (columnRS.next()) {
                        Column column = new Column();
                        String columnName = columnRS.getString("COLUMN_NAME");
                        column.setName(columnName);
                        column.setTable(table);
                        column.setRequired(columnRS.getInt("NULLABLE") != DatabaseMetaData.columnNullable);
                        JdbcTypeEnum typeInfo = JdbcTypeEnum.valueOf(columnRS.getInt("DATA_TYPE"));
                        column.setType(typeInfo.getName());

                        columnList.add(column);
                        columnMapping.put(columnName, column);
                    }

                    table.setColumnMapping(columnMapping);
                    table.setColumns(columnList);

                    ResultSet pkRS = metaData.getPrimaryKeys(connection.getCatalog(), null, tableName);
                    List<Column> pkList = new ArrayList<Column>();
                    while (pkRS.next()) {
                        String pk = pkRS.getString("COLUMN_NAME");
                        if (null != pk && pk.length() > 0) {
                            Column column = columnMapping.get(pk);
                            column.setConstraintType(ColumnConstraintEnum.PRIMARY_KEY);
                            pkList.add(column);
                        }
                    }

                    table.setExtProps(dbTable.getExtProps());

                    table.setPrimaryColumns(pkList);

                    tableList.add(table);
                }

                return tableList;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        return null;
    }
}
