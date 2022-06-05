package com.fzipp.pay.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzipp.pay.common.utils.PageUtil;
import com.fzipp.pay.entity.*;
import com.fzipp.pay.entity.child.AduitInfo;
import com.fzipp.pay.mapper.AuditMapper;
import com.fzipp.pay.results.audit.ExtraworkInfo;
import com.fzipp.pay.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-28
 */
@Service
public class AuditServiceImpl extends ServiceImpl<AuditMapper, Audit> implements AuditService {

    @Autowired
    private AuditMapper auditMapper;
    @Autowired
    private AudtypeService audtypeService;

    @Override
    public Map<String, Object> getAuditsByFinds(Audit audit, Integer pageNum, Integer pageSize){
        Map<String,Object> condition = new HashMap<>();
        System.out.println(audit);
        System.out.println("pageNum:"+pageNum+",pageSize:"+pageSize);
        condition.put("audtypeId",audit.getAudtypeId());
        condition.put("submituserId",audit.getSubmituserId());
        condition.put("status",audit.getStatus());
        condition.put("auditoruserId",audit.getAuditoruserId());
        condition.put("updatetime",audit.getUpdatetime());
        List<Audit> audits = auditMapper.selectByMap(condition);
        if (!(pageNum > 0 && pageSize > 0)){
            pageNum = 1;
            pageSize = 20;
        }
//        audit.setUpdatetime(DateUtil.getSqlInDate("20211223")); //可模糊查询日期
//        System.out.println(audit.getUpdatetime());

        //方式1
//        IPage<Audit> auditsByFinds = auditMapper.getAuditsByFinds(new Page<Audit>(pageNum, pageSize), audit);
        //方式2
        IPage<AduitInfo> iPage = auditMapper.getAduitInfoByFinds(new Page<AduitInfo>(pageNum, pageSize), audit);

        List<Object> list = new ArrayList<>();
        for (AduitInfo record : iPage.getRecords()) {
            HashMap<String,Object> hm = new HashMap<>();

            hm.put("auditId",record.getAuditId());

            try {
                HashMap<String,Object> audtype = new HashMap<>();
                audtype.put("audtypeId",record.getAudtype().getAudtypeId());
                audtype.put("name",record.getAudtype().getName());
                hm.put("audtype", audtype);
            } catch (Exception e) {
                hm.put("audtype", null);
            }

            try {
                HashMap<String,Object> submituser = new HashMap<>();
                submituser.put("userId",record.getSubmituserId());
                submituser.put("realname",record.getSubmituser().getRealname());
                HashMap<String,Object> position = new HashMap<>();
                position.put("positionId",record.getSubmituser().getPosition().getPositionId());
                position.put("pname",record.getSubmituser().getPosition().getPname());
                submituser.put("position",position);
                hm.put("submituser", submituser);
            } catch (Exception e) {
                hm.put("submituser", null);
            }

            hm.put("notes",record.getNotes());

            hm.put("status", record.getStatus());

            try {
                HashMap<String,Object> auditoruser = new HashMap<>();
                auditoruser.put("userId",record.getAuditoruserId());
                auditoruser.put("realname",record.getAuditoruser().getRealname());
                HashMap<String,Object> position1 = new HashMap<>();
                position1.put("positionId",record.getAuditoruser().getPosition().getPositionId());
                position1.put("pname",record.getAuditoruser().getPosition().getPname());
                auditoruser.put("position",position1);
                hm.put("auditoruser", auditoruser);
            } catch (Exception e) {
                hm.put("auditoruser", null);
            }

            hm.put("updatetime", record.getUpdatetime());

            list.add(hm);
        }

//        HashMap<String,Object> page = new HashMap<>();
//        page.put("current",iPage.getCurrent());
//        page.put("total",iPage.getTotal());
//        page.put("size",iPage.getSize());
//        page.put("pages",iPage.getPages());
//        page.put("data",list);

        return PageUtil.getPage(iPage,list);
    }

    @Override
    public AduitInfo getAuditInfoById(Integer auditId) {
        AduitInfo auditInfoById = auditMapper.getAuditInfoById(auditId);
        return auditInfoById;
    }

    @Autowired
    private UserService userService;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private ExtraworkService extraworkService;
    @Autowired
    private EwtypeService ewtypeService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private DeptService deptService;


    @Override
    public Map<String,Object> getAuditInfo(Integer auditId) {
        Map<String,Object> map = new HashMap<>();
        Object auditInfo = null;
        //获取类型表及id => 指定数据对象
        Audit audit = super.getById(auditId);
        Audtype audtype = audtypeService.getById(audit.getAudtypeId());
        Integer dataId = audit.getDataId();//id
        String tableName = audtype.getTablename();//数据表
        //对指定数据对象查询 表-id
        if("User".equalsIgnoreCase(tableName)){
            auditInfo = userService.getById(dataId);
        }
        if("Leave".equalsIgnoreCase(tableName)){
            auditInfo = leaveService.getById(dataId);
        }
        if("Extrawork".equalsIgnoreCase(tableName)){
            Extrawork byId = extraworkService.getById(dataId);
            Ewtype one = ewtypeService.getOne(new QueryWrapper<Ewtype>().select("name").eq("ewtypeId", byId.getEwtypeId()));
            ExtraworkInfo extraworkInfo = new ExtraworkInfo();
            BeanUtils.copyProperties(byId,extraworkInfo);
            extraworkInfo.setTypeName(one.getName());
            Integer userId = byId.getUserId();
            User u = userService.getOne(new QueryWrapper<User>().select("realname","deptId", "positionId").eq("userId", userId));
            String dname = deptService.getOne(new QueryWrapper<Dept>().select("dname").eq("deptId", u.getDeptId())).getDname();
            String pname = positionService.getOne(new QueryWrapper<Position>().select("pname").eq("positionId", u.getPositionId())).getPname();
            extraworkInfo.setRealname(u.getRealname());
            extraworkInfo.setDname(dname);
            extraworkInfo.setPname(pname);
            extraworkInfo.setDId(u.getDeptId());
            extraworkInfo.setPId(u.getPositionId());
            auditInfo = extraworkInfo;
        }
        map.put("audtypeId",audit.getAudtypeId());
        map.put("auditInfo",auditInfo);
        return map;
    }
}
