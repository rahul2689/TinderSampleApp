package com.example.root.tindersampleapp.application.model.manager;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.root.tindersampleapp.application.exception.AppErrorCodes;
import com.example.root.tindersampleapp.application.model.data.AppData;
import com.example.root.tindersampleapp.application.model.listener.ParsingExceptionCallback;
import com.example.root.tindersampleapp.application.model.parser.ApiParser;
import com.example.root.tindersampleapp.modules.constants.JSONConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class AppDataRequest extends Request<AppData> {

    private Response.Listener<AppData> responseOk;
    private ApiParser apiParser;
    private String TAG  = WebService.class.getSimpleName();
    private String requestTag;
    private ParsingExceptionCallback parsingExceptionCallback;
    /**
     * Creates a new request with the given method (one of the values from {@link Method}),
     * URL, and error listener.  Note that the normal response listener is not provided here as
     * delivery of responses is provided by subclasses, who have a better idea of how to deliver
     * an already-parsed response.
     *
     * @param method
     * @param url
     * @param listener
     */
    public AppDataRequest(int method, String url, Response.Listener<AppData> responseOk,
                          Response.ErrorListener listener, ApiParser apiParser,
                          String requestTag, ParsingExceptionCallback parsingExceptionCallback) {
        super(method, url, listener);
        this.responseOk = responseOk;
        this.apiParser = apiParser;
        this.requestTag = requestTag;
        this.parsingExceptionCallback = parsingExceptionCallback;
    }

    /**
     * Subclasses must implement this to parse the raw network response
     * and return an appropriate response type. This method will be
     * called from a worker thread.  The response will not be delivered
     * if you return null.
     *
     * @param response Response from the network
     * @return The parsed response, or null in the case of an error
     */
    @Override
    protected Response<AppData> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        Log.e(TAG + " " + requestTag + " Response", " : " + parsed);
        AppData appData = null;
        try {
            /*if (isSessionExpired(parsed)) {
                appData = new AppData();
                appData.setErrorCode(AppErrorCodes.LOG_OUT_USER);
                appData.setErrorMessage("Session expired !!");
            } else {*/
                appData = apiParser.parseApiResponse(parsed, requestTag);
           // }
        } catch (JSONException e) {
            parsingExceptionCallback.onApiParsingException(e);
        }
        return Response.success(appData, HttpHeaderParser.parseCacheHeaders(response));
    }

    private boolean isSessionExpired(String response) throws JSONException {
        boolean sessionExpired = false;
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.getInt(JSONConstant.STATUS) == 0) {
            JSONObject errorJsonObject = jsonObject.getJSONObject(JSONConstant.ERROR);
            if (errorJsonObject.getInt(JSONConstant.CODE) == 140011) {
                sessionExpired = true;
            }
        }
        return sessionExpired;
    }

    /**
     * Subclasses must implement this to perform delivery of the parsed
     * response to their listeners.  The given response is guaranteed to
     * be non-null; responses that fail to parse are not delivered.
     *
     * @param response The parsed response returned by
     *                 {@link #parseNetworkResponse(NetworkResponse)}
     */
    @Override
    protected void deliverResponse(AppData response) {
        this.responseOk.onResponse(response);
    }
}
