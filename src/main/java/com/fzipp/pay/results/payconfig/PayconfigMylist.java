package com.fzipp.pay.results.payconfig;

import com.fzipp.pay.entity.Payconfig;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PayconfigMylist {
    private Integer userId;
    private String realname;
    private Integer workage;
    private BigDecimal basepay;
    private String jobtitle;
    private BigDecimal jobbonus;
    private List<Payconfig> payconfigs;
}
