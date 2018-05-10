package vn.com.kidy.view.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.R;
import vn.com.kidy.data.Constants;
import vn.com.kidy.utils.Tools;
import vn.com.kidy.view.activity.MainActivity;
import vn.com.kidy.view.adapter.ImageSliderAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewImageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String[] images;
    private int position;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.vp_image)
    ViewPager vp_image;
    @BindView(R.id.main)
    CoordinatorLayout main;
    @BindView(R.id.btn_close)
    ImageView btn_close;
    @BindView(R.id.btn_download)
    ImageView btn_download;
    @BindView(R.id.txt_position)
    TextView txt_position;

    public ViewImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param images Parameter 1.
     * @return A new instance of fragment ViewImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewImageFragment newInstance(String[] images, int position) {
        ViewImageFragment fragment = new ViewImageFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, images);
        args.putInt(ARG_PARAM2, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            images = getArguments().getStringArray(ARG_PARAM1);
            position = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            main.setPadding(0, Tools.getStatusBarHeight(getContext()), 0, 0);
        }

        btn_close.setOnClickListener(view1 -> getActivity().onBackPressed());

        btn_download.setOnClickListener(view12 -> {
            if (((MainActivity) getActivity()).hasPermissions()) {
                downloadManager(images[position], "" + System.currentTimeMillis() + ".png");
            } else {
                Snackbar.make(main, R.string.write_permission_denied,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", view3 -> ((MainActivity) getActivity()).requestPermissions())
                        .show();

            }
        });

        txt_position.setText("" + (position + 1) + "/" + images.length);

        vp_image.setAdapter(new ImageSliderAdapter(getContext(), images));
        vp_image.setCurrentItem(position);
        vp_image.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pos) {
                position = pos;
                txt_position.setText("" + (position + 1) + "/" + images.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void downloadManager(String url, String name) {
        DownloadManager downloadmanager;
        downloadmanager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(Constants.IMAGE_BASE_URL + url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        File dir = new File(Environment.getExternalStorageDirectory()
                .toString() + Constants.FolderDownload);

        dir.mkdirs();


        Uri downloadLocation = Uri.fromFile(new File(dir, name));

        request.setDestinationUri(downloadLocation)
                .setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI
                                | DownloadManager.Request.NETWORK_MOBILE)
                .setTitle(name).setDescription("Downloading")
                .setMimeType("image/*");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            request.setShowRunningNotification(true);
        } else {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }

        downloadmanager.enqueue(request);
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
