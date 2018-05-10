package vn.com.kidy.interactor;

import io.reactivex.Observable;
import vn.com.kidy.data.model.calendar.CalendarNotes;
import vn.com.kidy.data.model.calendar.Events;
import vn.com.kidy.network.client.Service;

/**
 * Created by admin on 1/25/18.
 */

public class FullCalendarInteractor {
    private Service service;

    public FullCalendarInteractor(Service service) {
        this.service = service;
    }

    public Observable<CalendarNotes> getCalendarNotes(String classId, Events events) {
        return service.getCalendarNotes(classId, events);
    }
}
