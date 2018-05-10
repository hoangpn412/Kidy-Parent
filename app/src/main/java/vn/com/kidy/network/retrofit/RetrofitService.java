package vn.com.kidy.network.retrofit;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.calendar.Calendar;
import vn.com.kidy.data.model.calendar.CalendarNotes;
import vn.com.kidy.data.model.calendar.Events;
import vn.com.kidy.data.model.calendar.TimeTable;
import vn.com.kidy.data.model.comment.Comments;
import vn.com.kidy.data.model.comment.Content;
import vn.com.kidy.data.model.media.Photos;
import vn.com.kidy.data.model.note.Notes;
import vn.com.kidy.data.model.login.Account;
import vn.com.kidy.data.model.login.ClassInfo;
import vn.com.kidy.data.model.login.Kid;
import vn.com.kidy.data.model.login.Login;
import vn.com.kidy.data.model.login.User;
import vn.com.kidy.data.model.media.Medias;
import vn.com.kidy.data.model.news.News;
import vn.com.kidy.data.model.note.DayOffContent;
import vn.com.kidy.data.model.note.NoteContent;
import vn.com.kidy.data.model.notification.Notifications;

/**
 * Created by Family on 4/22/2017.
 */

public interface RetrofitService {

    @POST(Constants.EndPoint.LOGIN)
    Observable<Login> getLogin(@Body Account account);

    @GET(Constants.EndPoint.LOGIN_CONTACT)
    Observable<User> getLoginContact(@Path(Constants.Params.TOKEN) String token);

    @GET(Constants.EndPoint.LOGIN_KIDS)
    Observable<ArrayList<Kid>> getLoginKids(@Path(Constants.Params.PARENT) String parent);

    @GET(Constants.EndPoint.CLASS_INFO)
    Observable<ClassInfo> getClassInfo(@Path(Constants.Params.CLASSID) String classId);

    @GET(Constants.EndPoint.HOME_NEWS)
    Observable<News> getHomeNews(@Path(Constants.Params.SCHOOLID) String schoolId);

    @Headers({"Domain-Name: douban"}) // Add the Domain-Name header
    @GET(Constants.EndPoint.HOME_NOTES)
    Observable<Notes> getHomeNotes(@Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.CHILDRENID) String childrenId, @Path(Constants.Params.YEAR) int year, @Path(Constants.Params.MONTH) int month, @Path(Constants.Params.DAY) int day);

    @POST(Constants.EndPoint.CREATE_MESSAGE)
    Observable<Void> submitNote(@Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.CHILDRENID) String childrenId, @Body NoteContent noteContent);

//    @GET(Constants.EndPoint.HOME)
//    Observable<Home> getHome(@Query(Constants.Params.KID_ID) String kidId);

    @GET(Constants.EndPoint.CALENDAR)
    Observable<Calendar> getCalendar(@Query(Constants.Params.KID_ID) String kidId, @Query(Constants.Params.DATE) long date);

    @GET(Constants.EndPoint.TIMETABLE)
    Observable<ArrayList<TimeTable>> getTimeTables(@Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.DAYOFWEEK) int dayOfWeek);

    @GET(Constants.EndPoint.MEANMENU)
    Observable<ArrayList<TimeTable>> getMeanMenu(@Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.DAYOFWEEK) int dayOfWeek);

    @GET(Constants.EndPoint.NEWS)
    Observable<News> getNews(@Query(Constants.Params.KID_ID) String kidId);

    @GET(Constants.EndPoint.COMMENTS)
    Observable<Comments> getComments(@Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.CHILDRENID) String childrenId, @Query(Constants.Params.PAGESIZE) int pageSize, @Query(Constants.Params.PAGEINDEX) int pageIndex);

    @Headers({"Domain-Name: douban"}) // Add the Domain-Name header
    @GET(Constants.EndPoint.MEDIAS)
    Observable<Medias> getMedias(@Path(Constants.Params.CLASSID) String classId, @Query(Constants.Params.PAGESIZE) int pageSize, @Query(Constants.Params.PAGEINDEX) int pageIndex);

//    @GET(Constants.EndPoint.SUBMIT_NOTE)
//    Observable<SubmitNote> submitNote(@Query(Constants.Params.KID_ID) String kidId, @Query(Constants.Params.DATE) long date, @Query(Constants.Params.NOTE) String note);

    @GET(Constants.EndPoint.COMMENT)
    Observable<Content> getContentComment(@Query(Constants.Params.COMMENTID) String commentId);

    @GET(Constants.EndPoint.ARTICLE)
    Observable<Content> getArticle(@Query(Constants.Params.ARTICLEID) String articleId);

    @GET(Constants.EndPoint.NOTIFICATION)
    Observable<Notifications> getNotifications(@Query(Constants.Params.KID_ID) String kidId);

    @POST(Constants.EndPoint.FULLCALENDAR)
    Observable<CalendarNotes> getCalendarNotes(@Path(Constants.Params.CLASSID) String classId, @Body Events events);

    @POST(Constants.EndPoint.DAYOFF)
    Observable<Void> submitDayOff(@Body DayOffContent dayOffContent);

    @GET(Constants.EndPoint.ALBUM)
    Observable<Photos> getAlbum(@Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.ALBUMID) String albumId, @Query(Constants.Params.PAGESIZE) int pageSize, @Query(Constants.Params.PAGEINDEX) int pageIndex);
}
