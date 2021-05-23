package com.example.savingdays;



import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.savingdays.SQLiteHelper;
import com.example.savingdays.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_PRODUCT_ID = "com.exmaple.savingdays.product_id";
    public static final String EXTRA_SELECTED_DATE = "com.example.savingdays.selected_date";


    private int mProductId;                 // 제품 ID (-1 이면 추가모드, 아니면 수정모드)

    private Button mOpenDateButton;         // 날짜 버튼
    private Button mDueDateButton;
    private EditText mProductTitleEdit;     // 제품 제목 에딧텍스트
    private Spinner mProductTypeSpinner;    // 제품군 스피너

    private LocalDate mOpenDate;            // 개봉 날짜
    private LocalDate mDueDate;             // 소비기한 날짜

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // 전달된 제품 ID 를 확인한다
        mProductId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);
        Product product = null;
        if (mProductId != -1) {
            product = SQLiteHelper.getInstance(this).getProduct(mProductId);
        }

        mProductTitleEdit = findViewById(R.id.editProductTitle);
        if (product != null) {
            mProductTitleEdit.setText(product.getTitle());
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
        if (product != null) {
            mOpenDate = product.getOpenDate();
            mDueDate = product.getDueDate();
        } else {
            String strSelectedDate = getIntent().getStringExtra(EXTRA_SELECTED_DATE);
            mOpenDate = LocalDate.parse(strSelectedDate);
            mDueDate = LocalDate.parse(strSelectedDate);
        }
        updateDateButtons();

        // 제품군 스피너 초기화
        mProductTypeSpinner = findViewById(R.id.spinnerProductType);
        List<String> typeNameList = new ArrayList<>();
        int[] types = Product.getTypes();
        for (int type : types) {
            String typeName = Product.getTypeName(type);
            typeNameList.add(typeName);
        }
        mProductTypeSpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                typeNameList
        ));
        if (product != null) {
            int sel = Spinner.INVALID_POSITION;
            for (int i = 0; i < types.length; i++) {
                if (types[i] == product.getType()) {
                    sel = i;
                    break;
                }
            }
            mProductTypeSpinner.setSelection(sel);
        }


     // 스피너값에 따른 날짜
       mProductTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


           if(i==0){
               mDueDate=mOpenDate.plusMonths(1);
               String strDueDate = String.format(Locale.getDefault(),
                       "%d년 %d월 %d일",
                       mDueDate.getYear(), mDueDate.getMonthValue(), mDueDate.getDayOfMonth() );

               mDueDateButton.setText(strDueDate);
           }
           if(i==1){
               mDueDate=mOpenDate.plusMonths(2);
               String strDueDate = String.format(Locale.getDefault(),
                       "%d년 %d월 %d일",
                       mDueDate.getYear(), mDueDate.getMonthValue(), mDueDate.getDayOfMonth() );

               mDueDateButton.setText(strDueDate);

           }

           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });
    }

    // 개봉, 소비기한 날짜 버튼 업데이트

    private void updateDateButtons() {

        String strOpenDate = String.format(Locale.getDefault(),
                "%d년 %d월 %d일",
                mOpenDate.getYear(), mOpenDate.getMonthValue(), mOpenDate.getDayOfMonth());

        mOpenDateButton.setText(strOpenDate);
        String strDueDate = String.format(Locale.getDefault(),
                "%d년 %d월 %d일",
                mDueDate.getYear(), mDueDate.getMonthValue(), mDueDate.getDayOfMonth());

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
            // 확인 버튼 : 제품 추가 후 액티비티 종료
            if (addOrUpdateProduct()) {
                setResult(RESULT_OK);
                finish();
            }
        } else if (id == R.id.btnOpenDate) {
            // 개봉 날짜 버튼 : 날짜 선택 대화상자 띄우기
            showDateDialog(true);
        }
    }

    // DB 에 제품을 추가한다

    private boolean addOrUpdateProduct() {

        // 일정 제목을 확인한다
        String productTitle = mProductTitleEdit.getText().toString().trim();

        if (productTitle.isEmpty()) {
            Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 제품군을 확인한다
        int typePosition = mProductTypeSpinner.getSelectedItemPosition();
        if (typePosition == Spinner.INVALID_POSITION) {
            Toast.makeText(this, "제품군을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        int type = Product.getTypes()[typePosition];
        if (type==1){
            mDueDate=mOpenDate.plusMonths(1);
        }
        if (type==2){
            mDueDate=mOpenDate.plusMonths(2);
        }
        // DB 에 추가 및 업데이트
        Product product;
        if (mProductId == -1) {
            product = new Product(productTitle, type, mOpenDate, mDueDate);
            SQLiteHelper.getInstance(this).addProduct(product);
        } else {
            product = new Product(mProductId, productTitle, type, mOpenDate, mDueDate);
            SQLiteHelper.getInstance(this).updateProduct(product);
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
                    LocalDate date = LocalDate.of(year, month+1, dayOfMonth);

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
