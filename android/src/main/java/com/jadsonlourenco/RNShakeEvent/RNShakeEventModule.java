package com.jadsonlourenco.RNShakeEvent;

import android.content.Context;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.squareup.seismic.ShakeDetector;

/**
 * Created by richard on 20/09/16.
 */
public class RNShakeEventModule extends ReactContextBaseJavaModule implements ShakeDetector.Listener {
    public RNShakeEventModule(ReactApplicationContext reactContext) {
        super(reactContext);
        final SensorManager sensorManager = (SensorManager) reactContext.getSystemService(Context.SENSOR_SERVICE);
        final ShakeDetector shakeDetector = new ShakeDetector(this);
        reactContext.addLifecycleEventListener(new LifecycleEventListener() {
            @Override
            public void onHostResume() {
                shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME);
            }

            @Override
            public void onHostPause() {
                shakeDetector.stop();
            }

            @Override
            public void onHostDestroy() {
                shakeDetector.stop();
            }
        });
    }

    @Override
    public String getName() {
        return "RNShakeEvent";
    }

    @Override
    public void hearShake() {
        sendEvent(this.getReactApplicationContext(), "ShakeEvent", null);
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }
}
