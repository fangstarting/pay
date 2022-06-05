package com.fzipp.pay.service.myService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fzipp.pay.common.utils.RequestUtil;
import com.fzipp.pay.entity.Power;
import com.fzipp.pay.entity.RolePower;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.service.PowerService;
import com.fzipp.pay.service.RolePowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 后端接口权限认证服务
 */
@Service
public class AuthorityCertificationService {

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private RolePowerService rolePowerService;

    @Autowired
    private PowerService powerService;

    @Value("${pay.power.admin}")
    private String adminPath;

    /**
     * 权限判断
     * @param headPath
     * @param mappingPath
     * @return 通过：true ,否则：false
     */
    public Boolean verifyPath(String headPath,String mappingPath){
        //权限判断
        User user = requestUtil.getUser();
        QueryWrapper<RolePower> wrapper = new QueryWrapper<>();
        wrapper.eq("roleId",user.getRoleId());
        List<RolePower> list = rolePowerService.list(wrapper);
        if (list.size()<=0)return false;
        List<Integer> powerIds = new ArrayList<>();
        list.forEach(e->powerIds.add(e.getPowerId()));
        List<Power> powers = powerService.listByIds(powerIds);
        for (Power power : powers) {
            String path = power.getPath();
            if (headPath.equalsIgnoreCase(path)||mappingPath.equalsIgnoreCase(path)||adminPath.equalsIgnoreCase(path)){
                return true;
            }
        }
        return false;
    }

}
