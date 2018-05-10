package vn.com.kidy.presenter;

import java.util.ArrayList;

import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.calendar.Calendar;
import vn.com.kidy.data.model.calendar.TimeTable;
import vn.com.kidy.data.model.note.Notes;
import vn.com.kidy.interactor.CalendarInteractor;

/**
 * Created by admin on 1/25/18.
 */

public class CalendarPresenter extends Presenter<CalendarPresenter.View> {
    private CalendarInteractor calendarInteractor;

    public CalendarPresenter(CalendarInteractor calendarInteractor) {
        this.calendarInteractor = calendarInteractor;
    }

//    public void onGetCalendar(String kidId, long date) {
//        calendarInteractor.getCalendar(kidId, date).subscribe(calendar -> {
//            if (calendar == null) {
//                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
//            } else {
//                getView().getDataSuccess(calendar);
//            }
//        }, Throwable::printStackTrace);
//    }

    public void onGetHomeNotes(String classId, String childrenId, int year, int month, int day) {
        calendarInteractor.getHomeNotes(classId, childrenId, year, month, day).subscribe(notes -> {
            if (notes == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                getView().getHomeNotesSuccess(notes);
            }
        }, throwable -> {
            throwable.printStackTrace();
            getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
        });
    }

    public void onGetTimeTables(String classId, int dayOfWeek) {
        calendarInteractor.getTimeTables(classId, dayOfWeek).subscribe(timeTables -> {
            if (timeTables == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                getView().getTimeTableSuccess(timeTables);
            }
        }, throwable -> {
            throwable.printStackTrace();
            getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
        });
    }

    public void onGetMeanMenu(String classId, int dayOfWeek) {
        calendarInteractor.getMeanMenu(classId, dayOfWeek).subscribe(meanMenu -> {
            if (meanMenu == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                getView().getMeanMenuSuccess(meanMenu);
            }
        }, throwable -> {
            throwable.printStackTrace();
            getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
        });
    }


    public interface View extends Presenter.View {
//        void getDataSuccess(Calendar calendar);

        void getDataError(int statusCode);

        void getHomeNotesSuccess(Notes notes);

        void getTimeTableSuccess(ArrayList<TimeTable> timeTables);

        void getMeanMenuSuccess(ArrayList<TimeTable> meanMenu);
    }
}
