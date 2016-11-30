package com.example.pavan.sunshine.sunshinewear;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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

    private final Paint timePaint, datePaint, backgroundPaint;
    private final Time time;

    private boolean shouldShowSeconds = true;
    private int backgroundColor = BACKGROUND_DEFAULT_COLOR;
    private int dateAndTimeColor = DATE_AND_TIME_DEFAULT_COLOR;

    SunshineWatchFace(Paint timePaint, Paint datePaint, Paint backgroundPaint, Time time) {

        this.timePaint = timePaint;
        this.datePaint = datePaint;
        this.backgroundPaint = backgroundPaint;
        this.time = time;
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

        return new SunshineWatchFace(timePaint, datePaint, backgroundPaint, new Time());
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
    }

    private float computeXOffset(String text, Paint paint, Rect watchBounds) {
        float centerX = watchBounds.exactCenterX();
        float timeLength = paint.measureText(text);
        return centerX - (timeLength / 2.0f);
    }

    private float computeTimeYOffset(String timeText, Paint paint, Rect watchBounds) {

        float centerY = watchBounds.exactCenterY();
        Rect textBounds = new Rect();
        timePaint.getTextBounds(timeText, 0, timeText.length(), textBounds);
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


}



