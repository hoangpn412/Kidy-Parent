package vn.com.kidy.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.R;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.calendar.CalendarNote;
import vn.com.kidy.data.model.calendar.CalendarNotes;
import vn.com.kidy.data.model.calendar.Events;
import vn.com.kidy.data.model.login.Kid;
import vn.com.kidy.interactor.FullCalendarInteractor;
import vn.com.kidy.network.client.Client;
import vn.com.kidy.presenter.FullCalendarPresenter;
import vn.com.kidy.utils.Tools;
import vn.com.kidy.view.activity.MainActivity;
import vn.com.kidy.view.widget.decorators.EventDecorator;
import vn.com.kidy.view.widget.decorators.HighlightWeekendsDecorator;
import vn.com.kidy.view.widget.decorators.OneDayDecorator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FullCalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FullCalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullCalendarFragment extends Fragment implements OnDateSelectedListener, FullCalendarPresenter.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private long date;
    private int kidPos;
    private String kidId;
    private Kid kid;

    private OnFragmentInteractionListener mListener;

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;
    @BindView(R.id.sv_view)
    ScrollView sv_view;
    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;

    private FullCalendarPresenter fullCalendarPresenter;

    public FullCalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param date Parameter 1.
     * @return A new instance of fragment FullCalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FullCalendarFragment newInstance(int kidPos, long date) {
        FullCalendarFragment fragment = new FullCalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, kidPos);
        args.putLong(ARG_PARAM2, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kidPos = getArguments().getInt(ARG_PARAM1);
            date = getArguments().getLong(ARG_PARAM2);
            kid = ((MainActivity) getActivity()).getKids().get(kidPos);
            kidId = kid.getBabyId();
        }

        fullCalendarPresenter = new FullCalendarPresenter(new FullCalendarInteractor(new Client(Constants.API_SLL_URL)));
        fullCalendarPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            sv_view.setPadding(0, Tools.getStatusBarHeight(getContext()), 0, 0);
        }

        initCalendar();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -90);
        Date fromDate = calendar.getTime();
        Date toDate = Calendar.getInstance().getTime();
        Events events = new Events(kidId, fromDate, toDate);
        fullCalendarPresenter.onGetCalendarNotes(kid.getClassId(), events);
    }

    private void initCalendar() {
        calendarView.setOnDateChangedListener(this);

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR) - 1, Calendar.JANUARY, 1);

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR) + 1, Calendar.DECEMBER, 31);

        calendarView.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();
        calendarView.setCurrentDate(new Date(date));
        calendarView.setSelectedDate(new Date(date));

        calendarView.addDecorators(
//                new MySelectorDecorator(getActivity()),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );
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

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();
        Log.e("a", date.getDay() + " " + date.getMonth() + " " + date.getYear());
        ((MainActivity) getActivity()).setSelectedDate(date);
        getActivity().onBackPressed();
    }

    @Override
    public void getDataSuccess(CalendarNotes calendarNotes) {
        Log.e("a", "getDataSuccess");
        calendarView.setVisibility(View.VISIBLE);
        progress_loading.setVisibility(View.GONE);

        ArrayList<CalendarDay> dates1 = new ArrayList<>();
        ArrayList<CalendarDay> dates2 = new ArrayList<>();
        ArrayList<CalendarDay> dates3 = new ArrayList<>();
        ArrayList<CalendarDay> dates12 = new ArrayList<>();
        ArrayList<CalendarDay> dates13 = new ArrayList<>();
        ArrayList<CalendarDay> dates23 = new ArrayList<>();
        ArrayList<CalendarDay> dates123 = new ArrayList<>();
        for (int i = 0; i < calendarNotes.getCalendarNote().size(); i++) {
            CalendarNote calendarNote = calendarNotes.getCalendarNote().get(i);
//            Date date = new Date(calendarNote.getDate());

            Calendar cal  = Tools.stringDatetoCalendar(calendarNote.getDate());
            CalendarDay day = CalendarDay.from(cal);

            if ((calendarNote.getHasMealMenu() || calendarNote.getHasTimeTable()) && !calendarNote.getHasTeacherMessage() && !calendarNote.getHasParentMessage()) {
                dates1.add(day);
            }
            if (!(calendarNote.getHasMealMenu() || calendarNote.getHasTimeTable()) && calendarNote.getHasTeacherMessage() && !calendarNote.getHasParentMessage()) {
                dates2.add(day);
            }
            if (!(calendarNote.getHasMealMenu() || calendarNote.getHasTimeTable()) && !calendarNote.getHasTeacherMessage() && calendarNote.getHasParentMessage()) {
                dates3.add(day);
            }
            if ((calendarNote.getHasMealMenu() || calendarNote.getHasTimeTable()) && calendarNote.getHasTeacherMessage() && !calendarNote.getHasParentMessage()) {
                dates12.add(day);
            }
            if ((calendarNote.getHasMealMenu() || calendarNote.getHasTimeTable()) && !calendarNote.getHasTeacherMessage() && calendarNote.getHasParentMessage()) {
                dates13.add(day);
            }
            if (!(calendarNote.getHasMealMenu() || calendarNote.getHasTimeTable()) && calendarNote.getHasTeacherMessage() && calendarNote.getHasParentMessage()) {
                dates23.add(day);
            }
            if ((calendarNote.getHasMealMenu() || calendarNote.getHasTimeTable()) && calendarNote.getHasTeacherMessage() && calendarNote.getHasParentMessage()) {
                dates123.add(day);
            }
        }
        if (dates1.size() > 0)
            calendarView.addDecorator(new EventDecorator(dates1, getResources().getColor(R.color.cirle_time_table)));
        if (dates2.size() > 0)
            calendarView.addDecorator(new EventDecorator(dates2, getResources().getColor(R.color.orange_button_color)));
        if (dates3.size() > 0)
            calendarView.addDecorator(new EventDecorator(dates3, getResources().getColor(R.color.blue_button_color)));
        if (dates12.size() > 0)
            calendarView.addDecorator(new EventDecorator(dates12, getResources().getColor(R.color.cirle_time_table), getResources().getColor(R.color.orange_button_color)));
        if (dates13.size() > 0)
            calendarView.addDecorator(new EventDecorator(dates13, getResources().getColor(R.color.cirle_time_table), getResources().getColor(R.color.blue_button_color)));
        if (dates23.size() > 0)
            calendarView.addDecorator(new EventDecorator(dates23, getResources().getColor(R.color.orange_button_color), getResources().getColor(R.color.blue_button_color)));
        if (dates123.size() > 0)
            calendarView.addDecorator(new EventDecorator(dates123, getResources().getColor(R.color.cirle_time_table), getResources().getColor(R.color.orange_button_color), getResources().getColor(R.color.blue_button_color)));
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
