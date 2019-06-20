package com.qfedu.ljb.server.user.dao;

import com.qfedu.ljb.entity.Points;
import org.apache.ibatis.annotations.Param;

public interface PointsMapper {
    int insert(Points record);


}