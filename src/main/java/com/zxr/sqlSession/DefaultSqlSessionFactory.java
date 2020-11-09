package com.zxr.sqlSession;

import com.zxr.pojo.Configuration;

/**
 * @author zhaoxiangrui
 * @date 2020/11/8 22:13
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory{

    private Configuration configuration;

    public DefaultSqlSessionFactory(final Configuration configuration){
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(this.configuration);
    }
}
