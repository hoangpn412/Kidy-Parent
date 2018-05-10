package vn.com.kidy.data;

/**
 * Created by admin on 1/13/18.
 */

public class Constants {
    public class FragmentName {
        public static final String LOGIN_FRAGMENT = "LOGIN_FRAGMENT";
        public static final String HOME_FRAGMENT = "HOME_FRAGMENT";
        public static final String CALENDAR_FRAGMENT = "CALENDAR_FRAGMENT";
        public static final String NEWS_FRAGMENT = "NEWS_FRAGMENT";
        public static final String COMMENT_FRAGMENT = "COMMENT_FRAGMENT";
        public static final String MEDIA_FRAGMENT = "MEDIA_FRAGMENT";
        public static final String ADDNOTE_FRAGMENT = "ADDNOTE_FRAGMENT";
        public static final String FULLCALENDAR_FRAGMENT = "FULLCALENDAR_FRAGMENT";
        public static final String CONTENTDETAIL_FRAGMENT = "CONTENTDETAIL_FRAGMENT";
        public static final String ARTICLE_FRAGMENT = "ARTICLE_FRAGMENT";
        public static final String NOTIFICATION_FRAGMENT = "NOTIFICATION_FRAGMENT";
        public static final String ALBUM_FRAGMENT = "ALBUM_FRAGMENT";
        public static final String VIEW_IMAGE_FRAGMENT = "VIEW_IMAGE_FRAGMENT";
        public static final String SETTING_FRAGMENT = "SETTING_FRAGMENT";
    }

    public static final String API_BASE_URL = "http://tubemate.biz/api/kidy/";
    public static final String API_ACCOUNT_URL = "http://api-account.kidysolution.com/";
    public static final String API_SLL_URL = "http://api-sll.kidysolution.com/";
    public static final String API_NEWS_URL = "http://api-news.kidysolution.com/";
    public static final String APP_PREF = "Kidy";

    public static final String IMAGE_BASE_URL = "http://image.kidysolution.com";

    public static final class EndPoint {
        public static final String LOGIN = "api/login";
        public static final String LOGIN_CONTACT = "api/validatetoken/{token}";
        public static final String LOGIN_KIDS = "api/parent/childrens/{parent}";
        public static final String CLASS_INFO = "api/school/class/{classId}";
        public static final String HOME_NEWS = "api/news/{schoolId}";
        public static final String HOME_NOTES = "api/school/class/{classId}/message/{childrenId}/{year}/{month}/{day}";
        public static final String CREATE_MESSAGE = "api/school/class/{classId}/message/{childrenId}";
        public static final String DAYOFF = "api/parent/dayoff";

        public static final String HOME = "home.txt";
        public static final String CALENDAR = "calendar.txt";

        public static final String TIMETABLE = "api/school/class/timetable/{classId}/{dayOfWeek}";
        public static final String MEANMENU = "api/school/class/meanmenu/{classId}/{dayOfWeek}";

        public static final String NEWS = "news.txt";
        public static final String COMMENTS = "api/school/class/{classId}/comments/{childrenId}";
        public static final String MEDIAS = "api/school/class/{classId}/albums";
        public static final String SUBMIT_NOTE = "submitnote.txt";
        public static final String COMMENT = "comment.txt";
        public static final String ARTICLE = "article.txt";
        public static final String NOTIFICATION = "notifications.txt";
        public static final String FULLCALENDAR = "api/calendar/class/{classId}/events";
        public static final String ALBUM = "api/school/class/{classId}/album/{albumId}/photos";
    }

    public static final class Params {
        public static final String TOKEN = "token";
        public static final String EMAIL = "email";
        public static final String PARENT = "parent";
        public static final String CLASSID = "classId";
        public static final String CHILDRENID = "childrenId";

        public static final String SCHOOLID = "schoolId";
        public static final String YEAR = "year";
        public static final String MONTH = "month";
        public static final String DAY = "day";

        public static final String DAYOFWEEK = "dayOfWeek";

        public static final String PASSWORD = "password";
        public static final String KID_ID = "kidId";
        public static final String DATE = "date";
        public static final String NOTE = "note";
        public static final String COMMENTID = "commentId";
        public static final String ARTICLEID = "articleId";
        public static final String ALBUMID = "albumId";

        public static final String PAGESIZE = "pageSize";
        public static final String PAGEINDEX = "pageIndex";
    }

    public static final class STATUS_CODE {
        public static final int LOGIN_SUCCESS = 1;
        public static final int LOGIN_ERROR = 2;
        public static final int SERVER_ERROR = 0;
        public static final int TOKEN_EXPIRED = 3;
    }

    public static final class NOTIFICATION_TYPE {
        public static final int TYPE_NEW = 0;
        public static final int TYPE_COMMENT = 1;
        public static final int TYPE_NOTE = 2;
        public static final int TYPE_ALBUM = 3;
    }

    public static final String ADD_NOTE_NAME = "THÊM GHI CHÚ";
    public static final String ADD_REQUEST_NAME = "XIN NGHỈ HỌC";
    public static final String FolderDownload = "/KidyImages";
}
