package com.zxr.sqlSession;

import com.zxr.pojo.Configuration;
import com.zxr.pojo.MappedStatement;
import com.zxr.pojo.SqlType;

import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public <T> T getMapper(Class<?> mapperClass) {
        //动态代理dao提供的接口，调用proxy代理对象中的任何方法都是调用invoke方法
        /**
         * proxy 当前代理对象的应用
         * method 当前调用的方法的引用
         * args 调用方法的入参
         */
        InvocationHandler invocationHandler = (proxy, method, args) -> {
             //底层都是执行JDBC。根据不同情况 调用selectList 或者 selectOne方法
             //获取当前方法名
             String methodName = method.getName();
             //接口全限定名
             String className = method.getDeclaringClass().getName();
             //组装statementId
             String statementId = className + "." + methodName;
             //获取执行sql类型
             MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
             SqlType sqlType = mappedStatement.getSqlType();
             //根据不同的sqlType执行不同的方法
             switch (sqlType){
                 case DELETE:
                     return delete(statementId, args);
                 case INSERT:
                     return insert(statementId, args);
                 case UPDATE:
                     return update(statementId, args);
                 case SELECT:
                     //获取被调用方法的返回值类型
                     Type genericReturnType = method.getGenericReturnType();
                     //判断是否进行了泛型类型参数化
                     if(genericReturnType instanceof ParameterizedType){
                         return selectList(statementId, args);
                     }
                     return selectOne(statementId, args);
             }
             //程序执行到此处，证明sql类型有误
             throw new Exception("sql类型不存在");
         };
        T t = (T)Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class[]{mapperClass}, invocationHandler);
        return t;
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

    @Override
    public Integer update(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return simpleExecutor.update(this.configuration, mappedStatement, params);
    }

    @Override
    public Integer delete(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return simpleExecutor.delete(this.configuration, mappedStatement, params);
    }

    @Override
    public Integer insert(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return simpleExecutor.insert(this.configuration, mappedStatement, params);
    }
}
