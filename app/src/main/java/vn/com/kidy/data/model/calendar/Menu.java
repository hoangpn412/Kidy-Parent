package vn.com.kidy.data.model.calendar;

import java.util.ArrayList;

/**
 * Created by admin on 1/25/18.
 */

public class Menu
{
    private String title;

    private ArrayList<TimeData> data;

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public ArrayList<TimeData> getData ()
    {
        return data;
    }

    public void setData (ArrayList<TimeData> data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [title = "+title+", data = "+data+"]";
    }
}
