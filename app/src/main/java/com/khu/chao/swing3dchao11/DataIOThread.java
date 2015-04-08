package com.khu.chao.swing3dchao11;

import android.widget.SeekBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class DataIOThread extends Thread
{
    private MyRenderer mRenderer;
    private int mshowRange;
    private SeekBar seekBar1;
    private MyGLSurfaceView mGLView;

    DataIOThread(MyRenderer renderer, int showRange, SeekBar seekBar, MyGLSurfaceView GLView)
    {
        mRenderer = renderer;
        mshowRange = showRange;
        seekBar1 = seekBar;
        mGLView = GLView;
    }

    public void readFile()
    {
        String fileName = "/sdcard/3Dswing/test.txt";
        String data = readSwing(fileName);

        mRenderer.readButtonTapped(data);
        mshowRange = mRenderer.getLineLength();
        seekBar1.setMax((mshowRange - 1)/3 );
        seekBar1.setProgress((mshowRange - 1)/3);
        mGLView.requestRender();
    }


    protected String readSwing(String fileName)
    {
        File file = new File(fileName);
        String outPut = null;
        try
        {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer StrBuf = new StringBuffer();
            String aLine = "";
            while(aLine != null)
            {
                aLine = reader.readLine();
                if(aLine != null)
                {
                    StrBuf.append(aLine + "\n");
                }
            }
            outPut = StrBuf.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return outPut;
    }
}
