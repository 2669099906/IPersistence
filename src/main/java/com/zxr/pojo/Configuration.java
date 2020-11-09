package com.zxr.pojo;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaoxiangrui
 * @date 2020/11/8 20:13
 */
public class Configuration {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }

    public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }

    /**
     * key: statementId namespace+selectId
     * value: 封装好的MappedStatement对象
     */
    Map<String, MappedStatement> mappedStatementMap = new HashMap<>();
}
