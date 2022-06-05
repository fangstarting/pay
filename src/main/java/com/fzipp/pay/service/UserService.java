package com.fzipp.pay.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fzipp.pay.entity.User;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
public interface UserService extends IService<User> {

    /**
     * 根据账号Id返回User对象
     * @param accountId
     * @return
     */
    User getUser(Integer accountId);

    /**
     * 验证用户状态
     * true:启用
     * false:禁用
     * @param accountId
     * @return
     */
    boolean verifyStatus(Integer accountId);

    /**
     * 更新用户激活信息
     * @param user
     * @return
     */
    boolean upUserInfo(User user);

    /**
     * 组合查询（查询均为已激活的用户）  不支持时间查询
     * @param user
     * @return
     */
    IPage<User> getUsersByFinds(User user, Integer pageNum, Integer pageSize) throws UnknownHostException;

    List<User> getUsers();

    /**
     * 工龄计算
     * @param userId
     * @param curr 截止日期
     * @return
     */
    Integer workYear(Integer userId, Date curr);
}
