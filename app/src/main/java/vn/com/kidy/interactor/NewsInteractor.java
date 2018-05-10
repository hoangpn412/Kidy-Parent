package vn.com.kidy.interactor;

import io.reactivex.Observable;
import vn.com.kidy.data.model.calendar.Calendar;
import vn.com.kidy.data.model.news.News;
import vn.com.kidy.network.client.Service;

/**
 * Created by admin on 1/25/18.
 */

public class NewsInteractor {
    private Service service;

    public NewsInteractor(Service service) {
        this.service = service;
    }

    public Observable<News> getNews(String schoolId) {
        return service.getHomeNews(schoolId);
    }
}
