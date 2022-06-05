package com.fzipp.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.redis.cache.RedisClient;
import com.fzipp.pay.common.utils.DateUtil;
import com.fzipp.pay.common.utils.MD5Util;
import com.fzipp.pay.common.utils.SubstringUtil;
import com.fzipp.pay.entity.Account;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.mapper.AccountMapper;
import com.fzipp.pay.mapper.UserMapper;
import com.fzipp.pay.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisClient redisClient;

    @Override
    public boolean check(String id, String code) {
        //获取传过来的id 和 code
        if (id == null || "".equals(id) || code == null || "".equals(code)) return false;
        //获取redis里面存的code
        Object obj = redisClient.get(id);
        if (obj == null) return false;
        //比较输入的code和redis的code
//        log.error("id："+id+"验证码："+obj.toString());
        boolean flag = code.equalsIgnoreCase(obj.toString());
        //匹配成功就删除redis存储
        if (flag) redisClient.delete(id);
        return flag;
    }

    @Override
    public boolean login(String username, String password) {
        Account accountByPass = accountMapper.getAccountByPass(username, MD5Util.getMd5(password+username));
        return accountByPass==null?false:true;
    }

    @Override
    public Account getAccount(String username, String password) {
        Account accountByPass = accountMapper.getAccountByPass(username, MD5Util.getMd5(password+username));
        return accountByPass;
    }

    @Override
    public boolean reg(Account account) {
        account.setStatus(1);
        account.setUpdatetime(DateUtil.getSqlDate());
        account.setPassword(MD5Util.getPass(account.getPassword(),account.getUsername()));
        try {
            accountMapper.addAccount(account);
        } catch (Exception e) {
            return false;
        }

        //添加信息到user表中
        User user = new User();//accountId,profile,sex,birthday,status,updatetime
        user.setAccountId(account.getAccountId());
        user.setProfile(SysProp.DEFAULT_USER_PROFILE);
        user.setSex(SubstringUtil.getSexByCard(account.getIdCard()));
        user.setBirthday(DateUtil.getSqlInDate(SubstringUtil.getBirthByCard(account.getIdCard())));
        user.setStatus(0);
        user.setUpdatetime(DateUtil.getSqlDate());
        int i = userMapper.addUser(user);
        return i>0?true:false;
    }

    @Override
    public boolean verifyStatus(Integer accountId) {
        Integer status = accountMapper.getStatus(accountId);
        if(status==1)return true;
        return false;
    }
}
