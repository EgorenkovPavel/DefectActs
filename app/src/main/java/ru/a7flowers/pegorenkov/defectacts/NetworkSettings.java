package ru.a7flowers.pegorenkov.defectacts;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

public class NetworkSettings {

    SharedPreferences sharedPref;

    private String serverPath;
    private String user;
    private String password;
    private String auth;
    private ServerPathChangeListener mServerPathChangeListener;
    private SharedPreferences.OnSharedPreferenceChangeListener mPrefChangeListener;

    public NetworkSettings(Context context) {
        sharedPref =  PreferenceManager.getDefaultSharedPreferences(context);

        final String key_server = context.getString(R.string.pref_server);
        final String key_user = context.getString(R.string.pref_user);
        final String key_password = context.getString(R.string.pref_password);

        serverPath = sharedPref.getString(key_server, BuildConfig.ServerPath);
        user = sharedPref.getString(key_user, BuildConfig.ServerUsername);
        password = sharedPref.getString(key_password, BuildConfig.ServerPassword);
        auth = calcAuth();

        mPrefChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                if(key.equals(key_server)){
                    serverPath = sharedPref.getString(key_server, BuildConfig.ServerPath);
                    if(mServerPathChangeListener != null) mServerPathChangeListener.onServerPathChanged();
                }else if(key.equals(key_user)){
                    user = sharedPref.getString(key_user, BuildConfig.ServerUsername);
                    auth = calcAuth();
                }else if(key.equals(key_password)){
                    password = sharedPref.getString(key_password, BuildConfig.ServerPassword);
                    auth = calcAuth();
                };
            }
        };
        sharedPref.registerOnSharedPreferenceChangeListener(mPrefChangeListener);
    }

    public String getServerPath() {
        return serverPath;
    }

    public String getAuth() {
        return auth;
    }

    private String calcAuth() {
        final String pair = user + ":" + password;
        final byte[] encodedBytes = Base64.encode(pair.getBytes(), Base64.NO_WRAP);
        final String auth = new String(encodedBytes);
        return "Basic " + auth;
    }

    public interface ServerPathChangeListener{
        void onServerPathChanged();
    }

    public void setServerPathChangeListener(ServerPathChangeListener listener){
        this.mServerPathChangeListener = listener;
    }

}
