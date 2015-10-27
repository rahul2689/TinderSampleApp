package com.example.root.tindersampleapp.modules.tinderHome.model.parser;

import com.example.root.tindersampleapp.application.model.data.AppData;
import com.example.root.tindersampleapp.application.model.parser.ApiParser;
import com.example.root.tindersampleapp.modules.tinderHome.model.data.Products;
import com.example.root.tindersampleapp.modules.tinderHome.model.data.ProductsData;
import com.example.root.tindersampleapp.modules.constants.RequestTagConstant;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CardsParser implements ApiParser {
    /**
     * Parse all responses.
     * @param apiResponse
     * @param requestTag
     * @return
     * @throws JSONException
     */
    @Override
    public AppData parseApiResponse(String apiResponse, String requestTag) throws JSONException {
        if(requestTag.equals(RequestTagConstant.FETCH_PRODUCTS_DATA_TAG)) {
            return parseProductsCardResponse(apiResponse);
        }else {
            return new AppData();
        }
    }

    private Products parseProductsCardResponse(String apiResponse)  throws JSONException {
        ArrayList<ProductsData> productsDataList = new ArrayList<>();
        Products products = new Products();
        if(apiResponse !=null){
            JSONArray jsonArray = new JSONArray(apiResponse);
            Gson gson = new Gson();
            if(jsonArray != null && jsonArray.length() > 0){
                for(int i=0; i<jsonArray.length(); i++ ){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject != null){
                        ProductsData productsData = gson.fromJson(jsonObject.toString(), ProductsData.class);
                        productsDataList.add(productsData);
                    }
                }
            }
        }

        if(productsDataList != null && productsDataList.size() > 0){
                products.setProductsDataList(productsDataList);
        }

        return products;
    }
}
