package com.fzipp.pay.controller;


import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.entity.Ewtype;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.service.EwtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@RestController
@RequestMapping("/ewtype")
public class EwtypeController {

    @Autowired
    private EwtypeService ewtypeService;

    @GetMapping("/list")
    public Result list(){
        Result result = new Result();
        try {
            List<Ewtype> list = ewtypeService.list();
            result.setError(ErrorCode.CORRECT);
            result.setData(list);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

}

