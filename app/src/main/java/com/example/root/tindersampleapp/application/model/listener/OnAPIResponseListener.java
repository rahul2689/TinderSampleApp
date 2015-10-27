package com.example.root.tindersampleapp.application.model.listener;


import com.example.root.tindersampleapp.application.model.data.AppData;

import org.json.JSONException;

public interface OnAPIResponseListener {
    /**
     *  When we get HTTP 200 as response Code.
     * @param appData
     * @param requestTag
     */
    void onAPIResponse(AppData appData, String requestTag);
    /**
     * When we get other then HTTP 200 as response Code.
     * @param message
     * @param requestTag
     */
    void onInternalServerError(String message, String requestTag);

    /**
     * When api get broken or response structure changes.
     * @param e
     * @param requestTag
     */
    void onAPIParsingException(JSONException e, String requestTag);
}
