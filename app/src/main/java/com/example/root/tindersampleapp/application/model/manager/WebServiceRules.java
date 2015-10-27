package com.example.root.tindersampleapp.application.model.manager;

import java.io.File;
import java.util.Map;
public interface WebServiceRules {
    void getData(String apiUrl, final Map<String, String> requestHeader, String requestTag);
    void postData(String apiUrl, final Map<String, String> requestHeader, final Map<String, String> postParams, String requestTag);
    void uploadFiles(String apiUrl, final Map<String, String> requestHeader, Map<String, String> postParams, Map<String, File> fileInputParams, String requestTag);
    void cancelRequestByTag(String requestTag);
}
