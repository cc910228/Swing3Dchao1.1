package com.khu.chao.swing3dchao11;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer
{
    private int inputButtonTapped = 0;
    private int readButtonTapped = 0;
    static int NO_BUTTON = 0;
    static int BUTTON_TAPPED = 1;

    private Context mContext;
    private Line mLine;
    private float yAngle;
    private float xAngle;
    private float mAngle;
    private float mDx;
    private float mDy;
    private float mDz;
    private float posX = 0.0f;
    private float posY = 0.0f;
    private float posZ = 0.0f;
    private float sizeCoef = 1.0f;

    ArrayList<Float> vertexBuff = new  ArrayList<Float>();       //Save total Vertex

    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config)
    {
        gl.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
    }

    public MyRenderer(Context context)
    {
        mContext = context;
        mLine = new Line();
    }

    public void onDrawFrame(GL10 gl)
    {
        if(inputButtonTapped == BUTTON_TAPPED)
        {
            mLine.insertVertex(posX, posY, posZ);
            inputButtonTapped = NO_BUTTON;
        }
        else if(readButtonTapped == BUTTON_TAPPED)
        {
            readButtonTapped = NO_BUTTON;
        }
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(mDx, mDy, mDz);
        gl.glLineWidth(5.0f);
        gl.glRotatef(xAngle, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(yAngle, 1.0f, 0.0f, 0.0f);
        gl.glScalef(sizeCoef, sizeCoef, sizeCoef);
        try
        {
            mLine.listToArray();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        mLine.draw(gl);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);
    }

    public void inputButtonTapped(float x, float y, float z)
    {
        posX = x;
        posY = y;
        posZ = z;
        inputButtonTapped = 1;
    }

    public ArrayList<Float> readButtonTapped(String data)
    {
        mLine.clear();
        ArrayList<Float> vertexes = new ArrayList<Float>();

        String dataAry[] = data.split("\n");
        for(int i = 0; i < dataAry.length; i++)
        {
            String coord[] = dataAry[i].split("/");
            float x = Float.parseFloat(coord[0].toString());
            float y = Float.parseFloat(coord[1].toString());
            float z = Float.parseFloat(coord[2].toString());
            mLine.insertVertex(x,y,z);
        }
        readButtonTapped = 1;
        vertexBuff = mLine.getVertexes();
        return mLine.getVertexes();
    }

    public void DrawTo(int it)
    {
        ArrayList<Float> iVertex = new ArrayList<Float>();
        for(int i = 0; i < it; i++)
        {
            iVertex.add(vertexBuff.get(i));
        }

        mLine.setVertexes(iVertex);
    }

    public int getLineLength()
    {
        return vertexBuff.size();
    }
    public void insertVertexInLine(float x, float y, float z)
    {
        mLine.insertVertex(x,y,z);
    }
    public void setYAngle(float Y)
    {
        yAngle = Y;
    }
    public void setXAngle(float X)
    {
        xAngle = X;
    }
    public void setAngle(float f)
    {
        mAngle = f;
    }
    public float getXAngle()
    {
        return xAngle;
    }
    public float getYAngle()
    {
        return yAngle;
    }
    public float getAngle()
    {
        return mAngle;
    }
    public void setDx(float dx)
    {
        mDx = dx;
    }
    public void setDy(float dy)
    {
        mDy = dy;
    }
    public float getDx()
    {
        return mDx;
    }
    public float getDy()
    {
        return mDy;
    }
    public void scaleing(float size)
    {
        sizeCoef = size;
    }
    public float getSizeCoef()
    {
        return sizeCoef;
    }



}
