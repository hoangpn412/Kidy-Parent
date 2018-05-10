package vn.com.kidy.data.model.login;

/**
 * Created by admin on 3/22/18.
 */

public class Contact
{
    private String fullName;

    public String getFullName() { return this.fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    private String email;

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    private String phoneNumber;

    public String getPhoneNumber() { return this.phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    private String address;

    public String getAddress() { return this.address; }

    public void setAddress(String address) { this.address = address; }

    private String brithday;

    public String getBrithday() { return this.brithday; }

    public void setBrithday(String brithday) { this.brithday = brithday; }

    private String avatar;

    public String getAvatar() { return this.avatar; }

    public void setAvatar(String avatar) { this.avatar = avatar; }

    private String userId;

    public String getUserId() { return this.userId; }

    public void setUserId(String userId) { this.userId = userId; }

    private int contactType;

    public int getContactType() { return this.contactType; }

    public void setContactType(int contactType) { this.contactType = contactType; }

    private String hashedPassword;

    public String getHashedPassword() { return this.hashedPassword; }

    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }
}