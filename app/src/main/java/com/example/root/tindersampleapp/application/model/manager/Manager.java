package com.example.root.tindersampleapp.application.model.manager;

import com.example.root.tindersampleapp.application.model.listener.OnAPIResponseListener;
import com.example.root.tindersampleapp.application.model.parser.ApiParser;

import java.io.File;
import java.util.Map;

/**
 * This main manager class. Every module manager must extend this class, to use its common functionality.
 * Like make webservice call using GET POST. and return data from API in AddData dataType. you can also write
 * pojo OR Bean objects to internal file system using Serialization technique of JAVA.
 */
public abstract class Manager implements WebServiceRules {

    private WebService webService;

    /**
     * We must use this constructor if we want to use WebService feature of manager.
     *
     * @param apiParser
     * @param onApiResponseListener
     */
    public Manager(ApiParser apiParser, OnAPIResponseListener onApiResponseListener) {
        this();
        webService = new WebService(apiParser, onApiResponseListener);
    }

    public Manager() {
    }

    @Override
    public void getData(String apiUrl, Map<String, String> requestHeader, String requestTag) {
        if (webService == null) {
            throw new IllegalStateException(WebService.WEB_SERVICE_INITIALISATION_EXCEPTION_MESSAGE);
        } else {
            webService.getData(apiUrl, requestHeader, requestTag);
        }
    }

    /**
     * This method will make POST Request on WebAPI.
     *
     * @param apiUrl
     * @param requestHeader
     * @param postParams
     * @param requestTag
     */
    @Override
    public void postData(String apiUrl, Map<String, String> requestHeader, Map<String, String> postParams, String requestTag) {
        if (webService == null) {
            throw new IllegalStateException(WebService.WEB_SERVICE_INITIALISATION_EXCEPTION_MESSAGE);
        } else {
            webService.postData(apiUrl, requestHeader, postParams, requestTag);
        }
    }

    /**
     * This method will make POST multipart request on WebAPI and will upload file over server.
     *
     * @param apiUrl
     * @param requestHeader
     * @param postParams
     * @param requestTag
     */
    @Override
    public void uploadFiles(String apiUrl, Map<String, String> requestHeader, Map<String, String> postParams, Map<String, File> fileInputParams, String requestTag) {
        if (webService == null) {
            throw new IllegalStateException(WebService.WEB_SERVICE_INITIALISATION_EXCEPTION_MESSAGE);
        } else {
            webService.uploadFiles(apiUrl, requestHeader, postParams, fileInputParams, requestTag);
        }
    }

    /**
     * You may cancel any request any time using its RequestTag.
     *
     * @param requestTag
     */
    @Override
    public void cancelRequestByTag(String requestTag) {
        if (webService == null) {
            throw new IllegalStateException(WebService.WEB_SERVICE_INITIALISATION_EXCEPTION_MESSAGE);
        } else {
            webService.cancelRequestByTag(requestTag);
        }
    }

    public void initWebService(ApiParser apiParser, OnAPIResponseListener onApiResponseListener) {
        webService = new WebService(apiParser, onApiResponseListener);
    }

    public void setShouldCacheTheResponse(boolean shouldCacheTheResponse) {
        if (webService != null) {
            webService.setShouldCacheTheResponse(shouldCacheTheResponse);
        }
    }

}
