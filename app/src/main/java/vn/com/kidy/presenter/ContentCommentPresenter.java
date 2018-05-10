package vn.com.kidy.presenter;

import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.comment.Comments;
import vn.com.kidy.data.model.comment.Content;
import vn.com.kidy.interactor.CommentsInteractor;
import vn.com.kidy.interactor.ContentCommentInteractor;

/**
 * Created by admin on 1/30/18.
 */

public class ContentCommentPresenter extends Presenter<ContentCommentPresenter.View> {

    private ContentCommentInteractor contentCommentInteractor;

    public ContentCommentPresenter(ContentCommentInteractor contentCommentInteractor) {
        this.contentCommentInteractor = contentCommentInteractor;
    }

    public void onGetContentComment(String commentId) {
        contentCommentInteractor.getContentComment(commentId).subscribe(content -> {
            if (content == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                getView().getDataSuccess(content);
            }
        }, Throwable::printStackTrace);
    }

    public interface View extends Presenter.View {
        void getDataSuccess(Content content);

        void getDataError(int statusCode);
    }
}
