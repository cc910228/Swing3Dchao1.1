package com.khu.chao.swing3dchao11;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class UpperActivity extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upper);

        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent mChanYoung = new Intent(getApplicationContext(),ChanMain.class);
                startActivity(mChanYoung);
            }
        });

        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent mChaoWei = new Intent(getApplicationContext(),ZainMain.class);
                startActivity(mChaoWei);
            }
        });
    }

    public void onPause()
    {
        super.onPause();
    }

    public void onResume()
    {
        super.onResume();
    }
}
