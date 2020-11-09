package com.zxr.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zxr.config.XMLMapperBuilder;
import com.zxr.io.Resources;
import com.zxr.pojo.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author zhaoxiangrui
 * @date 2020/11/8 21:16
 */
public class XMLConfiguration {

    private Configuration configuration;

    public XMLConfiguration(){
        this.configuration = new Configuration();
    }

    /**
     * 使用dom4j解析xml封装Configuration
     * @param in xml文件输入流
     * @return 返回封装好的Configuration对象
     */
    public Configuration parseConfig(InputStream in) throws DocumentException, PropertyVetoException {
        Document document = new SAXReader().read(in);
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element element : list) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name, value);
        }
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource("c3p0");
        comboPooledDataSource.setUser(properties.getProperty("user"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));


        this.configuration.setDataSource(comboPooledDataSource);

        //mapper.xml解析
        List<Element> mapperList = rootElement.selectNodes("//mapper");
        for (Element element : mapperList) {
            String path = element.attributeValue("resource");
            InputStream resourceAsStream = Resources.getResourceAsStream(path);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
            xmlMapperBuilder.parse(resourceAsStream);
        }
        return configuration;
    }

}
