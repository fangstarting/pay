package com.fzipp.pay.entity.child;

import com.fzipp.pay.entity.Position;
import com.fzipp.pay.entity.User;

/**
 * @ClassName UserInfo
 * @Description
 * @Author 24k
 * @Date 2021/12/29 15:27
 * @Version 1.0
 */
public class UserInfo extends User {
    private Position position;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
