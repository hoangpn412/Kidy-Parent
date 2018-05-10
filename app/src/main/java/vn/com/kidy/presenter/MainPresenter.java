package vn.com.kidy.presenter;

import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.login.ClassInfo;
import vn.com.kidy.data.model.notification.Notifications;
import vn.com.kidy.interactor.MainInteractor;
import vn.com.kidy.utils.Tools;

/**
 * Created by admin on 1/30/18.
 */

public class MainPresenter extends Presenter<MainPresenter.View> {

    private MainInteractor mainInteractor;

    public MainPresenter(MainInteractor mainInteractor) {
        this.mainInteractor = mainInteractor;
    }

    public void onGetNotifications(String kidId) {
        mainInteractor.getNotifications(kidId).subscribe(notifications -> {
            if (notifications == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                for (int i = 0; i < notifications.getNotifications().size(); i++) {
                    notifications.getNotifications().get(i).setDateStr(Tools.longtoDate(notifications.getNotifications().get(i).getDate()));
                }
                getView().getDataSuccess(notifications);
            }
        }, Throwable::printStackTrace);
    }

    public void onGetClassInfo(String classId) {
        mainInteractor.getClassInfo(classId).subscribe(classInfo -> {
            if (classInfo == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                getView().getClassInfoSuccess(classInfo);
            }
        }, throwable -> {
            throwable.printStackTrace();
            getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
        });
    }

    public interface View extends Presenter.View {
        void getDataSuccess(Notifications notifications);

        void getDataError(int statusCode);

        void getClassInfoSuccess(ClassInfo classInfo);
    }
}
