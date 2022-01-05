package com.example.fypintelidea.core.session

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.ArrayMap
import android.util.SparseIntArray
import com.connectavo.app.connectavo_android.core.providers.models.*
import com.example.fypintelidea.core.providers.models.Asset
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class SessionManager(context: Context) {

    private var pref: SharedPreferences = context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    private var editor: SharedPreferences.Editor? = null

    fun put(key: String, value: String) {
        doEdit()
        editor!!.putString(key, value)
        doCommit()
    }

    fun put(key: String, value: Int) {
        doEdit()
        editor!!.putInt(key, value)
        doCommit()
    }

    fun put(key: String, value: Boolean) {
        doEdit()
        editor!!.putBoolean(key, value)
        doCommit()
    }


    fun put(key: String, value: Float) {
        doEdit()
        editor!!.putFloat(key, value)
        doCommit()
    }

    fun put(key: String, value: Double) {
        doEdit()
        editor!!.putString(key, value.toString())
        doCommit()
    }


    fun put(key: String, value: Long) {
        doEdit()
        editor!!.putLong(key, value)
        doCommit()
    }

    fun put(key: String, value: MutableSet<String>) {
        doEdit()
        editor!!.putStringSet(key, value)
        doCommit()
    }

    fun getSet(key: String): MutableSet<String> {
        return pref.getStringSet(key, null) ?: mutableSetOf()
    }

    fun getString(key: String, defaultValue: String): String? {
        return pref.getString(key, defaultValue)
    }

    fun getString(key: String): String? {
        return pref.getString(key, null)
    }

    fun getStringWithoutNull(key: String): String {
        return pref.getString(key, "") ?: ""
    }


    fun getInt(key: String): Int {
        return pref.getInt(key, 0)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return pref.getInt(key, defaultValue)
    }

    fun getLong(key: String): Long {
        return pref.getLong(key, 0)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return pref.getLong(key, defaultValue)
    }

    fun getFloat(key: String): Float {
        return pref.getFloat(key, 0f)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return pref.getFloat(key, defaultValue)
    }

    fun putArrayMap(key: String, value: ArrayMap<String, SparseIntArray>) {
        val stringValue = Gson().toJson(value)
        doEdit()
        editor!!.putString(key, stringValue)
        doCommit()
    }

    fun getArrayMap(key: String): ArrayMap<String, SparseIntArray>? {
        val sparseArrayString = pref.getString(key, null) ?: return null
        val sparseArrayType = object : TypeToken<ArrayMap<String, SparseIntArray>>() {}.type
        return Gson().fromJson(sparseArrayString, sparseArrayType)
    }


    @JvmOverloads
    fun getDouble(key: String, defaultValue: Double = 0.0): Double {
        return try {
            java.lang.Double.valueOf(pref.getString(key, defaultValue.toString()) ?: "")
        } catch (nfe: NumberFormatException) {
            defaultValue
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return pref.getBoolean(key, defaultValue)
    }

    fun getBoolean(key: String): Boolean {
        return pref.getBoolean(key, false)
    }

    fun remove(vararg keys: String) {
        doEdit()
        for (key in keys) {
            editor!!.remove(key)
        }
        doCommit()
    }


    /**
     * Save ArrayList in SharedPreference
     */

    fun saveArrayList(key: String, list: List<String?>?) {
        val editor = pref.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()
    }

    /**
     * Get ArrayList in SharedPreference
     */
    fun getArrayList(key: String): List<String> {
        val gson = Gson()
        val json = pref.getString(key, null)
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return if (json.isNullOrEmpty()) {
            ArrayList()
        } else {
            gson.fromJson(json, type)
        }

    }

    fun saveObjData(key: String, any: Asset) {
        val editor = pref.edit()
        val gson = Gson()
        val json = gson.toJson(any)
        editor.putString(key, json)
        editor.apply()
    }

    /**
     * Get ObjData in SharedPreference
     */
    fun getObjData(key: String): Asset {
        val gson = Gson()
        val json = pref.getString(key, null)
        val type = object : TypeToken<Asset>() {}.type
        return gson.fromJson(json, type)
    }

    @SuppressLint("CommitPrefEdits")
    fun edit() {
        editor = pref.edit()
    }

    fun commit() {
        editor!!.commit()
        editor = null
    }

    @SuppressLint("CommitPrefEdits")
    private fun doEdit() {
        if (editor == null) {
            editor = pref.edit()
        }
    }

    private fun doCommit() {
        if (editor != null) {
            editor!!.commit()
            editor = null
        }
    }

    fun loginUser(
        userId: String,
        token: String,
        email: String,
        companyId: String
    ) {
        put(KEY_LOGIN_TOKEN, token)
        put(KEY_LOGIN_ID, userId)
        put(KEY_LOGIN_COMPANY_ID, companyId)
        put(KEY_LOGIN_EMAIL, email)
    }

    fun logoutUser() {
        doEdit()
        remove(KEY_LOGIN_ID)
        remove(KEY_LOGIN_ROLE)
        remove(KEY_LOGIN_NAME)
        remove(KEY_LOGIN_NUMBER)
        remove(KEY_LOGIN_EMAIL)
        remove(KEY_LOGIN_SUBDOMAIN)
        remove(KEY_LOGIN_TOKEN)
//        editor!!.clear() // will delete all keys
        doCommit()
    }

    companion object {
        const val TAG = "SessionManager"
        const val USER_ROLE_ADMIN = "admin"
        const val USER_ROLE_TEAMLEADER = "teamleader"
        const val USER_ROLE_TECHNICIAN = "technician"
        const val USER_ROLE_PRODUCTION = "production"
        const val USER_ROLE_TENANT = "tenant"
        const val KEY_COMPANY_TYPE = "company_type"
        const val KEY_LOGIN_ID = "user_login_id"
        const val KEY_SINGLE_ASSET = "single_asset"
        const val KEY_LOGIN_TOKEN = "user_login_token"
        const val KEY_LOGIN_EMAIL = "user_login_email"
        const val KEY_LOGIN_PASSWORD = "user_login_password"
        const val KEY_LOGIN_STATE = "user_biometric_state"
        const val KEY_LOGIN_ROLE = "user_login_role"
        const val KEY_LOGIN_TEAM_IDS = "user_login_teams_ids"
        const val KEY_LOGIN_NAME = "user_login_name"
        const val KEY_LOGIN_NUMBER = "user_login_number"
        const val KEY_LOGIN_SUBDOMAIN = "user_login_subdomain"
        const val KEY_LOGIN_IMAGEPATH = "user_login_imagepath"
        const val KEY_LOGIN_COMPANY_ID = "user_login_company_id"
        const val KEY_LOGIN_TIMESTAMP = "user_login_timestamp"
        const val KEY_LOGIN_FIREBASE_REG_ID = "user_login_firebase_reg_id"
        const val KEY_LOGIN_MODE = "user_login_mode"
        const val MODE_NORMAL = "user_login_normal"
        const val MODE_NEW_INSTALL = "user_login_new_install"
        const val MODE_UPGRADE = "user_login_upgrade"
        const val LAST_APP_VISIT = "last_app_visit"
        const val FIRST_RUN_AFTER_LOGIN = "first_run_after_login"
        const val KEY_UPDATE_AVAILABLE_VERSION = "update_available_version"

        // Sharedpref file name
        const val PREF_NAME = "ProjectConnectavoPrefs"
    }
}