package com.example.fypintelidea.core.permissions;

import android.content.Context;

import com.example.fypintelidea.core.providers.models.Permission;
import com.example.fypintelidea.core.session.SessionManager;
import com.example.fypintelidea.core.providers.models.Workorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class PermissionsHelper {

    public static final String PERMISSION_MANAGE_ALL = "manage all";
    private static final String PERMISSION_CREATE_REQUEST = "create request";
    private static final String PERMISSION_CREATE_ASSETS = "create assets";
    private static final String PERMISSION_VIEW_REQUEST = "view request";
    private static final String PERMISSION_VIEW_OWN_WORKORDERS = "view own workorders";
    private static final String PERMISSION_VIEW_OWN_ASSETS = "view own assets";
    private static final String PERMISSION_VIEW_TEAM_WORKORDERS = "view team workorders";
    private static final String PERMISSION_VIEW_TEAM_ASSETS = "view team assets";
    private static final String PERMISSION_CREATE_WORKORDER = "create workorder";
    private static final String PERMISSION_INPROGRESS_OWN_WORKORDERS = "in progress own workorders";
    private static final String PERMISSION_INPROGRESS_TEAM_WORKORDERS = "in progress team workorders";
    private static final String PERMISSION_COMPLETE_OWN_WORKORDERS = "complete own workorders";
    private static final String PERMISSION_COMPLETE_TEAM_WORKORDERS = "complete team workorders";
    private static final String PERMISSION_VIEW_ALL_WORKORDERS = "view all workorders";
    private static final String PERMISSION_VIEW_ALL_ASSETS = "view all assets";
    private static final String PERMISSION_INPROGRESS_ALL_WORKORDERS = "in progress all workorders";
    private static final String PERMISSION_COMPLETE_ALL_WORKORDERS = "complete all workorders";
    private static final String PERMISSION_ADD_SPARE_PARTS_ON_WORKORDER_CREATE = "add spare parts on workorder create";
    private static final String PERMISSION_ADD_SPARE_PARTS_ON_WORKORDER_COMPLETE = "add spare parts on workorder complete";

    public static boolean canInProgressWorkOrder(Context mContext, Workorder workorder) {
        //check if it has permission to inprogress all workorders
        if (actionAllowed(PermissionsHelper.PERMISSION_INPROGRESS_ALL_WORKORDERS)) {
            return true;
        } else if (PermissionsHelper.actionAllowed(PermissionsHelper.PERMISSION_INPROGRESS_TEAM_WORKORDERS)) {
            SessionManager sessionManager = new SessionManager(mContext);
            sessionManager.getArrayList(SessionManager.KEY_LOGIN_TEAM_IDS);
            if (workorder.getTeamIds() != null) {
                //check if it is 'in team' workorder
                return !Collections.disjoint(sessionManager.getArrayList(SessionManager.KEY_LOGIN_TEAM_IDS), workorder.getTeamIds());
            }
        } else if (PermissionsHelper.actionAllowed(PermissionsHelper.PERMISSION_INPROGRESS_OWN_WORKORDERS)) {
            if (workorder.getAssigned_to() != null) {
                //check if it is own workorder
                return Objects.requireNonNull(workorder.getAssigned_to().getId()).equalsIgnoreCase(new SessionManager(mContext).getString(SessionManager.KEY_LOGIN_ID));
            }
        }
        return false;
    }

    public static boolean canDoneWorkOrder(Context mContext, Workorder workorder) {
        //check if it has permission to inprogress all workorders
        if (actionAllowed(PermissionsHelper.PERMISSION_COMPLETE_ALL_WORKORDERS)) {
            return true;
        } else if (PermissionsHelper.actionAllowed(PermissionsHelper.PERMISSION_COMPLETE_TEAM_WORKORDERS)) {
            SessionManager sessionManager = new SessionManager(mContext);
            sessionManager.getArrayList(SessionManager.KEY_LOGIN_TEAM_IDS);
            if (workorder.getTeamIds() != null) {
                //check if it is 'in team' workorder
                return !Collections.disjoint(sessionManager.getArrayList(SessionManager.KEY_LOGIN_TEAM_IDS), workorder.getTeamIds());
            }
        } else if (PermissionsHelper.actionAllowed(PermissionsHelper.PERMISSION_COMPLETE_OWN_WORKORDERS)) {
            if (workorder.getAssigned_to() != null) {
                //check if it is own workorder
                return workorder.getAssigned_to().getId().equalsIgnoreCase(new SessionManager(mContext).getString(SessionManager.KEY_LOGIN_ID));
            }
        }
        return false;
    }

    public static boolean actionAllowed(String permission) {
        if (hasThisPermission(PERMISSION_MANAGE_ALL)) {
            return true;
        } else return hasThisPermission(permission);
    }

//    private static boolean hasThisPermission(String permissionName) {
//            //todo check if permission is in
//            SessionPermissions.permissions.contains()
//    }

    private static boolean hasThisPermission(String permission) {
        ArrayList<Permission> list = null;
        try {
            list = (ArrayList<Permission>) Permission.find(Permission.class, "name = ?", permission);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return list != null && list.size() > 0;
    }

    public static boolean canChangeStatusWhileCreatingWorkOrders() {
        return actionAllowed(PERMISSION_COMPLETE_ALL_WORKORDERS);
    }

    public static boolean canViewWorkOrders() {
        return !actionAllowed(PERMISSION_VIEW_ALL_WORKORDERS) && !actionAllowed(PERMISSION_VIEW_TEAM_WORKORDERS) && !actionAllowed(PERMISSION_VIEW_OWN_WORKORDERS);
    }

    public static boolean canViewAssets() {
        return !actionAllowed(PERMISSION_VIEW_ALL_ASSETS) && !actionAllowed(PERMISSION_VIEW_TEAM_ASSETS) && !actionAllowed(PERMISSION_VIEW_OWN_ASSETS);
    }

    public static boolean canViewRequest() {
        return !actionAllowed(PERMISSION_VIEW_REQUEST);
    }

    public static boolean canCreateWorkOrders() {
        return !actionAllowed(PERMISSION_CREATE_WORKORDER);
    }

    public static boolean canCreateRequest() {
        return actionAllowed(PERMISSION_CREATE_REQUEST);
    }
    public static boolean canCreateAssets() {
        return actionAllowed(PERMISSION_CREATE_ASSETS);
    }

    public static boolean canAddSparePartsOnWorkOrderCreate() {
        return actionAllowed(PERMISSION_ADD_SPARE_PARTS_ON_WORKORDER_CREATE);
    }

    public static boolean canAddSparePartsOnWorkOrderComplete() {
        return actionAllowed(PERMISSION_ADD_SPARE_PARTS_ON_WORKORDER_COMPLETE);
    }
}
