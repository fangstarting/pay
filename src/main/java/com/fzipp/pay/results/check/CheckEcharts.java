package com.fzipp.pay.results.check;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckEcharts {

    private String date;
    private Integer theory;//理论考勤
    private Integer actual;//实际出勤
    private Integer absenteeism;//异常缺勤
    private Integer late;//迟到人数
    private Integer early;//早退人数

    public CheckEcharts(Integer theory, Integer actual, Integer absenteeism, Integer late, Integer early) {
        this.theory = theory;
        this.actual = actual;
        this.absenteeism = absenteeism;
        this.late = late;
        this.early = early;
    }
}
