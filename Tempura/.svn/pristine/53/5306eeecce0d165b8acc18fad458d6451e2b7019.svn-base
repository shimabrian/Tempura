package ok.ks.tempura;

import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class MyLocationManager implements LocationListener {

    private final static String TAG = ".::MyLocationManager::.";
    private Context _Context;
    private List<String> providers;
    private LocationManager _LocationManager;
    private Location _Location = new Location("");
    private Criteria criteria;
    private MyLocationManagerListener _MyLocationManagerListener;

    /**
     * コンストラクタ
     * @param context
     */
    public MyLocationManager(Context context) {
        if (MyData.DEBUG) Log.i(TAG, "start to get LocationManager");
        _Context = context;
        try
        {
            _LocationManager = (LocationManager) _Context.getSystemService(Context.LOCATION_SERVICE);
            if (_LocationManager == null) return;
            _LocationManager.removeUpdates(this);
            
            criteria = new Criteria();
            criteria.setBearingRequired(false);     // 方位不要
            criteria.setSpeedRequired(false);       // 速度不要
            criteria.setAltitudeRequired(false);    // 高度不要
            //provider = _LocationManager.getBestProvider(criteria, true);
            providers = _LocationManager.getProviders(true);
            if (providers == null)
            {
                // トースト表示
                Toast.makeText(_Context, "Temperature sensor is not equipped. Set <<GPS>> to be available.", Toast.LENGTH_LONG).show();
                // GPS設定画面へ
                enableLocationSettings();
                // アプリは終了
                ((MainActivity)_Context).finish();
//                // 位置情報が有効になっていない場合は、Google Maps アプリライクな [現在地機能を改善] ダイアログを起動します。
//                new AlertDialog.Builder(_Context)
//                    .setTitle("- Location Settings -")
//                    .setMessage("Should to be set on <<GPS>> or <<wifi>> settings. :D")
//                    .setPositiveButton("settings", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(final DialogInterface dialog, final int which) {
//                            // 端末の位置情報設定画面へ遷移
//                            try {
//                                _Context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                            } catch (final ActivityNotFoundException e) {
//                                // 位置情報設定画面がない糞端末の場合は、仕方ないので何もしない
//                            }
//                        }
//                    })
//                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                        @Override public void onClick(final DialogInterface dialog, final int which) {} // 何も行わない
//                    })
//                    .create()
//                    .show();
//                // アプリは終了
//                //((MainActivity)_Context).finish();
                return;
            }
            
//            final boolean gpsEnabled = _LocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//            if (!gpsEnabled)
//            {
//                // トースト表示
//                Toast.makeText(_Context, "Temperature sensor is not equipped. Set <<GPS>> to be available.", Toast.LENGTH_LONG).show();
//                // GPS設定画面へ
//                enableLocationSettings();
//                // アプリは終了
//                ((MainActivity)_Context).finish();
//                return;
//            }
            Log.i(TAG, "Set LocationManage --- OK!");
        }
        catch (Exception ex)
        {
            Log.e("MyLocationManager", ex.getMessage());
        }
    }

    /**
     * Eventリスナーを設定
     * 
     * @param listener
     */
    public void setEventListener(MyLocationManagerListener listener) {
        this._MyLocationManagerListener = listener;
    }
    
    /**
     * Eventリスナーを削除
     * 
     * @param listener
     */
    public void removeEventListener() {
        this._MyLocationManagerListener = null;
    }

    /**
     * GPS設定画面へ遷移させる。
     */
    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        _Context.startActivity(settingsIntent);
    }
    
    /**
     * アクティビティのスタートイベントで呼ぶ
     */
    public void onStart()
    {
        if (MyData.DEBUG) Log.i(TAG, "onStart");
//        try
//        {
//            if (_LocationManager != null)
//            {
//                // 通知間隔を設定
//                _LocationManager.requestLocationUpdates(providers, 0, 0, this);
//            }
//        }
//        catch (Exception ex)
//        {
//            Log.e(TAG, ex.getMessage());
//        }
    }

    /**
     * アクティビティのレジュームイベントで呼ぶ
     */
    public void onResume()
    {
        if (MyData.DEBUG) Log.i(TAG, "onResume");
        try
        {
            if (_LocationManager != null)
            {
//                // 最後に取得できた位置情報が5分以内のものであれば有効とします。
//                final Location lastKnownLocation = _LocationManager.getLastKnownLocation(provider);
//                if (lastKnownLocation != null && (new Date().getTime() - lastKnownLocation.getTime()) <= (5 * 60 * 1000L)) {
//                    _Location = lastKnownLocation;
//                    _MyLocationManagerListener.UpdatedLocation(_Location);
//                    if (MyData.DEBUG) Log.i(TAG, "Latitude:" + _Location.getLatitude() + ", Longitude:" + _Location.getLongitude());
//                }
//                else
//                {
                    boolean success = false;
                    for (String provider : providers) {
                        final Location lastKnownLocation = _LocationManager.getLastKnownLocation(provider);
                        if (lastKnownLocation != null && (new Date().getTime() - lastKnownLocation.getTime()) <= (5 * 60 * 1000L)) {
                            _Location = lastKnownLocation;
                            _MyLocationManagerListener.UpdatedLocation(_Location);
                            if (MyData.DEBUG) Log.i(TAG, "Latitude:" + _Location.getLatitude() + ", Longitude:" + _Location.getLongitude());
                            success = true;
                            break;
                        }
                    }
                    if (!success) {
                        for (String provider : providers) {
                            // 通知間隔を設定
                            _LocationManager.requestLocationUpdates(provider, 0, 0, this);
                        }
                    }
                    
                    
//                }
//                // 最新の位置情報を取得
//                _Location = _LocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                _MyLocationManagerListener.UpdatedLocation(_Location);
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    /**
     * アクティビティのポーズイベントで呼ぶ
     */
    public void onPause()
    {
        // 重要：requestLocationUpdatesしたままアプリを終了すると挙動がおかしくなる。
        if (MyData.DEBUG) Log.i(TAG, "onPause");
        if (_LocationManager != null)
            _LocationManager.removeUpdates(this);
    }
    
    /**
     * アクティビティのスタートイベントで呼ぶ
     */
    public void onStop()
    {
        if (MyData.DEBUG) Log.i(TAG, "onStop");
        if (_LocationManager != null)
            _LocationManager.removeUpdates(this);
    }

    /**
     * アクティビティの破棄イベントで呼ぶ
     */
    public void onDestroy()
    {
        // 重要：requestLocationUpdatesしたままアプリを終了すると挙動がおかしくなる。
        if (_LocationManager != null)
            _LocationManager.removeUpdates(this);
        // イベントリスナーの削除
        removeEventListener();
        // 最後にマネージャを削除
        _LocationManager = null;
    }

    /**
     * オーバーライドメソッド
     */
    @Override
    public void onLocationChanged(Location location) {
        // TODO 自動生成されたメソッド・スタブ
        if (_MyLocationManagerListener != null) {
            if (MyData.DEBUG) Log.i(TAG, "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
            _MyLocationManagerListener.UpdatedLocation(location);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO 自動生成されたメソッド・スタブ
        
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO 自動生成されたメソッド・スタブ
        
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO 自動生成されたメソッド・スタブ
        
    }
}
