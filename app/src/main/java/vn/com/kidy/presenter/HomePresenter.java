package vn.com.kidy.presenter;


import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.media.Medias;
import vn.com.kidy.data.model.media.Photos;
import vn.com.kidy.data.model.note.Notes;
import vn.com.kidy.data.model.news.News;
import vn.com.kidy.interactor.HomeInteractor;
import vn.com.kidy.utils.Tools;

/**
 * Created by admin on 1/22/18.
 */

public class HomePresenter extends Presenter<HomePresenter.View> {

    private HomeInteractor homeInteractor;

    public HomePresenter(HomeInteractor homeInteractor) {
        this.homeInteractor = homeInteractor;
    }

    public void onGetHome(String kidId) {
//        homeInteractor.getHome(kidId).subscribe(home -> {
//            Log.e("a", home.getMessage() + kidId);
//            if (home == null) {
//                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
//            } else {
//                for (int i = 0; i < home.getNews().getData().size(); i++) {
//                    home.getNews().getData().get(i).setDateStr(Tools.longtoDate(home.getNews().getData().get(i).getDate()));
//                }
//                home.getNotes().setDateStr(Tools.longtoDate(home.getNotes().getDate()));
//                home.getMedia().setDateStr(Tools.longtoDate(home.getMedia().getDate()));
//                getView().getDataSuccess(home);
//            }
//        }, Throwable::printStackTrace);
    }

    public void onGetHomeNews(String schoolId) {
        homeInteractor.getHomeNews(schoolId).subscribe(news -> {
            if (news == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                for (int i = 0; i < news.getNewsList().size(); i++) {
                    news.getNewsList().get(i).setCreatedDate(Tools.dateToStringDate(news.getNewsList().get(i).getCreatedDate()));
                }
                getView().getHomeNewsSuccess(news);
            }
        }, throwable -> {
            throwable.printStackTrace();
            getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
        });
    }

    public void onGetHomeNotes(String classId, String childrenId, int year, int month, int day) {
        homeInteractor.getHomeNotes(classId, childrenId, year, month, day).subscribe(notes -> {
            if (notes == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                getView().getHomeNotesSuccess(notes);
            }
        }, throwable -> {
            throwable.printStackTrace();
            getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
        });
    }

    public void onGetHomeMedias(String classId, int pageSize, int pageIndex) {
        homeInteractor.getHomeMedias(classId, pageSize, pageIndex).subscribe(medias -> {
            if (medias == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
//                for (int i = 0; i < medias.getAlbums().size(); i++) {
//                    medias.getAlbums().get(i).setDateStr(Tools.longtoDate(medias.getAlbums().get(i).getDate()));
//                }
                getView().getHomeMediaSuccess(medias);
            }
        }, Throwable::printStackTrace);
    }

    public interface View extends Presenter.View {
//        void getDataSuccess(Home home);

        void getDataError(int statusCode);

        void getHomeNewsSuccess(News news);

        void getHomeNotesSuccess(Notes notes);

        void getHomeMediaSuccess(Medias medias);
    }
}
