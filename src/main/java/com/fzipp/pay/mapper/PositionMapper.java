package com.fzipp.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fzipp.pay.entity.Position;
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
public interface PositionMapper extends BaseMapper<Position> {

    @Select("SELECT positionId,pname,status FROM position WHERE deptId=#{deptId}")
    List<Position> getINSByDeptId(Integer deptId);

    @Select("SELECT * FROM position WHERE positionId=#{positionId}")
    Position getPositionById(Integer positionId);
}
