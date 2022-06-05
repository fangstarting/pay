package com.fzipp.pay.params.check;

import com.fzipp.pay.params.PageParam;
import lombok.Data;

import java.util.List;

@Data
public class CheckListsParam extends PageParam {
    private Integer userId;
    private List<String> date;
    private Integer status;
}
