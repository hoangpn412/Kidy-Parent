package vn.com.kidy.interactor;

import android.util.Log;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import vn.com.kidy.data.model.note.DayOffContent;
import vn.com.kidy.data.model.note.NoteContent;
import vn.com.kidy.data.model.note.SubmitNote;
import vn.com.kidy.network.client.Service;

/**
 * Created by admin on 1/25/18.
 */

public class SubmitNoteInteractor {
    private Service service;

    public SubmitNoteInteractor(Service service) {
        this.service = service;
    }

    public Observable<Void> submitNote(String classId, String childrenId, NoteContent noteContent) {
        return service.submitNote(classId, childrenId, noteContent);
    }

    public Observable<Void> submitDayOff(DayOffContent dayOffContent) {
        return service.submitDayOff(dayOffContent);
    }
}
