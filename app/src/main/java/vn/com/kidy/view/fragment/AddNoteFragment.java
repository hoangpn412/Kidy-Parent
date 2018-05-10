package vn.com.kidy.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.R;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.home.Note;
import vn.com.kidy.data.model.note.Message;
import vn.com.kidy.data.model.note.Notes;
import vn.com.kidy.data.model.login.Kid;
import vn.com.kidy.data.model.note.DayOffContent;
import vn.com.kidy.data.model.note.NoteContent;
import vn.com.kidy.interactor.SubmitNoteInteractor;
import vn.com.kidy.network.client.Client;
import vn.com.kidy.presenter.SubmitNotePresenter;
import vn.com.kidy.utils.Tools;
import vn.com.kidy.view.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNoteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNoteFragment extends Fragment implements View.OnClickListener, SubmitNotePresenter.View, DatePickerDialog.OnDateSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int kidPos;
    private Kid kid;
    private String kidId;
    private boolean dataFetched = false;
    private String noteName;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.main)
    CoordinatorLayout main;
    @BindView(R.id.rl_toolbar)
    RelativeLayout rl_toolbar;
    @BindView(R.id.ln_note_content)
    LinearLayout ln_note_content;
    @BindView(R.id.edt_addnote)
    EditText edt_addnote;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.progress_submit)
    ProgressBar progress_submit;
    @BindView(R.id.txt_note_name)
    TextView txt_note_name;
    @BindView(R.id.txt_note_time)
    TextView txt_note_time;
    @BindView(R.id.rl_choose_date)
    RelativeLayout rl_choose_date;
    @BindView(R.id.rl_add_note)
    RelativeLayout rl_add_note;
    @BindView(R.id.rl_add_request)
    RelativeLayout rl_add_request;
    @BindView(R.id.txt_choose_from_date)
    TextView txt_choose_from_date;
    @BindView(R.id.txt_choose_to_date)
    TextView txt_choose_to_date;
    @BindView(R.id.txt_full_calendar)
    TextView txt_full_calendar;

    private Notes notes;
    private Message addNote;
    private SubmitNotePresenter submitNotePresenter;
    private DatePickerDialog dpd;

    private boolean pickFrom = true;
    private CalendarDay selectedDate;

    private Date fromDate, toDate;

    public AddNoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param kidPos Parameter 1.
     * @return A new instance of fragment AddNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNoteFragment newInstance(int kidPos, String noteName) {
        AddNoteFragment fragment = new AddNoteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, kidPos);
        args.putString(ARG_PARAM2, noteName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kidPos = getArguments().getInt(ARG_PARAM1);
            noteName = getArguments().getString(ARG_PARAM2);
            kid = ((MainActivity) getActivity()).getKids().get(kidPos);
            kidId = kid.getBabyId();
            notes = ((MainActivity) getActivity()).getNotes();
//            if (notes != null) {
////                Log.e("a", "onCreate AddNoteFragment: " + notes.getData().size());
//            }
        }

        initDatePicker();

        submitNotePresenter = new SubmitNotePresenter(new SubmitNoteInteractor(new Client(Constants.API_SLL_URL)));
        submitNotePresenter.setView(this);
        selectedDate = ((MainActivity) getActivity()).getSelectedDate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_toolbar.setPadding(0, Tools.getStatusBarHeight(getContext()), 0, 0);
        }

        initView();
        rl_add_note.setOnClickListener(this);
        rl_add_request.setOnClickListener(this);
        txt_choose_from_date.setOnClickListener(this);
        txt_choose_to_date.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        txt_full_calendar.setOnClickListener(this);

//        if (!dataFetched) {
        preventNotes();
//        }


    }

    private void initView() {
        txt_note_name.setText(noteName);
        long curDate = System.currentTimeMillis();
        txt_choose_from_date.setText(Tools.longtoDateWithString(curDate));
        txt_choose_to_date.setText(Tools.longtoDateWithString(curDate));

        fromDate = Calendar.getInstance().getTime();
        toDate = Calendar.getInstance().getTime();
        Log.e("a", fromDate + " " + toDate + " " + curDate);

        if (noteName.equals(Constants.ADD_NOTE_NAME)) {
            rl_choose_date.setVisibility(View.GONE);
            edt_addnote.setHint("Nhập nội dung ghi chú");
            ln_note_content.setVisibility(View.VISIBLE);
            rl_add_note.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.blue_button));
            rl_add_request.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bolder_white_button));
        } else {
            rl_choose_date.setVisibility(View.VISIBLE);
            edt_addnote.setHint("Lý do xin nghỉ học");
            ln_note_content.setVisibility(View.GONE);
            rl_add_note.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bolder_white_button));
            rl_add_request.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.orange_button));
        }
    }

    private void preventNotes() {
//        Log.e("a", "preventNotes: " + notes.getData().size());
        dataFetched = true;
        ln_note_content.removeAllViews();
        if (notes == null || notes.getMessages() == null) return;
        Message note;
        for (int i = 0; i < notes.getMessages().size(); i++) {
            note = notes.getMessages().get(i);
            addNote(note, false);
        }
    }

    private void addNote(Message note, boolean insert) {
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
        } else {
            ln_note_content.addView(view);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_submit:
                submitNote();
                break;
            case R.id.rl_add_note:
                noteName = Constants.ADD_NOTE_NAME;
                initView();
                break;
            case R.id.rl_add_request:
                noteName = Constants.ADD_REQUEST_NAME;
                initView();
                break;
            case R.id.txt_choose_from_date:
                pickFrom = true;
                showDatePickerDiaglog();
                break;
            case R.id.txt_choose_to_date:
                pickFrom = false;
                showDatePickerDiaglog();
                break;
            case R.id.txt_full_calendar:
                ((MainActivity) getActivity()).addFullCalendarFragment(kidPos, selectedDate.getDate().getTime());
                break;
        }
    }

    private void submitNote() {
        String note = edt_addnote.getText().toString();
        if (note == null || note.length() == 0) {
            Toast.makeText(getContext(), "Bạn chưa nhập nội dung", Toast.LENGTH_LONG).show();
            return;
        }
        if (noteName.equals(Constants.ADD_NOTE_NAME)) {
            long date = System.currentTimeMillis();
            NoteContent noteContent = new NoteContent(note, false);
            submitNotePresenter.submitNote(kid.getClassId(), kid.getBabyId(), noteContent);
            addNote = new Message();
            addNote.setIsFromParent(true);
//            addNote.setName("Phụ huynh");
            addNote.setContent(note);
        } else {
            Log.e("a", fromDate + " ..." + toDate);
            if (fromDate.getTime() > toDate.getTime()) {
                Toast.makeText(getContext(), "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc", Toast.LENGTH_LONG).show();
                return;
            }
            DayOffContent dayOffContent = new DayOffContent(kid.getClassId(), kid.getBabyId(), fromDate, toDate, note);
            submitNotePresenter.submitDayOff(dayOffContent);
        }
        progress_submit.setVisibility(View.VISIBLE);
        btn_submit.setVisibility(View.GONE);
    }

    @Override
    public void addNoteSuccess() {
//        Log.e("a", submitNote.getMessage());
        progress_submit.setVisibility(View.GONE);
        btn_submit.setVisibility(View.VISIBLE);
        edt_addnote.setFocusable(true);
        edt_addnote.setText("");
        addNote(addNote, true);
        ((MainActivity) getActivity()).addNote(addNote);
    }

    @Override
    public void addDayOffSuccess() {
        Snackbar.make(main, "Đã gửi đơn xin nghỉ học!",
                Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", view3 -> {

                })
                .show();

        progress_submit.setVisibility(View.GONE);
        btn_submit.setVisibility(View.VISIBLE);
        edt_addnote.setFocusable(true);
        edt_addnote.setText("");
    }

    @Override
    public void getDataError(int statusCode) {
        progress_submit.setVisibility(View.GONE);
        btn_submit.setVisibility(View.VISIBLE);
        edt_addnote.setFocusable(true);
        Toast.makeText(getContext(), "Xảy ra lỗi, bạn vui lòng thử lại", Toast.LENGTH_LONG).show();
    }

    @Override
    public Context context() {
        return null;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        String dom, moy;
        if (dayOfMonth < 10) {
            dom = "0" + dayOfMonth;
        } else {
            dom = "" + dayOfMonth;
        }
        if ((monthOfYear + 1) < 10) {
            moy = "0" + (monthOfYear + 1);
        } else {
            moy = "" + (monthOfYear + 1);
        }
        String dateStr = dom + "/" + moy + "/" + year;
        if (pickFrom) {
            txt_choose_from_date.setText(dateStr);
            fromDate = new Date(year - 1900, monthOfYear, dayOfMonth);
        } else {
            toDate = new Date(year - 1900, monthOfYear, dayOfMonth);
            txt_choose_to_date.setText(dateStr);
        }
        Toast.makeText(getContext(), date, Toast.LENGTH_LONG).show();
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

    private void initDatePicker() {
        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_1);
        dpd.setAccentColor(getContext().getResources().getColor(R.color.colorPrimary));
    }

    private void showDatePickerDiaglog() {
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onResume() {
        selectedDate = ((MainActivity) getActivity()).getSelectedDate();
        txt_note_time.setText(Html.fromHtml(Tools.calendarDaytoDateWithDayofWeekString(selectedDate)));
        Log.e("a", "onResume");
        super.onResume();
    }
}
