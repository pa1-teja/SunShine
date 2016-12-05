package com.example.pavan.sunshine.sunshinewear;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.text.format.Time;
import android.util.Log;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by KVR on 11/30/2016.
 */

public class SunshineWatchFace {
    private static final String TIME_FORMAT_WITHOUT_SECONDS = "%02d.%02d";
    private static final String TIME_FORMAT_WITH_SECONDS = TIME_FORMAT_WITHOUT_SECONDS + ".%02d";
    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final int DATE_AND_TIME_DEFAULT_COLOR = Color.WHITE;
    private static final int BACKGROUND_DEFAULT_COLOR = Color.BLACK;
    public Canvas canvas;
    private Paint timePaint, datePaint, backgroundPaint, lowTempPaint, hightempPaint, linePaint, imagePaint;
    private Time time;
    private String TAG = getClass().getSimpleName();
    private boolean shouldShowSeconds = true;
    private int backgroundColor = BACKGROUND_DEFAULT_COLOR;
    private int dateAndTimeColor = DATE_AND_TIME_DEFAULT_COLOR;
    private int highTemp;
    private int lowTemp;
    private Bitmap weatherImageBitmap;
    private Date date;
    private boolean imageAvailable = false;
    private int artResourseId;
    private BitmapDrawable result;
    private BitmapDrawable bmd;
    private Context context;

    SunshineWatchFace(Paint timePaint, Paint datePaint, Paint backgroundPaint, Time time,
                      Paint hightempPaint, Paint lowTempPaint, Paint linePaint, Paint imagePaint) {

        this.timePaint = timePaint;
        this.datePaint = datePaint;
        this.backgroundPaint = backgroundPaint;
        this.time = time;
        this.hightempPaint = hightempPaint;
        this.lowTempPaint = lowTempPaint;
        this.linePaint = linePaint;
        this.imagePaint = imagePaint;
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
        hightempPaint.setTextSize(30);
        Typeface hightempTypeFace = hightempPaint.getTypeface();
        Typeface bold = Typeface.create(hightempTypeFace, Typeface.BOLD);
        hightempPaint.setTypeface(bold);


        Paint lowTempPaint = new Paint();
        lowTempPaint.setColor(Color.WHITE);
        lowTempPaint.setTextSize(30);
        Typeface lowTempTypeface = lowTempPaint.getTypeface();
        Typeface lowTempTypefacee = Typeface.create(lowTempTypeface, Typeface.BOLD);
        lowTempPaint.setTypeface(lowTempTypefacee);

        Paint linePaint = new Paint();
        linePaint.setColor(Color.WHITE);

        Paint imagePaint = new Paint();
        imagePaint.setStyle(Paint.Style.FILL);
//        canvas.drawRect(mRedPaddleRect, mPaint);



        return new SunshineWatchFace(timePaint, datePaint, backgroundPaint, new Time(), hightempPaint, lowTempPaint, linePaint, imagePaint);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getArtResourseId() {
        return artResourseId;
    }

    public void setArtResourseId(int artResourseId) {
        this.artResourseId = artResourseId;
    }

    public void draw(Canvas canvas, Rect bounds) {
        time.setToNow();
        canvas.drawRect(0, 0, bounds.width(), bounds.height(), backgroundPaint);


        String timeText = String.format(TIME_FORMAT_WITHOUT_SECONDS,
                time.hour, time.minute);

        float timeXOffset = computeXOffset(timeText, timePaint, bounds);
        float timeYOffset = computeTimeYOffset(timeText, timePaint, bounds);
        canvas.drawText(timeText, timeXOffset, timeYOffset, timePaint);

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        try {
            date = format.parse(time.month + 1 + "/" + time.monthDay + "/" + time.year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        format = new SimpleDateFormat("E");

        String dayText = format.format(date);

        String dateText = dayText + ", " + getMonthForInt(month) + " " + day + " " + year;

        float dateXOffset = computeXOffset(dateText, datePaint, bounds);
        float dateYOffset = computeDateYOffset(dateText, datePaint);
        canvas.drawText(dateText, dateXOffset, timeYOffset + dateYOffset, datePaint);


        float highTempXOffset = computeHighTempXOffset(highTemp, hightempPaint, bounds);
        float highTempYOffset = computeHighTempYOffset(highTemp, hightempPaint, bounds);
        canvas.drawText(String.valueOf(highTemp) + "\u00b0", highTempXOffset, highTempYOffset, hightempPaint);

        float lowTempXOffset = computeLowTempXOffset(lowTemp, lowTempPaint, bounds);
        float lowTempYOffset = computeLowTempYOffset(lowTemp, lowTempPaint, bounds);
        canvas.drawText(String.valueOf(lowTemp) + "\u00b0", lowTempXOffset, lowTempYOffset, lowTempPaint);

        canvas.drawLine(130.0f, 160.0f, 180.0f, 160.0f, linePaint);

        if (bmd != null)
            canvas.drawBitmap(bmd.getBitmap(), 80.0f, 130.0f, imagePaint);
    }


    public void createBitmapFromDrawable(Resources resources, int artResourceId) {

        Log.d(TAG, "drawable  : " + Utility.getArtResourceForWeatherCondition(artResourceId) + " artResID : " + artResourceId);

        Bitmap weatherImageBitmap = BitmapFactory.decodeResource(resources,
                Utility.getArtResourceForWeatherCondition(artResourceId));
        // load the origial BitMap (500 x 500 px)


        int width = weatherImageBitmap.getWidth();
        int height = weatherImageBitmap.getHeight();
        int newWidth = 200;
        int newHeight = 200;

        // calculate the scale - in this case = 0.4f
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // createa matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // rotate the Bitmap
        matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(weatherImageBitmap, 0, 0,
                newWidth, newHeight, matrix, true);

        // make a Drawable from Bitmap to allow to set the BitMap
        // to the ImageView, ImageButton or what ever
        bmd = new BitmapDrawable(resizedBitmap);



        if (weatherImageBitmap == null)
            Log.d(TAG, "bit map is null");

    }


    private float computeLowTempXOffset(double lowTemp, Paint paint, Rect lowTempBounds) {
        float centerX = 220.0f;
        float textLength = paint.measureText(String.valueOf(lowTemp));
        return centerX - (textLength / 5.0f);
    }

    private float computeLowTempYOffset(double lowTemp, Paint paint, Rect lowTempBounds) {
        float centerY = 200.0f;
        Rect lowTempBoundss = new Rect();
        paint.getTextBounds(String.valueOf(lowTemp), 0, String.valueOf(lowTemp).length(), lowTempBoundss);
        int highTempTextHeight = lowTempBoundss.height();
        return centerY + (highTempTextHeight / 10.0f);
    }

    private float computeXOffset(String text, Paint paint, Rect watchBounds) {
        float centerX = 150.0f;
        float timeLength = paint.measureText(text);
        return centerX - (timeLength / 2.0f);
    }

    private float computeHighTempXOffset(double highTemp, Paint paint, Rect highTempBounds) {
        float centerX = 160.0f;
        float textLength = paint.measureText(String.valueOf(highTemp));
        return centerX - (textLength / 5.0f);
    }

    private float computeHighTempYOffset(double highTemp, Paint paint, Rect highTempBounds) {
//        float centerY = highTempBounds.exactCenterY();
        float centerY = 200.0f;
        Rect highTempBoundss = new Rect();
        paint.getTextBounds(String.valueOf(highTemp), 0, String.valueOf(highTemp).length(), highTempBoundss);
        int highTempTextHeight = highTempBoundss.height();
        return centerY + (highTempTextHeight / 10.0f);
    }


    private float computeTimeYOffset(String timeText, Paint paint, Rect watchBounds) {

        float centerY = 100.0f;
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

    public void setHighTemp(double highTemp) {
        highTemp = Math.round(highTemp);
        this.highTemp = (int) highTemp;
    }



    public void setLowTemp(double lowTemp) {
        this.lowTemp = (int) lowTemp;
    }

    public String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        month = month.substring(0, 3);
        return month;
    }
}



