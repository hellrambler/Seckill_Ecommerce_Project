package com.seckillproject.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ValidationRes {

    //whether the validation passes
    private boolean hasErr = false;

    //save err info
    private Map<String, String> errMsgMap = new HashMap<>();

    public boolean isHasErr() {
        return hasErr;
    }

    public void setHasErr(boolean hasErr) {
        this.hasErr = hasErr;
    }

    public Map<String, String> getErrMsgMap() {
        return errMsgMap;
    }

    public void setErrMsgMap(Map<String, String> errMsgMap) {
        this.errMsgMap = errMsgMap;
    }

    // output commonly formatted err msg
    public String getErrMsg(){
        return StringUtils.join(errMsgMap.values().toArray(), ", ");
    }
}
