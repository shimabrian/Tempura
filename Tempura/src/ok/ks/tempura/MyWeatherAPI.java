package ok.ks.tempura;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.content.Context;
import android.location.Location;
import android.util.Log;

public class MyWeatherAPI {

    /**
     * TAG
     */
    private static final String TAG = ".::MyWeatherAPI::.";

    /**
     * Bindするコンテキスト
     */
    private Context mContext;

    /**
     * Upload URL
     */
    //http://api.worldweatheronline.com/free/v1/weather.ashx?q=26.256406%2C+127.718339&format=json&num_of_days=1&key=6n7mpc5q82g8v3kda23jx5j7
    //private static final String BASE_URL = "http://api.worldweatheronline.com/free/v1/weather.ashx";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";//?lat=26.256439&lon=127.718357&cnt=1&mode=json";

    /**
     * API Key
     */
    public static final String API_KEY = "6n7mpc5q82g8v3kda23jx5j7";
    
    /**
     * Action ID of Upload
     */
    public static final int ACT_UPLOAD = 1;

    /**
     * Event Listener
     */
    private MyWeatherAPIListener mWeatherAPIListener;

    /**
     * コンストラクタ
     * @param context
     */
    public MyWeatherAPI(Context context) {
        this.mContext = context;
    }

    /**
     * 温度をWeatherAPIから取得
     */
    public void getTemperatureFromWeatherAPI(Location loc)
    {
        if (MyData.DEBUG) Log.d(TAG, "getTemperatureFromWeatherAPI");
        try
        {
            final String latitude = "" + loc.getLatitude();
            final String longitude = "" + loc.getLongitude();
            final String key[] = { "lat", "lon", "cnt", "mode" };
            final String value[] = { latitude, longitude, "1", "json" };
            this.sendData(key, value);
        }
        catch (Exception ex)
        {
            Log.e("getTemperatureFromWeatherAPI", ex.getMessage());
        }
    }

    /**
     * データ送信
     * @param key
     * @param value
     */
    public void sendData(String[] key, String[] value) {
        String url = BASE_URL;
        PostThread mPostThread = new PostThread(ACT_UPLOAD, url, key, value);
        mPostThread.start();
    }

    private class PostThread extends Thread {
        private String url;
        private int type;

        public PostThread(int type, String url, String[] key, String[] value) {
            this.url = url;
            this.type = type;
            
            this.url = this.url + "?";
            for (int i = 0; i < key.length; i++) {
                this.url = this.url + key[i] + "=" + value[i] + "&";
            }

        }

        public void run() {
            HttpClient mHttpClient = new DefaultHttpClient();

            try {
                HttpGet mHttpGet = new HttpGet(url);
                if (MyData.DEBUG) Log.i(TAG, "url:" + url);

                // Connect
                HttpResponse mResponse = mHttpClient.execute(mHttpGet);

                // Response Code
                int resCode = mResponse.getStatusLine().getStatusCode();
                // Response Type
                String resType = mResponse.getEntity().getContentType().getValue();
                // Response Value
                HttpEntity httpEntity = mResponse.getEntity();
                String resValue = EntityUtils.toString(httpEntity);

//                if (MyData.DEBUG) {
//                    Log.i(TAG, "resCode:" + resCode);
//                    Log.i(TAG, "resType:" + resType);
//                    Log.i(TAG, "resValue:" + resValue);
//                }

                // OK
                if (resCode == HttpStatus.SC_OK) {
                    mWeatherAPIListener.onLoad(type, resValue);
                }
                // NG
                else {
                    mWeatherAPIListener.onLoad(type, "-1");
                }

            } catch (IOException e) {
                // NG
                mWeatherAPIListener.onLoad(type, "-1");
            }
        }
    }

    /**
     * Eventリスナーを設定
     * 
     * @param listener
     */
    public void setEventListener(MyWeatherAPIListener listener) {
        this.mWeatherAPIListener = listener;
    }

}