package com.example.match_app.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.match_app.R;

public class Fragment1 extends Fragment {



    Fragment SelFragment = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);









        return viewGroup;
    }

}