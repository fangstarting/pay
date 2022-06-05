package com.fzipp.pay.results.payconfig;

import com.fzipp.pay.entity.Jobgrade;
import com.fzipp.pay.entity.Payconfig;

public class PayconfigList extends Payconfig {
    private Jobgrade jobgrade;

    public Jobgrade getJobgrade() {
        return jobgrade;
    }

    public void setJobgrade(Jobgrade jobgrade) {
        this.jobgrade = jobgrade;
    }
}
