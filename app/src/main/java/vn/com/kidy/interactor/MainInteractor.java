package vn.com.kidy.interactor;

import io.reactivex.Observable;
import vn.com.kidy.data.model.comment.Comments;
import vn.com.kidy.data.model.login.ClassInfo;
import vn.com.kidy.data.model.notification.Notifications;
import vn.com.kidy.network.client.Service;

/**
 * Created by admin on 1/25/18.
 */

public class MainInteractor {
    private Service service;

    public MainInteractor(Service service) {
        this.service = service;
    }

    public Observable<Notifications> getNotifications(String kidId) {
        return service.getNotifications(kidId);
    }

    public Observable<ClassInfo> getClassInfo(String classId) {
        return service.getClassInfo(classId);
    }
}
