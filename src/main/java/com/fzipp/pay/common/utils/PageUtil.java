package com.fzipp.pay.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PageInfo
 * @Description
 * @Author 24k
 * @Date 2021/12/30 16:24
 * @Version 1.0
 */
public class PageUtil {

    /**
     * 自定义分页格式
     * @param iPage
     * @param data
     * @return
     */
    public static Map<String,Object> getPage(IPage<?> iPage,List<?> data){
        Map<String,Object> page = new HashMap<>();
        page.put("current",iPage.getCurrent());
        page.put("total",iPage.getTotal());
        page.put("size",iPage.getSize());
        page.put("pages",iPage.getPages());
        if(data==null){
            page.put("data",iPage.getRecords());
        }else  page.put("data",data);
        return page;
    }

}
