package vn.com.kidy.data.model.calendar;

import java.util.ArrayList;

/**
 * Created by admin on 2/10/18.
 */

public class CalendarNotes {
    private ArrayList<CalendarNote> events;

    public ArrayList<CalendarNote> getCalendarNote() { return this.events; }

    public void setCalendarNote(ArrayList<CalendarNote> events) { this.events = events; }
}
