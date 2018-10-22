package vn.ifactory.retrofit.data.remote;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.ifactory.retrofit.model.TokenReponse;
import vn.ifactory.retrofit.model.TokenRequest;
import vn.ifactory.retrofit.model.Users;

/**
 * Created by PC on 10/18/2018.
 */

public interface APIInterface {
    // init method Restful at here
    // example: POST, PUT, DELETE, GET

    @FormUrlEncoded
    @POST(ToDoAppRestAPI.getToken)
    Call<TokenReponse> getToken(@Field("username") String userName,
                                @Field("password") String password,
                                @Field("grant_type") String grantType);

    @GET(ToDoAppRestAPI.getUserInfo)
    Call<Users> getUserInfo(@Header(value = "Authorization")String token,
                            @Path("username") String username,
                            @Path("password") String password);
}
