package vn.com.kidy.presenter;

import android.util.Log;

import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Response;
import retrofit2.HttpException;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.note.DayOffContent;
import vn.com.kidy.data.model.note.NoteContent;
import vn.com.kidy.data.model.note.SubmitNote;
import vn.com.kidy.interactor.SubmitNoteInteractor;

/**
 * Created by admin on 1/30/18.
 */

public class SubmitNotePresenter extends Presenter<SubmitNotePresenter.View> {

    private SubmitNoteInteractor submitNoteInteractor;

    public SubmitNotePresenter(SubmitNoteInteractor submitNoteInteractor) {
        this.submitNoteInteractor = submitNoteInteractor;
    }

    public void submitNote(String classId, String childrenId, NoteContent noteContent) {
        submitNoteInteractor.submitNote(classId, childrenId, noteContent).subscribe(new Consumer<Void>() {
            @Override
            public void accept(Void aVoid) throws Exception {
                Log.e("a", "Success add note");
                getView().addNoteSuccess();
            }

        }, throwable -> {
            Log.e("a", "b" + (throwable instanceof HttpException));
            throwable.printStackTrace();
            if (throwable instanceof NullPointerException) {
                getView().addNoteSuccess();
            } else {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            }
        });
    }

    public void submitDayOff(DayOffContent dayOffContent) {
        submitNoteInteractor.submitDayOff(dayOffContent).subscribe(new Consumer<Void>() {
            @Override
            public void accept(Void aVoid) throws Exception {
                Log.e("a", "Success add dayOff");
                getView().addDayOffSuccess();
            }

        }, throwable -> {
            Log.e("a", "b" + (throwable instanceof HttpException));
            throwable.printStackTrace();
            if (throwable instanceof NullPointerException) {
                getView().addDayOffSuccess();
            } else {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            }
        });
    }

    public interface View extends Presenter.View {
        void addNoteSuccess();
        void addDayOffSuccess();

        void getDataError(int statusCode);
    }
}
