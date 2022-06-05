package com.fzipp.pay.results.user;

import com.fzipp.pay.entity.Dept;
import com.fzipp.pay.entity.Position;
import com.fzipp.pay.entity.Role;
import com.fzipp.pay.entity.User;

public class GetUserInfo {
    private User user;
    private Role role;
    private Dept dept;
    private Position position;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
