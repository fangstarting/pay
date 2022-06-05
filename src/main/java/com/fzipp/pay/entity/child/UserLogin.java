package com.fzipp.pay.entity.child;

import com.fzipp.pay.entity.User;
import lombok.Data;

@Data
public class UserLogin {
    private User user;
    private Integer accountId;
    private String username;
    private Integer loginStatus;
}
