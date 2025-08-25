package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.PageVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Override
    public PageVO<UserVO> queryUserByPage(UserQuery query) {
        // 1.分页条件
        String name = query.getName();
        Integer status = query.getStatus();

        Page<User> p = query.toMpPageDefaultSortByCreateTimeDesc(); // 返回统一的格式和查询条件
        // 2.调用ServiceImpl的查询父类方法
//        page(p);

        p = lambdaQuery() //添加User的自定义查询条件
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .page(p);
        // 3.数据格式返回
        return PageVO.of(p, user -> {
            UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
            String phone = vo.getPhone();
            vo.setPhone(phone.substring(0, phone.length() - 4) + "****");
            return vo;
        });
    }

    @Override
    public List<UserVO> queryUsers(UserQuery query) {
        // 1.组织条件
        String username = query.getName();
        Integer status = query.getStatus();
        Integer minBalance = query.getMinBalance();
        Integer maxBalance = query.getMaxBalance();
        // 2.查询用户
        List<User> users = lambdaQuery()
                .like(username != null, User::getUsername, username)
                .eq(status != null, User::getStatus, status)
                .ge(minBalance != null, User::getBalance, minBalance)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .list();
        //        .page();
        //        .one();
        //        .count();

        // 3.处理vo
        return BeanUtil.copyToList(users, UserVO.class);
    }

    @Override
    public void deductBalance(Long id, Integer money) {
        // 1.查询用户
        User user = getById(id);
        // 2.判断用户状态
        if (user == null || user.getStatus().getValue() == 2) {
            throw new RuntimeException("用户状态异常");
        }
        // 3.判断用户余额
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足");
        }
        // 4.扣减余额
//        baseMapper.deductMoneyById(id, money);
        //需求：当扣减余额为0时，冻结用户
        int remainBalance = user.getBalance() - money;
        lambdaUpdate()
                .set(User::getBalance, remainBalance)
                .set(remainBalance == 0, User::getStatus, 2)
                .eq(User::getId, user.getId())
                .eq(User::getBalance, user.getBalance())  //乐观锁机制判断，防止并发误修改
                .update();
    }


    @Override
    public UserVO queryUserAndAddressById(Long userId) {
        // 1.查询用户
        User user = getById(userId);
        if (user == null) {
            return null;
        }
        // 2.查询收货地址
        // 采用了Db的静态方法，因此避免了注入AddressService，减少了循环依赖的风险
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, userId)
                .list();
        // 3.处理vo
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        if (!CollectionUtils.isEmpty(addresses)) {
            userVO.setAddresses(BeanUtil.copyToList(addresses, AddressVO.class));
        }
        return userVO;
    }

}
