package vn.com.kidy.data.model.login;

/**
 * Created by admin on 3/22/18.
 */

public class User
{
    private boolean isSuccess;

    public boolean getIsSuccess() { return this.isSuccess; }

    public void setIsSuccess(boolean isSuccess) { this.isSuccess = isSuccess; }

    private Contact contact;

    public Contact getContact() { return this.contact; }

    public void setContact(Contact contact) { this.contact = contact; }
}

