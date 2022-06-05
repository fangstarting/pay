package com.fzipp.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.utils.LocalHostUtil;
import com.fzipp.pay.common.utils.PojoToQWrapperUtil;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.mapper.UserMapper;
import com.fzipp.pay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUser(Integer accountId) {
        User user = userMapper.getUserByAccountId(accountId);
        return user;
    }

    @Override
    public boolean verifyStatus(Integer accountId) {
        Integer statusByAccountId = userMapper.getStatusByAccountId(accountId);
        if(statusByAccountId==0)return false;
        return true;
    }

    @Override
    public boolean upUserInfo(User user) {
        int i = userMapper.updateUser(user);
        return i>0?true:false;
    }

    @Value("${path.down.profile}")
    private String pathDownProfile;
    @Value("${path.down.profileLast}")
    private String pathDownProfileLast;

    @Override
    public IPage<User> getUsersByFinds(User user, Integer pageNum, Integer pageSize) throws UnknownHostException {
        //查询均为已激活的用户
        try {
            if(pageNum<=0||pageSize<=0){
                pageNum = 1;
                pageSize = 20;
            }
        } catch (Exception e) {
            pageNum = 1;
            pageSize = 20;
        }
        String realname = null;
        if (user.getRealname()!=null){
            realname = user.getRealname();
            user.setRealname(null);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        QueryWrapper qWrapperOfPojo = PojoToQWrapperUtil.getQWrapperOfPojo(user, queryWrapper);
        qWrapperOfPojo.like(realname!=null,"realname",realname);
        qWrapperOfPojo.isNotNull("roleId");//roleId非空null
        qWrapperOfPojo.orderByAsc("userId");
        qWrapperOfPojo.ne("status",0);
        Page<User> userPage = super.page(new Page<>(pageNum,pageSize), qWrapperOfPojo);
        String localIP = SysProp.HTTP_TOP + LocalHostUtil.getLocalIP()+pathDownProfileLast;
        userPage.getRecords().forEach(e->e.setProfile(localIP+e.getProfile()));
//        userPage.getRecords().forEach(e->e.setProfile(pathDownProfile+e.getProfile()));
        return userPage;
    }

    @Override
    public List<User> getUsers() {
        return userMapper.getUsers();
    }

    @Override
    public Integer workYear(Integer userId, Date curr) {
        User byId = super.getById(userId);
        Date hiredate = byId.getHiredate();
        long l = curr.getTime() - hiredate.getTime();
        long l1 = l / 1000 / 60 / 60 / 24 / 365;
        return Integer.valueOf(Math.toIntExact(l1));
    }

}
