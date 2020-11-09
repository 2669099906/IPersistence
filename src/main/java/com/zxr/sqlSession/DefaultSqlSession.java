package com.zxr.sqlSession;

import com.zxr.pojo.Configuration;
import com.zxr.pojo.MappedStatement;

import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public <T> T getMapper(Class<?> mapperClass) {
        //动态代理dao提供的接口，调用proxy代理对象中的任何方法都是调用invoke方法
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            /**
             * proxy 当前代理对象的应用
             * method 当前调用的方法的引用
             * args 调用方法的入参
             */
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //底层都是执行JDBC。根据不同情况 调用selectList 或者 selectOne方法
                //获取当前方法名
                String methodName = method.getName();
                //接口全限定名
                String className = method.getDeclaringClass().getName();
                //组装statementId
                String statementId = className + "." + methodName;
                //获取被调用方法的返回值类型
                Type genericReturnType = method.getGenericReturnType();
                //判断是否进行了泛型类型参数化
                if(genericReturnType instanceof ParameterizedType){
                    return selectList(statementId, args);
                }
                return selectOne(statementId, args);
            }
        };
        return (T)Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class[]{mapperClass}, invocationHandler);
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
