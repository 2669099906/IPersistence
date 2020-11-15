package com.zxr.config;

import com.zxr.pojo.Configuration;
import com.zxr.pojo.MappedStatement;
import com.zxr.pojo.SqlType;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * @author zhaoxiangrui
 * @date 2020/11/8 21:55
 */
public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(final Configuration configuration){
        this.configuration = configuration;
    }

    public void parse(InputStream in) throws DocumentException {
        Document document = new SAXReader().read(in);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        //解析select配置
        List<Element> selectList = rootElement.selectNodes("//select");
        parseBySqlType(SqlType.SELECT, selectList, namespace);
        //解析update配置
        List<Element> updateList = rootElement.selectNodes("//update");
        parseBySqlType(SqlType.UPDATE, updateList, namespace);
        //解析insert配置
        List<Element> insertList = rootElement.selectNodes("//insert");
        parseBySqlType(SqlType.INSERT, insertList, namespace);
        //解析delete配置
        List<Element> deleteList = rootElement.selectNodes("//delete");
        parseBySqlType(SqlType.DELETE, deleteList, namespace);

    }

    private void parseBySqlType(SqlType sqlType, List<Element> elements, String namespace){
        for (Element element : elements) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String parameterType = element.attributeValue("parameterType");
            String sqlText = element.getTextTrim();
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setParamType(parameterType);
            mappedStatement.setResultType(resultType);
            mappedStatement.setSql(sqlText);
            mappedStatement.setSqlType(sqlType);
            String key = namespace + "." + id;
            configuration.getMappedStatementMap().put(key, mappedStatement);
        }
    }
}
