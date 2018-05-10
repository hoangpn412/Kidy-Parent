package vn.com.kidy.presenter;

import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.comment.Comments;
import vn.com.kidy.interactor.CommentsInteractor;
import vn.com.kidy.utils.Tools;

/**
 * Created by admin on 1/30/18.
 */

public class CommentsPresenter extends Presenter<CommentsPresenter.View> {

    private CommentsInteractor commentsInteractor;

    public CommentsPresenter(CommentsInteractor commentsInteractor) {
        this.commentsInteractor = commentsInteractor;
    }

    public void onGetComments(String classId, String childrenId, int pageSize, int pageIndex) {
        commentsInteractor.getComments(classId, childrenId, pageSize, pageIndex).subscribe(comments -> {
            if (comments == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                for (int i = 0; i < comments.getComments().size(); i++) {
                    comments.getComments().get(i).setFromDate(Tools.dateToStringDateWithoutDayofWeek(comments.getComments().get(i).getFromDate()));
                    comments.getComments().get(i).setToDate(Tools.dateToStringDateWithoutDayofWeek(comments.getComments().get(i).getToDate()));
                }
                getView().getDataSuccess(comments);
            }
        }, Throwable::printStackTrace);
    }

    public interface View extends Presenter.View {
        void getDataSuccess(Comments comments);

        void getDataError(int statusCode);
    }
}
