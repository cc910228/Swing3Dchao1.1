package com.khu.chao.swing3dchao11;


import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.FloatMath;
import android.view.MotionEvent;


public class MyGLSurfaceView extends GLSurfaceView
{
    public static int ROTATE_BUTTON = 1;
    public static int MOVE_BUTTON = 2;
    public int pointer;
    public float distance;
    private MyRenderer mRenderer;
    private float mPreviousX1;
    private float mPreviousY1;
    private int moveMode = 0;
    private float dx1;
    private float dy1;
    private float XAngle;
    private float YAngle;
    private float difx;
    private float dify;
    private float scaleXYZ = 1.0f;
    private float newdist;


    public MyGLSurfaceView(Context context)
    {
        super(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event)  //Touch event
    {
        pointer = event.getPointerCount();
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if(pointer == 1)
                {
                    mPreviousX1 = event.getX();
                    mPreviousY1 = event.getY();
                }
                distance = 0;
            break;
            case MotionEvent.ACTION_MOVE:
                if(pointer == 1)
                {
                    dx1 = event.getX() - mPreviousX1;
                    dy1 = event.getY() - mPreviousY1;
                    if (moveMode == ROTATE_BUTTON)
                    {
                        XAngle = XAngle + dx1;
                        YAngle = YAngle + dy1;
                        mRenderer.setXAngle(XAngle / getWidth() * 100);
                        mRenderer.setYAngle(YAngle / getHeight() * 100);
                    }
                    else if (moveMode == MOVE_BUTTON)
                    {
                        difx = difx + dx1;
                        dify = dify + dy1;
                        mRenderer.setDx((difx) / getWidth());
                        mRenderer.setDy((dify) / getHeight());
                    }
                    mPreviousX1 = event.getX();
                    mPreviousY1 = event.getY();
                }
                else if(pointer == 2)
                {
                    if(distance <= 0)
                    {
                        distance = FloatMath.sqrt((event.getX(0) - event.getX(1))*(event.getX(0) - event.getX(1)) + (event.getY(0) - event.getY(1))*(event.getY(0) - event.getY(1)));
                    }
                    newdist = FloatMath.sqrt((event.getX(0) - event.getX(1))*(event.getX(0) - event.getX(1)) + (event.getY(0) - event.getY(1))*(event.getY(0) - event.getY(1)));
                    scaleXYZ = scaleXYZ*newdist/distance;
                    mRenderer.scaleing(scaleXYZ);
                    distance = newdist;
                }
                requestRender();
                break;
            case MotionEvent.ACTION_UP:
                mPreviousX1 = event.getX();
                mPreviousY1 = event.getY();
                break;
        }

        return true;
    }

    public void setRenderer(Renderer renderer)
    {
        super.setRenderer(renderer);
        mRenderer = (MyRenderer)renderer;
    }

    public void setMoveMode(int mode)
    {
        moveMode = mode;
    }
    public int getMoveMode()
    {
        return moveMode;
    }


}
