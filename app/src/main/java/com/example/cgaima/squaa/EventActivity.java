package com.example.cgaima.squaa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity {

    //resource variables
    @BindView(R.id.toolbar) Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this); //bind butterknife after

        if (useToolbar()){
            setActionBar(toolbar);
        }
        else {
            toolbar.setVisibility(View.GONE);
        }
    }

    protected boolean useToolbar(){
        return true;
    }

}
