package vn.com.kidy.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.R;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.login.Kid;
import vn.com.kidy.data.model.media.Album;
import vn.com.kidy.data.model.media.Medias;
import vn.com.kidy.interactor.MediasInteractor;
import vn.com.kidy.network.client.Client;
import vn.com.kidy.presenter.MediasPresenter;
import vn.com.kidy.view.activity.MainActivity;
import vn.com.kidy.view.adapter.MediaAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MediaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MediaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaFragment extends Fragment implements MediasPresenter.View{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int kidPos;
    private Kid kid;
    private String kidId;
    private boolean dataFetched = false;
    private Medias medias;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.rv_medias)
    RecyclerView rv_medias;
    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;

    private MediasPresenter mediasPresenter;
    private MediaAdapter adapter;

    public MediaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param kidPos Parameter 1.
     * @return A new instance of fragment MediaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MediaFragment newInstance(int kidPos) {
        MediaFragment fragment = new MediaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, kidPos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kidPos = getArguments().getInt(ARG_PARAM1);
            kid = ((MainActivity) getActivity()).getKids().get(kidPos);
            kidId = kid.getBabyId();
        }
        mediasPresenter = new MediasPresenter(new MediasInteractor(new Client(Constants.API_SLL_URL)));
        mediasPresenter.setView(this);
        adapter = new MediaAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medias, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        rv_medias.requestFocus();
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_medias.setLayoutManager(linearLayoutManager);
        rv_medias.setAdapter(adapter);
        adapter.setItemClickListener((items, item, position) -> onMediaClick(item));

        if (!dataFetched) {
            if (kidPos != ((MainActivity) getActivity()).getKidPos()) {
                kidPos = ((MainActivity) getActivity()).getKidPos();
                kid = ((MainActivity) getActivity()).getKids().get(kidPos);
                kidId = kid.getBabyId();
            }
            mediasPresenter.onGetMedias(kid.getClassId(), 20, 0);
        } else {
            if (kidPos != ((MainActivity) getActivity()).getKidPos()) {
                kidPos = ((MainActivity) getActivity()).getKidPos();
                kid = ((MainActivity) getActivity()).getKids().get(kidPos);
                kidId = kid.getBabyId();
                refreshData();
            } else {
                getDataSuccess(medias);
            }
        }
    }

    private void onMediaClick(Album album) {
        ((MainActivity) getActivity()).addAlbumFragment(album.getId(), album.getTitle());
    }

    private void refreshData() {
        Log.e("a", "refreshData Medias");
        dataFetched = false;
        mediasPresenter.onGetMedias(kid.getClassId(), 20, 0);
        progress_loading.setVisibility(View.VISIBLE);
        rv_medias.setVisibility(View.INVISIBLE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void getDataSuccess(Medias medias) {
        this.medias = medias;
        progress_loading.setVisibility(View.INVISIBLE);
        rv_medias.setVisibility(View.VISIBLE);
        adapter.setItems(medias.getAlbums());
        adapter.notifyDataSetChanged();
        rv_medias.smoothScrollToPosition(0);
    }

    @Override
    public void getDataError(int statusCode) {

    }

    @Override
    public Context context() {
        return null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
