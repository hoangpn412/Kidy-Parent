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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.R;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.comment.Content;
import vn.com.kidy.interactor.ArticleInteractor;
import vn.com.kidy.network.client.Client;
import vn.com.kidy.presenter.ArticlePresenter;
import vn.com.kidy.utils.Tools;
import vn.com.kidy.view.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArticleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleFragment extends Fragment implements ArticlePresenter.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String title;
    private String time;
    private String image;
    private String content;

    @BindView(R.id.rl_toolbar)
    RelativeLayout rl_toolbar;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.img_art)
    SimpleDraweeView img_art;
    @BindView(R.id.wv_content)
    WebView wv_content;
    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;
    @BindView(R.id.txt_note_name)
    TextView txt_note_name;
    @BindView(R.id.txt_note_time)
    TextView txt_note_time;
    @BindView(R.id.btn_back)
    ImageView btn_back;

    private OnFragmentInteractionListener mListener;

    private ArticlePresenter articlePresenter;

    public ArticleFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ArticleFragment newInstance(String title, String content, String time, String image) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, content);
        args.putString(ARG_PARAM3, time);
        args.putString(ARG_PARAM4, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            content = getArguments().getString(ARG_PARAM2);
            time = getArguments().getString(ARG_PARAM3);
            image = getArguments().getString(ARG_PARAM4);
        }
        articlePresenter = new ArticlePresenter(new ArticleInteractor(new Client(Constants.API_BASE_URL)));
        articlePresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        txt_note_name.setText("TIN TỨC");
        txt_note_time.setText(Html.fromHtml(((MainActivity) getActivity()).getKid().getSchoolInfo().getName()));
        btn_back.setOnClickListener(view1 -> getActivity().onBackPressed());

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_toolbar.setPadding(0, Tools.getStatusBarHeight(getContext()), 0, 0);
        }

        txt_title.setText(title);
        txt_time.setText(Html.fromHtml(time));
        img_art.setImageURI(Constants.IMAGE_BASE_URL + Uri.parse(image));
        wv_content.setWebViewClient(new WebViewClient());
        wv_content.getSettings().setJavaScriptEnabled(true);
        wv_content.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (content != null && content.length() > 0) {
            wv_content.loadData(content, "text/html", "utf-8");
            progress_loading.setVisibility(View.GONE);
        } else {
            progress_loading.setVisibility(View.VISIBLE);
//            articlePresenter.onGetArticleContent(articleId);
        }
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
    public void getDataSuccess(Content content) {
        this.content = content.getContent();
        progress_loading.setVisibility(View.GONE);
        wv_content.loadData(content.getContent(), "text/html", "utf-8");
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
