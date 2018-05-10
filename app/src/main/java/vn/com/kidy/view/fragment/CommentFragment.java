package vn.com.kidy.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
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
import vn.com.kidy.data.model.comment.Comments;
import vn.com.kidy.data.model.login.Kid;
import vn.com.kidy.interactor.CommentsInteractor;
import vn.com.kidy.network.client.Client;
import vn.com.kidy.presenter.CommentsPresenter;
import vn.com.kidy.view.activity.MainActivity;
import vn.com.kidy.view.adapter.CommentAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment implements CommentsPresenter.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int kidPos;
    private Kid kid;
    private String kidId;
    private boolean dataFetched = false;

    private CommentsPresenter commentsPresenter;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.rv_comments)
    RecyclerView rv_comments;
    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;

    private Comments comments;
    private CommentAdapter adapter;

    public CommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param kidPos Parameter 1.
     * @return A new instance of fragment CommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentFragment newInstance(int kidPos) {
        CommentFragment fragment = new CommentFragment();
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
        commentsPresenter = new CommentsPresenter(new CommentsInteractor(new Client(Constants.API_SLL_URL)));
        commentsPresenter.setView(this);
        adapter = new CommentAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        rv_comments.requestFocus();
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_comments.setLayoutManager(linearLayoutManager);
        rv_comments.setAdapter(adapter);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(rv_comments.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        rv_comments.addItemDecoration(horizontalDecoration);

        adapter.setItemClickListener((items, item, position) -> {
//            Toast.makeText(getContext(), item.get, Toast.LENGTH_SHORT).show();
            ((MainActivity) getActivity()).addCommentDetailFragment(item.getWeek(), item.getYear(), item.getContent(), item.getFromDate(), item.getToDate());
        });

        if (!dataFetched) {
            if (kidPos != ((MainActivity) getActivity()).getKidPos()) {
                kidPos = ((MainActivity) getActivity()).getKidPos();
                kid = ((MainActivity) getActivity()).getKids().get(kidPos);
                kidId = kid.getBabyId();
            }
            getComments();
        } else {
            if (kidPos != ((MainActivity) getActivity()).getKidPos()) {
                kidPos = ((MainActivity) getActivity()).getKidPos();
                kid = ((MainActivity) getActivity()).getKids().get(kidPos);
                kidId = kid.getBabyId();
                refreshData();
            } else {
                getDataSuccess(comments);
            }
        }
    }

    private void getComments() {
        commentsPresenter.onGetComments(kid.getClassId(), kid.getBabyId(), 20, 0);
    }

    private void refreshData() {
        Log.e("a", "refreshData Comments");
        dataFetched = false;
        getComments();
        progress_loading.setVisibility(View.VISIBLE);
        rv_comments.setVisibility(View.INVISIBLE);
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
    public void getDataSuccess(Comments comments) {
        Log.e("a", "get list Comment success: " + comments.getComments().size());
        this.comments = comments;
        progress_loading.setVisibility(View.INVISIBLE);
        rv_comments.setVisibility(View.VISIBLE);
        adapter.setItems(comments.getComments());
        adapter.notifyDataSetChanged();
        rv_comments.smoothScrollToPosition(0);
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
