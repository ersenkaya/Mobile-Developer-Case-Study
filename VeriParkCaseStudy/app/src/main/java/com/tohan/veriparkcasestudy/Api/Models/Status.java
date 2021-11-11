package com.tohan.veriparkcasestudy.Api.Models;

import java.util.HashMap;
import java.util.Map;


public class Status {

    private Boolean isSuccess;
    private Error error;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }


}
