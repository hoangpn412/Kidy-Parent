package vn.com.kidy.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.R;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.login.Account;
import vn.com.kidy.data.model.login.Kid;
import vn.com.kidy.data.model.login.Login;
import vn.com.kidy.data.model.login.User;
import vn.com.kidy.interactor.LoginInteractor;
import vn.com.kidy.network.client.Client;
import vn.com.kidy.presenter.LoginPresenter;
import vn.com.kidy.utils.GridSpacingItemDecoration;
import vn.com.kidy.view.activity.MainActivity;
import vn.com.kidy.view.adapter.KidAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, LoginPresenter.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //BindView
    // Login Screen
    @BindView(R.id.rl_login)
    RelativeLayout rl_login;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.txt_error)
    TextView txt_error;
    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;
    @BindView(R.id.btn_submit_login)
    Button btn_submit_login;

    // List Kids Screen
    @BindView(R.id.rl_lst_kids)
    RelativeLayout rl_lst_kids;
    @BindView(R.id.parent_avatar)
    SimpleDraweeView parent_avatar;
    @BindView(R.id.txt_hello)
    TextView txt_hello;
    @BindView(R.id.rv_kids)
    RecyclerView rv_kids;

    private String email, password, token;
    private KidAdapter adapter;

    private LoginPresenter loginPresenter, loginPresenter2;
    private SharedPreferences preferences;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        preferences = getContext().getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE);
        email = preferences.getString("email", "");
        token = preferences.getString("token", "");

        adapter = new KidAdapter();

        loginPresenter = new LoginPresenter(new LoginInteractor(new Client(Constants.API_ACCOUNT_URL)));
        loginPresenter.setView(this);

        loginPresenter2 = new LoginPresenter(new LoginInteractor(new Client(Constants.API_SLL_URL)));
        loginPresenter2.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView();

    }

    private void initView() {
        btn_submit_login.setOnClickListener(this);
        rv_kids.requestFocus();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        rv_kids.setLayoutManager(mLayoutManager);
        rv_kids.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        rv_kids.setItemAnimator(new DefaultItemAnimator());
        rv_kids.setAdapter(adapter);
        adapter.setItemClickListener(
                (kids, kid, position) -> onKidClick(kids, kid, position));
        //
        if (email.length() > 0 && token.length() > 0) {
            edt_email.setText(email);
            edt_password.setText("********");

            progress_loading.setVisibility(View.VISIBLE);
            btn_submit_login.setVisibility(View.INVISIBLE);
            edt_email.setFocusable(false);
            edt_password.setFocusable(false);

            getUserContact();
//            onBtnLoginClick();
        }
    }

    private void getUserContact() {
        loginPresenter.onGetUserContact(token);
    }

    private void onKidClick(List<Kid> kids, Kid kid, int position) {
        Toast.makeText(getContext(), kid.getFullName(), Toast.LENGTH_LONG).show();
        ((MainActivity) getActivity()).setCurKid(position);
        ((MainActivity) getActivity()).closeLoginFragment();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_login:
                onBtnLoginClick();
                break;
        }
    }

    private void onBtnLoginClick() {
        email = edt_email.getText().toString();
        password = edt_password.getText().toString();
        if (email.length() == 0) {
            txt_error.setVisibility(View.VISIBLE);
            txt_error.setText(getString(R.string.error_username));
            return;
        }
        if (password.length() == 0) {
            txt_error.setVisibility(View.VISIBLE);
            txt_error.setText(getString(R.string.error_password));
            return;
        }
        txt_error.setVisibility(View.INVISIBLE);
        progress_loading.setVisibility(View.VISIBLE);
        btn_submit_login.setVisibility(View.INVISIBLE);
        edt_email.setFocusable(false);
        edt_password.setFocusable(false);

        Account account = new Account(email, password);
        loginPresenter.onGetLogin(account);
    }

    @Override
    public void loginSuccess(Login login) {
//        progress_loading.setVisibility(View.INVISIBLE);
//        txt_error.setVisibility(View.INVISIBLE);
//        doLoginSuccess(login);
        token = login.getToken();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("token", token);
        editor.commit();

        getUserContact();
    }

    private void doLoginSuccess(Login login) {
//        Toast.makeText(getContext(), getString(R.string.hello) + " " + parent.getTitle() + " " + parent.getName(), Toast.LENGTH_LONG).show();
//        rl_login.setVisibility(View.INVISIBLE);
//        rl_lst_kids.setVisibility(View.VISIBLE);
//
//        Uri uri = Uri.parse(parent.getAvatar());
//        parent_avatar.setImageURI(uri);
//        txt_hello.setText(getString(R.string.hello) + " " + parent.getTitle() + " " + parent.getName());
//
//        adapter.setItems(parent.getKids());
//        adapter.notifyDataSetChanged();
//
//        ((MainActivity) getActivity()).setMyParent(parent);
//
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("email", email);
//        editor.putString("password", password);
//        editor.commit();
    }

    @Override
    public void loginError(int statusCode, String message) {
        progress_loading.setVisibility(View.INVISIBLE);
        txt_error.setVisibility(View.VISIBLE);
        switch (statusCode) {
            case Constants.STATUS_CODE.SERVER_ERROR:
                txt_error.setText(getString(R.string.error_server));
                break;
            case Constants.STATUS_CODE.LOGIN_ERROR:
                txt_error.setText(getString(R.string.error_login));
                break;
            case Constants.STATUS_CODE.TOKEN_EXPIRED:
                txt_error.setText("Phiên đăng nhập hết hạn. Bạn vui lòng đăng nhập lại!");
                break;
        }
        edt_password.setText("");
        btn_submit_login.setVisibility(View.VISIBLE);
        edt_email.setFocusableInTouchMode(true);
        edt_email.setFocusable(true);
        edt_password.setFocusableInTouchMode(true);
        edt_password.setFocusable(true);
    }

    @Override
    public void getUserSuccess(User user) {
        loginPresenter2.onGetKids(user.getContact().getUserId());

        ((MainActivity) getActivity()).setUser(user);
    }

    @Override
    public void getKidsSuccess(ArrayList<vn.com.kidy.data.model.login.Kid> kids) {
        Log.e("a", kids.size() + " kids");
        User user = ((MainActivity) getActivity()).getUser();

        Toast.makeText(getContext(), getString(R.string.hello) + " " + user.getContact().getFullName(), Toast.LENGTH_LONG).show();
        rl_login.setVisibility(View.INVISIBLE);
        rl_lst_kids.setVisibility(View.VISIBLE);

        if (user.getContact().getAvatar() != null) {
            Uri uri = Uri.parse(user.getContact().getAvatar());
            parent_avatar.setImageURI(uri);
        }
        txt_hello.setText(getString(R.string.hello) + " " + user.getContact().getFullName());

        adapter.setItems(kids);
        adapter.notifyDataSetChanged();
        ((MainActivity) getActivity()).setKids(kids);
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
