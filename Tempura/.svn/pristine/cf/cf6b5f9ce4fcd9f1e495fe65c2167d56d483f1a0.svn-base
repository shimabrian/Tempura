package ok.ks.tempura;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class MyTimerTaskManager {
    private final static String TAG = ".::MyTimerTaskManager::.";
    private Handler handler;
    private Context context;
    private Timer mTimer;
    private MyTimerTask myTimer;
    private MyTimerManagerListener myTimerManagerListener;
      
    public MyTimerTaskManager(Context context) {
        handler = new Handler();
        this.context = context;
    }

    /**
     * Eventリスナーを設定
     * 
     * @param listener
     */
    public void setEventListener(MyTimerManagerListener listener) {
        this.myTimerManagerListener = listener;
    }
    
    /**
     * タイマー開始
     * @param firstInterval 始めの間隔
     * @param period    間隔
     */
    public void Start(long firstInterval, long period)
    {
        mTimer = new Timer();
        myTimer = new MyTimerTask(context);
        mTimer.schedule(myTimer, firstInterval, period);
        Log.i(TAG, "Set MyTimerTaskManager --- OK!");
    }
    
    /**
     * タイマー停止
     */
    public void Stop()
    {
        if (mTimer != null)
        {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }
    
    class MyTimerTask extends TimerTask {
        private Handler handler;
        private Context context;
          
        public MyTimerTask(Context context) {
            handler = new Handler();
            this.context = context;
        }
          
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    myTimerManagerListener.MyTimerCallBack(context);
                }
            });
        }
    }
}
