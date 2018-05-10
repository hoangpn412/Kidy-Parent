package vn.com.kidy.data.model.login;

/**
 * Created by admin on 3/20/18.
 */

public class Login {
    private boolean isSuccess;

    public boolean getIsSuccess() {
        return this.isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    private String token;

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
