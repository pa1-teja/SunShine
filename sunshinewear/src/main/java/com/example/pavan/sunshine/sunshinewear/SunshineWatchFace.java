package com.example.pavan.sunshine.sunshinewear;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.format.Time;

/**
 * Created by KVR on 11/30/2016.
 */

public class SunshineWatchFace {
    private static final String TIME_FORMAT_WITHOUT_SECONDS = "%02d.%02d";
    private static final String TIME_FORMAT_WITH_SECONDS = TIME_FORMAT_WITHOUT_SECONDS + ".%02d";
    private static final String DATE_FORMAT = "%02d.%02d.%d";
    private static final int DATE_AND_TIME_DEFAULT_COLOR = Color.WHITE;
    private static final int BACKGROUND_DEFAULT_COLOR = Color.BLACK;
    private final Paint timePaint, datePaint, backgroundPaint, lowTempPaint, hightempPaint;
    private final Time time;
    private String TAG = getClass().getSimpleName();
    private boolean shouldShowSeconds = true;
    private int backgroundColor = BACKGROUND_DEFAULT_COLOR;
    private int dateAndTimeColor = DATE_AND_TIME_DEFAULT_COLOR;
    private double highTemp;
    private double lowTemp;
    private Bitmap weatherImageBitmap;

    SunshineWatchFace(Paint timePaint, Paint datePaint, Paint backgroundPaint, Time time,
                      Paint hightempPaint, Paint lowTempPaint) {

        this.timePaint = timePaint;
        this.datePaint = datePaint;
        this.backgroundPaint = backgroundPaint;
        this.time = time;
        this.hightempPaint = hightempPaint;
        this.lowTempPaint = lowTempPaint;
    }

    public static SunshineWatchFace newInstance(Context context) {
        Paint timePaint = new Paint();
        timePaint.setColor(DATE_AND_TIME_DEFAULT_COLOR);
        timePaint.setTextSize(46);
        timePaint.setAntiAlias(true);

        Paint datePaint = new Paint();
        datePaint.setColor(DATE_AND_TIME_DEFAULT_COLOR);
        datePaint.setTextSize(20);
        datePaint.setAntiAlias(true);

        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(BACKGROUND_DEFAULT_COLOR);

        Paint hightempPaint = new Paint();
        hightempPaint.setColor(Color.WHITE);
        hightempPaint.setTextSize(20);
        Typeface hightempTypeFace = hightempPaint.getTypeface();
        Typeface bold = Typeface.create(hightempTypeFace, Typeface.BOLD);
        hightempPaint.setTypeface(bold);


        Paint lowTempPaint = new Paint();
        lowTempPaint.setColor(Color.WHITE);
        lowTempPaint.setColor(20);
        Typeface lowTempTypeface = lowTempPaint.getTypeface();
        Typeface lowTempTypefacee = Typeface.create(lowTempTypeface, Typeface.NORMAL);
        lowTempPaint.setTypeface(lowTempTypefacee);

        return new SunshineWatchFace(timePaint, datePaint, backgroundPaint, new Time(), hightempPaint, lowTempPaint);
    }

    public void draw(Canvas canvas, Rect bounds) {
        time.setToNow();
        canvas.drawRect(0, 0, bounds.width(), bounds.height(), backgroundPaint);

        String timeText = String.format(shouldShowSeconds ? TIME_FORMAT_WITH_SECONDS : TIME_FORMAT_WITHOUT_SECONDS,
                time.hour, time.minute, time.second);

        float timeXOffset = computeXOffset(timeText, timePaint, bounds);
        float timeYOffset = computeTimeYOffset(timeText, timePaint, bounds);
        canvas.drawText(timeText, timeXOffset, timeYOffset, timePaint);

        String dateText = String.format(DATE_FORMAT, time.monthDay, (time.month + 1), time.year);

        float dateXOffset = computeXOffset(dateText, datePaint, bounds);
        float dateYOffset = computeDateYOffset(dateText, datePaint);
        canvas.drawText(dateText, dateXOffset, timeYOffset + dateYOffset, datePaint);


        float highTempXOffset = computeHighTempXOffset(highTemp, hightempPaint, bounds);
        float highTempYOffset = computeHighTempYOffset(highTemp, hightempPaint, bounds);
        canvas.drawText(String.valueOf(highTemp), highTempXOffset, highTempYOffset, hightempPaint);

    }

    private float computeXOffset(String text, Paint paint, Rect watchBounds) {
        float centerX = watchBounds.exactCenterX();
        float timeLength = paint.measureText(text);
        return centerX - (timeLength / 2.0f);
    }

    private float computeHighTempXOffset(double highTemp, Paint paint, Rect highTempBounds) {
        float centerX = highTempBounds.exactCenterX();
        float textLength = paint.measureText(String.valueOf(highTemp));
        return centerX - (textLength / 3.0f);
    }

    private float computeHighTempYOffset(double highTemp, Paint paint, Rect highTempBounds) {
        float centerY = highTempBounds.exactCenterY();
        Rect highTempBoundss = new Rect();
        paint.getTextBounds(String.valueOf(highTemp), 0, String.valueOf(highTemp).length(), highTempBoundss);
        int highTempTextHeight = highTempBoundss.height();
        return centerY + (highTempTextHeight / 3.0f);
    }

    private float computeLowTempXOffset(double lowTemp, Paint paint, Rect lowTempBounds) {
        float centerX = lowTempBounds.exactCenterX();
        float textLength = paint.measureText(String.valueOf(lowTemp));
        return centerX - (textLength / 3.0f);
    }

    private float computeLowTempYOffset(double lowTemp, Paint paint, Rect lowTempBounds) {
        float centerY = lowTempBounds.exactCenterY();
        Rect lowTempBoundss = new Rect();
        paint.getTextBounds(String.valueOf(lowTemp), 0, String.valueOf(lowTemp).length(), lowTempBoundss);
        int highTempTextHeight = lowTempBoundss.height();
        return centerY + (highTempTextHeight / 3.0f);
    }


    private float computeTimeYOffset(String timeText, Paint paint, Rect watchBounds) {

        float centerY = watchBounds.exactCenterY();
        Rect textBounds = new Rect();
        paint.getTextBounds(timeText, 0, timeText.length(), textBounds);
        int textHeight = textBounds.height();
        return centerY + (textHeight / 2.0f);
    }

    private float computeDateYOffset(String dateText, Paint datePaint) {
        Rect textBounds = new Rect();
        datePaint.getTextBounds(dateText, 0, dateText.length(), textBounds);
        return textBounds.height() + 10.0f;
    }

    public void setAntiAlias(boolean antiAlias) {
        timePaint.setAntiAlias(antiAlias);
        datePaint.setAntiAlias(antiAlias);
    }

    public void updateTimeZoneWith(String timeZone) {
        time.clear(timeZone);
        time.setToNow();
    }

    public void setShowSeconds(boolean showSeconds) {
        shouldShowSeconds = showSeconds;
    }

    public void updateBackgroundColorTo(int color) {
        backgroundColor = color;
        backgroundPaint.setColor(color);
    }


    public void updateBackgroundColorToDefault() {
        backgroundPaint.setColor(BACKGROUND_DEFAULT_COLOR);
    }

    public void restoreBackgroundColor() {
        backgroundPaint.setColor(backgroundColor);
    }

    public void updateDateAndTimeColorToDefault() {
        timePaint.setColor(DATE_AND_TIME_DEFAULT_COLOR);
        datePaint.setColor(DATE_AND_TIME_DEFAULT_COLOR);
    }

    public void updateDateAndTimeColorTo(int color) {
        dateAndTimeColor = color;
        timePaint.setColor(color);
        datePaint.setColor(color);
    }


    public void restoreDateAndTimeDefaultColor() {
        timePaint.setColor(dateAndTimeColor);
        datePaint.setColor(dateAndTimeColor);
    }

    public void updateHighAndLowTemp() {

    }

    public double getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(double highTemp) {
        this.highTemp = highTemp;
    }

    public double getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(double lowTemp) {
        this.lowTemp = lowTemp;
    }

    public void setWeatherImageBitmap(Bitmap weatherImageBitmap) {
        this.weatherImageBitmap = weatherImageBitmap;
    }

}



