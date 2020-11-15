package com.zxr.sqlSession;


import java.util.List;

/**
 * @author zhaoxiangrui
 * @date 2020/11/8 22:16
 */
public interface SqlSession {

    //为dao接口生成代理实现类
    public <T> T getMapper(Class<?> mapperClass);

    //查询所有
    public <E> List<E> selectList(String statementId, Object... params) throws Exception;
    //根据条件查询单个
    public <E> E selectOne(String statementId, Object... params) throws Exception;
    //更新接口
    public Integer update(String statementId, Object... params) throws Exception;
    //删除接口
    public Integer delete(String statementId, Object... params) throws Exception;
    //添加接口
    public Integer insert(String statementId, Object... params) throws Exception;
}
