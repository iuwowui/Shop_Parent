package com.qfedu.ljb.server.user.service;

import com.qfedu.common.exception.UserException;
import com.qfedu.common.vo.R;
import com.qfedu.ljb.entity.User;

public interface UserService {

    R save(User user) throws UserException;

    R all();

    R checkPhone(String phone);
}
