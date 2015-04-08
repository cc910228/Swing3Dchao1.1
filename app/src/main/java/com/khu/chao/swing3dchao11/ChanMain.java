package com.khu.chao.swing3dchao11;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;


public class ChanMain extends Activity
{
    private int showRange;
    private MyGLSurfaceView mGLView;
    private MyRenderer mRenderer;
    private int findRng = 0;
    Handler mHandler = new Handler();

    RadioButton radioButton1;
    RadioButton radioButton2;
    FrameLayout frameLayout1;
    SeekBar seekBar1;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chan);

        frameLayout1 = (FrameLayout) findViewById(R.id.FrameLayout1);

        mRenderer = new MyRenderer(this);
        mGLView = new MyGLSurfaceView(this);
        mGLView.setRenderer(mRenderer);
        mGLView.setRenderMode(mGLView.RENDERMODE_WHEN_DIRTY);
        frameLayout1.addView(mGLView);

        radioButton1 = (RadioButton)findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton)findViewById(R.id.radioButton2);
        seekBar1 = (SeekBar)findViewById(R.id.seekBar1);

        radioButton1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(radioButton1.isChecked())
                {
                    mGLView.setMoveMode(mGLView.ROTATE_BUTTON);
                }
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(radioButton2.isChecked())
                {
                    mGLView.setMoveMode(mGLView.MOVE_BUTTON);
                }
            }
        });

        Button button1 = (Button) findViewById(R.id.button1);  //read file
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DataIOThread dataIO = new DataIOThread(mRenderer,showRange,seekBar1,mGLView);
                dataIO.readFile();
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MoreLine();
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LessLine();
            }
        });

        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autoRun();
            }
        });

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                showRange=(seekBar1.getProgress()+1)*3;
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mRenderer.DrawTo(showRange);
                        mGLView.requestRender();
                    }
                }).start();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }
    protected void MoreLine()
    {
        if(showRange < mRenderer.getLineLength() - 2)
        {
            seekBar1.setProgress(seekBar1.getProgress()+1);
        }
    }

    protected void LessLine()
    {
        if(showRange > 5)
        {
            seekBar1.setProgress(seekBar1.getProgress()-1);
        }
    }

    public void autoRun()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                seekBar1.setProgress(0);
                while(findRng < seekBar1.getMax())
                {
                    if(seekBar1.getMax() == 0)
                    {
                        break;
                    }
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    mHandler.post(new Runnable()
                    {
                        public void run()
                        {
                            seekBar1.setProgress(seekBar1.getProgress()+1);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mGLView.onResume();
    }
}