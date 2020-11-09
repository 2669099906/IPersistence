package com.zxr.sqlSession;

import com.zxr.pojo.Configuration;
import com.zxr.pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zhaoxiangrui
 * @date 2020/11/8 22:17
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(final Configuration configuration){
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {

        //完成simpleExecutor
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return simpleExecutor.query(this.configuration, mappedStatement, params);
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<T> objects = selectList(statementId, params);
        if(objects.size() == 1){
            return objects.get(0);
        }else{
            throw new RuntimeException("查询结果为空或者结果过多");
        }
    }
}
