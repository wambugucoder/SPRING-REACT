package com.server.pollingapp.request;

import javax.validation.constraints.NotNull;

public class ChoiceRequest {

    @NotNull(message = "Choice cannot be Null")
    private String option;

    public String getOption() {
        return option;
    }
}
