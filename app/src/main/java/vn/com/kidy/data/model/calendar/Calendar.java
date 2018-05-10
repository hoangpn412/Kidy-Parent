package vn.com.kidy.data.model.calendar;

import vn.com.kidy.data.model.note.Notes;

/**
 * Created by admin on 1/25/18.
 */

public class Calendar
{
    private Menu menu;

    private TimeTable timetable;

    private Notes notes;

    private String date;

    public Menu getMenu ()
    {
        return menu;
    }

    public void setMenu (Menu menu)
    {
        this.menu = menu;
    }

    public TimeTable getTimetable ()
    {
        return timetable;
    }

    public void setTimetable (TimeTable timetable)
    {
        this.timetable = timetable;
    }

    public Notes getNotes ()
    {
        return notes;
    }

    public void setNotes (Notes notes)
    {
        this.notes = notes;
    }

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [menu = "+menu+", timetable = "+timetable+", notes = "+notes+", date = "+date+"]";
    }
}
