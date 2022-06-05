package com.fzipp.pay.controller;


import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.service.PowerService;
import com.fzipp.pay.service.myService.AuthorityCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@RestController
@RequestMapping("/power")
public class PowerController {
    private static final String HEAD_PATH = "/power/**";
    @Autowired
    private AuthorityCertificationService authorityCertificationService;

    @Autowired
    private PowerService powerService;

    @GetMapping("/powers")
    public Result getPowers(){
        Result result = new Result();
        try {
            result.setData(powerService.list());
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

}

