package com.fzipp.pay.entity.child;

import com.fzipp.pay.entity.Dept;

/**
 * @ClassName DeptInfo
 * @Description
 * @Author 24k
 * @Date 2022/1/3 17:20
 * @Version 1.0
 */
public class DeptInfo extends Dept {
    private String headName;

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    @Override
    public String toString() {
        return "DeptInfo{" +
                "headName='" + headName + '\'' +
                "} " + super.toString();
    }
}
