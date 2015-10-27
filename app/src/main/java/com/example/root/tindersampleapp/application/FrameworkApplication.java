package com.example.root.tindersampleapp.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.root.tindersampleapp.application.utilities.LruBitmapCache;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

public class FrameworkApplication extends Application {

    private static FrameworkApplication sInstance;
    public static final String TAG = FrameworkApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private int ONE_MINUTE = 60000;
    private ImageLoader mImageLoader;
    private LruBitmapCache mLruBitmapCache;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Boolean isDebuggingEnabled = false;
        try {
            isDebuggingEnabled = isDebuggable(this);
        } catch (Exception e) {
            isDebuggingEnabled = false;
        }
    }

    private boolean isDebuggable(Context ctx) {
        boolean debuggable = false;
        PackageManager pm = ctx.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(ctx.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags &= ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {

        }

        return debuggable;
    }

    public static FrameworkApplication getInstance() {
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // If you need to directly manipulate cookies later on, hold onto this client
            // object as it gives you access to the Cookie Store
            // reference --- http://stackoverflow.com/questions/16680701/using-cookies-with-android-volley-library
          /* DefaultHttpClient httpClient = new DefaultHttpClient();
            CookieStore cookieStore = new BasicCookieStore();
            httpClient.setCookieStore(cookieStore);
            HttpStack httpStack = new HttpClientStack(httpClient);
           mRequestQueue = Volley.newRequestQueue(getApplicationContext(), httpStack);*/

            DefaultHttpClient mDefaultHttpClient = new DefaultHttpClient();
            final ClientConnectionManager mClientConnectionManager = mDefaultHttpClient.getConnectionManager();
            final HttpParams mHttpParams = mDefaultHttpClient.getParams();
            final ThreadSafeClientConnManager mThreadSafeClientConnManager = new ThreadSafeClientConnManager( mHttpParams, mClientConnectionManager.getSchemeRegistry() );
            mDefaultHttpClient = new DefaultHttpClient( mThreadSafeClientConnManager, mHttpParams );
            final HttpStack httpStack = new HttpClientStack( mDefaultHttpClient );
            this.mRequestQueue = Volley.newRequestQueue(this.getApplicationContext(), httpStack);

            // Default Implementation.
           /* mRequestQueue = Volley.newRequestQueue(getApplicationContext());*/
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(
                ONE_MINUTE,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * You'll need this in your class to get the helper from the manager once per class.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        /*
		 * You'll need this in your class to release the helper when done.
		 */
    }
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }
    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }
}
