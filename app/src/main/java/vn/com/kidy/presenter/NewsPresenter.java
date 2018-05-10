package vn.com.kidy.presenter;

import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.news.News;
import vn.com.kidy.interactor.NewsInteractor;
import vn.com.kidy.utils.Tools;

/**
 * Created by admin on 1/25/18.
 */

public class NewsPresenter extends Presenter<NewsPresenter.View> {
    private NewsInteractor newsInteractor;

    public NewsPresenter(NewsInteractor newsInteractor) {
        this.newsInteractor = newsInteractor;
    }

    public void onGetNews(String schoolId) {
        newsInteractor.getNews(schoolId).subscribe(news -> {
            if (news == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                for (int i = 0; i < news.getNewsList().size(); i++) {
                    news.getNewsList().get(i).setCreatedDate(Tools.dateToStringDate(news.getNewsList().get(i).getCreatedDate()));
                }
                getView().getDataSuccess(news);
            }
        }, Throwable::printStackTrace);
    }

    public interface View extends Presenter.View {
        void getDataSuccess(News news);

        void getDataError(int statusCode);
    }
}
