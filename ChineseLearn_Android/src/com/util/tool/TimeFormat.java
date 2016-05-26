package com.util.tool;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;

import com.hw.chineseLearn.R;

@SuppressLint("SimpleDateFormat") public class TimeFormat {
    private long mTimeStamp;
    private Context mContext;

    public TimeFormat(Context context, long timeStamp) {
        this.mContext = context;
        this.mTimeStamp = timeStamp;
    }
    
    public String getSimpleTime(){
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm ");
    	return format.format(mTimeStamp);
    }
    
    public String getSimpleDayTime(){
    	long currentTime = System.currentTimeMillis();
    	Date currentDate = new Date(currentTime);
    	Date date2 = new Date(mTimeStamp);
    	SimpleDateFormat format = new SimpleDateFormat("MM-dd");
    	SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
    	if (currentDate.getDate() - date2.getDate() == 0) {//今天
    		return format2.format(date2);
    	} else {
    		return format.format(date2);
    	}
    }

    // 用于显示会话时间
    public String getTime() {
        long currentTime = System.currentTimeMillis();
        Date currentDate = new Date(currentTime);
        Date date2 = new Date(mTimeStamp);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm ");
        String date = format.format(mTimeStamp);
        int hour = Integer.parseInt(date.substring(0, 2));
        // 今天
        if (currentDate.getDate() - date2.getDate() == 0) {
            if (hour < 6)
                return mContext.getString(R.string.before_dawn) + " " + date;
            else if (hour < 12)
                return mContext.getString(R.string.morning) + " " + date;
            else if (hour < 18)
                return mContext.getString(R.string.afternoon) + " " + date;
            else
                return mContext.getString(R.string.night) + " " + date;
            // 昨天
        } else if (currentDate.getDate() - date2.getDate() == 1) {
            return mContext.getString(R.string.yesterday);
        } else if (currentDate.getDay() - date2.getDay() > 0) {
            if (date2.getDay() == 1)
                return mContext.getString(R.string.monday);
            else if (date2.getDay() == 2)
                return mContext.getString(R.string.tuesday);
            else if (date2.getDay() == 3)
                return mContext.getString(R.string.wednesday);
            else if (date2.getDay() == 4)
                return mContext.getString(R.string.thursday);
            else if (date2.getDay() == 5)
                return mContext.getString(R.string.friday);
            else if (date2.getDay() == 6)
                return mContext.getString(R.string.saturday);
            else
                return mContext.getString(R.string.sunday);

        } else if (currentDate.getYear() == date2.getYear()) {
            return date2.getMonth() + 1 + mContext.getString(R.string.month)
                    + date2.getDate() + mContext.getString(R.string.day);
        } else
            return format1.format(mTimeStamp);
    }

    // 用于显示消息具体时间
    public String getDetailTime() {
        long currentTime = System.currentTimeMillis();
        Date currentDate = new Date(currentTime);
        Date date2 = new Date(mTimeStamp);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String date = format.format(mTimeStamp);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy年-MM月-dd日");
        String date3 = format1.format(mTimeStamp);
        int hour = Integer.parseInt(date.substring(0, 2));
        if (currentDate.getDate() - date2.getDate() == 0) {
            if (hour < 6)
                return mContext.getString(R.string.before_dawn) + date;
            else if (hour < 12)
                return mContext.getString(R.string.morning) + date;
            else if (hour < 18)
                return mContext.getString(R.string.afternoon) + date;
            else
                return mContext.getString(R.string.night) + date;
        } else if (currentDate.getDate() - date2.getDate() == 1) {
            if (hour < 6)
                return mContext.getString(R.string.yesterday) + " "
                        + mContext.getString(R.string.before_dawn) + date;
            else if (hour < 12)
                return mContext.getString(R.string.yesterday) + " "
                        + mContext.getString(R.string.morning) + date;
            else if (hour < 18)
                return mContext.getString(R.string.yesterday) + " "
                        + mContext.getString(R.string.afternoon) + date;
            else
                return mContext.getString(R.string.yesterday) + " "
                        + mContext.getString(R.string.night) + date;
        } else if (currentDate.getYear() == date2.getYear()) {
            if (hour < 6)
                return date2.getMonth() + 1
                        + mContext.getString(R.string.month) + date2.getDate()
                        + mContext.getString(R.string.day) + " "
                        + mContext.getString(R.string.before_dawn) + date;
            else if (hour < 12)
                return date2.getMonth() + 1
                        + mContext.getString(R.string.month) + date2.getDate()
                        + mContext.getString(R.string.day) + " "
                        + mContext.getString(R.string.morning) + date;
            else if (hour < 18)
                return date2.getMonth() + 1
                        + mContext.getString(R.string.month) + date2.getDate()
                        + mContext.getString(R.string.day) + " "
                        + mContext.getString(R.string.afternoon) + date;
            else
                return date2.getMonth() + 1
                        + mContext.getString(R.string.month) + date2.getDate()
                        + mContext.getString(R.string.day) + " "
                        + mContext.getString(R.string.night) + date;
        } else if (hour < 6)
            return date3 + " " + mContext.getString(R.string.before_dawn)
                    + date;
        else if (hour < 12)
            return date3 + " " + mContext.getString(R.string.morning) + date;
        else if (hour < 18)
            return date3 + " " + mContext.getString(R.string.afternoon) + date;
        else
            return date3 + " " + mContext.getString(R.string.night) + date;
    }
}
