package vn.com.kidy.data.model.note;

import java.util.Date;

/**
 * Created by admin on 4/1/18.
 */

public class DayOffContent {
    final String classId, childrenId, reason;
    final Date fromDate, toDate;

    public DayOffContent(String classId, String childrenId, Date fromDate, Date toDate, String reason) {
        this.classId = classId;
        this.childrenId = childrenId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
    }
}
