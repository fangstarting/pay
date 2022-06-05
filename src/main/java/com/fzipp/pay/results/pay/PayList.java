package com.fzipp.pay.results.pay;

import com.fzipp.pay.entity.Pay;
import lombok.Data;

@Data
public class PayList extends Pay {
    private String realName;
    private String deptName;
    private String jobTitle;
}
