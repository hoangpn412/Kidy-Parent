package vn.com.kidy.presenter;

import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.media.Album;
import vn.com.kidy.data.model.media.Photo;
import vn.com.kidy.data.model.media.Photos;
import vn.com.kidy.interactor.AlbumInteractor;

/**
 * Created by admin on 3/2/18.
 */

public class AlbumPresenter extends Presenter<AlbumPresenter.View> {

    private AlbumInteractor albumInteractor;

    public AlbumPresenter(AlbumInteractor albumInteractor) {
        this.albumInteractor = albumInteractor;
    }

    public void onGetAlbum(String classId, String albumId, int pageSize, int pageIndex) {
        albumInteractor.getAlbum(classId, albumId, pageSize, pageIndex).subscribe((Photos photos) -> {
            if (photos == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                getView().getDataSuccess(photos);
            }
        }, Throwable::printStackTrace);
    }

    public interface View extends Presenter.View {
        void getDataSuccess(Photos photos);

        void getDataError(int statusCode);
    }
}
