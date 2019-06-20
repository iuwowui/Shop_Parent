package com.qfedu.ljb.server.user.dao;

import com.qfedu.ljb.entity.Userlog;

public interface UserlogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Userlog record);

    int insertSelective(Userlog record);

    Userlog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Userlog record);

    int updateByPrimaryKey(Userlog record);
}