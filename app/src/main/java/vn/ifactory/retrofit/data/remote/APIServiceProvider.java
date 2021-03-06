package vn.ifactory.retrofit.data.remote;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.ifactory.retrofit.model.ToDo;
import vn.ifactory.retrofit.model.TokenReponse;
import vn.ifactory.retrofit.model.TokenRequest;
import vn.ifactory.retrofit.model.Users;

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
        // RestClient to making call API (in this project using OkhttpClient)
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

    public Call<TokenReponse> getToken(String userName, String password, String grantType){
        return apiInterface.getToken(userName, password, grantType);
    }

    public Call<Users> getUserInfo(String token, String userName, String password) {
        return apiInterface.getUserInfo(token, userName, password);
    }

    public Call<String> registerUser(Users users) {
        return apiInterface.registerUser(users);
    }

    public Call<List<ToDo>> getTodoByUser(String token, int userId) {
        return apiInterface.getTodoes(token, userId);
    }

    public Call<String> addTodoItem(String token, String todoName, String description, int userId) {
        return apiInterface.addTodoItem(token, todoName, description, userId);
    }

    public Call<String> modifyTodoItem(String token, int todoId, String name, String description ) {
        return apiInterface.modifyTodoItem(token, todoId, name, description);
    }

    public Call<String> deleteTodoItem(String token, int todoId) {
        return apiInterface.deleteTodoItem(token, todoId);
    }
}
