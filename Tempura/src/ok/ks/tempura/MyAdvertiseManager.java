package ok.ks.tempura;

import java.util.Locale;

import net.nend.android.NendAdView;
import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.gms.ads.*;

public class MyAdvertiseManager{// implements AdListener{

    private final static String TAG = ".::MyAdvertiseManager::.";
    // AdMobのView
    private AdView adView;
    // アクティビティー
    private Activity _Activity = null;

    public MyAdvertiseManager(Activity act)
    {
        _Activity = act;
    }

    /**
     * 広告設定
     */
    public void setAdvertise()
    {
        try
        {
//            Locale locale = Locale.getDefault();
//            if (locale.equals(Locale.JAPAN) || locale.equals(Locale.JAPANESE))
//            {
//                // Nendの広告を組み込む
//                setNend();
//            }
//            else
//            {
                // AdMobを表示
                setAdMob();
//            }
            Log.i(TAG, "Advertise --- OK!");
        }
        catch (Exception ex)
        {
            Log.e("setAdvertise", ex.getMessage());
        }
    }
    
    /**
     * 破棄処理
     */
    public void destroy()
    {
        // AdMobの破棄
//        if (adMobView != null) adMobView.destroy();
    }

//    /**
//     * Nendの広告を組み込む
//     */
//    private void setNend()
//    {
//        try
//        {
//            /*
//             * NendAdView(Context context, int [Spotid], String [Apikey])
//             */
//            NendAdView nendAdView = 
//                    new NendAdView(_Activity.getApplicationContext(), 88909, "a8cd5c54a3dd1651ec36fae329c1369eab7c1129");
//            LinearLayout l = (LinearLayout)_Activity.findViewById(R.id.gad);
//            l.addView(nendAdView, new LinearLayout.LayoutParams(
//                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                                        LinearLayout.LayoutParams.WRAP_CONTENT));
//            
//            nendAdView.loadAd();
//        }
//        catch (Exception ex)
//        {
//            Log.e("setNend", ex.getMessage());
//        }
//    }
    
    /* AdMob START ---------------------------------------------------------------------------------------------------------*/
    /**
     * AdMobの広告を組み込む
     */
    private void setAdMob()
    {
        try
        {
//            // AdMobを表示
//            // adView を作成する
//            adMobView = new AdView(_Activity);
//            adMobView.setAdSize(AdSize.BANNER);
//            adMobView.setAdUnitId(_Activity.getString(R.string.AdMob_Publisher_ID));

            // AdView をリソースとしてルックアップしてリクエストを読み込む
            adView = (AdView)_Activity.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        
//            // リスナー登録
//            adMobView.setAdListener(this);

            // 属性 android:id="@+id/mainLayout" が与えられているものとして
            // LinearLayout をルックアップする
            //LinearLayout layout = (LinearLayout)_Activity.findViewById(R.id.gad);

            // adView を追加
            //layout.addView(adMobView);

//            // 一般的なリクエストを行って広告を読み込む
//            AdRequest _AdRequest = new AdRequest.Builder().build();
//            adMobView.loadAd(_AdRequest);
        }
        catch (Exception ex)
        {
            Log.e("setAdMob", ex.getMessage());
        }
    }
    public void Pause(){ adView.pause(); }
    public void Resume(){ adView.resume(); }
    public void Destroy(){ adView.destroy(); }
    /* AdMob END ---------------------------------------------------------------------------------------------------------*/

//    /* Adlantis START ---------------------------------------------------------------------------------------------------------*/
//    /**
//     * Adlantisの広告を組み込む
//     */
//    private void setAdlantis()
//    {
//        try
//        {
//            // Adlantisを読み込む
//            AdlantisView adView = new AdlantisView(_Activity);
//            /*
//            * 以下の xxx の部分には、管理画面より取得したPublisherIDを入れ替えてください。
//            * (ApplicationManifest.xmlで事前に定義した場合は不要です)
//            */
//            adView.setPublisherID(_Activity.getString(R.string.Adlantis_Publisher_ID));
//            _Activity.addContentView(adView, new LinearLayout.LayoutParams(
//                                                        LinearLayout.LayoutParams.FILL_PARENT,
//                                                        AdlantisUtils.adHeightPixels(_Activity)));
//        }
//        catch (Exception ex)
//        {
//            Log.e("setAdlantis", ex.getMessage());
//        }
//    }
//    /* Adlantis END ---------------------------------------------------------------------------------------------------------*/
}
