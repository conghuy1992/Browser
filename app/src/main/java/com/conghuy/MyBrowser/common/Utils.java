package com.conghuy.MyBrowser.common;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.conghuy.MyBrowser.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
                                   int resId, android.support.v4.app.Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setCustomAnimations(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        if (addToBackStack != null && addToBackStack.trim().length() > 0)
            transaction.addToBackStack(addToBackStack);
        transaction.replace(resId, fragment);
        transaction.commit();
    }

    public static boolean isContains(ArrayList<String> list, String url) {
        int n = list.size();
        for (int i = 0; i < n; i++) {
            if (url.contains(list.get(i)))
                return true;
        }
        return false;
    }

    public static ArrayList<String> get_adv(Context context) {
        ArrayList<String> list = new ArrayList<>();
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

    public static void hideKeyboard(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearFocus(EditText editText) {
        editText.setFocusableInTouchMode(false);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
    }

    public static String fullUrl(String url) {
        if (url.startsWith(Statics.http) || url.startsWith(Statics.https))
            return url;
        else return Statics.http + url;
    }
}
