package com.example.fypintelidea.core.providers.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TCustomField extends SugarRecord implements Serializable {

    @Ignore
    public static final String FIELD_TYPE_TEXT = "text";
    @Ignore
    public static final String FIELD_TYPE_NUMERIC = "numeric";
    @Ignore
    public static final String FIELD_TYPE_SELECT = "select";

    private String name;

    private String fieldValue;

    private String fieldType;

    private String customFieldOptionId;

    private String type;

    private int sort;

    private boolean required;

    private String serverId;

    private String syncStatus;

    private String entity;

    public TCustomField() {
    }

    public static void resetAllCustomFieldsValues() {
        ArrayList<TCustomField> list = null;
        try {
            list = (ArrayList<TCustomField>) TCustomField.findWithQuery(TCustomField.class, "update T_CUSTOM_FIELD SET custom_field_option_id = null, field_value = null");
        } catch (IllegalArgumentException e) {
        }
    }

    public static TCustomField getColumnFromServerId(String id) {
        ArrayList<TCustomField> list = null;
        try {
            list = (ArrayList<TCustomField>) TCustomField.find(TCustomField.class, "server_id = ? ", id);
        } catch (IllegalArgumentException e) {
            return null;
        }
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static List<TCustomField> getAllAssetsCustomFields() {
        ArrayList<TCustomField> list = null;
        try {
            list = (ArrayList<TCustomField>) Select.from(TCustomField.class).where(Condition.prop("entity").eq("assets")).orderBy("sort DESC").list();
        } catch (IllegalArgumentException e) {
            return null;
        }
        return list;
    }

    public static List<TCustomField> getAllWOCustomFields() {
        ArrayList<TCustomField> list = null;
        try {
            list = (ArrayList<TCustomField>) Select.from(TCustomField.class).where(Condition.prop("entity").eq("wo")).orderBy("sort DESC").list();
        } catch (IllegalArgumentException e) {
            return null;
        }
        return list;
    }

    public static List<TCustomField> getAllCustomFieldsWithRequiredOnesOnTop() {
        ArrayList<TCustomField> list = null;
        try {
            list = (ArrayList<TCustomField>) TCustomField.findWithQuery(TCustomField.class, "select * from T_CUSTOM_FIELD ORDER BY required DESC");
        } catch (IllegalArgumentException e) {
            return null;
        }
        return list;
    }

    public static List<TCustomField> getAllRequiredCustomFields() {
        ArrayList<TCustomField> list = null;
        try {
            list = (ArrayList<TCustomField>) TCustomField.findWithQuery(TCustomField.class, "select * from T_CUSTOM_FIELD where required = 1");
        } catch (IllegalArgumentException e) {
            return null;
        }
        return list;
    }

    public ArrayList<TCustomFieldOption> getAllCustomFieldOptions() {
        ArrayList<TCustomFieldOption> allCustomFieldOptionsOfThisCustomField = null;
        allCustomFieldOptionsOfThisCustomField = (ArrayList<TCustomFieldOption>) TCustomFieldOption.findWithQuery(TCustomFieldOption.class, "Select * from T_CUSTOM_FIELD_OPTION where custom_field = '" + getId() + "'");
        return allCustomFieldOptionsOfThisCustomField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }


    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getCustomFieldOptionId() {
        return customFieldOptionId;
    }

    public void setCustomFieldOptionId(String customFieldOptionId) {
        this.customFieldOptionId = customFieldOptionId;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
