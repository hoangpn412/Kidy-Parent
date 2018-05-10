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
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.R;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.login.Kid;
import vn.com.kidy.data.model.news.News;
import vn.com.kidy.interactor.NewsInteractor;
import vn.com.kidy.network.client.Client;
import vn.com.kidy.presenter.NewsPresenter;
import vn.com.kidy.view.activity.MainActivity;
import vn.com.kidy.view.adapter.NewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements NewsPresenter.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int kidPos;
    private String kidId;
    private Kid kid;
    private News news;

    private OnFragmentInteractionListener mListener;

    private NewsPresenter newsPresenter;
    private boolean dataFetched = false;

    @BindView(R.id.rv_news)
    RecyclerView rv_news;
    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;

    private NewAdapter adapter;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param kidPos Parameter 1.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(int kidPos) {
        NewsFragment fragment = new NewsFragment();
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
        newsPresenter = new NewsPresenter(new NewsInteractor(new Client(Constants.API_NEWS_URL)));
        newsPresenter.setView(this);
        adapter = new NewAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        rv_news.requestFocus();
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_news.setLayoutManager(linearLayoutManager);
        rv_news.setAdapter(adapter);
        adapter.setItemClickListener((items, item, position) -> {
            Toast.makeText(getContext(), item.getNewsTitle(), Toast.LENGTH_SHORT).show();
            ((MainActivity) getActivity()).addArticleFragment(item.getNewsTitle(), item.getNewsContent(), item.getCreatedDate(), item.getNewsPresentImage());
        });
        if (!dataFetched) {
            if (kidPos != ((MainActivity) getActivity()).getKidPos()) {
                kidPos = ((MainActivity) getActivity()).getKidPos();
                kid = ((MainActivity) getActivity()).getKids().get(kidPos);
                kidId = kid.getBabyId();
            }
            newsPresenter.onGetNews(kid.getSchoolInfo().getUuid());
        } else {
            if (kidPos != ((MainActivity) getActivity()).getKidPos()) {
                kidPos = ((MainActivity) getActivity()).getKidPos();
                kid = ((MainActivity) getActivity()).getKids().get(kidPos);
                kidId = kid.getBabyId();
                refreshData();
            } else {
                getDataSuccess(news);
            }
        }
    }

    private void refreshData() {
        Log.e("a", "refreshData News");
        dataFetched = false;
        newsPresenter.onGetNews(kid.getSchoolInfo().getUuid());
        progress_loading.setVisibility(View.VISIBLE);
        rv_news.setVisibility(View.INVISIBLE);
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
    public void getDataSuccess(News news) {
        this.news = news;
        dataFetched = true;
        progress_loading.setVisibility(View.INVISIBLE);
        rv_news.setVisibility(View.VISIBLE);
//        Log.e("a", "news: " + news.getNewsList().size() + " " + news.getNewsList().get(0).getNewsTitle());
        adapter.setItems(news.getNewsList());
        adapter.notifyDataSetChanged();
        rv_news.smoothScrollToPosition(0);
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
