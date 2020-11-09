package com.zxr.sqlSession;

import com.zxr.pojo.Configuration;
import com.zxr.config.XMLConfiguration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * @author zhaoxiangrui
 * @date 2020/11/8 21:13
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream in) throws PropertyVetoException, DocumentException {
        //1.使用dom4j解析配置文件， 将解析出来的内容封装到Configuration中
        XMLConfiguration xmlConfiguration = new XMLConfiguration();
        Configuration configuration = xmlConfiguration.parseConfig(in);
        //2.创建sqlSessionFactory对象：工厂类  生产sqlSession:会话对象
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return defaultSqlSessionFactory;
    }
}
