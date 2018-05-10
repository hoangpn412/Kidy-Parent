package vn.com.kidy.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.R;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.calendar.Calendar;
import vn.com.kidy.data.model.calendar.TimeData;
import vn.com.kidy.data.model.calendar.TimeTable;
import vn.com.kidy.data.model.home.Note;
import vn.com.kidy.data.model.login.Kid;
import vn.com.kidy.data.model.note.Message;
import vn.com.kidy.data.model.note.Notes;
import vn.com.kidy.interactor.CalendarInteractor;
import vn.com.kidy.network.client.Client;
import vn.com.kidy.presenter.CalendarPresenter;
import vn.com.kidy.utils.Tools;
import vn.com.kidy.view.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements CalendarPresenter.View, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int kidPos;
    private String kidId;
    private Kid kid;

    private Notes notes;
    private ArrayList<TimeTable> meanMenu, timeTables;

    private OnFragmentInteractionListener mListener;

    private CalendarPresenter calendarPresenter;

    private boolean dataFetched = false;
    private long curDate;
    private int year, month, day, dayofWeek;
//    private Calendar calendar;

    @BindView(R.id.sc_data)
    ScrollView sc_data;
    @BindView(R.id.rl_date)
    RelativeLayout rl_date;
    @BindView(R.id.card_view_note)
    CardView card_view_note;
    @BindView(R.id.ln_note_content)
    LinearLayout ln_note_content;
    @BindView(R.id.ln_time_table_content)
    LinearLayout ln_time_table_content;
    @BindView(R.id.ln_foot_menu_content)
    LinearLayout ln_foot_menu_content;
    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;
    @BindView(R.id.rl_add_note)
    RelativeLayout rl_add_note;
    @BindView(R.id.rl_add_request)
    RelativeLayout rl_add_request;
    @BindView(R.id.txt_date)
    TextView txt_date;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param kidPos Parameter 1.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(int kidPos) {
        CalendarFragment fragment = new CalendarFragment();
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
        calendarPresenter = new CalendarPresenter(new CalendarInteractor(new Client(Constants.API_SLL_URL)));
        calendarPresenter.setView(this);
        curDate = System.currentTimeMillis();

        java.util.Calendar calendar  = java.util.Calendar.getInstance();
        year = calendar.get(java.util.Calendar.YEAR);
        month = calendar.get(java.util.Calendar.MONTH) + 1;
        day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        dayofWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Log.e("a", "onViewCreated CalendarFragment");

        rl_add_note.setOnClickListener(this);
        rl_add_request.setOnClickListener(this);
        rl_date.setOnClickListener(this);

        txt_date.setText(Html.fromHtml(Tools.longtoDateWithDayofWeekString(curDate)));

        if (!dataFetched || notes == null || meanMenu == null || timeTables == null) {
            if (kidPos != ((MainActivity) getActivity()).getKidPos()) {
                kidPos = ((MainActivity) getActivity()).getKidPos();
                kid = ((MainActivity) getActivity()).getKids().get(kidPos);
                kidId = kid.getBabyId();
            }
            getData();
        } else {
            if (kidPos != ((MainActivity) getActivity()).getKidPos()) {
                kidPos = ((MainActivity) getActivity()).getKidPos();
                kid = ((MainActivity) getActivity()).getKids().get(kidPos);
                kidId = kid.getBabyId();
                refreshData();
            } else {
                getHomeNotesSuccess(notes);
                getMeanMenuSuccess(meanMenu);
                getTimeTableSuccess(timeTables);
            }
        }
    }

    private void getData() {
        calendarPresenter.onGetHomeNotes(kid.getClassId(), kid.getBabyId(), year, month, day);
        calendarPresenter.onGetTimeTables(kid.getClassId(), day);
        calendarPresenter.onGetMeanMenu(kid.getClassId(), day);
    }

    private void refreshData() {
        Log.e("a", "refreshData Calendar");
        dataFetched = false;

        getData();

        progress_loading.setVisibility(View.VISIBLE);
        sc_data.setVisibility(View.INVISIBLE);
    }

    public void refreshDateData(CalendarDay calendarDay) {
        Log.e("a", "refreshDateData");
        curDate = calendarDay.getDate().getTime();

        year = calendarDay.getYear();
        month = calendarDay.getMonth() + 1;
        day = calendarDay.getDay();
        dayofWeek = calendarDay.getCalendar().get(java.util.Calendar.DAY_OF_WEEK);

        txt_date.setText(Html.fromHtml(Tools.longtoDateWithDayofWeekString(curDate)));
        refreshData();
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
//    public void getDataSuccess(Calendar calendar) {
//        progress_loading.setVisibility(View.INVISIBLE);
//        sc_data.setVisibility(View.VISIBLE);
//        dataFetched = true;
////        this.calendar = calendar;
//        ((MainActivity) getActivity()).setNotes(calendar.getNotes());
//
//        // Card Note
//        ln_note_content.removeAllViews();
//        Message note;
//        for (int i = 0; i < calendar.getNotes().getMessages().size(); i++) {
//            note = calendar.getNotes().getMessages().get(i);
//            addNote(note, false);
//        }
////        // Card Time Table
////        ln_time_table_content.removeAllViews();
////        TimeData timeData;
////        for (int i = 0; i < calendar.getTimetable().getData().size(); i++) {
////            timeData = calendar.getTimetable().getData().get(i);
////            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_time_table_content, null, false);
////            TextView txt_time = view.findViewById(R.id.txt_table_time);
////            TextView txt_content = view.findViewById(R.id.txt_table_content);
////            txt_time.setText(timeData.getTime());
////            txt_content.setText(Html.fromHtml(timeData.getContent()));
////            ln_time_table_content.addView(view);
////        }
//        // Card Foot Menu
////        ln_foot_menu_content.removeAllViews();
////        for (int i = 0; i < calendar.getMenu().getData().size(); i++) {
////            timeData = calendar.getMenu().getData().get(i);
////            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_time_table_content, null, false);
////            TextView txt_time = view.findViewById(R.id.txt_table_time);
////            TextView txt_content = view.findViewById(R.id.txt_table_content);
////            txt_time.setText(timeData.getTime());
////            txt_content.setText(Html.fromHtml(timeData.getContent()));
////            ln_foot_menu_content.addView(view);
////        }
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
            ln_note_content.addView(view, 0);
//            calendar.getNotes().getData().add(0, note);
        } else {
            ln_note_content.addView(view);
        }
    }

    @Override
    public void getDataError(int statusCode) {
        Log.e("a", "getDataerror: " + statusCode);
    }

    @Override
    public void getHomeNotesSuccess(Notes notes) {
        Log.e("a", "getHomeNotesSuccess");
        this.notes = notes;
        progress_loading.setVisibility(View.INVISIBLE);
        sc_data.setVisibility(View.VISIBLE);

        ln_note_content.removeAllViews();
        ((MainActivity) getActivity()).setNotes(notes);
        Message note;
        for (int i = 0; i < notes.getMessages().size(); i++) {
            note = notes.getMessages().get(i);
            addNote(note, false);
        }
    }

    @Override
    public void getTimeTableSuccess(ArrayList<TimeTable> timeTables) {
    // Card Time Table
        this.timeTables = timeTables;
        ln_time_table_content.removeAllViews();
        TimeTable timeData;
        for (int i = 0; i < timeTables.size(); i++) {
            timeData = timeTables.get(i);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_time_table_content, null, false);
            TextView txt_time = view.findViewById(R.id.txt_table_time);
            TextView txt_content = view.findViewById(R.id.txt_table_content);
            txt_time.setText(timeData.getTime());
            txt_content.setText(Html.fromHtml(timeData.getContent()));
            ln_time_table_content.addView(view);
        }
    }

    @Override
    public void getMeanMenuSuccess(ArrayList<TimeTable> meanMenu) {
        this.meanMenu = meanMenu;
        ln_foot_menu_content.removeAllViews();
        TimeTable timeData;
        for (int i = 0; i < meanMenu.size(); i++) {
            timeData = meanMenu.get(i);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_time_table_content, null, false);
            TextView txt_time = view.findViewById(R.id.txt_table_time);
            TextView txt_content = view.findViewById(R.id.txt_table_content);
            txt_time.setText(timeData.getTime());
            txt_content.setText(Html.fromHtml(timeData.getContent()));
            ln_foot_menu_content.addView(view);
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
            case R.id.rl_date:
                ((MainActivity) getActivity()).addFullCalendarFragment(kidPos, curDate);
                break;
        }
    }

    public void addRequest() {
        ((MainActivity) getActivity()).addRequestFragment();
    }

    private void addNote() {
        ((MainActivity) getActivity()).addNoteFragment();
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
