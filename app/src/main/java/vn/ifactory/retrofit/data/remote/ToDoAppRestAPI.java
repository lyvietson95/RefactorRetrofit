package vn.ifactory.retrofit.data.remote;

/**
 * Created by PC on 10/18/2018.
 */

public interface ToDoAppRestAPI {
    String baseLocalHostUrl = "http://192.168.0.107:80/api/";
    String baseRemoteUrl = "http://192.168.0.107:80/api/";

    String registerAuthor="users/";

    String getToken = "token";

    String getUserInfo = "users/{username}/{password}";

    String logout = registerAuthor+"signout/";

    String addToDoItem = "todo/";

    String getToDoItem = "todoes/";

    String deleteToDo = addToDoItem;

    String modifyToDoUrl = addToDoItem;
}
