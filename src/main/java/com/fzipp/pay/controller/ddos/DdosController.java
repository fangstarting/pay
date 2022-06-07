package com.fzipp.pay.controller.ddos;

import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.common.ddos.DdosBean;
import com.fzipp.pay.params.ddos.DdosStartParam;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.service.myService.AuthorityCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program pay
 * @description 远程攻击
 * @Author FFang
 * @Create 2022-06-07 04:34
 */
@RestController
@RequestMapping("/ddos")
public class DdosController {
    private static final String HEAD_PATH = "/ddos/**";

    @Autowired
    private AuthorityCertificationService authorityCertificationService;

    @PostMapping("/start")
    public Result startDdos(@RequestBody DdosStartParam param){
        Result result = new Result();
        String mappingPath = "/ddos/start";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        if (param.getApi()==null||"".equals(param.getApi())) {
            result.setError(ErrorCode.WARNING);
            result.setMessage(Messages.PARAMS_EX);
            return result;
        }
        try {
            if (DdosBean.isDdosBean()){
                result.setError(ErrorCode.WARNING);
                result.setMessage("已经开启远程攻击，若修改参数请先停止攻击！");
                return result;
            }
            String startDDOS = DdosBean.startDDOS(param);
            result.setMessage(startDDOS);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/stop")
    public Result stopDdos(){
        Result result = new Result();
        String mappingPath = "/ddos/stop";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            if (!DdosBean.isDdosBean()){
                result.setError(ErrorCode.WARNING);
                result.setMessage("暂无远程攻击任务！");
                return result;
            }
            String stop = DdosBean.ddosObj().stop();
            result.setMessage(stop);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }
}
