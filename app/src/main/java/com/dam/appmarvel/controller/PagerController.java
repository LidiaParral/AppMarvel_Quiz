package com.dam.appmarvel.controller;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dam.appmarvel.FilmsFragment;
import com.dam.appmarvel.QuizFragment;

import java.util.ArrayList;

public class PagerController extends FragmentStateAdapter {

    ArrayList<Fragment> arrayList = new ArrayList<>();

    public PagerController(@NonNull @org.jetbrains.annotations.NotNull FragmentManager fragmentManager, @NonNull @org.jetbrains.annotations.NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addFragment(Fragment fragment){
        arrayList.add(fragment);
    }
}
