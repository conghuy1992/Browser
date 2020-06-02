package com.huydev.simplebrowser.common;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.huydev.simplebrowser.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Android on 4/11/2018.
 */

public class Utils {
    static String TAG = "Utils";

    public static int getColor(Context context, int id) {
        return context.getResources().getColor(id);
    }

    public static String getMsg(Context context, int id) {
        if (context == null) return "";
        return context.getResources().getString(id);
    }

    public static void showMsg(Context context, String msg) {
        if (context != null) {
            try {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showMsg(Context context, int id) {
        if (context != null) {
            try {
                Toast.makeText(context, getMsg(context, id), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int getDimenInPx(Context context, int id) {
        return (int) context.getResources().getDimension(id);
    }


    public static void addFragment(FragmentManager fragmentManager, String addToBackStack,
                                   int resId, androidx.fragment.app.Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setCustomAnimations(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        if (addToBackStack != null && addToBackStack.trim().length() > 0)
            transaction.addToBackStack(addToBackStack);
        transaction.replace(resId, fragment);
        transaction.commit();
    }

    public static String formatTime(long date, String defaultPattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultPattern, Locale.getDefault());
        return simpleDateFormat.format(new Date(date));
    }

    public static void shareIntent(Context context, String EXTRA_TEXT) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, EXTRA_TEXT);
        sendIntent.setType("text/plain");
//        sendIntent.putExtra(Intent.EXTRA_SUBJECT, Const.GOOGLE_PLAY);
        context.startActivity(Intent.createChooser(sendIntent, "Share via " + context.getResources().getString(R.string.app_name)));
    }

    public static boolean isResponseObject(String response) {
        if (response.startsWith("\ufeff"))
            return true;
        if (response.startsWith("{"))
            return true;
        return false;
    }

    public static boolean isEmpty(String msg) {
        if (msg == null || msg.trim().length() == 0) return true;
        return false;
    }

    public static long getTimeMilisecondFromDate(String myDateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Statics.yyyy_MM_dd_T_HH_mm_ss, Locale.getDefault());
            Date date = sdf.parse(myDateString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            return calendar.getTimeInMillis();
        } catch (Exception e) {
            return 0;
        }
    }


    public static String getTypeFile(String url) {
        String str[] = url.split("[.]");
        return str[str.length - 1];
    }

    public static String getFileName(String url) {
        String str[] = url.split("/");
        return str[str.length - 1];
    }

    public static void startDownload(Context context, String url) {
        showMsg(context, "Start Download");
        Log.d(TAG, "url download:" + url);
        final long refer;
        String fileName = getFileName(url);
        Log.d(TAG, "fileName:" + fileName);
        final DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDescription(getMsg(context, R.string.app_name)).setTitle(fileName);
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setVisibleInDownloadsUi(true);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        refer = downloadManager.enqueue(request);

        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver downloadcomplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long r = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (refer == r) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(r);
                    Cursor cursor = downloadManager.query(query);
                    cursor.moveToFirst();
                    //get status of the download
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int status = cursor.getInt(columnIndex);
//                    int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
//                    String saveFilePath = cursor.getString(filenameIndex);
//                    int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
//                    int reason = cursor.getInt(columnReason);
                    switch (status) {
                        case DownloadManager.STATUS_SUCCESSFUL:
                            if (context != null)
                                showMsg(context, "Download Completed");
                            break;
                        case DownloadManager.STATUS_FAILED:
                            showMsg(context, "Download Fail");
                            break;
                        case DownloadManager.STATUS_PAUSED:
                            // do something                            break;
                        case DownloadManager.STATUS_PENDING:
                            // do something                            break;
                        case DownloadManager.STATUS_RUNNING:
                            // do something                            break;
                    }
                }
            }
        };
        context.registerReceiver(downloadcomplete, intentFilter);
    }

    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    public static int KEY_FROM_SERVER = 200;
    public static int KEY_TO_SERVER = 201;

    public static String showTimeWithoutTimeZone(long date, String defaultPattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultPattern, Locale.getDefault());
        //simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(new Date(date));
    }

    public static String getTimezoneOffsetInMinutes() {
        TimeZone tz = TimeZone.getDefault();
        int offsetMinutes = tz.getRawOffset() / 60000;
        String sign = "";
        if (offsetMinutes < 0) {
            sign = "-";
            offsetMinutes = -offsetMinutes;
        }
        return sign + "" + offsetMinutes;
    }

    /**
     * Convert Time Device To Time Server
     */
    public static String convertTimeDeviceToTimeServerDefault(String regDate) {
        return "/Date(" + getTime(regDate) + ")/";
    }

    public static String displayTimeWithoutOffset(String timeString, String locale) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(locale, Locale.getDefault());
            return formatter.format(new Date(getTime(timeString)));
        } catch (Exception e) {
            return "";
        }
    }

    public static String displayTimeWithoutOffset(String timeString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(Statics.DATE_FORMAT_YYYY_MM_DD, Locale.getDefault());
            return formatter.format(new Date(getTime(timeString)));
        } catch (Exception e) {
            return "";
        }
    }

    public static long getTime(String timeString) {
        try {
            long time;

            if (timeString.contains("(")) {
                timeString = timeString.replace("/Date(", "");
                int plusIndex = timeString.indexOf("+");
                int minusIndex = timeString.indexOf("-");
                if (plusIndex != -1) {
                    time = Long.valueOf(timeString.substring(0, plusIndex));
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    cal.setTimeInMillis(time);
                    cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(timeString.substring(plusIndex + 1, plusIndex + 3)));
                    cal.add(Calendar.MINUTE, Integer.parseInt(timeString.substring(plusIndex + 3, plusIndex + 5)));
                    Calendar tCal = Calendar.getInstance();
                    tCal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
                    time = tCal.getTimeInMillis();
                } else if (minusIndex != -1) {
                    time = Long.valueOf(timeString.substring(0, minusIndex));
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    cal.setTimeInMillis(time);
                    cal.setTimeZone(TimeZone.getDefault());
                    cal.add(Calendar.HOUR_OF_DAY, -Integer.parseInt(timeString.substring(minusIndex + 1, minusIndex + 3)));
                    cal.add(Calendar.MINUTE, -Integer.parseInt(timeString.substring(minusIndex + 3, minusIndex + 5)));
                    Calendar tCal = Calendar.getInstance();
                    tCal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
                    time = tCal.getTimeInMillis();
                } else {
                    time = Long.valueOf(timeString.substring(0, timeString.indexOf(")")));
                }
            } else {
                time = Long.valueOf(timeString);
            }

            return time;
        } catch (Exception e) {
            Log.d("lchTest", e.toString());
            return 0;
        }
    }


    //-2: today
    //-3: Yesterday
    //-4: this month
    //-5: last Month
    //-1: default
    public static long getTimeForMail(long time) {
        int date = -1;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        if (cal.get(Calendar.YEAR) == getYearNote(time)) {
            if (cal.get(Calendar.MONTH) == getMonthNote(time)) {
                int temp = cal.get(Calendar.DAY_OF_MONTH) - getDateNote(time);
                if (cal.get(Calendar.DAY_OF_MONTH) == getDateNote(time)) {
                    date = -2;
                } else if (temp == 1) {
                    date = -3;
                } else {
                    date = -4;
                }
            } else if (cal.get(Calendar.MONTH) - 1 == getMonthNote(time)) {
                date = -5;
            }
        } else if (cal.get(Calendar.YEAR) == getYearNote(time) + 1) {
            if (cal.get(Calendar.MONTH) == 0 && getMonthNote(time) == 11) {
                date = -5;
            }
        }

        return date;
    }

    public static long getStttimeMessage(long time, long lasttime) {
        long date = -1;
        int day_time = getDateNote(time);
        int month_time = getMonthNote(time);
        int year_time = getYearNote(time);
        if (year_time == getYearNote(lasttime)) {
            if (month_time == getMonthNote(lasttime)) {
                int temp = day_time - getDateNote(time);
                if (day_time == getDateNote(lasttime)) {
                    date = -2;
                } else if (temp == -1) {
                    date = -3;
                } else {
                    date = -4;
                }

            } else if (month_time - 1 == getMonthNote(lasttime)) {
                date = -5;
            }

        } else if (year_time == getYearNote(lasttime) + 1) {
            if (month_time == 0 && getMonthNote(time) == 11) {
                date = -5;
            }
        }


        return date;
    }

    //1: today
    //2: Yesterday
    //0: default
    public static int getYearNote(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonthNote(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal.get(Calendar.MONTH);
    }

    public static int getDateNote(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean compareTime(long date1, long date2) {
        SimpleDateFormat formatter = new SimpleDateFormat(Statics.DATE_FORMAT_YY_MM_DD);
        String date = formatter.format(new Date(date1));
        String dateTemp = formatter.format(new Date(date2));
        return date.equalsIgnoreCase(dateTemp);
    }

    // Notification time convert to string
    public static String timeToStringNotAMPM(int hourOfDay, int minute) {
        String text = "";
        String minutes = "";
        if (minute < 10) {
            minutes = "0" + minute;
        } else {
            minutes = String.valueOf(minute);
        }
        if ((hourOfDay == 12 && minute > 0) || hourOfDay > 12) {// PM
            text += hourOfDay + ":" + minutes;
        } else { // AM
            if (hourOfDay < 10) {
                text += "0";
            }
            text += hourOfDay + ":" + minutes;
        }
        return text;
    }

    public static String timeToString(int hourOfDay, int minute) {
        String text = "";
        String minutes = "";
        if (minute < 10) {
            minutes = "0" + minute;
        } else {
            minutes = String.valueOf(minute);
        }
        if ((hourOfDay == 12 && minute > 0) || hourOfDay > 12) {// PM
            text = "PM ";
            text += hourOfDay + ":" + minutes;
        } else { // AM
            text = "AM ";
            if (hourOfDay < 10) {
                text += "0";
            }
            text += hourOfDay + ":" + minutes;
        }
        return text;
    }

    public static String getFilename(int currentFormat, String fileName) {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, Statics.AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + fileName + Statics.AUDIO_RECORDER_FILE_EXT_MP3);
    }

    public static String audioFormatDuration(int second) {
        try {
            int m = second / 60;
            int s = second % 60;

            if (m == 0 && s == 0) s = 1;

            String MIN = "" + m;
            if (m < 10) MIN = "0" + m;
            String SEC = "" + s;
            if (s < 10) SEC = "0" + s;
            return MIN + ":" + SEC;
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isAudio(String fileType) {
        if (fileType.equalsIgnoreCase(Statics.AUDIO_MP3)
                || fileType.equalsIgnoreCase(Statics.AUDIO_AMR)
                || fileType.equalsIgnoreCase(Statics.AUDIO_WMA)
                || fileType.equalsIgnoreCase(Statics.AUDIO_M4A)
                || fileType.equalsIgnoreCase(Statics.AUDIO_OGG)
                || fileType.equalsIgnoreCase(Statics.AUDIO_WAV)) {
            return true;
        }
        return false;
    }

    public static int sdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static void setColorForViewWithOldVersion(Context context, View v) {
//        if (v == null) return;
//        if (Utils.sdkVersion() < 21)
//            v.setBackgroundColor(Utils.getColor(context, R.color.login_google_plus));
    }

    public static GradientDrawable gradientDrawable(int fillColor, int strokeColor, int strokeValue, int cornerRadius) {
        // Initialize a new GradientDrawable
        GradientDrawable gd = new GradientDrawable();

        // Specify the shape of drawable
        gd.setShape(GradientDrawable.RECTANGLE);

        // Set the fill color of drawable
        gd.setColor(fillColor); // make the background transparent

        // Create a 2 pixels width red colored border for drawable
        gd.setStroke(strokeValue, strokeColor); // border width and color

        // Make the border rounded
        gd.setCornerRadius(cornerRadius); // border corner radius

        return gd;
    }


    public static void hideKeyboardFrom(Context context, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyBoard(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<String> get_adv(Context context) {
        List<String> list = new ArrayList<>();
        String data;
        InputStream in = context.getResources().openRawResource(R.raw.advfile);
        InputStreamReader inreader = new InputStreamReader(in);
        BufferedReader bufreader = new BufferedReader(inreader);
//        StringBuilder builder = new StringBuilder();
        if (in != null) {
            try {
                while ((data = bufreader.readLine()) != null) {
//                    Log.d(TAG, "data:" + data);
//                    builder.append(data);
//                    builder.append("\n");
                    list.add(data);
                }
                in.close();
            } catch (IOException ex) {
                Log.e("ERROR", ex.getMessage());
            }
        }
        if (list == null) list = new ArrayList<>();
        return list;
    }

    public static boolean isContains(List<String> list, String url) {
        int n = list.size();
        for (int i = 0; i < n; i++) {
            if (url.contains(list.get(i)))
                return true;
        }
        return false;
    }

}
