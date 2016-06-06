package com.lifeonwalden.codeGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lifeonwalden.codeGenerator.bean.Column;
import com.lifeonwalden.codeGenerator.bean.Constraint;
import com.lifeonwalden.codeGenerator.bean.Database;
import com.lifeonwalden.codeGenerator.bean.EnumConst;
import com.lifeonwalden.codeGenerator.bean.IndexColumn;
import com.lifeonwalden.codeGenerator.bean.Table;
import com.lifeonwalden.codeGenerator.bean.config.Generator;
import com.lifeonwalden.codeGenerator.constant.ColumnConstraintEnum;
import com.lifeonwalden.codeGenerator.dll.TableGenerator;
import com.lifeonwalden.codeGenerator.dll.impl.MySQLTableGeneratorImpl;
import com.lifeonwalden.codeGenerator.javaClass.DAOGenerator;
import com.lifeonwalden.codeGenerator.javaClass.DTOGenerator;
import com.lifeonwalden.codeGenerator.javaClass.EnumGenerator;
import com.lifeonwalden.codeGenerator.javaClass.impl.DAOGeneratorImpl;
import com.lifeonwalden.codeGenerator.javaClass.impl.DTOGeneratorImpl;
import com.lifeonwalden.codeGenerator.javaClass.impl.EnumGeneratorImpl;
import com.lifeonwalden.codeGenerator.js.JsEnumGenerator;
import com.lifeonwalden.codeGenerator.js.impl.JsEnumGeneratorImpl;
import com.lifeonwalden.codeGenerator.mybatis.impl.XMLMapperGenerator;
import com.thoughtworks.xstream.XStream;

public class GenerateCodeMain {
	public static void main(String[] args) {
		String[][] argList = {
				{ "C:\\workspace\\lifeonwalden-codeGenerator\\src\\test\\resources\\stock_build.xml",
						"C:\\workspace\\lifeonwalden-codeGenerator\\target" },
				{ "C:\\workspace\\lifeonwalden-codeGenerator\\src\\test\\resources\\personallife_build.xml",
						"C:\\workspace\\lifeonwalden-codeGenerator\\target" },
				{ "C:\\workspace\\lifeonwalden-codeGenerator\\src\\test\\resources\\personallife_auth_build.xml",
						"C:\\workspace\\lifeonwalden-codeGenerator\\target" },
				{ "C:\\workspace\\lifeonwalden-codeGenerator\\src\\test\\resources\\Reading_build.xml",
						"C:\\workspace\\lifeonwalden-codeGenerator\\target" } };
		for (String[] _args : argList) {
			XStream xStream = new XStream();
			xStream.processAnnotations(Generator.class);
			Generator generator = (Generator) xStream.fromXML(new File(_args[0]));
			generator.getConfig().setOutputLocation(_args[1]);
			init(generator);
			Database database = generator.getDatabase();
			if (null != database.getConstPool()) {
				EnumGenerator enumJavaClassGenerator = new EnumGeneratorImpl();
				for (EnumConst enumConst : database.getConstPool()) {
					enumJavaClassGenerator.generate(enumConst, generator.getConfig());
				}

				JsEnumGenerator jsEnumGenerator = new JsEnumGeneratorImpl();
				jsEnumGenerator.generate(database.getConstPool(), generator.getConfig());
			}
			TableGenerator tableGenerator = new MySQLTableGeneratorImpl();
			DAOGenerator daoGenerator = new DAOGeneratorImpl();
			DTOGenerator dtoGenerator = new DTOGeneratorImpl();

			XMLMapperGenerator xmlMapperGenerator = new XMLMapperGenerator();
			StringBuilder sqlBuilder = new StringBuilder();
			for (Table table : database.getTables()) {
				sqlBuilder.append(tableGenerator.generator(table, generator.getConfig()));
				daoGenerator.generate(table, generator.getConfig());
				dtoGenerator.generate(table, generator.getConfig());

				xmlMapperGenerator.generate(table, generator.getConfig());
			}
			try {
				String file = new File(generator.getConfig().getOutputLocation()).getPath() + "\\" + database.getName() + ".sql";
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
				bw.write(sqlBuilder.toString());
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void init(Generator generator) {
		Database database = generator.getDatabase();
		Map<String, EnumConst> constMapping = new HashMap<String, EnumConst>();
		if (null != database.getConstMapping()) {
			for (EnumConst enumCost : database.getConstPool()) {
				constMapping.put(enumCost.getName(), enumCost);
			}
			database.setConstMapping(constMapping);
		}

		for (Table table : database.getTables()) {
			if ((database.getAddDBFields() == null || database.getAddDBFields()) && (table.getAddDBFields() == null || table.getAddDBFields())) {
				List<Column> columnList = table.getColumns();
				Column column = new Column();
				column.setName("createTime");
				column.setNote("创建时间");
				column.setType("datetime");
				columnList.add(column);

				column = new Column();
				column.setName("createUser");
				column.setNote("创建者");
				column.setType("varchar");
				column.setLength("32");
				columnList.add(column);

				column = new Column();
				column.setName("updateTime");
				column.setNote("更新时间");
				column.setType("datetime");
				columnList.add(column);

				column = new Column();
				column.setName("updateUser");
				column.setNote("更新者");
				column.setType("varchar");
				column.setLength("32");
				columnList.add(column);

				column = new Column();
				column.setName("logicalDel");
				column.setNote("逻辑删除");
				column.setType("int");
				column.setDefaultVal("0");
				columnList.add(column);
			}

			Map<String, Column> columnMapping = new HashMap<String, Column>();
			for (Column column : table.getColumns()) {
				columnMapping.put(column.getName(), column);

				if (null != column.getOptionRef()) {
					column.setOptionRefObj(constMapping.get(column.getOptionRef()));
				}

			}
			table.setColumnMapping(columnMapping);

			if (null != table.getConstraints()) {
				List<Constraint> constraintList = table.getConstraints();
				for (Constraint constraint : constraintList) {
					ColumnConstraintEnum constraintEnum = ColumnConstraintEnum.forAlias(constraint.getType().toUpperCase());

					if (ColumnConstraintEnum.PRIMARY_KEY == constraintEnum) {
						List<Column> primaryCols = new ArrayList<Column>();

						for (IndexColumn indexColumn : constraint.getColumn()) {
							Column column = columnMapping.get(indexColumn.getName());
							primaryCols.add(column);
							if (constraint.getColumn().size() > 0) {
								column.setConstraintType(ColumnConstraintEnum.UNION_PRIMARY_KEY);
							} else {
								column.setConstraintType(ColumnConstraintEnum.PRIMARY_KEY);
							}
						}

						table.setPrimaryColumns(primaryCols);
					}
				}
			}
		}
	}
}
