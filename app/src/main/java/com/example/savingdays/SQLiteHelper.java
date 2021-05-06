package com.example.savingdays;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.savingdays.Product;
import com.example.savingdays.Schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    // 데이터베이스 이름
    private static final String DATABASE_NAME = "database";
    // 현재 버전
    private static final int DATABASE_VERSION = 2;

    // 일정 테이블의 정보
    public static final String TABLE_SCHEDULES = "schedules";
    public static final String COLUMN_SCHEDULE_ID = "id";
    public static final String COLUMN_SCHEDULE_TITLE = "title";
    public static final String COLUMN_SCHEDULE_START_DATE = "startDate";
    public static final String COLUMN_SCHEDULE_END_DATE = "endDate";

    // 제품 테이블의 정보
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_TITLE = "title";
    public static final String COLUMN_PRODUCT_TYPE = "type";
    public static final String COLUMN_PRODUCT_OPEN_DATE = "openDate";
    public static final String COLUMN_PRODUCT_DUE_DATE = "dueDate";

    // 데이터베이스 헬퍼 객체
    private static SQLiteHelper instance;

    // 데이터베이스 헬퍼 객체 구하기.
    public static synchronized SQLiteHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteHelper(context.getApplicationContext());
        }
        return instance;
    }

    // 생성자
    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 초기화 : 데이터베이스에 일정 테이블을 생성한다.
        String CREATE_SCHEDULES_TABLE = "CREATE TABLE " + TABLE_SCHEDULES +
                "(" +
                COLUMN_SCHEDULE_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_SCHEDULE_TITLE + " TEXT NOT NULL, " +
                COLUMN_SCHEDULE_START_DATE + " TEXT NOT NULL, " +
                COLUMN_SCHEDULE_END_DATE + " TEXT NOT NULL" +
                ")";
        db.execSQL(CREATE_SCHEDULES_TABLE);

        // 초기화 : 데이터베이스에 제품 테이블을 생성한다.
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS +
                "(" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_PRODUCT_TITLE + " TEXT NOT NULL, " +
                COLUMN_PRODUCT_TYPE + " INTEGER NOT NULL, " +
                COLUMN_PRODUCT_OPEN_DATE + " TEXT NOT NULL, " +
                COLUMN_PRODUCT_DUE_DATE + " TEXT NOT NULL" +
                ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // 기존의 데이터베이스 버전이 현재와 다르면 테이블을 지우고 빈 테이블 다시 만들기.
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
            onCreate(db);
        }
    }

    // 일정 추가

    public void addSchedule(Schedule schedule) {

        // 쓰기용 DB 를 연다.
        SQLiteDatabase db = getWritableDatabase();

        // DB 입력 시작
        db.beginTransaction();
        try {
            // 정보를 모두 values 객체에 입력한다
            ContentValues values = new ContentValues();
            values.put(COLUMN_SCHEDULE_TITLE, schedule.getTitle());
            values.put(COLUMN_SCHEDULE_START_DATE, schedule.getStartDate().toString());
            values.put(COLUMN_SCHEDULE_END_DATE, schedule.getEndDate().toString());

            // 데이터베이스에 values 를 입력한다.
            db.insertOrThrow(TABLE_SCHEDULES, null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    // 일정 조회

    public Schedule getSchedule(int id) {

        Schedule schedule = null;

        // 읽기용 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        // 데이터베이스의 테이블을 가리키는 커서를 가져온다.
        String SELECT_SCHEDULES = "SELECT * FROM " + TABLE_SCHEDULES
                + " WHERE " + COLUMN_SCHEDULE_ID + " = " + id;
        Cursor cursor = db.rawQuery(SELECT_SCHEDULES, null);

        try {
            if (cursor.moveToFirst()) {

                // 커서를 움직이면서 테이블의 정보들을 가져온다.
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_TITLE));
                String strStartDate = cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_START_DATE));
                String strEndDate = cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_END_DATE));

                // 정보로 객체를 만들어 리스트에 추가한다.
                schedule = new Schedule(
                        id, title,
                        LocalDate.parse(strStartDate),
                        LocalDate.parse(strEndDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return schedule;
    }

    public List<Schedule> getScheduleByDate(LocalDate date) {

        List<Schedule> scheduleList = new ArrayList<>();

        // 읽기용 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        // 데이터베이스의 테이블을 가리키는 커서를 가져온다.
        String SELECT_SCHEDULES =
                "SELECT * FROM " + TABLE_SCHEDULES
                        + " WHERE " + COLUMN_SCHEDULE_START_DATE + " <= '" + date.toString() + "'"
                        + " AND " + COLUMN_SCHEDULE_END_DATE + " >= '" + date.toString() + "'";
        Cursor cursor = db.rawQuery(SELECT_SCHEDULES, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    // 커서를 움직이면서 테이블의 정보들을 가져온다.
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_SCHEDULE_ID));
                    String title = cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_TITLE));
                    String strStartDate = cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_START_DATE));
                    String strEndDate = cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_END_DATE));

                    // 정보로 객체를 만들어 리스트에 추가한다.
                    Schedule schedule = new Schedule(
                            id, title,
                            LocalDate.parse(strStartDate),
                            LocalDate.parse(strEndDate));
                    scheduleList.add(schedule);

                    // 테이블 끝에 도달할 때까지 실시한다.
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return scheduleList;
    }

    public List<Schedule> getScheduleByMonth(LocalDate date) {

        List<Schedule> scheduleList = new ArrayList<>();

        LocalDate nextMonth = date.plusMonths(1);
        LocalDate prevMonth = date.minusMonths(1);

        // 읽기용 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        // 데이터베이스의 테이블을 가리키는 커서를 가져온다.
        String SELECT_SCHEDULES =
                "SELECT * FROM " + TABLE_SCHEDULES;
        Cursor cursor = db.rawQuery(SELECT_SCHEDULES, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    // 커서를 움직이면서 테이블의 정보들을 가져온다.
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_SCHEDULE_ID));
                    String title = cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_TITLE));
                    String strStartDate = cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_START_DATE));
                    String strEndDate = cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_END_DATE));

                    // 정보로 객체를 만들어 검사후 리스트에 추가한다.
                    Schedule schedule = new Schedule(
                            id, title,
                            LocalDate.parse(strStartDate),
                            LocalDate.parse(strEndDate));

                    if (schedule.getStartDate().getMonthValue() == date.getMonthValue()
                            || schedule.getEndDate().getMonthValue() == date.getMonthValue()) {
                        scheduleList.add(schedule);
                    }
                    scheduleList.add(schedule);

                    // 테이블 끝에 도달할 때까지 실시한다.
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return scheduleList;
    }

    // 일정 업데이트

    public void updateSchedule(Schedule schedule) {

        // 쓰기용 DB 얻기
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SCHEDULE_TITLE, schedule.getTitle());
        values.put(COLUMN_SCHEDULE_START_DATE, schedule.getStartDate().toString());
        values.put(COLUMN_SCHEDULE_END_DATE, schedule.getEndDate().toString());

        db.update(TABLE_SCHEDULES, values, COLUMN_SCHEDULE_ID + " = ?",
                new String[]{String.valueOf(schedule.getId())});
    }

    // 일정 삭제

    public void deleteSchedule(int id) {

        // 쓰기용 DB 열기
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            // 데이터베이스에서 주어진 id를 가진 정보를 삭제한다.
            db.delete(TABLE_SCHEDULES,
                    COLUMN_SCHEDULE_ID + "=?",
                    new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }



    // 제품 추가

    public void addProduct(Product product) {

        // 쓰기용 DB 를 연다.
        SQLiteDatabase db = getWritableDatabase();

        // DB 입력 시작
        db.beginTransaction();
        try {
            // 정보를 모두 values 객체에 입력한다
            ContentValues values = new ContentValues();
            values.put(COLUMN_PRODUCT_TITLE, product.getTitle());
            values.put(COLUMN_PRODUCT_TYPE, product.getType());
            values.put(COLUMN_PRODUCT_OPEN_DATE, product.getOpenDate().toString());
            values.put(COLUMN_PRODUCT_DUE_DATE, product.getDueDate().toString());

            // 데이터베이스에 values 를 입력한다.
            db.insertOrThrow(TABLE_PRODUCTS, null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    // 제품 조회

    public Product getProduct(int id) {

        Product product = null;

        // 읽기용 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        // 데이터베이스의 테이블을 가리키는 커서를 가져온다.
        String SELECT_PRODUCTS = "SELECT * FROM " + TABLE_PRODUCTS
                + " WHERE " + COLUMN_PRODUCT_ID + " = " + id;
        Cursor cursor = db.rawQuery(SELECT_PRODUCTS, null);

        try {
            if (cursor.moveToFirst()) {

                // 커서를 움직이면서 테이블의 정보들을 가져온다.
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_TITLE));
                int type = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_TYPE));
                String strOpenDate = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_OPEN_DATE));
                String strDueDate = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_DUE_DATE));

                // 정보로 객체를 만들어 리스트에 추가한다.
                product = new Product(
                        id, title, type,
                        LocalDate.parse(strOpenDate),
                        LocalDate.parse(strDueDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return product;
    }

    public List<Product> getProductByDate(LocalDate date) {

        List<Product> productList = new ArrayList<>();

        // 읽기용 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        // 데이터베이스의 테이블을 가리키는 커서를 가져온다.
        String SELECT_PRODUCTS =
                "SELECT * FROM " + TABLE_PRODUCTS
                        + " WHERE " + COLUMN_PRODUCT_OPEN_DATE + " <= '" + date.toString() + "'"
                        + " AND " + COLUMN_PRODUCT_DUE_DATE + " >= '" + date.toString() + "'";
        Cursor cursor = db.rawQuery(SELECT_PRODUCTS, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    // 커서를 움직이면서 테이블의 정보들을 가져온다.
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID));
                    String title = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_TITLE));
                    int type = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_TYPE));
                    String strOpenDate = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_OPEN_DATE));
                    String strDueDate = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_DUE_DATE));

                    // 정보로 객체를 만들어 리스트에 추가한다.
                    Product product = new Product(
                            id, title, type,
                            LocalDate.parse(strOpenDate),
                            LocalDate.parse(strDueDate));
                    productList.add(product);

                    // 테이블 끝에 도달할 때까지 실시한다.
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return productList;
    }

    // 제품 업데이트

    public void updateProduct(Product product) {

        // 쓰기용 DB 얻기
        SQLiteDatabase db = this.getWritableDatabase();

        // 업데이트를 위해 values 를 만든다
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_TITLE, product.getTitle());
        values.put(COLUMN_PRODUCT_TYPE, product.getType());
        values.put(COLUMN_PRODUCT_OPEN_DATE, product.getOpenDate().toString());
        values.put(COLUMN_PRODUCT_DUE_DATE, product.getDueDate().toString());

        // 데이터베이스의 id 위치에 values 를 입력하여 업데이트한다.
        db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
    }

    // 제품 삭제

    public void deleteProduct(int id) {

        // 쓰기용 DB 열기
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            // 데이터베이스에서 주어진 id를 가진 정보를 삭제한다.
            db.delete(TABLE_PRODUCTS,
                    COLUMN_PRODUCT_ID + "=?",
                    new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public List<Product> getProductByMonth(LocalDate date) {

        List<Product> productList = new ArrayList<>();

        // 읽기용 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        // 데이터베이스의 테이블을 가리키는 커서를 가져온다.
        String SELECT_PRODUCTS =
                "SELECT * FROM " + TABLE_PRODUCTS;
        Cursor cursor = db.rawQuery(SELECT_PRODUCTS, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID));
                    String title = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_TITLE));
                    int type = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_TYPE));
                    String strOpenDate = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_OPEN_DATE));
                    String strDueDate = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_DUE_DATE));

                    // 정보로 객체를 만들어 검사후 리스트에 추가한다.
                    Product product = new Product(
                            id, title, type,
                            LocalDate.parse(strOpenDate),
                            LocalDate.parse(strDueDate));

                    if (product.getOpenDate().getMonthValue() == date.getMonthValue()
                            || product.getDueDate().getMonthValue() == date.getMonthValue()) {
                        productList.add(product);
                    }

                    // 테이블 끝에 도달할 때까지 실시한다.
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return productList;
    }


}

