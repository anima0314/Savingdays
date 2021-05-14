package com.example.savingdays;





import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.savingdays.SQLiteHelper;
import com.example.savingdays.Food;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddFoodActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_FOOD_ID = "com.example.savingdays.food_id";
    public static final String EXTRA_SELECTED_DATE = "com.example.savingdays.selected_date";

    private int mFoodId;                 // 제품 ID (-1 이면 추가모드, 아니면 수정모드)

    private Button mOpenDateButton;         // 날짜 버튼
    private Button mDueDateButton;
    private EditText mFoodTitleEdit;     // 제품 제목 에딧텍스트


    private LocalDate mOpenDate;            // 개봉 날짜
    private LocalDate mDueDate;             // 소비기한 날짜

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // 전달된 제품 ID 를 확인한다
        mFoodId = getIntent().getIntExtra(EXTRA_FOOD_ID, -1);
        Food food = null;
        if (mFoodId != -1) {
            food = SQLiteHelper.getInstance(this).getFood(mFoodId);
        }

        mFoodTitleEdit = findViewById(R.id.editFoodTitle);
        if (food != null) {
            mFoodTitleEdit.setText(food.getTitle());
        }

        // 버튼에 리스너를 설정한다
        ImageButton cancelIButton = findViewById(R.id.ibtnCancel);
        ImageButton confirmIButton = findViewById(R.id.ibtnConfirm);
        cancelIButton.setOnClickListener(this);
        confirmIButton.setOnClickListener(this);

        mOpenDateButton = findViewById(R.id.btnOpenDate);
        mDueDateButton = findViewById(R.id.btnDueDate);
        mOpenDateButton.setOnClickListener(this);
        mDueDateButton.setOnClickListener(this);

        // 개봉, 소비기한 날짜 초기화
        if (food != null) {
            mOpenDate = food.getOpenDate();
            mDueDate = food.getDueDate();
        } else {
            String strSelectedDate = getIntent().getStringExtra(EXTRA_SELECTED_DATE);
            mOpenDate = LocalDate.parse(strSelectedDate);
            mDueDate = LocalDate.parse(strSelectedDate);
        }
        updateDateButtons();


    }

    // 개봉, 소비기한 날짜 버튼 업데이트

    private void updateDateButtons() {

        String strOpenDate = String.format(Locale.getDefault(),
                "%d년 %d월 %d일",
                mOpenDate.getYear(), mOpenDate.getMonthValue(), mOpenDate.getDayOfMonth());

        String strDueDate = String.format(Locale.getDefault(),
                "%d년 %d월 %d일",
                mDueDate.getYear(), mDueDate.getMonthValue(), mDueDate.getDayOfMonth());

        mOpenDateButton.setText(strOpenDate);
        mDueDateButton.setText(strDueDate);
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
            // 확인 버튼 : 음식 추가 후 액티비티 종료
            if (addOrUpdateFood()) {
                setResult(RESULT_OK);
                finish();
            }
        } else if (id == R.id.btnOpenDate) {
            // 개봉 날짜 버튼 : 날짜 선택 대화상자 띄우기
            showDateDialog(true);
        } else if (id == R.id.btnDueDate) {
            // 소비기한 날짜 버튼 : 날짜 선택 대화상자 띄우기
            showDateDialog(false);
        }
    }

    // DB 에 음식을 추가한다

    private boolean addOrUpdateFood() {

        // 음식 제목을 확인한다
        String foodTitle = mFoodTitleEdit.getText().toString().trim();

        if (foodTitle.isEmpty()) {
            Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        // DB 에 추가 및 업데이트
        Food food;
        if (mFoodId == -1) {
            food = new Food(foodTitle, mOpenDate, mDueDate);
            SQLiteHelper.getInstance(this).addFood(food);
        } else {
            food = new Food(mFoodId, foodTitle,  mOpenDate, mDueDate);
            SQLiteHelper.getInstance(this).updateFood(food);
        }

        return true;
    }

    // 개봉/소비기한 날짜 선택 대화상자를 띄운다

    private void showDateDialog(boolean openOrDue) {

        LocalDate initialDate = openOrDue ? mOpenDate : mDueDate;
        int initialYear = initialDate.getYear();
        int initialMonth = initialDate.getMonthValue() - 1;
        int initialDayOfMonth = initialDate.getDayOfMonth();

        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // 날짜 선택 시 개봉/소비기한 날짜를 변경한다
                    LocalDate date = LocalDate.of(year, month + 1, dayOfMonth);

                    if (openOrDue) {
                        if (!date.isAfter(mDueDate)) {
                            mOpenDate = date;
                            updateDateButtons();
                        } else {
                            Toast.makeText(this,
                                    "앞선 날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!date.isBefore(mOpenDate)) {
                            mDueDate = date;
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

