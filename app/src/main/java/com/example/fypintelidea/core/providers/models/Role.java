package com.example.fypintelidea.core.providers.models;//package com.connectavo.app.connectavo_android.core.providers.models;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.orm.SugarRecord;
//import com.orm.dsl.Ignore;
//
//import java.util.List;
//
//@Deprecated
//public class Role extends SugarRecord {
//
//    private String _id;
//
//    private String name;
//
//    private String permissions_android_Store;
//
//    @Ignore
//    private List<Permission> permission_;
//
//    public Role() {
//    }
//
//    public List<Permission> getMyPermissionsList(){
//        //Convert from JSON string to List
//        permission_ =  new Gson().fromJson(this.permissions_android_Store,new TypeToken<List<Permission>>(){}.getType());
//        return permission_;
//    }
//
//    public void setMyPermissionsList(List<Permission> stringList){
//        this.permission_ = stringList;
//    }
//
//    //Override save to ensure the list is converted into JSON before saving.
//    @Override
//    public long save(){
//        this.permissions_android_Store = new Gson().toJson(permission_);
//        return super.save();
//    }
//
//
//    public String get_id() {
//        return _id;
//    }
//
//    public void set_id(String _id) {
//        this._id = _id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public String toString() {
//        return "ClassPojo [permission_ = " + permission_ + ", _id = " + _id + ", name = " + name + "]";
//    }
//}
//
