package com.fzipp.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzipp.pay.common.utils.DateUtil;
import com.fzipp.pay.entity.Position;
import com.fzipp.pay.mapper.PositionMapper;
import com.fzipp.pay.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements PositionService {

    @Autowired
    private PositionMapper positionMapper;

    @Override
    public boolean addPosition(Position position) {
        position.setUpdatetime(DateUtil.getSqlDate());
        Integer insert = positionMapper.insert(position);
        return insert>0?true:false;
    }

    @Override
    public List<Position> getINSByDeptId(Integer deptId) {
        List<Position> insByDeptId = positionMapper.getINSByDeptId(deptId);
        return insByDeptId;
    }
}
