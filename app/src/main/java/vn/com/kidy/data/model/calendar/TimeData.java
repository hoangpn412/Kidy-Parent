package vn.com.kidy.data.model.calendar;

/**
 * Created by admin on 1/25/18.
 */

public class TimeData {
    private String content;

    private String time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ClassPojo [content = " + content + ", time = " + time + "]";
    }
}
