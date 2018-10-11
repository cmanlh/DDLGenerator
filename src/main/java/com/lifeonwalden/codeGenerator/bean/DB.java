package com.lifeonwalden.codeGenerator.bean;

import com.lifeonwalden.codeGenerator.bean.config.Generator;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.Serializable;
import java.util.List;

@XStreamAlias(value = "db")
public class DB implements Serializable {
    private static final long serialVersionUID = 5192162457253499372L;

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private String driverClass;

    @XStreamAsAttribute
    private String connectionURL;

    @XStreamAsAttribute
    private String userId;

    @XStreamAsAttribute
    private String password;

    @XStreamAsAttribute
    private String schema;

    @XStreamAsAttribute
    private boolean withSchema;

    private List<DBTable> tables;

    @XStreamOmitField
    private Generator generatorNode;

    public String getName() {
        return name;
    }

    public DB setName(String name) {
        this.name = name;

        return this;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public DB setDriverClass(String driverClass) {
        this.driverClass = driverClass;

        return this;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public DB setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;

        return this;
    }

    public String getUserId() {
        return userId;
    }

    public DB setUserId(String userId) {
        this.userId = userId;

        return this;
    }

    public String getPassword() {
        return password;
    }

    public DB setPassword(String password) {
        this.password = password;

        return this;
    }

    public String getSchema() {
        return schema;
    }

    public DB setSchema(String schema) {
        this.schema = schema;

        return this;
    }

    public boolean isWithSchema() {
        return withSchema;
    }

    public DB setWithSchema(boolean withSchema) {
        this.withSchema = withSchema;

        return this;
    }

    public List<DBTable> getTables() {
        return tables;
    }

    public DB setTables(List<DBTable> tables) {
        this.tables = tables;

        return this;
    }

    public Generator getGeneratorNode() {
        return generatorNode;
    }

    public DB setGeneratorNode(Generator generatorNode) {
        this.generatorNode = generatorNode;

        return this;
    }
}
