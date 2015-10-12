package ok.ks.tempura;

import java.util.Calendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ライフサイクル
    onCreate()    Activityが生成された最初の１回だけ呼び出される
        ※重要※
        Activityが最初に起動するときに呼び出される。
        画面インターフェースの作成や、１度しか行わない最初の初期化処理を記述する。画面回りに関しては、作成するだけで、まだ触ってはいけない。
    onStart()   Activityが画面に表示されるときに呼び出される
        Activityが表示される前に呼ばれる。
        Activityの遷移で元に戻ったときにも呼び出される。（これは忘れやすい）
    onRestart()     ActivityがonStop()の後、復活するときに呼び出される。このあと、onStart()が呼び出される。
        終了状態になったActivityが再開するときに呼び出される。このあと、onStart()が呼ばれる。
    onResume()  Activityが前面になる時に呼び出される。
        ※重要※
        Activityが前面にきて、ユーザーとのやりとりを始められるようになる直前に呼び出される。
        データベースを利用する場合は、ここでデータベースに接続する。
        ここで行うべき処理は、
        ・データベースに接続する。
        ・データベース等から必要な情報を取り出す。
        ・画面に情報を表示する。
    onPause()   Activityがバックグラウンドに移動するときに呼び出される。
        ※重要※
        他のアプリが前面にきて、Activityがバックグラウンドに隠れるときに、呼ばれる。
        まだ完全に見えなくなったわけではない。見えなくなると、onStop()が呼び出される。
        このコールバックの後には、OSによりkillされる可能性がある。
        開発ガイドには、ユーザーがこのActivity上で実施したデータ変更は、このタイミングで保存（コミット）しろ、とある。
        Activityが持つ情報をファイルやデータベースに保存する処理はここで行うべきとある。
 
        データベースに関しては、以下の処理が必要なようだ。
        ・必要な情報を保存する。
        ・データベース接続を解放する。
 
        onPauseでデータベース接続を解放するということは、
        onResumeでデータベースに接続する必要がある。
 
        つまり、アプリを通じてデータベースをオープンしっぱなしなのはまずい。
        各ActivityのonResume()でデータベースをオープンし、
        onPause()でクローズするという扱いが必要になる。
        ということは、DBアクセサ等をシングルトンにする意味があまりないか？
         
        各Activityは、独立して、閉じたつくりにする必要があると思われる。

    onStop()    Activityが画面から見えなくなる時に呼び出される。
        ホーム画面に戻ったり、アプリが見えなくなるときに呼ばれる。
        このあと、システムの状態（メモリ不足等）によっては、OSによりActivityが殺される可能性もある。
        そのため、いつアプリが停止してもよい状態にしておく必要がある。
        ただし、onStop()が呼ばれずに、プロセスがkillされる場合がある。
        重要の情報の保管場所としては不適切。
    onDestroy()     Activityが終わる時に呼び出される。
        Activityが破棄される直前に呼ばれる。
 */

/**
 * メインアクティビティ
 * @author shima
 *
 */
public class MainActivity extends Activity
        implements MyLocationManagerListener
                    , MySensorManagerListener
                    , MyWeatherAPIListener
                    , MyTimerManagerListener {

    private final String TAG = ".::MainActivity::.";

    /* マイデータ */
    public static MyData _MyData;

    /* 背景色 */
	private int backgroundColor = 0;
	private final int backgroundColor1 = Color.rgb(123, 140, 90);  // グレー
	private final int backgroundColor2 = Color.rgb(207, 132, 2);   // オレンジ

	/* 広告管理 */
	private MyAdvertiseManager _MyAdvertiseManager;
    // センサーマネージャ
    private MySensorManager _MySensorManager;
    /* 天気API */
    private MyWeatherAPI _MyWeatherAPI;
    /* タイマー */
    private MyTimerTaskManager _MyTimerTaskManager;
    private final long firstInterval = 1000;
    private final long interval = 500;     // ミリ秒毎にタイマー起動
//    private long intervalCount = 20;    // インターバルの最大カウント
//    private long intervalCounter = 0;   // インターバルのカウンター
    
    /* ロケーションマネージャ */
    private MyLocationManager _MyLocationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        if (MyData.DEBUG) Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        // アップデートしたらメッセージを出す
        MyAppVersionCheck.AppCheckForUpdate(this);

        // 初期処理
		initialize();
    }

    @Override
    protected void onStart() {
        if (MyData.DEBUG) Log.d(TAG, "onStart");
        super.onStart();
    }
    
    @Override
    protected void onResume() {
        if (MyData.DEBUG) Log.d(TAG, "onResume");
        MyTimerCallBack(null);
        // ロケーションの更新
        if (_MyLocationManager != null)
            _MyLocationManager.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (MyData.DEBUG) Log.d(TAG, "onPause");
        if (_MyLocationManager != null)
            _MyLocationManager.onPause();
        super.onPause();
    }

    @Override
	protected void onStop() {
        if (MyData.DEBUG) Log.d(TAG, "onStop");
        if (_MyLocationManager != null)
            _MyLocationManager.onStop();
		super.onStop();
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MyData.DEBUG) Log.d(TAG, "onDestroy");
        // 広告の破棄
        if (_MyAdvertiseManager != null)
            _MyAdvertiseManager.destroy();
        // タイマー停止
        if (_MyTimerTaskManager != null)
            _MyTimerTaskManager.Stop();
        // ロケーションマネージャ
        if (_MyLocationManager != null)
            _MyLocationManager.onDestroy();
    }

    @Override
	public void onWindowFocusChanged(boolean hasFocus) {
        if (MyData.DEBUG) Log.d(TAG, "onWindowFocusChanged");
		super.onWindowFocusChanged(hasFocus);
		
		// サイズを設定
		resizeTextView(R.id.textView1, "123.4");
		resizeTextView(R.id.textView2, "123.4");
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (MyData.DEBUG) Log.d(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void UpdatedLocation(Object o) {
        // センサーがあるなら処理させない
        if (_MyData.isEnableSensor()) return;
        Location loc = (Location) o;
        if (_MyWeatherAPI != null)
            _MyWeatherAPI.getTemperatureFromWeatherAPI(loc);
    }

    @Override
    public void MyTimerCallBack(Object o) {
        if (MyData.DEBUG) Log.d(TAG, "MyTimerCallBack");
//        intervalCounter--;
//        if (intervalCounter < 0)
//        {
//            // センサーが無い場合、ロケーションから現在地取得
//            if (!_MyData.isEnableSensor())
//            {
//                if (_MyLocationManager != null)
//                    _MyLocationManager.onResume();
//            }
//            intervalCounter = intervalCount;
//        }
        View();
    }

    /*
     * テキストビューのリサイズ
     */
    private void resizeTextView(int id, String text)
    {
        if (MyData.DEBUG) Log.d(TAG, "resizeTextView");
        try
        {
            // テキストビューの取得
            final TextView _TextView = (TextView)findViewById(id);
            
            // テキストの幅
            final int viewWidth = _TextView.getWidth();//0
            // テキストサイズ
            float viewTextSize = _TextView.getTextSize();
            
            // テキストの情報取得
            final Paint p = new Paint();
            p.setTextSize(viewTextSize);
            // 表示しようとするテキストの幅
            float textWidth = p.measureText(text);//264
            
            // 表示しようとするテキストの幅 < テキストの幅
            while (textWidth < viewWidth) {
                // テキストサイズを加算
                viewTextSize++;
                
                // 再度テキストの幅を取得
                p.setTextSize(viewTextSize);
                textWidth = p.measureText(text);
            }
            
            // テキストサイズを設定
            _TextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewTextSize);
        }
        catch (Exception ex)
        {
            Log.e("resizeTextView", ex.getMessage());
        }
    }

    /*
	 * テキストのスタイルを設定
	 */
	private void setTextStyle()
	{
        if (MyData.DEBUG) Log.d(TAG, "setTextStyle");
		try
		{
		    /*
		     * ヘッダ名
		     */
		    if (!_MyData.isEnableSensor())
		    {
	            final TextView _TextView6 = (TextView)findViewById(R.id.textView6);
	            final String text6 = ".:: By Weather Online ::."; 
	            _TextView6.setText(text6);
	            _TextView6.setTextColor(Color.BLACK);
	            _TextView6.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
		    }
		    else
		    {
                final TextView _TextView6 = (TextView)findViewById(R.id.textView6);
                final String text6 = ".:: By Your Temperature Sensor :D ::."; 
                _TextView6.setText(text6);
                _TextView6.setTextColor(Color.BLACK);
                _TextView6.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
		    }
		    
		    /*
		     * 数値
		     */
			final TextView _TextView1 = (TextView)findViewById(R.id.textView1);
			final TextView _TextView2 = (TextView)findViewById(R.id.textView2);
			final TextView _TextView3 = (TextView)findViewById(R.id.textView3);

			// フォントを作成
			final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/f_7barspbd.ttf");
			// フォントをセット
			if (typeface != null)
			{
				_TextView1.setTypeface(typeface);
				_TextView2.setTypeface(typeface);
				_TextView3.setTypeface(typeface);
			}
			
			// フォントカラーを設定
			_TextView1.setTextColor(Color.BLACK);
			_TextView2.setTextColor(Color.BLACK);
			_TextView3.setTextColor(Color.BLACK);
		}
		catch (Exception ex)
		{
			Log.e("setTextStyle", ex.getMessage());
		}
	}

	/**
	 * 初期処理
	 */
	private void initialize()
	{
        if (MyData.DEBUG) Log.d(TAG, "initialize");
        try
        {
            // マイデータクラス
            _MyData = new MyData();
            
            // 日本語表記なら摂氏をデフォルトとする
            Locale locale = Locale.getDefault();
            if (locale.equals(Locale.JAPAN) || locale.equals(Locale.JAPANESE))
            {
                _MyData.setViewCelsius(true);
            }

            // 広告の設定
            _MyAdvertiseManager = new MyAdvertiseManager(this);
            _MyAdvertiseManager.setAdvertise();
            
            // タイマータスク
            _MyTimerTaskManager = new MyTimerTaskManager(this);
            
            // ロケーションマネージャ
            _MyLocationManager = new MyLocationManager(this);
            _MyLocationManager.setEventListener(this);
            
            // 画面の向きを縦固定する
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            // 気温のタップイベントリスナー登録
            final TextView _TextView1 = (TextView)findViewById(R.id.textView1);
            _TextView1.setOnClickListener(new ClickListner());
            // 背景のタップイベントリスナー登録
            final ImageView _ImageView = (ImageView)findViewById(R.id.imageView1);
            _ImageView.setOnClickListener(new ClickListner());
            
            // 背景色を決める
            backgroundColor = backgroundColor1;

            // センサーマネージャを取得
            _MySensorManager = new MySensorManager(this);
            _MySensorManager.setEventListener(this);
            _MySensorManager.StartSensorManager();
            _MyData.setEnableSensor(_MySensorManager.SetEnableSensor());

            // 天気ツール（センサー未搭載用）
            _MyWeatherAPI = new MyWeatherAPI(this);
            if (!_MyData.isEnableSensor())
            {
                Log.i(TAG, "MySensorManager --- NG!");
                // センサーが設定できなかった場合
                _MySensorManager.unregistListener();
                _MySensorManager = null;
                _MyWeatherAPI.setEventListener(this);
            }

            // タイマーのリスナー登録
            _MyTimerTaskManager.setEventListener(this);
            _MyTimerTaskManager.Start(firstInterval, interval);

            // テキストスタイルを設定
            setTextStyle();
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
	}

	/**
	 * WeatherAPIListenerのリスナーイベント
	 */
    @Override
    public void onLoad(int type, String json) {
        if (MyData.DEBUG)
        {
            Log.i("WEATHER", "json type=" + type);
            Log.i("WEATHER", "json=" + json);
        }

        try {
            float temp_C = 0.0f;
//            float temp_F = 0.0f;
            float humidity = 0.0f;
            
            JSONObject jsons = new JSONObject(json);
//            JSONObject jsonsData = jsons.getJSONObject("list");
            JSONArray jsonArray = jsons.getJSONArray("list");
//            String tempStatus = "";
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                try
                {
//                  tempStatus = jsonObj.getString("weatherCode");
                    JSONObject jo = jsonObj.getJSONObject("temp");
                    temp_C = (float) jo.getDouble("day");
//                    temp_F = (float) jsonObj.getDouble("temp_F");
                    humidity = (float) jsonObj.getDouble("humidity");
                } catch (JSONException e) {
                }
            }
            _MyData.setKelvin(true);
            _MyData.setTemperature(temp_C);
            _MyData.setHumidity(humidity);
        } catch (JSONException e) {
            Log.e("WEATHER", "error:" + e);
        }
    }

	/* センサーイベント START */
	/**
	 * センサーチェンジイベント
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		try
		{
			// センサータイプを判別
			final int sensor = event.sensor.getType();
			switch (sensor)
			{
				case Sensor.TYPE_TEMPERATURE:
				case Sensor.TYPE_AMBIENT_TEMPERATURE:
				    _MyData.setTemperature(event.values[0]);
					break;
				case Sensor.TYPE_RELATIVE_HUMIDITY:
				    _MyData.setHumidity(event.values[0]);
					break;
				default:
					break;
			}
		}
		catch (Exception ex)
		{
			Log.e("onSensorChanged", ex.getMessage());
		}
	}
	/**
	 * センサーシミュレータチェンジイベント
	 */
    @Override
    public void onSensorChanged(
            org.openintents.sensorsimulator.hardware.SensorEvent event) {
        try
        {
            // センサータイプを判別
            final int sensor = event.type;
            switch (sensor)
            {
                case org.openintents.sensorsimulator.hardware.Sensor.TYPE_TEMPERATURE:
                    _MyData.setTemperature(event.values[0]);
                    _MyData.setHumidity((float)(event.values[0] * 0.7));
                    break;
                default:
                    break;
            }
        }
        catch (Exception ex)
        {
            Log.e("onSensorChanged", ex.getMessage());
        }
    }
    /* センサーイベント END */

    /**
     * 表示処理
     */
    synchronized public void View()
    {
        if (MyData.DEBUG) Log.d(TAG, "View");
        // 温度
        viewTemperature();
        // 湿度
        viewHumidity();
    }

    /*
	 * 温度表示
	 */
	private void viewTemperature()
	{
		try
		{
            final ImageView backImage = (ImageView)findViewById(R.id.imageView3);
            backImage.setBackgroundColor(backgroundColor);

            /*
			 * 気温
			 */
			TextView _TextView1 = (TextView)findViewById(R.id.textView1);
			_TextView1.setText(String.valueOf(_MyData.getTemperature()));

			// 単位
			Bitmap image;
			if (!_MyData.isViewCelsius())
			{
				image = BitmapFactory.decodeResource(
											this.getBaseContext().getResources(),
											R.drawable.fahrenheit);
			}
			else
			{
				image = BitmapFactory.decodeResource(
											this.getBaseContext().getResources(),
											R.drawable.celsius);
			}
            final ImageView img = (ImageView)findViewById(R.id.imageView1);
			img.setImageBitmap(image);

			// 時間の表示
            viewTime();
		}
		catch (Exception ex)
		{
			Log.e("viewTemplature", ex.getMessage());
		}
	}
	
	/*
	 * 湿度表示
	 */
	private void viewHumidity()
	{
		try
		{
            final ImageView backImage = (ImageView)findViewById(R.id.imageView3);
            backImage.setBackgroundColor(backgroundColor);
			/*
			 * 湿度
			 */
			// 0番目に値が入っている
			final String txt = String.valueOf(_MyData.getHumidity());
			TextView _TextView2 = (TextView)findViewById(R.id.textView2);
			_TextView2.setText(txt);

			final Bitmap image = BitmapFactory.decodeResource(
										this.getBaseContext().getResources(),
										R.drawable.percentage);
            final ImageView img = (ImageView)findViewById(R.id.imageView2);
			img.setImageBitmap(image);

			// 時間の表示
            viewTime();
		}
		catch (Exception ex)
		{
			Log.e("viewHumidity", ex.getMessage());
		}
	}
	
	/*
	 * 時間表示
	 */
	private void viewTime()
	{
		try
		{
			/*
			 * 時間
			 */
			final String time = DateFormat.format("yyyy-MM-dd kk:mm:ss", Calendar.getInstance()).toString();
			final TextView _TextView3 = (TextView)findViewById(R.id.textView3);
			_TextView3.setText(time + " ");
		}
		catch (Exception ex)
		{
			Log.e("viewTime", ex.getMessage());
		}
	}

	/*
	 * クリックリスナー
	 */
	class ClickListner implements View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id)
			{
				case R.id.textView1:
				case R.id.imageView1:
					// 摂氏華氏表示を逆転させる
				    _MyData.setViewCelsius(!_MyData.isViewCelsius());
					View();
					break;
				default:
					break;
			}
		}
		
	}
	
	// タッチダウン
	private boolean istouched = false;
	// 最後のタッチX座標
	private float lastTouchX;
	// 現在のタッチX座標
	private float currentTouchX;
	// フリックだと判断する最低のX座標移動値
	private float flickRange = 50;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    switch (event.getAction()) {
	    case MotionEvent.ACTION_DOWN:
	        if (MyData.DEBUG) Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
	        istouched = true;
	        lastTouchX = event.getX();
	        break;
	    case MotionEvent.ACTION_UP:
	        if (MyData.DEBUG) Log.d("TouchEvent", "getAction()" + "ACTION_UP");
	        currentTouchX = event.getX();
	        if (currentTouchX + flickRange < lastTouchX)
	        {
	        	// 右から左へ
	        }
	        else if (lastTouchX + flickRange < currentTouchX)
	        {
	        	// 左から右へ
	        }
	        else
	        {
		        if (istouched)
		        {
		        	// タッチ
		        	if (backgroundColor == backgroundColor1)
		        	{
		        		backgroundColor = backgroundColor2;
		        	}
		        	else
		        	{
		        		backgroundColor = backgroundColor1;
		        	}
		        }
	        }
	        istouched = false;
	        break;
	    case MotionEvent.ACTION_MOVE:
	        if (MyData.DEBUG) Log.d("TouchEvent", "getAction()" + "ACTION_MOVE");
	        break;
	    case MotionEvent.ACTION_CANCEL:
	        if (MyData.DEBUG) Log.d("TouchEvent", "getAction()" + "ACTION_CANCEL");
	        break;
	    }
        View();
		return true;
	}

}
