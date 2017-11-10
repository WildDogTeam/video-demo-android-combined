package com.wilddog.boarddemo.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fly on 17-6-12.
 */

public class SharedpereferenceTool {
    private static SharedPreferences sp;
    private static final String FILE_NAME = "share_data";

    public static  void saveUserId(Context context, String uid){
        sp= context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userId",uid);
        editor.commit();
    }

    public static String getUserId(Context context){
        String userId = null;
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString("userId",null);
        return userId;
    }
    public static  void saveRoomId(Context context, String uid){
        sp= context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("roomId",uid);
        editor.commit();
    }

    public static String getRoomId(Context context){
        String userId = null;
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString("roomId",null);
        return userId;
    }
    public static  void saveDimension(Context context, String dimension ){
        sp= context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("dimension",dimension);
        editor.commit();
    }

    public static String getDimension(Context context){
        String dimension = null;
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        dimension = sp.getString("dimension","480P");
        return dimension;
    }
    public static  void saveFPS(Context context, String dimension ){
        sp= context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("fps",dimension);
        editor.commit();
    }

    public static String getFPS(Context context){
        String dimension = null;
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        dimension = sp.getString("fps","15");
        return dimension;
    }

    public static void setLoginStatus(Context context, boolean islogin){
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("loginStatus",islogin);
        editor.commit();
    }

    public static boolean getLoginStatus(Context context){
        boolean isLoginStatus = false;
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        isLoginStatus = sp.getBoolean("loginStatus",false);
        return isLoginStatus;
    }

    public static void setUserInfo(Context context, String userinfo){
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userinfo",userinfo);
        editor.commit();
    }

    public static String getUserInfo(Context context){
        String userInfo = "";
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        userInfo = sp.getString("userinfo","");
        return userInfo;
    }

}
