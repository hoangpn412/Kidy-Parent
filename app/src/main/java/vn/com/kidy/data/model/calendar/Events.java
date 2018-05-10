package vn.com.kidy.data.model.calendar;

import java.util.Date;

/**
 * Created by admin on 4/1/18.
 */

public class Events {
    final String kidId;
    final Date fromDate;
    final Date toDate;

    public Events(String kidId, Date fromDate, Date toDate) {
        this.kidId = kidId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}
