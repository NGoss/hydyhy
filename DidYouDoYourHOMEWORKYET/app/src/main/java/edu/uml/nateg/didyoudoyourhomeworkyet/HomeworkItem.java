package edu.uml.nateg.didyoudoyourhomeworkyet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;

// Do not modify

public class HomeworkItem {

    public static final String ITEM_SEP = System.getProperty("line.separator");

    public enum Priority {
        LOW, MED, HIGH
    };

    public final static String TITLE = "title";
    public final static String PRIORITY = "priority";
    public final static String DATE = "date";
    public final static String FILENAME = "filename";
    public final static String PERCENT = "percent_done";
    public final static String DESC = "description";

    public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.US);

    private String mTitle = new String();
    private Priority mPriority = Priority.LOW;
    private Date mDate = new Date();
    private int mPercentDone = 0;
    private String mDescrip = new String();

    HomeworkItem(String title, String description, Priority priority, Date date, int percentDone) {
        this.mTitle = title;
        this.mPriority = priority;
        this.mDate = date;
        this.mPercentDone = percentDone;
        this.mDescrip = description;
    }




    HomeworkItem(Intent intent) {

        mTitle = intent.getStringExtra(HomeworkItem.TITLE);
        mPriority = Priority.valueOf(intent.getStringExtra(HomeworkItem.PRIORITY));
        mPercentDone = intent.getIntExtra(HomeworkItem.PERCENT, 0);
        mDescrip = intent.getStringExtra(HomeworkItem.DESC);

        try {
            mDate = HomeworkItem.FORMAT.parse(intent.getStringExtra(HomeworkItem.DATE));
        } catch (ParseException e) {
            mDate = new Date();
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getPercentDone() {
        return mPercentDone;
    }

    public void setPercentDone(int percentDone) {
        mPercentDone = percentDone;
    }

    public String getDescription() {return mDescrip; }

    public void setDescription(String description) {mDescrip = description; }

    // Take a set of String data values and
    // package them for transport in an Intent

    public static void packageIntent(Intent intent, String title, String description,
                                     Priority priority, String date, int percent) {

        intent.putExtra(HomeworkItem.TITLE, title);
        intent.putExtra(HomeworkItem.DESC, description);
        intent.putExtra(HomeworkItem.PRIORITY, priority.toString());
        intent.putExtra(HomeworkItem.DATE, date);
        intent.putExtra(HomeworkItem.PERCENT, percent);

    }

    public String toString() {
        return mTitle + ITEM_SEP + mDescrip + ITEM_SEP + mPriority + ITEM_SEP
                + FORMAT.format(mDate) + ITEM_SEP + mPercentDone;
    }

    public String toLog() {
        return "Title:" + mTitle + ITEM_SEP + "Priority:" + mPriority
                + ITEM_SEP + "Date:"
                + FORMAT.format(mDate)
                + ITEM_SEP + "Percent Done:" + mPercentDone;
    }

}