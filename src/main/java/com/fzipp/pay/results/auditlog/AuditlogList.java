package com.fzipp.pay.results.auditlog;

import com.fzipp.pay.entity.Auditlog;
import lombok.Data;

@Data
public class AuditlogList extends Auditlog {
    private String realname;
}
