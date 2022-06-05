package com.fzipp.pay.params.pay;

import com.fzipp.pay.params.PageParam;
import lombok.Data;

@Data
public class PayListsParam extends PageParam {
    private Integer userId;
    private String date;
    private Integer status;
}
