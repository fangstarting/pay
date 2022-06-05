package com.fzipp.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzipp.pay.common.utils.PojoToQWrapperUtil;
import com.fzipp.pay.entity.Dept;
import com.fzipp.pay.entity.child.DeptInfo;
import com.fzipp.pay.mapper.DeptMapper;
import com.fzipp.pay.mapper.UserMapper;
import com.fzipp.pay.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean addDept(Dept dept) {
        Integer insert = deptMapper.insert(dept);
        return insert>0?true:false;
    }

    @Override
    public List<Dept> getDeptsOfIdName() {
        List<Dept> deptsOfIdName = deptMapper.getDeptsOfIdName();
        return deptsOfIdName;
    }

    @Override
    public List<DeptInfo> getDeptInfos(Dept dept) {
        if ("".equals(dept.getDname()))dept.setDname(null);
        if ("".equals(dept.getLoc()))dept.setLoc(null);
        QueryWrapper<Dept> wrapper = new QueryWrapper<>();
        QueryWrapper qWrapperOfPojo = PojoToQWrapperUtil.getQWrapperOfPojo(dept, wrapper);
        List<Dept> depts = deptMapper.selectList(qWrapperOfPojo);
        List<DeptInfo> deptInfos = new ArrayList<>();
        for (Dept d : depts) {
            DeptInfo e = new DeptInfo();
            e.setDeptId(d.getDeptId());
            e.setUpdatetime(d.getUpdatetime());
            e.setDname(d.getDname());
            e.setLoc(d.getLoc());
            e.setHeaduserId(d.getHeaduserId());
            e.setNotes(d.getNotes());
            e.setHeadName(userMapper.selectById(d.getHeaduserId()).getRealname());
            deptInfos.add(e);
        }
        return deptInfos;
    }
}
