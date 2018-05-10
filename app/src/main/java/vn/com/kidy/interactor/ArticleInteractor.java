package vn.com.kidy.interactor;

import io.reactivex.Observable;
import vn.com.kidy.data.model.comment.Content;
import vn.com.kidy.network.client.Service;

/**
 * Created by admin on 1/25/18.
 */

public class ArticleInteractor {
    private Service service;

    public ArticleInteractor(Service service) {
        this.service = service;
    }

    public Observable<Content> getArticleContent(String articleId) {
        return service.getArticleContent(articleId);
    }
}
