package com.example.fypintelidea.core.providers.models;

import com.google.gson.annotations.SerializedName;

public class RootWorkorder {

    @SerializedName("work_order")
    private Workorder work_order;


    public RootWorkorder(Workorder work_order) {
        this.work_order = work_order;
    }

    public Workorder getWork_order() {
        return work_order;
    }

    public void setWork_order(Workorder work_order) {
        this.work_order = work_order;
    }
}
