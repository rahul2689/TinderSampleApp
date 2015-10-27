package com.example.root.tindersampleapp.modules.tinderHome.controller;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.tindersampleapp.R;
import com.example.root.tindersampleapp.application.controller.BaseActivity;
import com.example.root.tindersampleapp.application.model.data.AppData;
import com.example.root.tindersampleapp.application.model.listener.OnAPIResponseListener;
import com.example.root.tindersampleapp.modules.constants.RequestTagConstant;
import com.example.root.tindersampleapp.modules.tinderHome.model.data.Products;
import com.example.root.tindersampleapp.modules.tinderHome.model.data.ProductsData;
import com.example.root.tindersampleapp.modules.tinderHome.model.manager.CardsManager;
import com.example.root.tindersampleapp.modules.tinderHome.model.parser.CardsParser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;


public class TinderCardsActivity extends BaseActivity {

    public static CardsAdapter sCardsAdapter;
    public static ViewHolder sViewHolder;
    private ArrayList<ProductsData> mProductDataList;
    private SwipeCardsAdapterView mCardsContainer;
    private ImageView mRefreshDataIv;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinder_cards);
        mRefreshDataIv = (ImageView) findViewById(R.id.iv_cards_activity_refresh);
        mCardsContainer = (SwipeCardsAdapterView) findViewById(R.id.cards_container);
        //get intent value
        mProductDataList = (ArrayList<ProductsData>) getIntent().getSerializableExtra(SplashActivity.PRODUCT_LIST_KEY);
        if(mProductDataList !=null){
            sCardsAdapter = new CardsAdapter(mProductDataList, TinderCardsActivity.this);
        }else{
            mProductDataList = new ArrayList<>();
            sCardsAdapter = new CardsAdapter(mProductDataList, TinderCardsActivity.this);
        }

        mCardsContainer.setAdapter(sCardsAdapter);
        mCardsContainer.setCardsListener(mCardsListener);

        // Optionally add an OnItemClickListener
        mCardsContainer.setOnItemClickListener(new SwipeCardsAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                View view = mCardsContainer.getSelectedView();
                view.findViewById(R.id.fl_cards_adapter_bg).setAlpha(0);
                sCardsAdapter.notifyDataSetChanged();
            }
        });

    }


    public void refreshData() {
        mRefreshDataIv.setVisibility(View.VISIBLE);
        mRefreshDataIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                mRefreshDataIv.setVisibility(View.INVISIBLE);
                CardsManager cardsManager = new CardsManager(new CardsParser(), onAPIResponseListener);
                cardsManager.fetchCardsFromNetwork(RequestTagConstant.FETCH_PRODUCTS_DATA_TAG);
            }
        });
    }

    private SwipeCardsAdapterView.onCardsListener mCardsListener = new SwipeCardsAdapterView.onCardsListener() {
        @Override
        public void removeFirstObjectInAdapter() {

        }

        @Override
        public void onLeftCardExit(Object dataObject) {
            mProductDataList.remove(0);
            sCardsAdapter.notifyDataSetChanged();
            if(mProductDataList.size() == 0){
                refreshData();
            }
        }

        @Override
        public void onRightCardExit(Object dataObject) {
            mProductDataList.remove(0);
            sCardsAdapter.notifyDataSetChanged();
            if(mProductDataList.size() == 0){
                refreshData();
            }
        }

        @Override
        public void onAdapterAboutToEmpty(int itemsInAdapter) {

        }

        @Override
        public void onScroll(float scrollProgressPercent) {

            View view = mCardsContainer.getSelectedView();
            view.findViewById(R.id.fl_cards_adapter_bg).setAlpha(0);
            view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
            view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
        }
    };

    public static void removeBackground() {
        sViewHolder.sBackground.setVisibility(View.GONE);
        sCardsAdapter.notifyDataSetChanged();
    }

    private OnAPIResponseListener onAPIResponseListener = new OnAPIResponseListener() {
        @Override
        public void onAPIResponse(AppData appData, String requestTag) {
            hideProgressBar();
            if(appData != null){
                if (appData instanceof Products){
                    Products products = (Products) appData;
                    mProductDataList = products.getProductsDataList();
                    sCardsAdapter = new CardsAdapter(mProductDataList);
                    mCardsContainer.setAdapter(sCardsAdapter);
                    sCardsAdapter.notifyDataSetChanged();
                }
            }else{
                Toast.makeText(TinderCardsActivity.this, "I am inside else and data is null" , Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onInternalServerError(String message, String requestTag) {

        }

        @Override
        public void onAPIParsingException(JSONException e, String requestTag) {

        }
    };

    private void showProgressBar() {
        mDialog = new Dialog(TinderCardsActivity.this, R.style.CustomDialogTheme);
        mDialog.setContentView(R.layout.dialog_loader);
        mDialog.setCancelable(false);
        if (null != TinderCardsActivity.this && !TinderCardsActivity.this.isFinishing()) {
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

    public static class ViewHolder {
        public static FrameLayout sBackground;
        public TextView mProductDescTv;
        public ImageView mProductIv;

        public ViewHolder(View rowView) {
            mProductDescTv = (TextView) rowView.findViewById(R.id.tv_cards_adapter_product_desc);
            sBackground = (FrameLayout) rowView.findViewById(R.id.fl_cards_adapter_bg);
            mProductIv = (ImageView) rowView.findViewById(R.id.iv_cards_adapter_product);
        }
    }

    public class CardsAdapter extends BaseAdapter {
        private ArrayList<ProductsData> mProductList;
        private Context mContext;

        private CardsAdapter(ArrayList<ProductsData> productsDataList, Context context) {
            this.mProductList = productsDataList;
            this.mContext = context;
        }

        public CardsAdapter(ArrayList<ProductsData> productDataList) {
            this.mProductList = productDataList;
        }


        @Override
        public int getCount() {
            if(mProductList.size() == 0){
                Toast.makeText(TinderCardsActivity.this, "No more cards left -- Please refresh to load new data", Toast.LENGTH_SHORT).show();
            }
            return mProductList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.adapter_cards_view, parent, false);
                sViewHolder = new ViewHolder(rowView);
                rowView.setTag(sViewHolder);
            } else {
                sViewHolder = (ViewHolder) convertView.getTag();
            }
            sViewHolder.mProductDescTv.setText(mProductList.get(position).getProductContent());
            //Using Picasso for setting image bitmap
            Picasso.with(TinderCardsActivity.this).load(mProductList.get(position).getMedImgUrl()).into(sViewHolder.mProductIv);
            return rowView;
        }
    }
}
