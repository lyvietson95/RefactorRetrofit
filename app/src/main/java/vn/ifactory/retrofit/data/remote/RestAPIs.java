package vn.ifactory.retrofit.data.remote;

import vn.ifactory.retrofit.AppConfig;

/**
 * Created by PC on 10/18/2018.
 */

public class RestAPIs {
    public static String getBaseUrl(){
        if(AppConfig.selectedEndPoint== AppConfig.API_ENDPOINTS.localhost){
            return ToDoAppRestAPI.baseLocalHostUrl;
        }else if (AppConfig.selectedEndPoint== AppConfig.API_ENDPOINTS.remote){
            return ToDoAppRestAPI.baseRemoteUrl;
        }else {
            return ToDoAppRestAPI.baseRemoteUrl;
        }
    }
}
