package com.example.fypintelidea.core.providers.models;

import com.orm.SugarRecord;

import java.util.ArrayList;

public class Permission extends SugarRecord {
    private String serverId;
    private String name;

    public Permission() {
    }

    public Permission(String serverId, String name) {
        this.serverId = serverId;
        this.name = name;
    }

    public static Permission getPermissionByServerId(String id) {
        ArrayList<Permission> list = null;
        try {
            list = (ArrayList<Permission>) Permission.find(Permission.class, "server_id = ? ", id);
        } catch (IllegalArgumentException e) {
            return null;
        }
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClassPojo [serverId = " + serverId + ", name = " + name + "]";
    }
}