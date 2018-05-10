package vn.com.kidy.presenter;

import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.calendar.CalendarNotes;
import vn.com.kidy.data.model.calendar.Events;
import vn.com.kidy.interactor.FullCalendarInteractor;

/**
 * Created by admin on 1/30/18.
 */

public class FullCalendarPresenter extends Presenter<FullCalendarPresenter.View> {

    private FullCalendarInteractor fullCalendarInteractor;

    public FullCalendarPresenter(FullCalendarInteractor fullCalendarInteractor) {
        this.fullCalendarInteractor = fullCalendarInteractor;
    }

    public void onGetCalendarNotes(String classId, Events events) {
        fullCalendarInteractor.getCalendarNotes(classId, events).subscribe(calendarNotes -> {
            if (calendarNotes == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                getView().getDataSuccess(calendarNotes);
            }
        }, Throwable::printStackTrace);
    }

    public interface View extends Presenter.View {
        void getDataSuccess(CalendarNotes calendarNotes);

        void getDataError(int statusCode);
    }
}
