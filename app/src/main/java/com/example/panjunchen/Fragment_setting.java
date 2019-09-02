
package com.example.panjunchen;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.allen.library.SuperTextView;

public class Fragment_setting extends Fragment {
    private SuperTextView modeSwitchView;
    private SuperTextView clearCacheView;

    public Fragment_setting(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.settings,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        modeSwitchView = getActivity().findViewById(R.id.modeSwitch_button);
        clearCacheView = getActivity().findViewById(R.id.clearCache_button);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)modeSwitchView.setRightString("普通模式");

        modeSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_to_mode(modeSwitchView.getRightString().equals("夜间模式"));
            }
        });

        clearCacheView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void change_to_mode(boolean mode) {
        if(mode){
            Log.d("setting","Change to Night Mode");
            modeSwitchView.setRightString("普通模式");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            getActivity().recreate();
        }
        else{
            Log.d("setting","Change to Normal Mode");
            modeSwitchView.setRightString("夜间模式");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getActivity().recreate();
        }
    }
}
