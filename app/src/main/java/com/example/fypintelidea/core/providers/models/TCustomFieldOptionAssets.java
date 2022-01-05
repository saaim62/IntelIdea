package com.example.fypintelidea.core.providers.models;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.ArrayList;

public class TCustomFieldOptionAssets extends SugarRecord implements Serializable {

    private String name;

    private String serverId;

    private TCustomFieldAssets customField;

    public static TCustomFieldOptionAssets getCustomFieldOptionFromServerId(String id) {
        ArrayList<TCustomFieldOptionAssets> list = null;
        try {
            list = (ArrayList<TCustomFieldOptionAssets>) TCustomFieldOptionAssets.find(TCustomFieldOptionAssets.class, "server_id = ? ", id);
        } catch (IllegalArgumentException e) {
            return null;
        }
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public TCustomFieldOptionAssets() {
    }

    public TCustomFieldOptionAssets(String name, TCustomFieldAssets tCustomField) {
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

    public TCustomFieldAssets getCustomField() {
        return customField;
    }

    public void setCustomField(TCustomFieldAssets customField) {
        this.customField = customField;
    }
}
