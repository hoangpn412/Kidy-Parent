package vn.com.kidy.data.model.notification;

/**
 * Created by admin on 2/6/18.
 */

public class Notification {
    private String notifyId;

    public String getNotifyId() {
        return this.notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    private String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private long date;

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    private String dateStr;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    private int type;

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private boolean read;

    public boolean getRead() {
        return this.read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
