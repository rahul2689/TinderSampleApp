package com.example.root.tindersampleapp.modules.tinderHome.controller;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.root.tindersampleapp.R;
import com.example.root.tindersampleapp.application.controller.BaseActivity;
import com.example.root.tindersampleapp.application.model.data.AppData;
import com.example.root.tindersampleapp.application.model.listener.OnAPIResponseListener;
import com.example.root.tindersampleapp.modules.tinderHome.model.data.Products;
import com.example.root.tindersampleapp.modules.tinderHome.model.data.ProductsData;
import com.example.root.tindersampleapp.modules.tinderHome.model.manager.CardsManager;
import com.example.root.tindersampleapp.modules.tinderHome.model.parser.CardsParser;
import com.example.root.tindersampleapp.modules.constants.RequestTagConstant;

import org.json.JSONException;

import java.util.ArrayList;


public class SplashActivity extends BaseActivity implements OnAPIResponseListener {

    public static final String PRODUCT_LIST_KEY = "productList";
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        showProgressBar();

        //call to fetch data from network --- here this keyword refers to the instance of OnApiListener implemented in this class
        CardsManager cardsManager = new CardsManager(new CardsParser(), this);
        cardsManager.fetchCardsFromNetwork(RequestTagConstant.FETCH_PRODUCTS_DATA_TAG);
    }


    private void showProgressBar() {
        mDialog = new Dialog(SplashActivity.this, R.style.CustomDialogTheme);
        mDialog.setContentView(R.layout.dialog_loader);
        mDialog.setCancelable(false);
        if (null != SplashActivity.this && !SplashActivity.this.isFinishing()) {
            mDialog.show();
        }
    }

    private void hideProgressBar() {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onAPIResponse(AppData appData, String requestTag) {
        hideProgressBar();
        if(appData != null){
            ArrayList<ProductsData> productsDataList = new ArrayList<>();
            if (appData instanceof Products){
               Products products = (Products) appData;
               productsDataList = products.getProductsDataList();
               Intent intent = new Intent(SplashActivity.this, TinderCardsActivity.class);
               intent.putExtra(PRODUCT_LIST_KEY, productsDataList);
               startActivity(intent);
           }
        }else{
            Toast.makeText(SplashActivity.this, "I am inside else and data is null" , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInternalServerError(String message, String requestTag) {

    }

    @Override
    public void onAPIParsingException(JSONException e, String requestTag) {

    }
}
