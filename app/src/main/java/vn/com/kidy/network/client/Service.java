package vn.com.kidy.network.client;


import java.util.ArrayList;

import io.reactivex.Observable;
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
import vn.com.kidy.data.model.login.User;
import vn.com.kidy.data.model.login.Login;
import vn.com.kidy.data.model.media.Medias;
import vn.com.kidy.data.model.news.News;
import vn.com.kidy.data.model.note.DayOffContent;
import vn.com.kidy.data.model.note.NoteContent;
import vn.com.kidy.data.model.notification.Notifications;

/**
 * Created by Family on 4/22/2017.
 */

public interface Service {
    Observable<Login> getLogin(Account account);
    Observable<User> getLoginContact(String token);
    Observable<ArrayList<Kid>> getLoginKids(String parent);
    Observable<ClassInfo> getClassInfo(String classId);
    Observable<News> getHomeNews(String schoolid);
    Observable<Notes> getHomeNotes(String classId, String childrenId, int year, int month, int day);

    Observable<Void> submitNote(String classId, String childrenId, NoteContent noteContent);

//    Observable<Home> getHome(String kidId);
    Observable<Calendar> getCalendar(String kidId, long date);
    Observable<ArrayList<TimeTable>> getTimeTables(String classId, int dayOfWeek);
    Observable<ArrayList<TimeTable>> getMeanMenu(String classId, int dayOfWeek);
//    Observable<News> getNews(String kidId);
    Observable<Comments> getComments(String classId, String childrenId, int pageSize, int pageIndex);
    Observable<Medias> getMedias(String classId, int pageSize, int pageIndex);
//    Observable<SubmitNote> submitNote(String kidId, long date, String note);
    Observable<Content> getContentComment(String commentId);
    Observable<Content> getArticleContent(String articleId);
    Observable<Notifications> getNotifications(String kidId);
    Observable<CalendarNotes> getCalendarNotes(String classId, Events events);
    Observable<Photos> getAlbum(String classId, String albumId, int pageSize, int pageIndex);

    Observable<Void> submitDayOff(DayOffContent dayOffContent);
}
