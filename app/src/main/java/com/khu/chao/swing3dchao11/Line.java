package com.khu.chao.swing3dchao11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class Line
{
    private ArrayList<Float> mVertexes = new ArrayList<Float>();
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;

    private float[] points =   //Not use this internal figures
            {
                    0.0f, 0.5f, 0.0f,
                    -0.5f, 0.0f, 0.5f,
                    0.5f, 0.0f, 0.0f,
            };

    private float colors[] =
            {
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 0.0f, 1.0f
            };

    public Line()
    {

    }

    public void draw(GL10 gl)
    {
        gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, points.length/3);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
    }


    public void insertVertex(float x, float y, float z)
    {
        mVertexes.add(x);
        mVertexes.add(y);
        mVertexes.add(z);
    }

    public void deleteVertex()
    {
        mVertexes.remove(mVertexes.size());
        mVertexes.remove(mVertexes.size());
        mVertexes.remove(mVertexes.size());
    }

    public void listToArray()
    {
        points = new float[mVertexes.size()];
        colors = new float[mVertexes.size()/3*4];
        for (int i = 0; i < mVertexes.size(); i++)
        {
            points[i] = mVertexes.get(i);
        }
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(points.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(points);
        vertexBuffer.position(0);

        for (int i = 0; i < colors.length; i++)
        {
            if ((i+1)%4 == 0 || (i+1)%5 == 0)
            {
                colors[i] = 1.0f;
            }
            else
            {
                colors[i] = 0.0f;
            }
        }

        byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        colorBuffer = byteBuf.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
    }


    public void clear()
    {
        mVertexes.clear();
    }
    public void setVertexes(ArrayList<Float> vertexes)
    {
        mVertexes = vertexes;
    }
    public ArrayList<Float> getVertexes()
    {
        return mVertexes;
    }
}
