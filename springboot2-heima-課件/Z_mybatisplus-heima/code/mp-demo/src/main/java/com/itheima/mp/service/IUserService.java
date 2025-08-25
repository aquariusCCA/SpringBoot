package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.PageVO;
import com.itheima.mp.domain.vo.UserVO;

import java.util.List;

public interface IUserService extends IService<User> {
    List<UserVO> queryUsers(UserQuery query);

    PageVO<UserVO> queryUserByPage(UserQuery query);

    void deductBalance(Long id, Integer money);

    UserVO queryUserAndAddressById(Long userId);
}
