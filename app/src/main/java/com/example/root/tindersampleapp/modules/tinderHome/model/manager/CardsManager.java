package com.example.root.tindersampleapp.modules.tinderHome.model.manager;

import com.example.root.tindersampleapp.application.model.listener.OnAPIResponseListener;
import com.example.root.tindersampleapp.application.model.manager.Manager;
import com.example.root.tindersampleapp.application.model.parser.ApiParser;
import com.example.root.tindersampleapp.modules.constants.URLConstant;

import java.util.HashMap;

public class CardsManager extends Manager {

    public CardsManager(ApiParser apiParser, OnAPIResponseListener onApiResponseListener) {
        super(apiParser, onApiResponseListener);
    }

    public void fetchCardsFromNetwork(String requestTag) {
        getData(URLConstant.FETCH_CARDS_DATA_URL, new HashMap<String, String>(), requestTag);
    }
}
