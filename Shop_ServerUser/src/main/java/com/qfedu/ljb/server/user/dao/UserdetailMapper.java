package com.qfedu.ljb.server.user.dao;

import com.qfedu.ljb.entity.Userdetail;

public interface UserdetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Userdetail record);

    int insertInit(int uid);

    int insertSelective(Userdetail record);

    Userdetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Userdetail record);

    int updateByPrimaryKey(Userdetail record);
}