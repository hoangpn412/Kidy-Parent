package vn.com.kidy.data.model;

import java.util.ArrayList;

/**
 * Created by admin on 1/15/18.
 */

public class Parent
{
    private int status;

    public int getStatus() { return this.status; }

    public void setStatus(int status) { this.status = status; }

    private String message;

    public String getMessage() { return this.message; }

    public void setMessage(String message) { this.message = message; }

    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private String title;

    public String getTitle() { return this.title; }

    public void setTitle(String title) { this.title = title; }

    private String avatar;

    public String getAvatar() { return this.avatar; }

    public void setAvatar(String avatar) { this.avatar = avatar; }

    private ArrayList<Kid> kids;

    public ArrayList<Kid> getKids() { return this.kids; }

    public void setKids(ArrayList<Kid> kids) { this.kids = kids; }
}
