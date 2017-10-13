package com.lifeonwalden.codeGenerator.bean.config;

import com.lifeonwalden.codeGenerator.bean.DB;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias(value = "dbGenerator")
public class DBSourceGenerator implements Serializable {
    private static final long serialVersionUID = -1765551957213581034L;

    private Config config;

    private DB db;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }
}
