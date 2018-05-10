package vn.com.kidy.presenter;

import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.media.Medias;
import vn.com.kidy.interactor.MediasInteractor;
import vn.com.kidy.utils.Tools;

/**
 * Created by admin on 1/30/18.
 */

public class MediasPresenter extends Presenter<MediasPresenter.View> {

    private MediasInteractor mediasInteractor;

    public MediasPresenter(MediasInteractor mediasInteractor) {
        this.mediasInteractor = mediasInteractor;
    }

    public void onGetMedias(String classId, int pageSize, int pageIndex) {
        mediasInteractor.getMedias(classId, pageSize, pageIndex).subscribe(medias -> {
            if (medias == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
//                for (int i = 0; i < medias.getAlbums().size(); i++) {
//                    medias.getAlbums().get(i).setDateStr(Tools.longtoDate(medias.getAlbums().get(i).getDate()));
//                }
                getView().getDataSuccess(medias);
            }
        }, Throwable::printStackTrace);
    }

    public interface View extends Presenter.View {
        void getDataSuccess(Medias medias);

        void getDataError(int statusCode);
    }
}
