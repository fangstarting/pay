package com.fzipp.pay.results.audit;

import com.fzipp.pay.entity.Extrawork;
import lombok.Data;

@Data
public class ExtraworkInfo extends Extrawork {
    private String typeName;
    private String realname;
    private String dname;
    private Integer dId;
    private String pname;
    private Integer pId;
}
