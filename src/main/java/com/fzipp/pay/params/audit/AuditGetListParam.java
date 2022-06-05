package com.fzipp.pay.params.audit;

import com.fzipp.pay.params.PageParam;
import lombok.Data;

@Data
public class AuditGetListParam extends PageParam {
    private Integer audtypeId;
    private Integer status;
}
