package com.example.zys.clockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zys on 16/10/21.
 */

public class ClockView extends View {

    //默认的颜色
    public static final int DEFAULT_COLOR = Color.BLACK;

    //默认刷新时间1秒
    private static final int DEFAULT_REFRESH_TIME = 1000;

    //默认的VIEW的大小
    private static final float DEFAULT_WIDTH = 200;

    //默认的表盘外圆宽度
    private static final float DEFAULT_CIRCLE_WIDTH = 5;

    //默认的整点刻度的宽度
    private static final float DEFAULT_LONGER_WIDTH = 5;

    //默认的整点刻度的高度
    private static final float DEFAULT_LONGER_HEIGHT = 60;

    //默认的非整点刻度的宽度
    private static final float DEFAULT_SHORTER_WIDTH = 3;

    //默认的非整点刻度的高度
    private static final float DEFAULT_SHORTER_HEIGHT = 30;

    //默认的文字大小
    private static final float DEFAULT_TEXT_SIZE = 60;

    //默认的表盘中央的原点半径
    private static final float DEFAULT_POINTER_CIRCLE_RADIUS = 10;

    //默认的时针宽度
    private static final float DEFAULT_HOUR_WIDTH = 14;

    //默认的分针宽度
    private static final float DEFAULT_MINUTE_WIDTH = 10;

    //默认的秒针宽度
    private static final float DEFAULT_SECOND_WIDTH = 8;

    //默认的时针长度比例
    private static final float DEFAULT_HOUR_HEIGHT_DENSITY = 0.55f;

    //默认的分针宽度
    private static final float DEFAULT_MINUTE_HEIGHT_DENSITY = 0.65f;

    //默认的秒针宽度
    private static final float DEFAULT_SECOND_HEIGHT_DENSITY = 0.8f;

    //默认的外表盘颜色
    public static final int DEFAULT_CIRCLE_COLOR = DEFAULT_COLOR;

    //默认的内圆点颜色
    public static final int DEFAULT_POINTER_COLOR = DEFAULT_COLOR;

    //默认的时针颜色
    public static final int DEFAULT_HOUR_COLOR = DEFAULT_COLOR;

    //默认的分针颜色
    public static final int DEFAULT_MINUTE_COLOR = DEFAULT_COLOR;

    //默认的秒针颜色
    public static final int DEFAULT_SECOND_COLOR = DEFAULT_COLOR;

    //外表盘宽度
    private float mCircleWidth;
    //整点刻度宽度
    private float mLongerWidth;
    //整点刻度高度
    private float mLongerHeight;
    //非整点刻度宽度
    private float mShortWidth;
    //非整点刻度高度
    private float mShortHeight;
    //文字大小
    private float mTextSize;
    //内部圆点半径
    private float mPointerRadius;
    //时针宽度
    private float mHourWidth;
    //分针宽度
    private float mMinuteWidth;
    //秒针宽度
    private float mSecondWidth;
    //时针高度比
    private float mHourHeightDensity;
    //分针高度比
    private float mMinuteHeightDensity;
    //秒针高度比
    private float mSecondHeightDensity;
    //外表盘颜色
    private int mCircleColor;
    //内圆点颜色
    private int mPointerColor;
    //时针颜色
    private int mHourColor;
    //分针颜色
    private int mMinuteColor;
    //秒针颜色
    private int mSecondColor;

    //外环
    private Paint mCirclePaint;
    //内部圆点
    private Paint mPointerPaint;
    //刻度线
    private Paint mDegreePaint;
    //画字体
    private Paint mTextPaint;
    //指针
    private Paint mGuidePaint;

    private int mHour, mMinute, mSecond;

    private Handler mHandler;
    private Runnable mRunnable;

    public ClockView(Context context) {
        this(context, null, 0);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
        initAttrs(typedArray);
        typedArray.recycle();
        initPaint();
        init();

    }

    private void init() {

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
                mHandler.postDelayed(this, DEFAULT_REFRESH_TIME);
            }
        };
        mHandler.postDelayed(mRunnable, DEFAULT_REFRESH_TIME);

    }

    private void initAttrs(TypedArray array) {
        mCircleWidth = array.getFloat(R.styleable.ClockView_circle_width, DEFAULT_CIRCLE_WIDTH);
        mLongerWidth = array.getFloat(R.styleable.ClockView_longer_width, DEFAULT_LONGER_WIDTH);
        mLongerHeight = array.getFloat(R.styleable.ClockView_longer_height, DEFAULT_LONGER_HEIGHT);
        mShortWidth = array.getFloat(R.styleable.ClockView_shorter_width, DEFAULT_SHORTER_WIDTH);
        mShortHeight = array.getFloat(R.styleable.ClockView_shorter_height, DEFAULT_SHORTER_HEIGHT);
        mTextSize = array.getFloat(R.styleable.ClockView_text_size, DEFAULT_TEXT_SIZE);
        mPointerRadius = array.getFloat(R.styleable.ClockView_pointer_circle_radius, DEFAULT_POINTER_CIRCLE_RADIUS);
        mHourWidth = array.getFloat(R.styleable.ClockView_hour_width, DEFAULT_HOUR_WIDTH);
        mMinuteWidth = array.getFloat(R.styleable.ClockView_minute_width, DEFAULT_MINUTE_WIDTH);
        mSecondWidth = array.getFloat(R.styleable.ClockView_second_width, DEFAULT_SECOND_WIDTH);
        mHourHeightDensity = array.getFloat(R.styleable.ClockView_hour_height_density, DEFAULT_HOUR_HEIGHT_DENSITY);
        mMinuteHeightDensity = array.getFloat(R.styleable.ClockView_minute_height_density, DEFAULT_MINUTE_HEIGHT_DENSITY);
        mSecondHeightDensity = array.getFloat(R.styleable.ClockView_second_height_density, DEFAULT_SECOND_HEIGHT_DENSITY);
        mCircleColor = array.getColor(R.styleable.ClockView_circle_color, DEFAULT_CIRCLE_COLOR);
        mPointerColor = array.getColor(R.styleable.ClockView_pointer_color, DEFAULT_POINTER_COLOR);
        mHourColor = array.getColor(R.styleable.ClockView_hour_color, DEFAULT_HOUR_COLOR);
        mMinuteColor = array.getColor(R.styleable.ClockView_minute_color, DEFAULT_MINUTE_COLOR);
        mSecondColor = array.getColor(R.styleable.ClockView_second_color, DEFAULT_SECOND_COLOR);

    }

    private void initPaint() {
        //外表盘
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStrokeWidth(mCircleWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        //内圆点
        mPointerPaint = new Paint();
        mPointerPaint.setAntiAlias(true);
        mPointerPaint.setColor(mPointerColor);
        mPointerPaint.setStyle(Paint.Style.FILL);

        //刻度
        mDegreePaint = new Paint();
        mDegreePaint.setAntiAlias(true);
        mDegreePaint.setColor(mCircleColor);
        mDegreePaint.setStyle(Paint.Style.FILL);

        //刻度字体
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mCircleColor);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);//字体的对齐方式
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setFakeBoldText(true);//设置粗体

        //指针
        mGuidePaint = new Paint();
        mGuidePaint.setAntiAlias(true);
        mGuidePaint.setStyle(Paint.Style.FILL);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
    }

    private int measureSize(int spec) {
        int result = (int) DEFAULT_WIDTH;
        int size = MeasureSpec.getSize(spec);
        int mode = MeasureSpec.getMode(spec);
        if (mode == MeasureSpec.EXACTLY || mode == MeasureSpec.AT_MOST) {
            result = size;
        } else if (mode == MeasureSpec.UNSPECIFIED) {
            result = Math.min(result, size);
            if (result == 0) {
                result = (int) DEFAULT_WIDTH;
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        float r = Math.min(width / 2, height / 2) - mCircleWidth;
        //绘制圆环
        canvas.drawCircle(width / 2, height / 2, r, mCirclePaint);
        float lineLength;
        //绘制刻度线
        for (int i = 0; i < 60; i++) {
            //整点
            if (i % 5 == 0) {
                mDegreePaint.setStrokeWidth(mLongerWidth);
                lineLength = mLongerHeight;
            } else {
                mDegreePaint.setStrokeWidth(mShortWidth);
                lineLength = mShortHeight;
            }
            canvas.drawLine(width / 2, Math.abs(width / 2 - height / 2) + mCircleWidth,
                width / 2, Math.abs(width / 2 - height / 2) + mCircleWidth + lineLength, mDegreePaint);
            //围绕圆心转
            canvas.rotate(360 / 60, width / 2, height / 2);
        }

        //绘制刻度数字
        /**
         * 一周是2π弧度，所以分成12分就是π/6
         * 要计算出来每一个数字的左上角到圆心的距离，然后用弧度算出来角度进行sin，算出对应的X
         */
        String text[] = getContext().getResources().getStringArray(R.array.clock);
        float startX = width / 2;
        float startY = height / 2 - width / 2 + 120;
       // float textR = (float) Math.sqrt(Math.pow(width / 2 - startX, 2) + Math.pow(height / 2 - startY, 2));
        float textR = (float) width / 2 -120;

        for (int i = 0; i < 12; i++) {
            float x = (float) (startX + Math.sin(Math.PI / 6 * i) * textR);
            float y = (float) (startY + textR - Math.cos(Math.PI / 6 * i) * textR);
            //
            if (i != 11 && i != 10 && i != 0) {
                y = y + mTextPaint.measureText(text[i]) / 2;
            }else {
                y = y + mTextPaint.measureText(text[i]) / 4;
            }
            canvas.drawText(text[i], x, y, mTextPaint);
        }

        //获取时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        mSecond = calendar.get(Calendar.SECOND);
        mMinute = calendar.get(Calendar.MINUTE);
        mHour = calendar.get(Calendar.HOUR);

        //绘制秒针
        mGuidePaint.setColor(mSecondColor);
        mGuidePaint.setStrokeWidth(mSecondWidth);
        float secondRotate = (float) 360 / 60 * mSecond;
        canvas.rotate(secondRotate, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2, width / 2, height / 2 - r * mSecondHeightDensity, mGuidePaint);
        canvas.rotate(-secondRotate, width / 2, height / 2);

        //绘制分针
        mGuidePaint.setColor(mMinuteColor);
        mGuidePaint.setStrokeWidth(mMinuteWidth);
        float minuteRotate = (float) 360 / 60 * mMinute;
        canvas.rotate(minuteRotate, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2, width / 2, height / 2 - r * mMinuteHeightDensity, mGuidePaint);
        canvas.rotate(-minuteRotate, width / 2, height / 2);

        //绘制时针
        mGuidePaint.setColor(mHourColor);
        mGuidePaint.setStrokeWidth(mHourWidth);
        int hourRotate = 360 / 60 / 12 * mMinute + 360 / 12 * mHour;
        canvas.rotate(hourRotate, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2, width / 2, height / 2 - r * mHourHeightDensity, mGuidePaint);
        canvas.rotate(-hourRotate, width / 2, height / 2);

        //绘制圆点
        canvas.drawCircle(width / 2, height / 2, mPointerRadius, mPointerPaint);

    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }
}
