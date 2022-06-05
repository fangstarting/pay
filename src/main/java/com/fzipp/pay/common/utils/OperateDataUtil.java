package com.fzipp.pay.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <K> 入参类型
 * @param <V> 返回类型
 */
public class OperateDataUtil {

    /**
     * 集合数据类型转换
     * @param list
     * @param clzOut
     * @param <I>
     * @param <O>
     * @return
     */
    public static <I, O> List<O> optionShiftList(List<I> list, Class<O> clzOut) {
        List<O> data = new ArrayList<>();
        try {
            for (I i : list) {
                O o = clzOut.newInstance();
                BeanUtils.copyProperties(i, o);
                data.add(o);
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private <Out, In> List<Out> op(List<In> list,Class<Out> clz){
        try {
            List<Out> data = new ArrayList<>();
            for (In in : list) {
                Out out = clz.newInstance();
                BeanUtils.copyProperties(in,out);
                data.add(out);
            }
            return data;
        }catch (Exception e){
            return null;
        }
    }
}
