package com.example.root.tindersampleapp.application.controller;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.root.tindersampleapp.application.exception.AppErrorCodes;
import com.example.root.tindersampleapp.application.model.data.AppData;


/**
 * This is BaseActivity for all activities of app. Here we have defined some common operation
 * we usually perform like showToast and showProgress Dialog with proper message and openActivity
 * and openActivityForResults. This is very useful when we want to change whole app's activities.
 * For example from ActionBarActivity to FragmentActivity or Activity to ActionBarActivity.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;


    public BaseActivity(){

    }

    /**
     * Show progress dialog with passed message.
     * @param message
     */
    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.show();
    }


    /**
     * Hide progress dialog with proper handling of exception related to mContext leak.
     */
    public void hideProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {

        } finally {
            progressDialog = null;
        }
    }


    /**
     * Shows Toast message for long Time.
     * @param toastMessage
     */
    public void showLongToast(final String toastMessage) {
        if (toastMessage != null && toastMessage.length() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BaseActivity.this, toastMessage, Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    /**
     * Shows Toast message for short period of time.
     * @param toastMessage
     */
    public void showShortToast(final String toastMessage) {
        if (toastMessage != null && toastMessage.length() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BaseActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    /**
     * Shows SnackBar message for long Time.
     */
    public void showSnackBarLong(View view,String toastMessage){
        Snackbar snackbar = Snackbar.make(view, toastMessage, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackbar.show();
    }

    /**
     * Shows SnackBar message for short period of time.
     */
    public void showSnackBarShort(View view,String toastMessage){
        Snackbar snackbar = Snackbar.make(view, toastMessage, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackbar.show();
    }

    /**
     * open tagetActivity with passed bundle if we pass bundle as null it won't send bundle at all.
     * @param targetActivity
     * @param bundle
     */
    public void openActivity(Class targetActivity, Bundle bundle){
        Intent intent = new Intent(this, targetActivity);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    /**
     * open tagetActivity with passed bundle if we pass bundle as null it won't send bundle at all.
     * @param targetActivity
     * @param bundle
     */
    public void openActivityClearTask(Class targetActivity, Bundle bundle){
        Intent intent = new Intent(this, targetActivity).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * open tagetActivity with requestCode and passed bundle if we pass bundle as null it won't send bundle at all.
     * @param targetActivity
     * @param bundle
     * @param requestCode
     */
    public void openActivityForResult(Class targetActivity, Bundle bundle, int requestCode){
        Intent intent = new Intent(this, targetActivity);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    protected void setupToolbar(){
    }

    protected void showPopupMessage(String message, String title, String buttonMessage){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        if (title != null) {
            alertBuilder.setTitle(title);
        }
        if (message != null) {
            alertBuilder.setMessage(message);
        }
        if (buttonMessage == null) {
            buttonMessage = "OK";
        }
        alertBuilder.setPositiveButton(buttonMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertBuilder.show();
    }

    public void handleCommonErrors(AppData appData){

        if (appData != null) {
            switch (appData.getErrorCode()){
                case AppErrorCodes.SHOW_TOAST_MESSAGE:
                    showLongToast(appData.getErrorMessage());
                    break;
                case AppErrorCodes.DO_NOTHING:
                    break;
                case AppErrorCodes.SHOW_POPUP_MESSAGE:
                    if (appData.getErrorMessage() != null) {
                        showPopupMessage(appData.getErrorMessage(),"info","OK");
                    }
                    break;
                default:
                    showLongToast(appData.getErrorMessage());
            }
        }
    }
}
