package com.fzipp.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fzipp.pay.entity.Position;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
public interface PositionService extends IService<Position> {

    /**
     * 添加职位
     * @param position
     * @return
     */
    boolean addPosition(Position position);

    /**
     * 返回List<position> {positionId,pname,status}
     * @return
     */
    List<Position> getINSByDeptId(Integer deptId);
}
