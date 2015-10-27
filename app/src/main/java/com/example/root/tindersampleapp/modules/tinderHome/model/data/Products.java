package com.example.root.tindersampleapp.modules.tinderHome.model.data;

import com.example.root.tindersampleapp.application.model.data.AppData;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 27/10/15.
 */
public class Products extends AppData implements Serializable{

    private ArrayList<ProductsData> productsDataList ;

    public Products(){
        productsDataList = new ArrayList<>();
    }

    public ArrayList<ProductsData> getProductsDataList() {
        return productsDataList;
    }

    public void setProductsDataList(ArrayList<ProductsData> productsDataList) {
        this.productsDataList = productsDataList;
    }
}
