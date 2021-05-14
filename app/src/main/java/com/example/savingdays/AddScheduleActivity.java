package com.example.savingdays;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.savingdays.SQLiteHelper;
import com.example.savingdays.Product;
import com.example.savingdays.Schedule;

import java.time.LocalDate;
import java.util.Locale;

public class AddScheduleActivity extends AppCompatActivity
        implements View.OnClickListener {

    public static final String EXTRA_SCHEDULE_ID = "com.example.savingdays.schedule_id";
    public static final String EXTRA_SELECTED_DATE = "com.example.savingdays.selected_date";

    private int mScheduleId;                 // 일정 ID (-1 이면 추가모드, 아니면 수정모드)

    private Button mStartDateButton;        // 날짜 버튼
    private Button mEndDateButton;
    private EditText mScheduleTitleEdit;    // 일정 제목 에딧텍스트

    private LocalDate mStartDate;           // 시작 날짜
    private LocalDate mEndDate;             // 종료 날짜

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        // 전달된 일정 ID 를 확인한다
        mScheduleId = getIntent().getIntExtra(EXTRA_SCHEDULE_ID, -1);
        Schedule schedule = null;
        if (mScheduleId != -1) {
            schedule = SQLiteHelper.getInstance(this).getSchedule(mScheduleId);
        }

        mScheduleTitleEdit = findViewById(R.id.editScheduleTitle);
        if (schedule != null) {
            mScheduleTitleEdit.setText(schedule.getTitle());
        }

        // 버튼에 리스너를 설정한다
        ImageButton cancelIButton = findViewById(R.id.ibtnCancel);
        ImageButton confirmIButton = findViewById(R.id.ibtnConfirm);
        cancelIButton.setOnClickListener(this);
        confirmIButton.setOnClickListener(this);

        mStartDateButton = findViewById(R.id.btnStartDate);
        mEndDateButton = findViewById(R.id.btnEndDate);
        mStartDateButton.setOnClickListener(this);
        mEndDateButton.setOnClickListener(this);

        // 시작, 종료 날짜 초기화

        if (schedule != null) {
            mStartDate = schedule.getStartDate();
            mEndDate = schedule.getEndDate();
        } else {
            String strSelectedDate = getIntent().getStringExtra(EXTRA_SELECTED_DATE);
            mStartDate = LocalDate.parse(strSelectedDate);
            mEndDate = LocalDate.parse(strSelectedDate);
        }
        updateDateButtons();
    }

    // 시작, 종료 날짜 버튼 업데이트

    private void updateDateButtons() {

        String strStartDate = String.format(Locale.getDefault(),
                "%d년 %d월 %d일",
                mStartDate.getYear(), mStartDate.getMonthValue(), mStartDate.getDayOfMonth());

        String strEndDate = String.format(Locale.getDefault(),
                "%d년 %d월 %d일",
                mEndDate.getYear(), mEndDate.getMonthValue(), mEndDate.getDayOfMonth());

        mStartDateButton.setText(strStartDate);
        mEndDateButton.setText(strEndDate);
    }

    // 버튼 클릭을 처리한다

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.ibtnCancel) {
            // 취소 버튼 : 액티비티 종료
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == R.id.ibtnConfirm) {
            // 확인 버튼 : 일정 추가 후 액티비티 종료
            if (addOrUpdateSchedule()) {
                setResult(RESULT_OK);
                finish();
            }
        } else if (id == R.id.btnStartDate) {
            // 시작 날짜 버튼 : 날짜 선택 대화상자 띄우기
            showDateDialog(true);
        } else if (id == R.id.btnEndDate) {
            // 종료 날짜 버튼 : 날짜 선택 대화상자 띄우기
            showDateDialog(false);
        }
    }

    // DB 에 일정을 추가 및 업데이트한다

    private boolean addOrUpdateSchedule() {

        // 일정 제목을 확인한다
        String scheduleTitle = mScheduleTitleEdit.getText().toString().trim();

        if (scheduleTitle.isEmpty()) {
            Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        // DB 에 추가 및 업데이트
        Schedule schedule;
        if (mScheduleId == -1) {
            schedule = new Schedule(scheduleTitle, mStartDate,mEndDate);
            SQLiteHelper.getInstance(this).addSchedule(schedule);
        } else {
            schedule = new Schedule(mScheduleId, scheduleTitle, mStartDate,mEndDate);
            SQLiteHelper.getInstance(this).updateSchedule(schedule);
        }

        return true;
    }

    // 시작/종료 날짜 선택 대화상자를 띄운다

    private void showDateDialog(boolean startOrEndDate) {

        LocalDate initialDate = startOrEndDate ? mStartDate : mEndDate;
        int initialYear = initialDate.getYear();
        int initialMonth = initialDate.getMonthValue() - 1;
        int initialDayOfMonth = initialDate.getDayOfMonth();

        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // 날짜 선택 시 시작/종료 날짜를 변경한다
                    LocalDate date = LocalDate.of(year, month + 1, dayOfMonth);

                    if (startOrEndDate) {
                        if (!date.isAfter(mEndDate)) {
                            mStartDate = date;
                            updateDateButtons();
                        } else {
                            Toast.makeText(this,
                                    "앞선 날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!date.isBefore(mStartDate)) {
                            mEndDate = date;
                            updateDateButtons();
                        } else {
                            Toast.makeText(this,
                                    "뒤의 날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                initialYear, initialMonth, initialDayOfMonth
        ).show();
    }

}



