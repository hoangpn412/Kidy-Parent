package vn.com.kidy.data.model.calendar;

import java.util.ArrayList;

/**
 * Created by admin on 1/25/18.
 */

public class TimeTable
{
    private int id;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    private int sortOrder;

    public int getSortOrder() { return this.sortOrder; }

    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }

    private String content;

    public String getContent() { return this.content; }

    public void setContent(String content) { this.content = content; }

    private String time;

    public String getTime() { return this.time; }

    public void setTime(String time) { this.time = time; }

    private int schoolId;

    public int getSchoolId() { return this.schoolId; }

    public void setSchoolId(int schoolId) { this.schoolId = schoolId; }

    private int classId;

    public int getClassId() { return this.classId; }

    public void setClassId(int classId) { this.classId = classId; }

    private int dayOfWeek;

    public int getDayOfWeek() { return this.dayOfWeek; }

    public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }

}

