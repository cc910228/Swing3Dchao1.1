package com.khu.chao.swing3dchao11;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.VideoView;

import java.io.File;

/**
 * Created by CHAO on 2015/3/5.
 */
public class ChaoSecond extends Activity implements OnClickListener {

    private VideoView videoView;
    private Button play;
    private Button pause;
    boolean do_pause = false;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        //make this second activity respond for button play of main activity
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chao_second);

        //setup the the media play
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        videoView = (VideoView) findViewById(R.id.video_view);
        //editText.setInputType(InputType.TYPE_CLASS_NUMBER);//输入类型为数字
        //MediaController mc = new MediaController(this);
        //videoView.setMediaController(mc);
        seekBar=(SeekBar)findViewById(R.id.seekBar);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                if(fromUser) {
                    // this is when actually seekbar has been seeked to a new position
                    videoView.seekTo(progress);
                }
            }
        });

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        initVideoPath();
    }

    private void initVideoPath(){
        File file = new File(Environment.getExternalStorageDirectory(),"myvideo.mp4");
        videoView.setVideoPath(file.getPath());
    }

    private Runnable onEverySecond=new Runnable() {

        @Override
        public void run() {

            if(seekBar != null) {
                seekBar.setProgress(videoView.getCurrentPosition());
            }

            if(videoView.isPlaying()) {
                seekBar.postDelayed(onEverySecond, 1000);
            }

        }
    };

    @Override
    public void onClick(View v){
    switch (v.getId()){
        case R.id.play:
            //if ((!videoView.isPlaying())&(!do_pause) ){
            //    videoView.resume();
            //    videoView.start();
            //}
            //if (do_pause){
                videoView.start();
                seekBar.setMax(videoView.getDuration());
                seekBar.postDelayed(onEverySecond, 1000);
            //    do_pause = false;
            //}
            break;

        case R.id.pause:
            if (videoView.isPlaying()){
                videoView.pause();
                do_pause = true;
            }
            break;
        /*case R.id.start_from:
            if (!videoView.isPlaying()){
                //String inputText = editText.getText().toString();
                videoView.resume();
            }
            break;*/
        default:
            break;
    }
    }



    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (videoView != null){
            videoView.suspend();
        }
    }
}