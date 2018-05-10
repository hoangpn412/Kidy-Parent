package vn.com.kidy.data.model.calendar;

/**
 * Created by admin on 2/10/18.
 */

public class CalendarNote
{
    private String date;

    public String getDate() { return this.date; }

    public void setDate(String date) { this.date = date; }

//    private boolean schedule;
//
//    public boolean getSchedule() { return this.schedule; }
//
//    public void setSchedule(boolean schedule) { this.schedule = schedule; }

    private boolean hasTeacherMessage;

    public boolean getHasTeacherMessage() { return this.hasTeacherMessage; }

    public void setHasTeacherMessage(boolean hasTeacherMessage) { this.hasTeacherMessage = hasTeacherMessage; }

    private boolean hasParentMessage;

    public boolean getHasParentMessage() { return this.hasParentMessage; }

    public void setHasParentMessage(boolean hasParentMessage) { this.hasParentMessage = hasParentMessage; }

    private boolean hasMealMenu;

    public boolean getHasMealMenu() {
        return this.hasMealMenu;
    }

    public void setHasMealMenu(boolean hasMealMenu) {
        this.hasMealMenu = hasMealMenu;
    }

    private boolean hasTimeTable;

    public boolean getHasTimeTable() {
        return this.hasTimeTable;
    }

    public void setHasTimeTable(boolean hasTimeTable) {
        this.hasTimeTable = hasTimeTable;
    }
}

