package vn.com.kidy.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import vn.com.kidy.data.model.comment.Content;
import vn.com.kidy.interactor.ContentCommentInteractor;
import vn.com.kidy.network.client.Client;
import vn.com.kidy.presenter.ContentCommentPresenter;
import vn.com.kidy.utils.Tools;
import vn.com.kidy.view.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommentDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommentDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentDetailFragment extends Fragment {//} implements ContentCommentPresenter.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "week";
    private static final String ARG_PARAM2 = "year";
    private static final String ARG_PARAM3 = "content";
    private static final String ARG_PARAM4 = "fromdate";
    private static final String ARG_PARAM5 = "todate";

    // TODO: Rename and change types of parameters
    private int week, year;
    private String fromdate, todate;
    private String content;

    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.txt_content)
    TextView txt_content;
    @BindView(R.id.rl_toolbar)
    RelativeLayout rl_toolbar;
    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;
    @BindView(R.id.txt_note_name)
    TextView txt_note_name;
    @BindView(R.id.txt_note_time)
    TextView txt_note_time;
    @BindView(R.id.btn_back)
    ImageView btn_back;

    private OnFragmentInteractionListener mListener;

    private ContentCommentPresenter contentCommentPresenter;

    public CommentDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CommentDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentDetailFragment newInstance(int week, int year, String content, String fromDate, String toDate) {
        CommentDetailFragment fragment = new CommentDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, week);
        args.putInt(ARG_PARAM2, year);
        args.putString(ARG_PARAM3, content);
        args.putString(ARG_PARAM4, fromDate);
        args.putString(ARG_PARAM5, toDate);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            week = getArguments().getInt(ARG_PARAM1);
            year = getArguments().getInt(ARG_PARAM2);
            content = getArguments().getString(ARG_PARAM3);
            fromdate = getArguments().getString(ARG_PARAM4);
            todate = getArguments().getString(ARG_PARAM5);
        }
//        contentCommentPresenter = new ContentCommentPresenter(new ContentCommentInteractor(new Client(Constants.API_BASE_URL)));
//        contentCommentPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        txt_note_name.setText("NHẬN XÉT");
        txt_note_time.setText("Tuần " + week + " năm " + year);
        btn_back.setOnClickListener(view1 -> getActivity().onBackPressed());

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_toolbar.setPadding(0, Tools.getStatusBarHeight(getContext()), 0, 0);
        }

        txt_title.setText("Tuần " + week + " năm " + year);
        txt_time.setText(Html.fromHtml("Từ ngày " + fromdate + " đến " + todate));
        txt_content.setText(Html.fromHtml(content));
//        if (content != null && content.length() > 0) {
//            progress_loading.setVisibility(View.GONE);
//
//        } else {
//            progress_loading.setVisibility(View.VISIBLE);
////            contentCommentPresenter.onGetContentComment(commentId);
//        }
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

//    @Override
//    public void getDataSuccess(Content content) {
//        progress_loading.setVisibility(View.GONE);
//        this.content = content.getContent();
//        txt_note_name.setText(title);
//        txt_note_time.setText(Tools.longtoDateWithDayofWeekString(date));
//        txt_content.setText(Html.fromHtml(content.getContent()));
//    }

//    @Override
//    public void getDataError(int statusCode) {
//
//    }
//
//    @Override
//    public Context context() {
//        return null;
//    }

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
