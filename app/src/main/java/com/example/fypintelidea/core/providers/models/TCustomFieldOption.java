package com.example.fypintelidea.core.providers.models;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.ArrayList;

public class TCustomFieldOption extends SugarRecord implements Serializable {

    private String name;

    private String serverId;

    private TCustomField customField;

    public static TCustomFieldOption getCustomFieldOptionFromServerId(String id) {
        ArrayList<TCustomFieldOption> list = null;
        try {
            list = (ArrayList<TCustomFieldOption>) TCustomFieldOption.find(TCustomFieldOption.class, "server_id = ? ", id);
        } catch (IllegalArgumentException e) {
            return null;
        }
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public TCustomFieldOption() {
    }

    public TCustomFieldOption(String name, TCustomField tCustomField) {
        this.name = name;
        this.customField = tCustomField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public TCustomField getCustomField() {
        return customField;
    }

    public void setCustomField(TCustomField customField) {
        this.customField = customField;
    }
}
