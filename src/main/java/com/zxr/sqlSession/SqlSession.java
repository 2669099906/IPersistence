package com.zxr.sqlSession;


import java.util.List;

/**
 * @author zhaoxiangrui
 * @date 2020/11/8 22:16
 */
public interface SqlSession {

    //查询所有
    public <E> List<E> selectList(String statementId, Object... params) throws Exception;
    //根据条件查询单个
    public <E> E selectOne(String statementId, Object... params) throws Exception;
}
