package com.zxr.sqlSession;

import com.zxr.pojo.Configuration;
import com.zxr.pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zhaoxiangrui
 * @date 2020/11/8 23:06
 */
public interface Executor {

    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;
    public int update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;
    public int delete(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;
    public int insert(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;
}
