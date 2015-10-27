package com.example.root.tindersampleapp.application.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.example.root.tindersampleapp.application.FrameworkApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
public class Utils {

    /**
     * Return package name or application Id with current app version.
     * @param context
     * @return Map - appId and appVersion
     */
    public static Map<String, String> getApiRequestHeader(Context context) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("appId", context.getPackageName());
        headers.put("appVersion", Utils.getAppVersion(context));
        return headers;
    }

    /**
     * Determine current version name of app
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return manager.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //Handle exception
        }
        return null;
    }

    /**
     * Return device unique Id.
     * @param context
     * @return String
     */
    public static String getDeviceId(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * Determine weather device is tablet or not.
     * @param context
     * @return true if device is Tablet false otherwise.
     */
    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    /**
     * Reads text file from assets folder.
     * @param context
     * @param fileName
     * @return String - content of file, or null if file does not exist.
     */
    public static String readFileFromAssetFolder(Context context, String fileName){
        String jsonString = null;
        //Create a InputStream to read the file into
        InputStream iS = null;
        //create a output stream to write the buffer into
        ByteArrayOutputStream oS = new ByteArrayOutputStream();

        try {
            //get the file as a stream
            iS = context.getResources().getAssets().open(fileName);
            //create a buffer that has the same size as the InputStream
            byte[] buffer = new byte[iS.available()];
            //read the text file as a stream, into the buffer
            iS.read(buffer);

            //write this buffer to the output stream
            oS.write(buffer);

        } catch (IOException e) {
        }finally {
            try {
                //Close the Input and Output streams
                oS.close();
                iS.close();
                //return the output stream as a String
                jsonString = oS.toString();
            } catch (Exception e) {

            }
        }
        return jsonString;
    }

    /**
     * By calling this method in OnCreate in any activity, it will hide keyboard.
     * @param activity
     */
    public static void hideKeyboardOnLoadingScreen(Activity activity){
        // Hide soft-keyboard:
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * By calling this method user can hide keyboard while leaving any activity.
     * @param activity
     * @param myEditText
     */
    public static void hideKeyboardOnLeavingScreen(Activity activity, EditText myEditText){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
    }

    /**
     * This method will show Soft keyBoard.
     * @param activity
     */
    public static void showKeyboard(Activity activity){
        // Show soft-keyboard:
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Will return darker color(int) as given factor.
     * @param color R.color.definedColor
     * @param factor 0.0 to 1.0
     * @return int color
     */
    public static int getDarkerColor (int color, float factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a,
                Math.max((int) (r * factor), 0),
                Math.max((int) (g * factor), 0),
                Math.max((int) (b * factor), 0));
    }


    public static boolean isInternetConnected(Context context) {
        if(context==null){
            context= FrameworkApplication.getInstance().getApplicationContext();
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Checking for WiFi
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        // Checking for Mobile Data Internet
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        // Checking for any other type of Network Connectivity
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }
}
