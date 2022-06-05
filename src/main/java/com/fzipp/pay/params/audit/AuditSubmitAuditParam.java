package com.fzipp.pay.params.audit;

import com.fzipp.pay.entity.Extrawork;
import com.fzipp.pay.entity.Leave;
import lombok.Data;

@Data
public class AuditSubmitAuditParam {
    private String audtype;
    private String notes;
    private Leave leavex;
    private Extrawork extrawork;
}
