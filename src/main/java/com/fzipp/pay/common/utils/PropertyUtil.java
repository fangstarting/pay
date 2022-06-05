package com.fzipp.pay.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertyUtil {
    /**
     * 读取 classpath 下 指定的properties配置文件，加载到Properties并返回Properties
     * @param name 配置文件名，如：mongo.properties
     * @return
     */
    public static Properties getConfig(String name){
        Properties props=null;
        try{
            props = new Properties();
            InputStream in = PropertyUtil.class.getClassLoader().getResourceAsStream(name);
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            props.load(bf);
            in.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return props;
    }

    public static String getPropValue(Properties prop,String key){
        if(key == null || "".equals(key.trim())){
            return null;
        }
        String value = prop.getProperty(key);
        if(value == null){
            return null;
        }
        value = value.trim();
        //判断是否是环境变量配置属性,例如 server.env=${serverEnv:local}
        if(value.startsWith("${") && value.endsWith("}") && value.contains(":")){
            int indexOfColon = value.indexOf(":");
            String envName = value.substring(2,indexOfColon);
            //获取系统环境变量 envName 的内容，如果没有找到，则返回defaultValue
            String envValue = System.getenv(envName);
            if(envValue == null){
                //配置的默认值
                return value.substring(indexOfColon+1,value.length()-1);
            }
            return envValue;
        }
        return value;
    }

    public static void main(String[] args) {
        Properties prop = PropertyUtil.getConfig("powerPath.properties");
        //
        System.out.println(prop.getProperty("power.payconfig"));

        //建议采用下面这种获取方法，能够处理 环境变量配置属性 例如 server.env=${serverEnv:local}
        System.out.println( PropertyUtil.getPropValue( prop , "spring.data.mongodb.database" ) );
    }
}
