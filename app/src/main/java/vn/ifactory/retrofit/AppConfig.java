package vn.ifactory.retrofit;

import android.app.Application;
import android.content.Context;

import okhttp3.logging.HttpLoggingInterceptor;
import vn.ifactory.retrofit.utils.Util;

/**
 * Created by PC on 10/18/2018.
 */

public class AppConfig extends Application {


    public static API_ENDPOINTS selectedEndPoint;
    public boolean isEmulator;

    static Context context;

    //private static APIServiceProvider apiServiceProvider;

    public static enum API_ENDPOINTS{
        localhost, remote
    }

    @Override
    public void onCreate() {
        super.onCreate();

        isEmulator = Util.isEmulator();
        context = getApplicationContext();
        /*apiServiceProvider = APIServiceProvider.getApiServiceProvider(
                RestAPIs.getBaseUrl(),
                5000,
                5000,
                HttpLoggingInterceptor.Level.BODY);*/
    }
}
