/**
 * *****************************************************************************
 * Copyright 2001 - 2017 Comit. All Rights Reserved.
 * 作者：YanWentao
 * 创建日期：2017/1/9 9:28
 * 修改日期：
 * 描述：
 * ****************************************************************************
 */
package com.ys.www.asscg.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * 创建时间：2017/1/9
 * <p/>
 * 创建作者：chuck
 * <p/>
 * 文件名称：CustomWaveView
 * <p/>
 * 文件作用：波浪动画
 */
public class ChuckWaveView extends View {

    /**
     * +------------------------+
     * |<--wave length->        |______
     * |   /\          |   /\   |  |
     * |  /  \         |  /  \  | amplitude
     * | /    \        | /    \ |  |
     * |/      \       |/      \|__|____
     * |        \      /        |  |
     * |         \    /         |  |
     * |          \  /          |  |
     * |           \/           | water level
     * |                        |  |
     * |                        |  |
     * +------------------------+__|____
     */

    private static final float DEFAULT_AMPLITUDE_RATIO = 0.03f; //振幅：波浪静止时水面距离底部的高度
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f; //水位：波浪垂直振动时偏离水面的最大距离
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f; //波长：一个完整的波浪的水平长度
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.4f; //偏移：波浪相对于初始位置的水平偏移

    private BitmapShader mWaveShader = null;
    private Matrix mShaderMatrix = null;
    private Paint mViewPaint = null;

    private float mDefaultWaterLevel = 0;
    private float mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;

    public ChuckWaveView(Context context) {
        super(context);
        init();
    }

    public ChuckWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChuckWaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mShaderMatrix = new Matrix();
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
    }


    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    /**
     * Shift the wave horizontally according to <code>waveShiftRatio</code>.
     *
     * @param waveShiftRatio Should be 0 ~ 1. Default to be 0.
     *                       Result of waveShiftRatio multiples width of WaveView is the length to shift.
     */
    public void setWaveShiftRatio(float waveShiftRatio) {
        if (mWaveShiftRatio != waveShiftRatio) {
            mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaterLevelRatio() {
        return mWaterLevelRatio;
    }

    /**
     * Set water level according to <code>waterLevelRatio</code>.
     *
     * @param waterLevelRatio Should be 0 ~ 1. Default to be 0.5.
     *                        Ratio of water level to WaveView height.
     */
    public void setWaterLevelRatio(float waterLevelRatio) {
        if (mWaterLevelRatio != waterLevelRatio) {
            mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createShader();
    }

    /**
     * 绘制横纵波浪的曲线
     */
    private void createShader() {
        mDefaultWaterLevel = getHeight() * DEFAULT_WATER_LEVEL_RATIO;
        float mDefaultWaveLength = getWidth();
        float mDefaultAmplitude = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        double mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / getWidth();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);

        // y=Asin(ωx+φ)+h 正弦曲线
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        float[] waveY = new float[endX];

        wavePaint.setColor(Color.parseColor("#f0f4f5")); //后面的波浪
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);

            waveY[beginX] = beginY;
        }

        wavePaint.setColor(Color.parseColor("#f0f4f5")); //前面的波浪
        final int wave2Shift = (int) (mDefaultWaveLength / 4);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
        }

        //将波浪曲线的Bitmap变量赋值给画笔
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mWaveShader != null) {
            if (mViewPaint.getShader() == null) {
                mViewPaint.setShader(mWaveShader);
            }

            // scale shader according to mWaveLengthRatio and mAmplitudeRatio
            // this decides the size(mWaveLengthRatio for width, mAmplitudeRatio for height) of waves
            mShaderMatrix.setScale(1, 1, 0, mDefaultWaterLevel);

            // translate shader according to mWaveShiftRatio and mWaterLevelRatio
            // this decides the start position(mWaveShiftRatio for x, mWaterLevelRatio for y) of waves
            mShaderMatrix.postTranslate(mWaveShiftRatio * getWidth(), (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());

            // assign matrix to invalidate the shader
            mWaveShader.setLocalMatrix(mShaderMatrix);

            canvas.drawRect(0, 0, getWidth(), getHeight(), mViewPaint);
        } else {
            mViewPaint.setShader(null);
        }
    }
}
