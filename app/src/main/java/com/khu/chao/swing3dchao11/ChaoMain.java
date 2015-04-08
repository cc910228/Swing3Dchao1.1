//automatically 10 seconds recording finished version
//replay finished version
package com.khu.chao.swing3dchao11;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//import android.os.Handler;
//import android.widget.TextView;

public class ChaoMain extends Activity {
    Button record, play_video;
    // video file
    File viodFile;
    MediaRecorder mRecorder;
    // show the SurfaceView of the video
    SurfaceView sView;

    boolean isRecording = false;
    Camera camera;
    Timer timer;
    Context temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide title bar  Chapter 2.24
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.chao_main);

        sView = (SurfaceView) findViewById(R.id.dView);
        // set Surface do not need its own buffer zone
        sView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // resolution
        sView.getHolder().setFixedSize(1920, 1080);
        // keep the screen
        sView.getHolder().setKeepScreenOn(true);

        MyThread myThread = new MyThread();
        new Thread(myThread).start();
        //timer = new Timer();
        //timer.schedule(new onetask(), 5000);
    }


    class MyThread implements Runnable {

        @Override
        public void run() {
            try {
                int cameraType = 1; // front
                camera = Camera.open(cameraType);
                List sizes = camera.getParameters().getSupportedVideoSizes();

                Camera.Size result = null;
                result = (Camera.Size) sizes.get(0);
                //Camera.Size size = getBestPreviewSize(720,560, camera.getParameters());
                //List sizes = camera.getParameters().getSupportedVideoSizes();
                //System.out.println(sizes);

                camera.setDisplayOrientation(90);
                camera.unlock();

                // create MediaPlayer
                mRecorder = new MediaRecorder();

                mRecorder.reset();
                mRecorder.setCamera(camera);

        /* camera = Camera.open();
         camera.unlock();
         camera.setDisplayOrientation(0);
         mRecorder.setCamera(camera);*/
                // save the video file
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String currentDateandTime = sdf.format(new Date());
                viodFile = new File(Environment.getExternalStorageDirectory().getCanonicalFile()+ "/myvideo" + currentDateandTime + ".mp4");
                if (!viodFile.exists())
                    viodFile.createNewFile();
                //android.hardware.Camera.open(0);

                // 设置从麦克风采集声音
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                // 设置从摄像头采集图像
                mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                // 设置视频、音频的输出格式
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                // 设置音频的编码格式、
                //CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
                //mRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
                mRecorder.setVideoEncodingBitRate(18000000);
                //mRecorder.setVideoSize(size.width, size.height);
                mRecorder.setVideoSize(result.width, result.height);
                //CamcorderProfile cpHigh = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_FRONT,CamcorderProfile.QUALITY_HIGH);
                //mRecorder.setProfile(cpHigh);
                //mRecorder.setVideoFrameRate(15);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                // 设置图像编码格式
                mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                //mRecorder.setMaxDuration(10000);
                //视频旋转90度
                mRecorder.setOrientationHint(270);
                //mRecorder.setVideoFrameRate(15);
                //mRecorder.setVideoSize(320, 280);
                // 指定SurfaceView来预览视频
                mRecorder.setPreviewDisplay(sView.getHolder().getSurface());
                mRecorder.setOutputFile(viodFile.getAbsolutePath());
                mRecorder.prepare();
                // start
                mRecorder.start();

                isRecording = true;

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    //int i = 10;
                    @Override
                    public void run(){
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                    isRecording = false;
                    camera.lock();
                    camera.release();
                    camera = null;
                    finish();

                    }

                }, 6000);
                } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    //public class onetask extends TimerTask{
        //int i = 10;
    //    @Override
     //   public void run() {
    //        mRecorder.stop();
    //        mRecorder.release();
    //        mRecorder = null;
    //        isRecording = false;
    //        camera.lock();
    //        camera.release();
    //        camera = null;
    //    }
   // }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    }

    //private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
    //    Camera.Size result=null;

    //    for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
    //        if (size.width<=width && size.height<=height) {
    //            if (result==null) {
    //                result=size;
    //            } else {
    //                int resultArea=result.width*result.height;
    //                int newArea=size.width*size.height;

    //                if (newArea>resultArea) {
    //                    result=size;
    //                }
    //            }
    //        }
    //   }
    //    return(result);
    //}



