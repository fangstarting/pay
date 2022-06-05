package com.fzipp.pay.entity.child;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerInfo {
    private String id;
    private String email;
    private String code;
}
