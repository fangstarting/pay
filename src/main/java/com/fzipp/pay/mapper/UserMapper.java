package com.fzipp.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.entity.child.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE userId=#{userId}")
    User getUserById(Integer userId);

    @Select("SELECT * FROM user WHERE accountId=#{accountId}")
    User getUserByAccountId(Integer accountId);

    @Insert("INSERT INTO user(accountId,profile,sex,birthday,status,updatetime) VALUES(#{accountId},#{profile},#{sex},#{birthday},#{status},#{updatetime})")
    @Options(useGeneratedKeys = true,keyProperty = "userId",keyColumn = "id")
    int addUser(User user);

    @Select("SELECT status FROM user WHERE accountId=#{accountId}")
    Integer getStatusByAccountId(Integer accountId);

    @Update("<script>" +
            "UPDATE user " +
            "<set>" +
            "<if test='roleId != null'> roleId=#{roleId},</if>" +
            "<if test='deptId != null'> deptId=#{deptId},</if>" +
            "<if test='positionId != null'> positionId=#{positionId},</if>" +
            "<if test='jobgradeId != null'> jobgradeId=#{jobgradeId},</if>" +
            "<if test='profile != null'> profile=#{profile},</if>" +
            "<if test='realname != null'> realname=#{realname},</if>" +
            "<if test='sex != null'> sex=#{sex},</if>" +
            "<if test='education != null'> education=#{education},</if>" +
            "<if test='phone != null'> phone=#{phone},</if>" +
            "<if test='basepay != null'> basepay=#{basepay},</if>" +
            "<if test='birthday != null'> birthday=#{birthday},</if>" +
            "<if test='hiredate != null'> hiredate=#{hiredate},</if>" +
            "<if test='status != null'> status=#{status},</if>" +
            "<if test='loginendtime != null'> loginendtime=#{loginendtime},</if>" +
            "<if test='updatetime != null'> updatetime=#{updatetime},</if>" +
            "</set>" +
            "WHERE userId=#{userId}" +
            "</script>")
    int updateUser(User user);

    @Select("<script>" +
            "SELECT * FROM user" +
            "<where>" +
            "<if test='u.roleId != null'>AND roleId=#{u.roleId}</if>" +
            "<if test='u.deptId != null'>AND deptId=#{u.deptId}</if>" +
            "</where>" +
            "</script>")
    IPage<User> getUsersByFinds(Page<?> page, @Param("u") User user);


    //UserInfo
    @Results({
            @Result(property = "position",one = @One(select = "com.fzipp.pay.mapper.PositionMapper.getPositionById"),column = "positionId")
    })
    @Select("SELECT * FROM user WHERE userId=#{userId}")
    UserInfo getUserInfoById(Integer userId);

    @Select("SELECT * FROM user")
    List<User> getUsers();
}
