package com.server.pollingapp.request;

import javax.validation.constraints.NotNull;

public class ChoiceRequest {
    public ChoiceRequest(String option) {
        this.option = option;
    }

    @NotNull(message = "Choice cannot be Null")
    private String option;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
