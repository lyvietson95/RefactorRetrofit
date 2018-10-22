package vn.ifactory.retrofit;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;

import okhttp3.logging.HttpLoggingInterceptor;
import vn.ifactory.retrofit.data.remote.APIServiceProvider;
import vn.ifactory.retrofit.data.remote.RestAPIs;
import vn.ifactory.retrofit.model.Users;
import vn.ifactory.retrofit.utils.SharedPrefs;
import vn.ifactory.retrofit.utils.Util;

/**
 * Created by PC on 10/18/2018.
 */

public class AppConfig extends Application {
    private static AppConfig sInstance;
    public static final String TAG = AppConfig.class.getSimpleName();
    private Gson mGson;

    public static API_ENDPOINTS selectedEndPoint;
    public boolean isEmulator;

    static Context context;

    private static APIServiceProvider apiServiceProvider;

    public static final String PRE_USERNAME = "USER_NAME";
    public static final String PRE_USER = "USER";
    public static final String PRE_SESSION_TOKEN = "SESSION_TOKEN";
    public static final String PREFIX_TOKEN = "Bearer ";
    public static enum API_ENDPOINTS{
        localhost, remote
    }


    // singleton with double-checker
    public static AppConfig getInstance() {
        AppConfig instance = sInstance;
        if (instance == null) {
            synchronized (AppConfig.class) {
                instance = sInstance;
                if (instance == null) {
                    instance = sInstance = new AppConfig();
                }
            }

        }

        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        isEmulator = Util.isEmulator();
        context = getApplicationContext();
        sInstance = this;
        mGson = new Gson();
        apiServiceProvider = APIServiceProvider.getApiServiceProvider(
                RestAPIs.getBaseUrl(),
                5000,
                5000,
                HttpLoggingInterceptor.Level.BODY);
    }

    public static APIServiceProvider getApiServiceProvider() {
        return apiServiceProvider;
    }

    public Gson getGson() {
        return mGson;
    }

    public void saveUserName(String userName) {
        SharedPrefs.getInstance().put(PRE_USERNAME, userName);
    }

    public String getUserName() {
        return SharedPrefs.getInstance().get(PRE_USERNAME, String.class);
    }

    public void saveFullUser(Users users) {
        SharedPrefs.getInstance().put(PRE_USER, users);
    }

    public Users getFullUser() {
        return SharedPrefs.getInstance().get(PRE_USER, Users.class);
    }

    public void saveSessionToken(String token) {
        SharedPrefs.getInstance().put(PRE_SESSION_TOKEN, PREFIX_TOKEN + token);
    }

    public String getSessionToken() {
        return SharedPrefs.getInstance().get(PRE_SESSION_TOKEN, String.class);
    }
}
