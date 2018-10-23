package vn.ifactory.retrofit.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.ifactory.retrofit.adapter.TodoItemAdapter;
import vn.ifactory.retrofit.model.ToDo;
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

    @POST(ToDoAppRestAPI.registerUser)
    Call<String> registerUser(@Body Users users);

    @GET(ToDoAppRestAPI.getToDoItem)
    Call<List<ToDo>> getTodoes(@Header(value = "Authorization") String token,
                               @Path("userId") int userId);

    @FormUrlEncoded
    @POST(ToDoAppRestAPI.addToDoItem)
    Call<String> addTodoItem(@Header(value = "Authorization")String token,
                             @Field("name") String name,
                             @Field("description") String description,
                             @Field("userId") int userId);

    @FormUrlEncoded
    @PUT(ToDoAppRestAPI.modifyToDoItem)
    Call<String> modifyTodoItem(@Header(value = "Authorization") String token,
                                @Field("todoId") int todoId,
                                @Field("name") String name,
                                @Field("description") String des);


    // @DELETE cannot BODY
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = ToDoAppRestAPI.deleteToDo, hasBody = true)
    Call<String> deleteTodoItem(@Header(value = "Authorization") String token,
                                @Field("todoId") int todoId);
}
