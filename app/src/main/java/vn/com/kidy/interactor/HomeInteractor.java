package vn.com.kidy.interactor;

import io.reactivex.Observable;
import vn.com.kidy.data.model.media.Medias;
import vn.com.kidy.data.model.media.Photos;
import vn.com.kidy.data.model.note.Notes;
import vn.com.kidy.data.model.news.News;
import vn.com.kidy.network.client.Service;

/**
 * Created by Family on 6/2/2017.
 */

public class HomeInteractor {
    private Service service;

    public HomeInteractor(Service service) {
        this.service = service;
    }

//    public Observable<Home> getHome(String kidId) {
//        return service.getHome(kidId);
//    }

    public Observable<News> getHomeNews(String schoolId) {
        return service.getHomeNews(schoolId);
    }

    public Observable<Notes> getHomeNotes(String classId, String childrenId, int year, int month, int day) {
        return service.getHomeNotes(classId, childrenId, year, month, day);
    }

    public Observable<Medias> getHomeMedias(String classId, int pageSize, int pageIndex) {
        return service.getMedias(classId, pageSize, pageIndex);
    }
}
