package com.lifeonwalden.codeGenerator.bean;

import java.io.Serializable;
import java.util.List;

import com.lifeonwalden.codeGenerator.bean.config.Generator;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

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

    private List<DBTable> tables;

    @XStreamOmitField
    private Generator generatorNode;

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DBTable> getTables() {
        return tables;
    }

    public void setTables(List<DBTable> tables) {
        this.tables = tables;
    }

    public Generator getGeneratorNode() {
        return generatorNode;
    }

    public void setGeneratorNode(Generator generatorNode) {
        this.generatorNode = generatorNode;
    }
}
