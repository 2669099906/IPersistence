package com.zxr.sqlSession;

/**
 * @author zhaoxiangrui
 * @date 2020/11/8 21:14
 */
public interface SqlSessionFactory {

    public SqlSession openSession();
}
