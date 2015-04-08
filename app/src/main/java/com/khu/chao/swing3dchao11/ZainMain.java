package com.khu.chao.swing3dchao11;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Timer;
import java.util.TimerTask;



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


public class ZainMain extends ActionBarActivity {

    File viodFile;
    MediaRecorder mRecorder;
    // show the SurfaceView of the video
    SurfaceView sView;

    boolean isRecording = false;
    Camera camera;
    Timer timer;

    private BluetoothAdapter mBtAdapter;
    String message=null;


    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";


    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private EditText mOutEditText;
    private Button mSendButton;
    private Button mSendFileBtn;
    private StringBuffer mOutStringBuffer;
    File file_to_transfer = new File(Environment.getExternalStorageDirectory() + "/DCIM/school.docx");
    String result ="";
    private long start,stop;




    /**
     * Member object for the chat services
     */
    private ThreadConnection mConnService = null;
    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zain_main);

        mSendButton = (Button) findViewById(R.id.send);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        start = 0;



        // If the adapter is null, then Bluetooth is not supported
        if (mBtAdapter == null) {

            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }
        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mConnService == null) {

            setupTransfer();
        }


    }

        @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mConnService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mConnService.getState() == ThreadConnection.STATE_NONE) {
                // Start the Bluetooth chat services
                mConnService.start();

            }
        }

    }



    @Override
    public synchronized void onPause() {
        super.onPause();
        if (true)
            Log.e("MainActivity", "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (true)
            Log.e("MainActivity", "-- ON STOP --");
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        switch (item.getItemId()) {
            case R.id.scan:
                // Launch the DeviceListActivity to see devices and do scan
                serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                return true;
            case R.id.discoverable:
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
        }
        return false;
    };



    private void ensureDiscoverable() {
        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);

        startActivityForResult(discoveryIntent,1);
    }



    private void setupTransfer() {


        mSendButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                message = "1";
                sendMessage(message);
                // Send a message using content of the edit text widget
                Intent mChaoWei = new Intent(getApplicationContext(),ChaoMain.class);
                startActivity(mChaoWei);
                //MyThread myThread = new MyThread();
                //new Thread(myThread).start();
            }
        });


        mConnService = new ThreadConnection(ZainMain.this, mHandler);

        mOutStringBuffer = new StringBuffer("");
    }





private void sendFile(File file_to_transfer) throws IOException {
        // Check that we're actually connected before trying anything
        if (mConnService.getState() != ThreadConnection.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        file_to_transfer = new File(Environment.getExternalStorageDirectory() + "/DCIM/hello.txt");



            try {
                FileInputStream fin =new FileInputStream(file_to_transfer);
                byte[] buffer= new byte[(int)file_to_transfer.length()];
                new DataInputStream(fin).readFully(buffer);
                fin.close();
                String s = new String(buffer, "US-ASCII");

                byte[] send = s.getBytes();
                mConnService.write(send);

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }


    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mConnService.getState() != ThreadConnection.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mConnService.write(send);
            Toast.makeText(this,"message sent", Toast.LENGTH_SHORT).show();

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (true)
            Log.d("MainActivity", "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {

                    connectDevice(intent);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupTransfer();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d("MainActivity", "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

        }
    }

    private void connectDevice(Intent intent) {
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        // Get the device MAC address
        String address = intent.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object

        BluetoothDevice device = mBtAdapter.getRemoteDevice(address);

        // Attempt to connect to the device
       mConnService.connect(device);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        ZainMain activity = ZainMain.this;
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        ZainMain activity = ZainMain.this;
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    public final Handler mHandler = new Handler() {
        @Override



        public void handleMessage(Message msg) {

            switch (msg.what) {
                case ZainMain.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case ThreadConnection.STATE_CONNECTED:

                            //make a toast
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            Toast.makeText(ZainMain.this,"You are connected to " + mConnectedDeviceName,Toast.LENGTH_LONG).show();


                            //mConversationArrayAdapter.clear();
                            break;
                        case ThreadConnection.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            Toast.makeText(ZainMain.this,"You are connecting to " + mConnectedDeviceName,Toast.LENGTH_LONG).show();
                            break;
                        case ThreadConnection.STATE_LISTEN:
                        case ThreadConnection.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            Toast.makeText(ZainMain.this,"You are  not connected",Toast.LENGTH_LONG).show();
                            break;
                    }

                    break;
                case ZainMain.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);
                    Toast.makeText(ZainMain.this, "Message "+writeMessage,Toast.LENGTH_LONG).show();

                    break;

                case ZainMain.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer

                    String readMessage = new String(readBuf, 0, msg.arg1);



                    if (readMessage .equals("1") )
                    {
                        Toast.makeText(ZainMain.this, " We have to send the file",Toast.LENGTH_LONG).show();
                    }
                    else{
                        try {
                            OutputStreamWriter myOutWrite = new OutputStreamWriter(new FileOutputStream(Environment.getExternalStorageDirectory() + "/DCIM/copy.txt",true));
                            BufferedWriter fbw = new BufferedWriter(myOutWrite);
                            fbw.write(readMessage);
                            fbw.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    break;

                case ZainMain.MESSAGE_DEVICE_NAME:
                    //save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(ZainMain.DEVICE_NAME);

                        //Toast.makeText(MainActivity.this, "Connected to DeviceName", Toast.LENGTH_SHORT).show();

                    break;

            }
        }
    };

    private void writeToFile(String readMessage) {



        try{
            File myFile = new File(Environment.getExternalStorageDirectory()+ "/DCIM/test.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWrite = new OutputStreamWriter(fOut);
            //myOutWrite.append(readMessage);
            myOutWrite.write(readMessage);
            myOutWrite.close();
            fOut.close();
            Toast.makeText(ZainMain.this, " We have saved file",Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }






}
