package vn.ifactory.retrofit.data.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PC on 10/18/2018.
 */

public class APIServiceProvider {
    private static APIServiceProvider sServiceInstance;

    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    APIInterface apiInterface;

    private APIServiceProvider(String baseUrl,
                               long readTimeout,
                               long connectTimeout,
                               HttpLoggingInterceptor.Level logLevel){

        // record log debug when making call API
        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(logLevel);

        // okHttpClient is library making call API (using base HttpUrlConnection to making call API)
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .build();

        // Retrofit is library communicate with Restful API:
        // BaseURL
        // Converter (use Gson to part Object to Json or Json to Object)
        // RestClient to making call API (at here use OkhttpClient)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        apiInterface = retrofit.create(APIInterface.class);
    }

    public static APIServiceProvider getApiServiceProvider(String baseUrl,
                                                           long readTimeout,
                                                           long connectTimeout,
                                                           HttpLoggingInterceptor.Level logLevel){
        if(sServiceInstance==null){
            sServiceInstance=new APIServiceProvider(baseUrl,readTimeout,connectTimeout,logLevel);
        }
        return sServiceInstance;
    }
}
