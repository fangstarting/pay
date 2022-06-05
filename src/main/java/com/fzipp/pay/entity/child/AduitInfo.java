package com.fzipp.pay.entity.child;

import com.fzipp.pay.entity.Audit;
import com.fzipp.pay.entity.Audtype;

/**
 * @ClassName AduitInfo
 * @Description
 * @Author 24k
 * @Date 2021/12/29 14:41
 * @Version 1.0
 */
public class AduitInfo extends Audit {
    private Audtype audtype;
    private UserInfo submituser;
    private UserInfo auditoruser;

    public Audtype getAudtype() {
        return audtype;
    }

    public void setAudtype(Audtype audtype) {
        this.audtype = audtype;
    }

    public UserInfo getSubmituser() {
        return submituser;
    }

    public void setSubmituser(UserInfo submituser) {
        this.submituser = submituser;
    }

    public UserInfo getAuditoruser() {
        return auditoruser;
    }

    public void setAuditoruser(UserInfo auditoruser) {
        this.auditoruser = auditoruser;
    }
}
