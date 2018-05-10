package vn.com.kidy;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by admin on 1/13/18.
 */

public class KidyApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
