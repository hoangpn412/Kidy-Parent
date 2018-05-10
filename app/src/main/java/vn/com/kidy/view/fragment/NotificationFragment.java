package vn.com.kidy.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.R;
import vn.com.kidy.data.model.login.Kid;
import vn.com.kidy.data.model.notification.Notification;
import vn.com.kidy.data.model.notification.Notifications;
import vn.com.kidy.utils.Tools;
import vn.com.kidy.view.activity.MainActivity;
import vn.com.kidy.view.adapter.NotificationAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int kidPos;
    private String kidId;
    private Kid kid;

    @BindView(R.id.view_shadow)
    View view_shadow;
    @BindView(R.id.rl_toolbar)
    RelativeLayout rl_toolbar;
    @BindView(R.id.rv_notifications)
    RecyclerView rv_notifications;
    @BindView(R.id.kid_avatar)
    SimpleDraweeView kid_avatar;
    @BindView(R.id.txt_kidName)
    TextView txt_kidName;
    @BindView(R.id.txt_birthday)
    TextView txt_birthday;

    private OnFragmentInteractionListener mListener;

    private NotificationAdapter adapter;
    private Notifications notifications;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param kidPos Parameter 1.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(int kidPos) {
        NotificationFragment fragment = new NotificationFragment();
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
        adapter = new NotificationAdapter();
        notifications = ((MainActivity) getActivity()).getNotifications();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_toolbar.setPadding(0, Tools.getStatusBarHeight(getContext()), 0, 0);
        }
        view_shadow.setOnClickListener(this);

        txt_kidName.setText(kid.getFullName());
        txt_birthday.setText(kid.getBirthday());
        kid_avatar.setImageURI(Uri.parse(kid.getAvatar()));

        rv_notifications.requestFocus();
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_notifications.setLayoutManager(linearLayoutManager);
        rv_notifications.setAdapter(adapter);

        rv_notifications.setAdapter(adapter);
        adapter.setItemClickListener((items, item, position) -> onNotificationClick(item));
        if (notifications != null && notifications.getNotifications() != null) {
            adapter.setItems(notifications.getNotifications());
        }
        adapter.notifyDataSetChanged();
    }

    private void onNotificationClick(Notification item) {
        Log.e("a", item.getNotifyId());
        ((MainActivity) getActivity()).onNotificationClick(item);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_shadow:
                getActivity().onBackPressed();
                break;
        }
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
