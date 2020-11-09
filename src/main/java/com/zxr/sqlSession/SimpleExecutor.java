package com.zxr.sqlSession;

import com.zxr.config.BoundSql;
import com.zxr.pojo.Configuration;
import com.zxr.pojo.MappedStatement;
import com.zxr.utils.GenericTokenParser;
import com.zxr.utils.ParameterMapping;
import com.zxr.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoxiangrui
 * @date 2020/11/8 23:08
 */
public class SimpleExecutor implements Executor {
    @Override
    public <E> List<E> query(Configuration configuration,
                             MappedStatement mappedStatement,
                             Object... params) throws Exception {
        //1.注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection("root", "123456");
        //2.获取sql语句 select * from user where id = #{id} and username = #{username}
        //2.1转换sql语句 select * from user where id = ? and username = ? ,并对{}中的值进行存储
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        //3.获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        //4.设置参数
        //4.1获取参数类型
        String paramType = mappedStatement.getParamType();
        Class<?> parameterTypeClass = getClassType(paramType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            //反射
            Field declaredField = parameterTypeClass.getDeclaredField(content);
            //设置暴力访问
            declaredField.setAccessible(true);
            //获取params第一个对象中的 [declaredField] 字段值
            Object o = declaredField.get(params[0]);
            preparedStatement.setObject(i+1, o);

        }
        //5.执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        //6.封装返回结果集
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);
        ArrayList<E> resultList = new ArrayList<>();
        while (resultSet.next()){
            ResultSetMetaData metaData = resultSet.getMetaData();
            Object o = resultTypeClass.getDeclaredConstructor().newInstance();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                //获取字段名
                String columnName = metaData.getColumnName(i);
                //字段名对应的值
                Object value = resultSet.getObject(columnName);
                //使用反射或者内省，根据数据库表与实体的对应关系，进行封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);
            }
            resultList.add((E)o);
        }
        return resultList;
    }

    private Class<?> getClassType(String paramType) throws ClassNotFoundException {
        if(paramType != null){
            Class<?> aClass = Class.forName(paramType);
            return aClass;
        }
        return null;
    }

    private BoundSql getBoundSql(String sql){
        //完成对#{}的解析工作 1.将#{}进行代替，并将大括号中的值进行存储
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析出来的sql
        String parseSql = genericTokenParser.parse(sql);
        //获取的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        //返回封装好的boundSql
        return new BoundSql(parseSql, parameterMappings);
    }
}
