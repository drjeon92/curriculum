package com.example.match_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.match_app.fragment.ChatListFragment;
import com.example.match_app.fragment.EtcFragment;
import com.example.match_app.fragment.HomeFragment;
import com.example.match_app.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IntroActivity MAIN : ";
    private Button button;
    private FirebaseAuth mAuth;
    ChatListFragment chatListFragment;

    HomeFragment fragment1;
    SearchFragment fragment2;
    ChatListFragment fragment3;
    EtcFragment fragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, IntroActivity.class));
                finish();
            }

<<<<<<< HEAD
=======



        });

        fragment1 = new HomeFragment();
        fragment2 = new SearchFragment();
        fragment3 = new ChatListFragment();
        fragment4 = new EtcFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contain, fragment1).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);

        //하단 네비게이션바
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab1:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contain, fragment1).commit();
                        break;

                    case R.id.tab2:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contain, fragment2).commit();
                        break;

                    case R.id.tab3:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contain, fragment3).commit();
                        break;

                    case R.id.tab4:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contain, fragment4).commit();
                        break;
                }

                return true;
            }
>>>>>>> ba4209955829d3a133719d27ba6719783dfc8c6f
        });
    }

    // 현재 사용자 계정정보가 있는가? if =null 이면 로그인 액티비티로 가시오
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "onStart: currentUser : "+currentUser);
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, Login02Activity.class));
            finish();
        }
    }
}