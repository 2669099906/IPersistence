package com.zxr.config;

import com.zxr.utils.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoxiangrui
 * @date 2020/11/8 23:22
 */
public class BoundSql {

    //解析过后的sql
    private String sqlText;

    private List<ParameterMapping> parameterMappingList;

    public BoundSql(String sqlText, List<ParameterMapping> parameterMappingList) {
        this.sqlText = sqlText;
        this.parameterMappingList = parameterMappingList;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public List<ParameterMapping> getParameterMappingList() {
        return parameterMappingList;
    }

    public void setParameterMappingList(List<ParameterMapping> parameterMappingList) {
        this.parameterMappingList = parameterMappingList;
    }
}
