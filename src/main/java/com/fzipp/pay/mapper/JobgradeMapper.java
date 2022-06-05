package com.fzipp.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fzipp.pay.entity.Jobgrade;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
public interface JobgradeMapper extends BaseMapper<Jobgrade> {

    @Select("SELECT jobgradeId,jobtitle FROM jobgrade")
    List<Jobgrade> getJobgradesOfIdTitle();

}
