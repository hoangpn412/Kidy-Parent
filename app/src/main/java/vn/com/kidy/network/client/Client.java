package vn.com.kidy.network.client;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.calendar.Calendar;
import vn.com.kidy.data.model.calendar.CalendarNotes;
import vn.com.kidy.data.model.calendar.Events;
import vn.com.kidy.data.model.calendar.TimeTable;
import vn.com.kidy.data.model.comment.Comments;
import vn.com.kidy.data.model.comment.Content;
import vn.com.kidy.data.model.login.Account;
import vn.com.kidy.data.model.login.ClassInfo;
import vn.com.kidy.data.model.login.Kid;
import vn.com.kidy.data.model.login.Login;
import vn.com.kidy.data.model.login.User;
import vn.com.kidy.data.model.media.Medias;
import vn.com.kidy.data.model.media.Photos;
import vn.com.kidy.data.model.news.News;
import vn.com.kidy.data.model.note.DayOffContent;
import vn.com.kidy.data.model.note.NoteContent;
import vn.com.kidy.data.model.note.Notes;
import vn.com.kidy.data.model.notification.Notifications;
import vn.com.kidy.network.retrofit.RetrofitClient;

/**
 * Created by Family on 4/22/2017.
 */

public class Client extends RetrofitClient implements Service {

    public Client(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public Observable<Login> getLogin(@Body Account account) {
        return getRetrofitService().getLogin(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<User> getLoginContact(@Path(Constants.Params.TOKEN) String token) {
        return getRetrofitService().getLoginContact(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ArrayList<Kid>> getLoginKids(String parent) {
        return getRetrofitService().getLoginKids(parent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ClassInfo> getClassInfo(String classId) {
        return getRetrofitService().getClassInfo(classId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<News> getHomeNews(String schoolid) {
        return getRetrofitService().getHomeNews(schoolid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Notes> getHomeNotes(String classId, String childrenId, int year, int month, int day) {
        return getRetrofitService().getHomeNotes(classId, childrenId, year, month, day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Void> submitNote(String classId, String childrenid, NoteContent noteContent) {
        return getRetrofitService().submitNote(classId, childrenid, noteContent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

//    @Override
//    public Observable<Home> getHome(@Query(Constants.Params.KID_ID) String kidId) {
//        return getRetrofitService().getHome(kidId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    @Override
    public Observable<Calendar> getCalendar(@Query(Constants.Params.KID_ID) String kidId, @Query(Constants.Params.DATE) long date) {
        return getRetrofitService().getCalendar(kidId, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ArrayList<TimeTable>> getTimeTables(String classId, int dayOfWeek) {
        return getRetrofitService().getTimeTables(classId, dayOfWeek)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ArrayList<TimeTable>> getMeanMenu(String classId, int dayOfWeek) {
        return getRetrofitService().getMeanMenu(classId, dayOfWeek)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Comments> getComments(String classId, String childrenId, int pageSize, int pageIndex) {
        return getRetrofitService().getComments(classId, childrenId, pageSize, pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Medias> getMedias(String classId, int pageSize, int pageIndex) {
        return getRetrofitService().getMedias(classId, pageSize, pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

//    @Override
//    public Observable<SubmitNote> submitNote(String kidId, long date, String note) {
//        return getRetrofitService().submitNote(kidId, date, note)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    @Override
    public Observable<Content> getContentComment(String commentId) {
        return getRetrofitService().getContentComment(commentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Content> getArticleContent(String articleId) {
        return getRetrofitService().getArticle(articleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Notifications> getNotifications(String kidId) {
        return getRetrofitService().getNotifications(kidId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<CalendarNotes> getCalendarNotes(String classId, Events events) {
        return getRetrofitService().getCalendarNotes(classId, events)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Photos> getAlbum(String classId, String albumId, int pageSize, int pageIndex) {
        return getRetrofitService().getAlbum(classId, albumId, pageSize, pageIndex).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Void> submitDayOff(DayOffContent dayOffContent) {
        return getRetrofitService().submitDayOff(dayOffContent).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
