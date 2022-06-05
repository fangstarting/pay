package com.fzipp.pay.params.account;

import com.fzipp.pay.entity.Account;
import lombok.Data;

@Data
public class RegParam extends Account {

    private String id;
    private String verifyCode;

}
