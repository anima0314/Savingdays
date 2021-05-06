package com.example.savingdays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; //바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag1 frag1;
    private Frag2 frag2;
    private Frag3 frag3;
    private Frag4 frag4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {{

    }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CalendarActivity 를 화면에 띄운다
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frag_container, new CalendarActivity())
                .commit();


    }
}