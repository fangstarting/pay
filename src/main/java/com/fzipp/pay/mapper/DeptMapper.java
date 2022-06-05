package com.fzipp.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fzipp.pay.entity.Dept;
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
public interface DeptMapper extends BaseMapper<Dept> {

    @Select("SELECT deptId,dname FROM dept")
    List<Dept> getDeptsOfIdName();

}
