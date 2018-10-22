package vn.ifactory.retrofit.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PC on 10/19/2018.
 */

public class RefreshToken {
    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("grant_type")
    private String grantType;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
