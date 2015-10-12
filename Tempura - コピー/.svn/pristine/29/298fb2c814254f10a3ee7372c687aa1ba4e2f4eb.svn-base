package ok.ks.tempura;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class MyAppVersionCheck {

	/*
	 * アプリをアップデートした後にメッセージを出す
	 */
	public static void AppCheckForUpdate(Context context)
	{
		//SharedPreferenceの設定
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        
        // 現バージョン情報を取得
        int vCode = sp.getInt("VersionCode", 1);
        String vName = sp.getString("VersionName", "1.0");
 
        //最新のバージョン情報を取得する
        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
 
        //最新のバージョン情報をPreferenceに保存
        editor.putInt("VersionCode", pi.versionCode);
        editor.putString("VersionName", pi.versionName);
        editor.commit();

    	if (pi != null) {
    	    // VersionCode でVersionUPを判断する
    	    if (pi.versionCode > vCode) {
                //VersionCodeが上がっている場合
    	        Toast.makeText(context, "Version Up @VersionCode Change to" + Integer.toString(pi.versionCode), Toast.LENGTH_LONG).show();
    	    }
	        // VersionName でVersionUPを判断する
    	    if (!pi.versionName.equals(vName)) {
                //VersionNameが異なる場合
    	        Toast.makeText(context, "Version Up @VersionName Change to " + pi.versionName, Toast.LENGTH_LONG).show();
    	    }
    	}
	}
}
