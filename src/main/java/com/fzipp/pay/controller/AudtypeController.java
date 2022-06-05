package com.fzipp.pay.controller;


import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.entity.Audtype;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.service.AudtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-28
 */
@RestController
@RequestMapping("/audtype")
public class AudtypeController {

    @Autowired
    private AudtypeService audtypeService;

    @RequestMapping("/addaudtype")
    public Map<String,Object> methdoName(@RequestBody Audtype audtype){
        Map<String,Object> map = new HashMap<>();
        audtype.setUpdatetime(new Date());
        boolean b = audtypeService.save(audtype);
        map.put("errorcode", ErrorCode.VERIFY_BIZ(b));
        return map;
    }

    @GetMapping("/list")
    public Result getList(){
        Result result = new Result();
        try {
            List<Audtype> list = audtypeService.list();
            result.setData(list);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.CORRECT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }
}

