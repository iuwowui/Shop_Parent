package com.qfedu.ljb.server.user.dao;

import com.qfedu.ljb.entity.Userpoints;
import org.apache.ibatis.annotations.Param;

public interface UserpointsMapper {

    int insert(Userpoints record);

    int update(@Param("uid") int uid,@Param("score") int score);
}