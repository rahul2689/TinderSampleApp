package com.example.root.tindersampleapp.application.model.listener;

import org.json.JSONException;

public interface ParsingExceptionCallback {
        void onApiParsingException(JSONException e);
    }