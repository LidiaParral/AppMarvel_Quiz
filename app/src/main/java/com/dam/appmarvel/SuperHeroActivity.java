package com.dam.appmarvel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.WindowManager;

import com.dam.appmarvel.controller.PagerController;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

public class SuperHeroActivity extends AppCompatActivity {


    PagerController controller;
    ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superhero);


        viewPager2 = findViewById(R.id.viewPager);

        controller = new PagerController(getSupportFragmentManager(), getLifecycle());

        controller.addFragment(new FilmsFragment());
        controller.addFragment(new QuizFragment());

        viewPager2.setAdapter(controller);

        TabLayout tabLayout = findViewById(R.id.tabLayout);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0: tab.setText("Chronology");
                    tab.setIcon(R.drawable.films);
                    break;
                    case 1: tab.setText("Quiz");
                    tab.setIcon(R.drawable.quiz);
                    break;
                }
            }
        }).attach();



    }
}