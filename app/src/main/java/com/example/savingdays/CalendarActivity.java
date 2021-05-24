package com.example.savingdays;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savingdays.ProductAdapter;
import com.example.savingdays.ScheduleAdapter;
import com.example.savingdays.FoodAdapter;

import com.example.savingdays.SQLiteHelper;
import com.example.savingdays.Product;
import com.example.savingdays.Food;

import com.example.savingdays.Schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CalendarActivity extends Fragment implements
        View.OnClickListener, CalendarView.OnDateChangeListener {

    public static final int REQUEST_ADD = 100;          // 추가하기 액티비티 요청코드

    public static final int CATEGORY_SCHEDULE = 100;    // 일정 카테고리
    public static final int CATEGORY_FOOD = 101;        // 음식 카테고리
    public static final int CATEGORY_PRODUCT = 102;     // 소모품 카테고리

    private int mCategory = CATEGORY_SCHEDULE;          // 현재 선택된 카테고리
    private Button mScheduleButton;                     // 카테고리 버튼
    private Button mFoodButton;
    private Button mProductButton;

    private LocalDate mSelectedDate;                    // 현재 선택된 날짜
    private CalendarView mCalendarView;                 // 달력 뷰
    private TextView mSelectedMonthText;                // 현재 달 텍스트뷰
    private GridLayout mCalendarGrid;

    private RecyclerView mRecycler;                     // 리사이클러뷰
    private ScheduleAdapter mScheduleAdapter;
    private ProductAdapter mProductAdapter;
    private FoodAdapter mFoodAdapter;

    private List<Schedule> mScheduleList;
    private List<Food> mFoodList;
    private List<Product> mProductList;
    private TextView mNoItemsText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        mNoItemsText = view.findViewById(R.id.txtNoItems);

        // 버튼에 리스너를 설정한다
        mScheduleButton = view.findViewById(R.id.btnSchedule);
        mFoodButton = view.findViewById(R.id.btnFood);
        mProductButton = view.findViewById(R.id.btnProduct);

        mScheduleButton.setOnClickListener(this);
        mFoodButton.setOnClickListener(this);
        mProductButton.setOnClickListener(this);

        ImageButton addIButton = view.findViewById(R.id.ibtnAdd);
        ImageButton prevMonthIButton = view.findViewById(R.id.ibtnPrevMonth);
        ImageButton nextMonthIButton = view.findViewById(R.id.ibtnNextMonth);
        addIButton.setOnClickListener(this);
        prevMonthIButton.setOnClickListener(this);
        nextMonthIButton.setOnClickListener(this);

        // 카테고리 버튼 초기화
        updateCategoryButtons();

        // 날짜 UI 초기화
        mCalendarView = view.findViewById(R.id.calendar);
        mCalendarGrid = view.findViewById(R.id.calendar_grid);
        mCalendarView.setOnDateChangeListener(this);
        mSelectedDate = LocalDate.now();

        mSelectedMonthText = view.findViewById(R.id.txtSelectedMonth);
        updateSelectedMonthUI();
        updateCalendarGrid();

        // 리사이클러뷰 초기화
        buildRecycler(view);
        updateRecycler();

        return view;
    }

    // 카테고리 버튼 (일정, 음식, 소모품) 을 업데이트한다

    private void updateCategoryButtons() {

        int gray = getResources().getColor(R.color.gray, null);
        int green = getResources().getColor(R.color.green, null);

        mScheduleButton.setBackgroundColor(Color.WHITE);
        mScheduleButton.setTextColor(gray);
        mFoodButton.setBackgroundColor(Color.WHITE);
        mFoodButton.setTextColor(gray);
        mProductButton.setBackgroundColor(Color.WHITE);
        mProductButton.setTextColor(gray);

        switch (mCategory) {
            case CATEGORY_SCHEDULE:
                mScheduleButton.setBackgroundColor(green);
                mScheduleButton.setTextColor(Color.WHITE);
                break;
            case CATEGORY_FOOD:
                mFoodButton.setBackgroundColor(green);
                mFoodButton.setTextColor(Color.WHITE);
                break;
            case CATEGORY_PRODUCT:
                mProductButton.setBackgroundColor(green);
                mProductButton.setTextColor(Color.WHITE);
                break;
        }
    }

    // 리사이클러뷰를 초기화한다

    private void buildRecycler(View view) {

        mRecycler = view.findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mScheduleList = new ArrayList<>();
        mScheduleAdapter = new ScheduleAdapter(mScheduleList);
        mScheduleAdapter.setOnItemClickListener(position -> {
            Schedule schedule = mScheduleList.get(position);
            new AlertDialog.Builder(getContext())
                    .setTitle("일정 수정")
                    .setPositiveButton("수정하기", (dialog, which) -> {
                        startAddActivity(schedule.getId());
                    })
                    .setNegativeButton("삭제하기", (dialog, which) -> {
                        SQLiteHelper.getInstance(getContext())
                                .deleteSchedule(schedule.getId());
                        Toast.makeText(getContext(), "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        updateRecycler();
                        updateCalendarGrid();
                    })
                    .setNeutralButton("취소", null)
                    .show();
        });
        mFoodList = new ArrayList<>();
        mFoodAdapter = new FoodAdapter(mFoodList);
        mFoodAdapter.setOnItemClickListener(position -> {
            Food food = mFoodList.get(position);
            new AlertDialog.Builder(getContext())
                    .setTitle("음식 소비기한 수정")
                    .setPositiveButton("수정하기", (dialog, which) -> {
                        startAddActivity(food.getId());
                    })
                    .setNegativeButton("삭제하기", (dialog, which) -> {
                        SQLiteHelper.getInstance(getContext())
                                .deleteFood(food.getId());
                        Toast.makeText(getContext(), "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        updateRecycler();
                        updateCalendarGrid();
                    })
                    .setNeutralButton("취소", null)
                    .show();
        });
        mProductList = new ArrayList<>();
        mProductAdapter = new ProductAdapter(mProductList);
        mProductAdapter.setOnItemClickListener(position -> {
            Product product = mProductList.get(position);
            new AlertDialog.Builder(getContext())
                    .setTitle("제품 수정")
                    .setPositiveButton("수정하기", (dialog, which) -> startAddActivity(product.getId()))
                    .setNegativeButton("삭제하기", (dialog, which) -> {
                        SQLiteHelper.getInstance(getContext())
                                .deleteProduct(product.getId());
                        Toast.makeText(getContext(), "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        updateRecycler();
                        updateCalendarGrid();
                    })
                    .setNeutralButton("취소", null)
                    .show();
        });
    }

    // 리사이클러뷰를 업데이트한다

    private void updateRecycler() {

        boolean isEmpty = true;

        switch (mCategory) {
            case CATEGORY_SCHEDULE:
                // 리사이클러뷰에 일정 목록을 띄운다
                mRecycler.setAdapter(mScheduleAdapter);
                mScheduleList.clear();
                mScheduleList.addAll(SQLiteHelper.getInstance(getContext())
                        .getScheduleByDate(mSelectedDate));
                isEmpty = mScheduleList.isEmpty();
                break;
            case CATEGORY_FOOD:
                // 리사이클러뷰에 일정 목록을 띄운다
                mRecycler.setAdapter(mFoodAdapter);
                mFoodList.clear();
                mFoodList.addAll(SQLiteHelper.getInstance(getContext())
                        .getFoodByDate(mSelectedDate));
                isEmpty = mFoodList.isEmpty();
                break;
            case CATEGORY_PRODUCT:
                // 리사이클러뷰에 소모품 목록을 띄운다
                mRecycler.setAdapter(mProductAdapter);
                mProductList.clear();
                mProductList.addAll(SQLiteHelper.getInstance(getContext())
                        .getProductByDate(mSelectedDate));
                isEmpty = mProductList.isEmpty();
                break;
        }

        // 항목이 없으면 문구를 보여준다
        mNoItemsText.setVisibility(isEmpty ? View.VISIBLE : View.INVISIBLE);
        mRecycler.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
    }

    // 현재 달 UI 를 업데이트한다
    private void updateSelectedMonthUI() {

        String strSelectedMonth = String.format(Locale.getDefault(),
                "%s %d",
                mSelectedDate.getMonth().toString(), mSelectedDate.getYear());

        mSelectedMonthText.setText(strSelectedMonth);
    }

    // 달력 격자 업데이트 : 항목이 존재하는 달력 날짜 하이라이트

    private void updateCalendarGrid() {

        Drawable normal = ResourcesCompat.getDrawable(getResources(),
                R.drawable.calendar_cell, null);
        Drawable highlighted = ResourcesCompat.getDrawable(getResources(),
                R.drawable.calendar_cell_highlighted, null);

        // 모든 날짜 하이라이트 해제
        for (int i = 0; i < 42; i++) {
            View dayView = mCalendarGrid.getChildAt(7 + i);
            dayView.setBackground(normal);
        }

        // 항목이 존재하는 날짜에만 하이라이트
        LocalDate firstDate;
        int firstPosition, maxDays;

        switch (mCategory) {
            case CATEGORY_SCHEDULE:
                List<Schedule> scheduleList = SQLiteHelper.getInstance(getContext())
                        .getScheduleByMonth(mSelectedDate);

                firstDate = mSelectedDate.withDayOfMonth(1);
                firstPosition = 7 + (firstDate.getDayOfWeek().getValue() % 7);
                maxDays = mSelectedDate.lengthOfMonth();

                for (int i = 0; i < maxDays; i++) {
                    LocalDate date = mSelectedDate.withDayOfMonth(i + 1);
                    View dayView = mCalendarGrid.getChildAt(firstPosition + date.getDayOfMonth() - 1);

                    for (Schedule schedule : scheduleList) {
                        if (!date.isBefore(schedule.getStartDate())
                                && !date.isAfter(schedule.getEndDate())) {
                            dayView.setBackground(highlighted);
                            break;
                        }
                    }
                }
                break;
            case CATEGORY_FOOD:
                List<Food> FoodList = SQLiteHelper.getInstance(getContext())
                        .getFoodByMonth(mSelectedDate);

                firstDate = mSelectedDate.withDayOfMonth(1);
                firstPosition = 7 + (firstDate.getDayOfWeek().getValue() % 7);
                maxDays = mSelectedDate.lengthOfMonth();

                for (int i = 0; i < maxDays; i++) {
                    LocalDate date = mSelectedDate.withDayOfMonth(i + 1);
                    View dayView = mCalendarGrid.getChildAt(firstPosition + date.getDayOfMonth() - 1);

                    for (Food food : FoodList) {
                        if (!date.isBefore(food.getOpenDate())
                                && !date.isAfter(food.getDueDate())) {
                            dayView.setBackground(highlighted);
                            break;
                        }
                    }
                }
                break;
            case CATEGORY_PRODUCT:
                List<Product> productList = SQLiteHelper.getInstance(getContext())
                        .getProductByMonth(mSelectedDate);

                firstDate = mSelectedDate.withDayOfMonth(1);
                firstPosition = 7 + (firstDate.getDayOfWeek().getValue() % 7);
                maxDays = mSelectedDate.lengthOfMonth();

                for (int i = 0; i < maxDays; i++) {
                    LocalDate date = mSelectedDate.withDayOfMonth(i + 1);
                    View dayView = mCalendarGrid.getChildAt(firstPosition + date.getDayOfMonth() - 1);

                    for (Product product : productList) {
                        if (!date.isBefore(product.getOpenDate())
                                && !date.isAfter(product.getDueDate())) {
                            dayView.setBackground(highlighted);
                            break;
                        }
                    }
                }
                break;
        }
    }

    // 버튼 클릭을 처리한다

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.btnSchedule) {
            selectCategory(CATEGORY_SCHEDULE);
        } else if (id == R.id.btnFood) {
            selectCategory(CATEGORY_FOOD);
        } else if (id == R.id.btnProduct) {
            selectCategory(CATEGORY_PRODUCT);
        } else if (id == R.id.ibtnAdd) {
            startAddActivity(-1);
        } else if (id == R.id.ibtnPrevMonth) {
            moveToPrevMonth();
        } else if (id == R.id.ibtnNextMonth) {
            moveToNextMonth();
        }
    }

    // 달력에서 특정 날짜를 선택했을 때

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

        mSelectedDate = LocalDate.of(year, month + 1, dayOfMonth);

        // 리사이클러뷰 업데이트
        updateRecycler();

        // 선택된 달 UI 업데이트
        updateSelectedMonthUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD) {
            // 추가 액티비티에서 추가가 실행된 경우 리사이클러뷰 업데이트
            if (resultCode == Activity.RESULT_OK) {
                updateRecycler();
                updateCalendarGrid();
            }
        }
    }

    // 추가/수정하기 액티비티를 시작한다

    private void startAddActivity(int id) {

        Intent intent = null;

        switch (mCategory) {
            case CATEGORY_SCHEDULE:
                intent = new Intent(getContext(), AddScheduleActivity.class);
                if (id != -1) {
                    intent.putExtra(AddScheduleActivity.EXTRA_SCHEDULE_ID, id);
                }
                intent.putExtra(AddScheduleActivity.EXTRA_SELECTED_DATE, mSelectedDate.toString());
                break;
            case CATEGORY_FOOD:
                intent = new Intent(getContext(), AddFoodActivity.class);
                if (id != -1) {
                    intent.putExtra(AddFoodActivity.EXTRA_FOOD_ID, id);
                }
                intent.putExtra(AddFoodActivity.EXTRA_SELECTED_DATE, mSelectedDate.toString());
                break;
            case CATEGORY_PRODUCT:
                intent = new Intent(getContext(), AddProductActivity.class);
                if (id != -1) {
                    intent.putExtra(AddProductActivity.EXTRA_PRODUCT_ID, id);
                }
                intent.putExtra(AddProductActivity.EXTRA_SELECTED_DATE, mSelectedDate.toString());
                break;
        }

        if (intent != null) {
            startActivityForResult(intent, REQUEST_ADD);
        }
    }

    // 카테고리 선택하기

    private void selectCategory(int newCategory) {

        mCategory = newCategory;
        updateCategoryButtons();
        updateRecycler();
        updateCalendarGrid();
    }

    // 이전 달로 이동
    private void moveToPrevMonth() {

        mSelectedDate = mSelectedDate.minusMonths(1);
        mCalendarView.setDate(mSelectedDate.toEpochDay() * 86400 * 1000);

        // 리사이클러뷰 업데이트
        updateRecycler();

        // 선택된 달 UI 업데이트
        updateSelectedMonthUI();

        updateCalendarGrid();
    }

    // 다음 달로 이동
    private void moveToNextMonth() {
        mSelectedDate = mSelectedDate.plusMonths(1);
        mCalendarView.setDate(mSelectedDate.toEpochDay() * 86400 * 1000);
        // 리사이클러뷰 업데이트
        updateRecycler();
        // 선택된 달 UI 업데이트
        updateSelectedMonthUI();
        updateCalendarGrid();
    }

}


