package com.fzipp.pay.common.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName PojoToQWrapperUtil
 * @Description Mybatis Plus根据传入的对象查询
 * @Author 24k
 * @Date 2021/12/31 15:25
 * @Version 1.0
 */
public class PojoToQWrapperUtil {

    /**
     * 反射实现Pojo转换成Mybatis-plus的条件构造器QueryWrapper
     * @param T 实体类型
     * @param wrapper 构造器对象
     * @param <T>
     * @return
     */
    public static<T> QueryWrapper getQWrapperOfPojo(T T, QueryWrapper<T> wrapper){
        for (Field field : T.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                //序列化 字段不需要查询
                if("serialVersionUID".equals(field.getName())){
                    continue;
                }
                //属性为空，不用查询
                if(field.get(T) == null){
                    continue;
                }
                //主键 注解TableId
                TableId tableId = field.getAnnotation(TableId.class);
                if (tableId != null){
                    //主键
                    wrapper.eq(tableId.value(),field.get(T));
                    continue;
                }
                //数据库中字段名和实体类属性不一致 注解TableField
                TableField tableField = field.getAnnotation(TableField.class);
                if(tableField != null){
                    if(tableField.exist()){
                        wrapper.eq(tableField.value(),field.get(T));
                    }// @TableField(exist = false) 不是表中内容 不形成查询条件
                    continue;
                }
                //数据库中字段名和实体类属性一致
//                wrapper.eq(humpToLine(field.getName()),field.get(T)); //调用humpToLine转换驼峰
                wrapper.eq(field.getName(),field.get(T)); //无需转换驼峰
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return wrapper;
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");
    /**
     * humpToLine(field.getName())
     * 这个的作用是把驼峰转为下滑线,按命名规范,根据对象查询时,对象的属性都是驼峰形式的,查询的时候,要转为数据库中的下划线格式
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
