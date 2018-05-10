package vn.com.kidy.data.model.comment;

/**
 * Created by admin on 1/30/18.
 */

public class Comment
{
    private String teacherName;

    public String getTeacherName() { return this.teacherName; }

    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    private int teacherRole;

    public int getTeacherRole() { return this.teacherRole; }

    public void setTeacherRole(int teacherRole) { this.teacherRole = teacherRole; }

    private String teacherAvatar;

    public String getTeacherAvatar() { return this.teacherAvatar; }

    public void setTeacherAvatar(String teacherAvatar) { this.teacherAvatar = teacherAvatar; }

    private String content;

    public String getContent() { return this.content; }

    public void setContent(String content) { this.content = content; }

    private int week;

    public int getWeek() { return this.week; }

    public void setWeek(int week) { this.week = week; }

    private int year;

    public int getYear() { return this.year; }

    public void setYear(int year) { this.year = year; }

    private String fromDate;

    public String getFromDate() { return this.fromDate; }

    public void setFromDate(String fromDate) { this.fromDate = fromDate; }

    private String toDate;

    public String getToDate() { return this.toDate; }

    public void setToDate(String toDate) { this.toDate = toDate; }

}

