package com.fzipp.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fzipp.pay.entity.Account;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
public interface AccountService extends IService<Account> {


    boolean check(String id,String code);
    /**
     * 登录验证
     * @param username
     * @param password
     * @return
     */
    boolean login(String username,String password);

    /**
     * 根据账户密码返回账户信息
     * @param username
     * @param password
     * @return
     */
    Account getAccount(String username,String password);

    /**
     * 注册
     * @param account
     * @return
     */
    @Transactional
    boolean reg(Account account);

    /**
     * 账户状态验证
     * true:正常 false:注销
     * @param accountId
     * @return
     */
    boolean verifyStatus(Integer accountId);
}
