package com.fzipp.pay.results.audit;

import com.fzipp.pay.entity.Audit;
import lombok.Data;

@Data
public class AuditMyList extends Audit {
    private String auditoruserName;
}
