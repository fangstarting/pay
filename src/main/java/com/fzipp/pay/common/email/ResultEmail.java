package com.fzipp.pay.common.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultEmail implements Serializable {

    private ToEmail toEmail;

    private Boolean status;

    private String message;

}
