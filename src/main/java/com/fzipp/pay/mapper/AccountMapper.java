package com.fzipp.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fzipp.pay.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
public interface AccountMapper extends BaseMapper<Account> {

    /**
     * 登录查询
     * @param username
     * @param password
     * @return
     */
    @Select("SELECT * FROM account WHERE username=#{username} AND password=#{password}")
    Account getAccountByPass(String username,String password);

    @Insert("INSERT INTO account(username,password,idCard,mail,status,updatetime) VALUES(#{username},#{password},#{idCard},#{mail},#{status},#{updatetime})")
    @Options(useGeneratedKeys=true, keyProperty="accountId", keyColumn="id")
    int addAccount(Account account);

    @Select("SELECT status FROM account WHERE accountId=#{accountId}")
    Integer getStatus(Integer accountId);
}
