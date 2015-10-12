package ok.ks.tempura;

import android.hardware.SensorEvent;

public interface MySensorManagerListener {

    public void onSensorChanged(SensorEvent event);
    public void onSensorChanged(org.openintents.sensorsimulator.hardware.SensorEvent event);
}
