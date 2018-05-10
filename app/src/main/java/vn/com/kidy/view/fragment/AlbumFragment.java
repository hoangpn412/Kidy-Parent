package vn.com.kidy.view.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.R;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.login.Kid;
import vn.com.kidy.data.model.media.Album;
import vn.com.kidy.data.model.media.Photos;
import vn.com.kidy.interactor.AlbumInteractor;
import vn.com.kidy.network.client.Client;
import vn.com.kidy.presenter.AlbumPresenter;
import vn.com.kidy.utils.GridSpacingItemDecoration;
import vn.com.kidy.utils.Tools;
import vn.com.kidy.view.activity.MainActivity;
import vn.com.kidy.view.adapter.AlbumAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlbumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment implements AlbumPresenter.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String albumId, title;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.rv_album)
    RecyclerView rv_album;
    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;
    @BindView(R.id.rl_toolbar)
    RelativeLayout rl_toolbar;
    @BindView(R.id.txt_note_name)
    TextView txt_note_name;
    @BindView(R.id.txt_note_time)
    TextView txt_note_time;
    @BindView(R.id.btn_back)
    ImageView btn_back;

    private AlbumPresenter albumPresenter;
    private AlbumAdapter adapter;
    private Photos photos;

    private int kidPos;
    private String kidId;
    private Kid kid;

    public AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param albumId Parameter 1.
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(int kidPos, String albumId, String title) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, kidPos);
        args.putString(ARG_PARAM2, albumId);
        args.putString(ARG_PARAM3, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            albumId = getArguments().getString(ARG_PARAM2);
            title = getArguments().getString(ARG_PARAM3);
            kidPos = getArguments().getInt(ARG_PARAM1);
            kid = ((MainActivity) getActivity()).getKids().get(kidPos);
            kidId = kid.getBabyId();
        }
        albumPresenter = new AlbumPresenter(new AlbumInteractor(new Client(Constants.API_SLL_URL)));
        albumPresenter.setView(this);
        adapter = new AlbumAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_toolbar.setPadding(0, Tools.getStatusBarHeight(getContext()), 0, 0);
        }

        if (photos != null && photos.getPhotos().size() > 0) {
            progress_loading.setVisibility(View.INVISIBLE);
        } else {
            progress_loading.setVisibility(View.VISIBLE);
            albumPresenter.onGetAlbum(kid.getClassId(), albumId, 100, 0);
        }
        txt_note_name.setText("ALBUM");
        txt_note_time.setText(title);
        btn_back.setOnClickListener(view1 -> getActivity().onBackPressed());

        rv_album.requestFocus();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        rv_album.setLayoutManager(mLayoutManager);
        rv_album.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        rv_album.setAdapter(adapter);
        adapter.setItemClickListener((items, item, position) -> {
            String[] images = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                images[i] = items.get(i).getPath();
            }
            ((MainActivity) getActivity()).addViewImageFragment(images, position);
        });
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
    public void getDataSuccess(Photos photos) {
        this.photos = photos;
        progress_loading.setVisibility(View.INVISIBLE);
        adapter.setItems(photos.getPhotos());
        adapter.notifyDataSetChanged();
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
