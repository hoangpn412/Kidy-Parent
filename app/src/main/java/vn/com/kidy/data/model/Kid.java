package vn.com.kidy.data.model;

import java.util.ArrayList;

/**
 * Created by admin on 1/15/18.
 */

public class Kid
{
    private String kidId;

    public String getKidId() { return this.kidId; }

    public void setKidId(String kidId) { this.kidId = kidId; }

    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private String nickname;

    public String getNickname() { return this.nickname; }

    public void setNickname(String nickname) { this.nickname = nickname; }

    private int gender;

    public int getGender() { return this.gender; }

    public void setGender(int gender) { this.gender = gender; }

    private String birthday;

    public String getBirthday() { return this.birthday; }

    public void setBirthday(String birthday) { this.birthday = birthday; }

    private String avatar;

    public String getAvatar() { return this.avatar; }

    public void setAvatar(String avatar) { this.avatar = avatar; }

    private String classId;

    public String getClassId() { return this.classId; }

    public void setClassId(String classId) { this.classId = classId; }

    private String className;

    public String getClassName() { return this.className; }

    public void setClassName(String className) { this.className = className; }

    private String schoolId;

    public String getSchoolId() { return this.schoolId; }

    public void setSchoolId(String schoolId) { this.schoolId = schoolId; }

    private String schoolName;

    public String getSchoolName() { return this.schoolName; }

    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }

    private ArrayList<Teacher> teachers;

    public ArrayList<Teacher> getTeachers() { return this.teachers; }

    public void setTeachers(ArrayList<Teacher> teachers) { this.teachers = teachers; }
}

