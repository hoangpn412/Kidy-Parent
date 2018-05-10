package vn.com.kidy.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.com.kidy.R;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.media.Medias;
import vn.com.kidy.data.model.note.Message;
import vn.com.kidy.data.model.note.Notes;
import vn.com.kidy.data.model.login.Kid;
import vn.com.kidy.data.model.news.News;
import vn.com.kidy.data.model.news.NewsList;
import vn.com.kidy.interactor.HomeInteractor;
import vn.com.kidy.network.client.Client;
import vn.com.kidy.network.retrofit.RetrofitService;
import vn.com.kidy.presenter.HomePresenter;
import vn.com.kidy.view.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HomePresenter.View, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int kidPos;
    private String kidId;
    private Kid kid;
//    private Home home;
    private Notes notes;
    private News news;
    private Medias medias;

    private OnFragmentInteractionListener mListener;

    private HomePresenter homePresenter;

    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;
    @BindView(R.id.sc_data)
    ScrollView sc_data;
    @BindView(R.id.item_home_news)
    RelativeLayout item_home_news;
    @BindView(R.id.item_home_note)
    RelativeLayout item_home_note;
    @BindView(R.id.item_home_media)
    RelativeLayout item_home_media;
    @BindView(R.id.txt_card_news_name)
    TextView txt_card_news_name;
    @BindView(R.id.txt_card_note_name)
    TextView txt_card_note_name;
    @BindView(R.id.txt_card_note_date)
    TextView txt_card_note_date;
    @BindView(R.id.rl_add_note)
    RelativeLayout rl_add_note;
    @BindView(R.id.rl_add_request)
    RelativeLayout rl_add_request;
    @BindView(R.id.media_image)
    SimpleDraweeView media_image;
    @BindView(R.id.txt_media_name)
    TextView txt_media_name;
    @BindView(R.id.txt_media_date)
    TextView txt_media_date;
    @BindView(R.id.card_home_news)
    LinearLayout card_home_news;
    @BindView(R.id.card_home_news_data)
    LinearLayout card_home_news_data;
    @BindView(R.id.card_home_note)
    LinearLayout card_home_note;
    @BindView(R.id.card_home_note_data)
    LinearLayout card_home_note_data;

    private boolean dataFetched = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param kidPos Parameter 1.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(int kidPos) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, kidPos);
        fragment.setArguments(args);
        Log.e("a", "newInstance: " + kidPos);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("a", "onCreate");
        if (getArguments() != null) {
            kidPos = getArguments().getInt(ARG_PARAM1);
            kid = ((MainActivity) getActivity()).getKids().get(kidPos);
            kidId = kid.getBabyId();
        }
        homePresenter = new HomePresenter(new HomeInteractor(new Client(Constants.API_NEWS_URL)));
        homePresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Log.e("a", "onViewCreated Home Fragment: " + kidPos + " " + ((MainActivity) getActivity()).getKidPos());

        rl_add_note.setOnClickListener(this);
        rl_add_request.setOnClickListener(this);

        if (!dataFetched || news == null || notes == null || medias == null) {
            if (kidPos != ((MainActivity) getActivity()).getKidPos()) {
                kidPos = ((MainActivity) getActivity()).getKidPos();
                kid = ((MainActivity) getActivity()).getKids().get(kidPos);
                kidId = kid.getBabyId();
            }
            getHomeData();
        } else {
            if (kidPos != ((MainActivity) getActivity()).getKidPos()) {
                kidPos = ((MainActivity) getActivity()).getKidPos();
                kid = ((MainActivity) getActivity()).getKids().get(kidPos);
                kidId = kid.getBabyId();
                Log.e("a", "kidPos: " + kidPos + " " + kid.getClassId());
                refreshData();
            } else {
                getHomeNewsSuccess(news);
                getHomeNotesSuccess(notes);
                getHomeMediaSuccess(medias);
            }
        }
    }


    private void setNewBaseUrl(String baseUrl) {
        Retrofit retrofit = retrofitBuilder(baseUrl);
        retrofit.create(RetrofitService.class);
    }


    private Retrofit retrofitBuilder(String baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        return new Retrofit.Builder().baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    private void getHomeData() {
//        setNewBaseUrl(Constants.API_NEWS_URL);
        homePresenter.onGetHomeNews(kid.getSchoolInfo().getUuid());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

//        homePresenter = new HomePresenter(new HomeInteractor(new Client(Constants.API_SLL_URL)));
        Log.e("a", year + " " + month + " " + day);
        RetrofitUrlManager.getInstance().putDomain("douban", Constants.API_SLL_URL);
        homePresenter.onGetHomeNotes(kid.getClassId(), kid.getBabyId(), year, month, day);

        RetrofitUrlManager.getInstance().putDomain("douban", Constants.API_SLL_URL);
        homePresenter.onGetHomeMedias(kid.getClassId(), 1, 0);
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
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void refreshData() {
        dataFetched = false;
        getHomeData();
        progress_loading.setVisibility(View.VISIBLE);
        sc_data.setVisibility(View.INVISIBLE);
    }

//    @Override
//    public void getDataSuccess(Home home) {
//        Log.e("a", "getDataSuccess");
//        dataFetched = true;
//        progress_loading.setVisibility(View.INVISIBLE);
//        this.home = home;
//        ((MainActivity) getActivity()).setNotes(home.getNotes());
//
//        if (home.getStatus() == Constants.STATUS_CODE.SERVER_ERROR) {
//            Toast.makeText(getContext(), home.getMessage(), Toast.LENGTH_LONG).show();
//            return;
//        }
//        // Data Success
//        sc_data.setVisibility(View.VISIBLE);
//
//        // Card Home
//        if (home.getNews().getData().size() == 0) {
//            item_home_news.setVisibility(View.GONE);
//        } else {
//            card_home_news_data.removeAllViews();
//            txt_card_news_name.setText(home.getNews().getTitle());
//
//            int newsSize = home.getNews().getData().size();
//            for (int i = 0; i < newsSize; i++) {
//                final NewsList mNew = home.getNews().getData().get(i);
//                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_new, null, false);
//                TextView txt_title_new = view.findViewById(R.id.txt_title_new);
//                TextView txt_time_new = view.findViewById(R.id.txt_time_new);
//                txt_title_new.setText(mNew.getNewsTitle());
//                txt_time_new.setText(mNew.getCreatedDate());
//                if (i == newsSize - 1) {
//                    View view_line_new = view.findViewById(R.id.view_line_new);
//                    view_line_new.setVisibility(View.GONE);
//                }
//                view.setOnClickListener(view1 -> ((MainActivity) getActivity()).addArticleFragment(mNew.getNewsContent(), mNew.getNewsId(), mNew.getCreatedDate(), mNew.getNewsPresentImage()));
//                card_home_news_data.addView(view);
//            }
//        }
//
//        // Card Note
////        txt_card_note_name.setText(home.getNotes().getTitle());
////        txt_card_note_date.setText(home.getNotes().getDateStr());
//        card_home_note_data.removeAllViews();
//        Message note;
//        for (int i = 0; i < home.getNotes().getMessages().size(); i++) {
//            note = home.getNotes().getMessages().get(i);
//            addNote(note, false);
//        }
//        // Card Media
//        Log.e("a", home.getMedia().getTitle() + "...");
//        if (home.getMedia().getTitle().equals("")) {
//            item_home_media.setVisibility(View.GONE);
//        } else {
//            txt_media_name.setText(home.getMedia().getTitle());
//            txt_media_date.setText(home.getMedia().getDateStr());
//            media_image.setImageURI(Uri.parse(home.getMedia().getImage()));
//            item_home_media.setOnClickListener(view -> {
//                Log.e("a", "Media: " + home.getMedia().getAlbumId());
//                ((MainActivity) getActivity()).addAlbumFragment(home.getMedia().getAlbumId(), home.getMedia().getTitle());
//            });
//        }
//    }

    public void addNote(Message note, boolean insert) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_note, null, false);
        View view_circle = view.findViewById(R.id.view_note_circle);
        TextView txt_note_content = view.findViewById(R.id.txt_note_content);
        String html;
        if (!note.getIsFromParent()) {
            view_circle.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.circle_blue));
            html = "<b><font color=\"#2CA4A2\">Giáo viên: </font></b>";
        } else {
            html = "<b><font color=\"#F9A83C\">Phụ huynh: </font></b>";
            view_circle.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.cirle_orange));
        }
        html += note.getContent();
        txt_note_content.setText(Html.fromHtml(html));
        if (insert) {
            card_home_note_data.addView(view, 0);
//            home.getNotes().getData().add(0, note);
        } else {
            card_home_note_data.addView(view);
        }
    }

    @Override
    public void getDataError(int statusCode) {

    }

    @Override
    public void getHomeNewsSuccess(News news) {
        this.news = news;
        progress_loading.setVisibility(View.INVISIBLE);
        Log.e("a", news.getNewsList().size() + " news");
        sc_data.setVisibility(View.VISIBLE);
        if (news.getNewsList().size() == 0) {
            card_home_news.setVisibility(View.GONE);
        } else {
            card_home_news_data.removeAllViews();
            int newsSize = news.getNewsList().size();
            if (newsSize > 3) newsSize = 3;
            for (int i = 0; i < newsSize; i++) {
                final NewsList mNew = news.getNewsList().get(i);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_new, null, false);
                TextView txt_title_new = view.findViewById(R.id.txt_title_new);
                TextView txt_time_new = view.findViewById(R.id.txt_time_new);
                txt_title_new.setText(mNew.getNewsTitle());
                txt_time_new.setText(Html.fromHtml(mNew.getCreatedDate()));
                if (i == newsSize - 1) {
                    View view_line_new = view.findViewById(R.id.view_line_new);
                    view_line_new.setVisibility(View.GONE);
                }
                view.setOnClickListener(view1 -> ((MainActivity) getActivity()).addArticleFragment(mNew.getNewsTitle(), mNew.getNewsContent(), mNew.getCreatedDate(), mNew.getNewsPresentImage()));
                card_home_news_data.addView(view);
            }
        }
    }

    @Override
    public void getHomeNotesSuccess(Notes notes) {
        Log.e("a", "getHomeNotesSuccess");
        card_home_note_data.removeAllViews();
        this.notes = notes;
        ((MainActivity) getActivity()).setNotes(notes);
        Message note;
        for (int i = 0; i < notes.getMessages().size(); i++) {
            note = notes.getMessages().get(i);
            addNote(note, false);
        }
    }

    @Override
    public void getHomeMediaSuccess(Medias medias) {
        this.medias = medias;
        if (medias.getAlbums().size() == 0) {
            item_home_media.setVisibility(View.GONE);
        } else {
            txt_media_name.setText(medias.getAlbums().get(0).getTitle());
            txt_media_date.setText(medias.getAlbums().get(0).getCreatedDate());
            media_image.setImageURI(Uri.parse(Constants.IMAGE_BASE_URL + medias.getAlbums().get(0).getThumbnail()));
            item_home_media.setOnClickListener(view -> {
                ((MainActivity) getActivity()).addAlbumFragment(medias.getAlbums().get(0).getId(), medias.getAlbums().get(0).getTitle());
            });
        }
    }

    @Override
    public Context context() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_add_note:
                addNote();
                break;
            case R.id.rl_add_request:
                addRequest();
                break;
        }
    }

    public void addNote() {
        ((MainActivity) getActivity()).addNoteFragment();
    }

    public void addRequest() {
        ((MainActivity) getActivity()).addRequestFragment();
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

    @Override
    public void onResume() {
        super.onResume();
    }
}
