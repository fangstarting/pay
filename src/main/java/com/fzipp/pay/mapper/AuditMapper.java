package com.fzipp.pay.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fzipp.pay.entity.Audit;
import com.fzipp.pay.entity.child.AduitInfo;
import org.apache.ibatis.annotations.*;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-28
 */
public interface AuditMapper extends BaseMapper<Audit> {
//1
    @Select("<script>" +
            "SELECT * FROM audit" +
            "<where>" +
            "<if test='e.audtypeId != null'>AND audtypeId=#{e.audtypeId}</if>" +
            "<if test='e.submituserId != null'>AND submituserId=#{e.submituserId}</if>" +
            "<if test='e.status != null'>AND status=#{e.status}</if>" +
            "<if test='e.auditoruserId != null'>AND auditoruserId=#{e.auditoruserId}</if>" +
            "<if test='e.updatetime != null'>AND updatetime LIKE CONCAT(#{e.updatetime},'%')</if>" +
            "</where>" +
            "</script>")
    IPage<Audit> getAuditsByFinds(Page<?> page, @Param("e") Audit audit);

//2
    @Results({
            @Result(property = "auditId",column = "auditId",id = true),
            @Result(property = "audtype",one = @One(select = "com.fzipp.pay.mapper.AudtypeMapper.getAudtypeById"),column = "audtypeId"),
            @Result(property = "submituser",one = @One(select = "com.fzipp.pay.mapper.UserMapper.getUserInfoById"),column = "submituserId"),
            @Result(property = "auditoruser",one = @One(select = "com.fzipp.pay.mapper.UserMapper.getUserInfoById"),column = "auditoruserId")
    })
    @Select("SELECT * FROM audit WHERE auditId=#{auditId}")
    AduitInfo getAuditInfoById(Integer auditId);

//3
    @Results({
            @Result(property = "auditId",column = "auditId",id = true),
            @Result(property = "audtypeId",column = "audtypeId"),
            @Result(property = "submituserId",column = "submituserId"),
            @Result(property = "auditoruserId",column = "auditoruserId"),
            @Result(property = "audtype",one = @One(select = "com.fzipp.pay.mapper.AudtypeMapper.getAudtypeById"),column = "audtypeId"),
            @Result(property = "submituser",one = @One(select = "com.fzipp.pay.mapper.UserMapper.getUserInfoById"),column = "submituserId"),
            @Result(property = "auditoruser",one = @One(select = "com.fzipp.pay.mapper.UserMapper.getUserInfoById"),column = "auditoruserId")
    })
    @Select("<script>" +
            "SELECT * FROM audit" +
            "<where>" +
            "<if test='e.audtypeId != null'> AND audtypeId=#{e.audtypeId} </if>" +
            "<if test='e.submituserId != null'> AND submituserId=#{e.submituserId} </if>" +
            "<if test='e.status != null'> AND status=#{e.status} </if>" +
            "<if test='e.auditoruserId != null'> AND auditoruserId=#{e.auditoruserId} </if>" +
            "<if test='e.updatetime != null'> AND updatetime LIKE CONCAT(#{e.updatetime},'%') </if>" +
            "</where>" +
            "</script>")
    IPage<AduitInfo> getAduitInfoByFinds(Page page, @Param("e") Audit audit);


}
