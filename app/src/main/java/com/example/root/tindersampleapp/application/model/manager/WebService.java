package com.example.root.tindersampleapp.application.model.manager;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.MultiPartRequest;
import com.example.root.tindersampleapp.application.FrameworkApplication;
import com.example.root.tindersampleapp.application.exception.AppErrorCodes;
import com.example.root.tindersampleapp.application.model.data.AppData;
import com.example.root.tindersampleapp.application.model.listener.OnAPIResponseListener;
import com.example.root.tindersampleapp.application.model.listener.ParsingExceptionCallback;
import com.example.root.tindersampleapp.application.model.parser.ApiParser;
import com.example.root.tindersampleapp.application.utilities.Utils;
import com.example.root.tindersampleapp.modules.constants.JSONConstant;
import com.example.root.tindersampleapp.modules.constants.MessageConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

/**
 * This class helps user to make GET and POST request on REST API.
 */
class WebService implements WebServiceRules, ParsingExceptionCallback {

    private Context context;
    private ApiParser apiParser;
    private OnAPIResponseListener onApiResponseListener;
    private String requestTag;
    public static final String WEB_SERVICE_INITIALISATION_EXCEPTION_MESSAGE = "Initialize webService first, using another constructor of manager";
    private String TAG  = WebService.class.getSimpleName();
    private boolean shouldCacheTheResponse = false;
    /**
     * This is public constructor of WebService.
     * @param apiParser
     * @param onApiResponseListener
     */
    public WebService(ApiParser apiParser, OnAPIResponseListener onApiResponseListener) {
        this.context = FrameworkApplication.getInstance().getApplicationContext();
        this.apiParser = apiParser;
        this.onApiResponseListener = onApiResponseListener;
    }

    /**
     * This method will make GET Request on WebAPI.
     * @param apiUrl
     * @param requestHeader
     * @param requestTag
     */
    @Override
    public void  getData(String apiUrl, final Map<String, String> requestHeader, String requestTag){
        Log.e(TAG + " get " + requestTag + " URL", " : " + apiUrl);
        this.requestTag = requestTag;
        AppDataRequest stringRequest = new AppDataRequest(Request.Method.GET, apiUrl, responseOk, responseNotOk, apiParser, requestTag, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(requestHeader != null) {
                    return requestHeader;
                }else {
                    return Utils.getApiRequestHeader(context);
                }
            }
        };
        //Setting should not be cached, now json response will not be cached.
        stringRequest.setShouldCache(shouldCacheTheResponse);
        // Adding request to request queue
        if(Utils.isInternetConnected(context)) {
            FrameworkApplication.getInstance().addToRequestQueue(stringRequest, requestTag);
        }else {
            onApiResponseListener.onInternalServerError(MessageConstants.NO_NETWORK_MESSAGE, requestTag);
        }
    }

    boolean bypassSessionCheck = false;
    /**
     *
     * @param apiUrl
     * @param requestHeader
     * @param postParams
     * @param requestTag
     */
    @Override
    public void postData(String apiUrl, final Map<String, String> requestHeader,final Map<String, String> postParams, String requestTag){

        if (apiUrl.contains("/mnapi/json/app_fb_process")) {
            bypassSessionCheck = true;
        }

        if (postParams != null)
            Log.e(TAG + " post " + requestTag + " URL", " : " + apiUrl + " params : " + postParams.toString());
        else
            Log.e(TAG + " post " + requestTag + " URL", " : " + apiUrl);

        this.requestTag = requestTag;
        AppDataRequest stringRequest = new AppDataRequest(Request.Method.POST, apiUrl, responseOk, responseNotOk, apiParser, requestTag, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(requestHeader != null) {
                    return requestHeader;
                }else {
                    return Utils.getApiRequestHeader(context);
                }
            }
            @Override
            protected Map<String, String> getParams() {
                return postParams;
            }
        };
        //Setting should not be cached, now json response will not be cached.
        stringRequest.setShouldCache(shouldCacheTheResponse);
        // Adding request to request queue
        if(Utils.isInternetConnected(context)) {
            FrameworkApplication.getInstance().addToRequestQueue(stringRequest, requestTag);
        }else {
            onApiResponseListener.onInternalServerError(MessageConstants.NO_NETWORK_MESSAGE, requestTag);
        }
    }

    /**
     *
     * @param apiUrl
     * @param requestHeader
     * @param postParams
     * @param requestTag
     */
    @Override
    public void uploadFiles(String apiUrl, final Map<String, String> requestHeader, Map<String, String> postParams, Map<String, File> fileInputParams, String requestTag){
        Log.e(TAG + " post multipart " + requestTag + " URL", " : " + apiUrl + " params : " + postParams.toString());
        this.requestTag = requestTag;
        MultiPartRequest uploadRequest = new MultiPartRequest(apiUrl, responseOkOld, responseNotOk, fileInputParams, postParams) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(requestHeader != null) {
                    return requestHeader;
                }else {
                    return Utils.getApiRequestHeader(context);
                }
            }
        };
        //Setting should not be cached, now json response will not be cached.
        uploadRequest.setShouldCache(shouldCacheTheResponse);
        // Adding request to request queue
        if(Utils.isInternetConnected(context)) {
            FrameworkApplication.getInstance().addToRequestQueue(uploadRequest, requestTag);
        }else {
            onApiResponseListener.onInternalServerError(MessageConstants.NO_NETWORK_MESSAGE, requestTag);
        }
    }

    /**
     *
     * @param requestTag
     */
    @Override
    public void cancelRequestByTag(String requestTag){
        FrameworkApplication.getInstance().cancelPendingRequests(requestTag);
    }

    private Response.Listener<AppData> responseOk = new Response.Listener<AppData>() {

        /**
         * Called when a response is received.
         *
         * @param response
         */
        @Override
        public void onResponse(AppData response) {
            try {
                onApiResponseListener.onAPIResponse(response, requestTag);
            }catch (Exception e){
                onApiResponseListener.onAPIResponse(null, requestTag);
            }
        }
    };
    private Response.Listener<String> responseOkOld = new Response.Listener<String>() {

        /**
         * Called when a response is received.
         *
         * @param response
         */
        @Override
        public void onResponse(String response) {
            try {
                Log.e(TAG + " " + requestTag + " Response", " : " + response);
                AppData appData;
                if (isSessionExpired(response)) {
                    appData = new AppData();
                    appData.setErrorCode(AppErrorCodes.LOG_OUT_USER);
                    appData.setErrorMessage("Session expired !!");
                } else {
                    appData = apiParser.parseApiResponse(response, requestTag);
                }
                onApiResponseListener.onAPIResponse(appData, requestTag);
            } catch (JSONException e) {
                onApiResponseListener.onAPIParsingException(e, requestTag);
            }catch (Exception e){
                onApiResponseListener.onAPIResponse(null, requestTag);
            }
        }
    };

    private boolean isSessionExpired(String response) throws JSONException {
        boolean sessionExpired = false;
        if (bypassSessionCheck) {
            return sessionExpired;
        }
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.getInt(JSONConstant.STATUS) == 0) {
            JSONObject errorJsonObject = jsonObject.getJSONObject(JSONConstant.ERROR);
            if (errorJsonObject.getInt(JSONConstant.CODE) == 140011) {
                sessionExpired = true;
            }
        }
        return sessionExpired;
    }

    @Override
    public void onApiParsingException(JSONException e) {
        onApiResponseListener.onAPIParsingException(e, requestTag);
    }

    private Response.ErrorListener responseNotOk = new Response.ErrorListener() {

        /**
         * Callback method that an error has been occurred with the
         * provided error code and optional user-readable message.
         *
         * @param error
         */
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG + " " + requestTag + " Server Error", " : " + error.getMessage());
            if (onApiResponseListener == null)
                return;
            if(error.getMessage() == null){
                onApiResponseListener.onInternalServerError("Internal Server Error" , requestTag);
            }else {
                onApiResponseListener.onInternalServerError("" + error.getMessage() , requestTag);
            }

        }
    };

    public void setShouldCacheTheResponse(boolean shouldCacheTheResponse) {
        this.shouldCacheTheResponse = shouldCacheTheResponse;
    }
}
