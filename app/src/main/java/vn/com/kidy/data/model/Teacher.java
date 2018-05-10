package vn.com.kidy.data.model;

/**
 * Created by admin on 1/15/18.
 */

public class Teacher
{
    private String teacherId;

    public String getTeacherId() { return this.teacherId; }

    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private String avatar;

    public String getAvatar() { return this.avatar; }

    public void setAvatar(String avatar) { this.avatar = avatar; }

    private String phoneNumber;

    public String getPhoneNumber() { return this.phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    private int gender;

    public int getGender() { return this.gender; }

    public void setGender(int gender) { this.gender = gender; }
}

