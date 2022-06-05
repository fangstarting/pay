package com.fzipp.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fzipp.pay.entity.Audtype;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-28
 */
public interface AudtypeMapper extends BaseMapper<Audtype> {

    @Select("SELECT * FROM audtype WHERE audtypeId=#{audtypeId}")
    Audtype getAudtypeById(Integer audtypeId);
}
