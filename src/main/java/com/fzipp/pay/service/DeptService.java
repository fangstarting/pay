package com.fzipp.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fzipp.pay.entity.Dept;
import com.fzipp.pay.entity.child.DeptInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
public interface DeptService extends IService<Dept> {

    /**
     * 添加部门
     * @param dept
     * @return
     */
    boolean addDept(Dept dept);

    /**
     * 返回depts{deptId,dname}
     * @return
     */
    List<Dept> getDeptsOfIdName();

    List<DeptInfo> getDeptInfos(Dept dept);
}
