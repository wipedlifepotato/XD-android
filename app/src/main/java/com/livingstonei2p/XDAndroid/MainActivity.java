package com.livingstonei2p.XDAndroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.livingstonei2p.XDAndroid.iniedotr.*;

public class MainActivity extends AppCompatActivity {
    public static String WorkDirectory=
            "/sdcard" +
            "/XDWorkDirectory";
    public static String PathFiles;
    public static Process xd=null;
    private static TextView errorText;
    private File workDirectoryFile;
    private ContextWrapper c;
    // maybe to another class?
    public static Process runXD(){
        try {
            Runtime.getRuntime().exec("chmod 7777 "+PathFiles+"/XD");
            Process process = Runtime.getRuntime().exec("./XD",
                    null, new File( PathFiles ) );
            return process;
        } catch (IOException e) {
            Log.d("ERROR",":");
            e.printStackTrace();
            errorText.setText(e.toString());
        }catch (Throwable e) {
            Log.d("ERROR",":");
            e.printStackTrace();
            errorText.setText(e.toString());
        }
        return null;
    }
    public static Process restartXD(){
	if(MainActivity.xd != null)
        	MainActivity.xd.destroy();
        MainActivity.xd = runXD();
        return MainActivity.xd;
    }
    Process initXD(){
        try {
            Log.d("initXD","XD init");

//        TextView text = findViewById(R.id.Text);
//      text.setText(c.getFilesDir().getPath());
            AssetManager am = this.getApplicationContext().getAssets();
            do {
                try {
                    File dir = new File(WorkDirectory + "/");
                    if (!dir.exists()) {
                        Log.d("DEBUG", "Create work directory");
                        if (!dir.mkdirs()) {
                            Log.d("ERROR", "create directory is fail, " + WorkDirectory);
                        }
                    } else System.out.println("Directory is exist: "+WorkDirectory);
                    InputStream in = am.open("XD");
                    File outFile = new File(PathFiles + "/XD");
                    if (outFile.exists()) {
                        outFile.setExecutable(true);
                        IniEditor ini = new IniEditor();
                        ini.load(c.getFilesDir().getPath() + "/torrents.ini");
                        ini.set("storage", "rootdir", WorkDirectory);
                        ini.set("storage", "downloads", WorkDirectory + "/downloads");
                        ini.set("storage", "completed", WorkDirectory + "/seeding");
                        ini.save(c.getFilesDir().getPath() + "/torrents.ini");
                        break;
                    }
                    OutputStream out = new FileOutputStream(outFile);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    outFile.setExecutable(true);
                    restartXD();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } while (false);
            return runXD();
        }catch(Throwable e){
            errorText.setText(e.toString());
            return null;
        }
    }
//end xd
    public static void updateWorkDirectory(String nWorkDirectory) throws IOException {
        //toDo: normalSettings
        File workDirecotryFile = new File(PathFiles + "/workDirectoryPathFile");
        if(workDirecotryFile.exists()) workDirecotryFile.delete();
        workDirecotryFile.createNewFile();
        workDirecotryFile.setWritable(true);
        OutputStream out = new FileOutputStream(workDirecotryFile);
        out.write( nWorkDirectory.getBytes() , 0, nWorkDirectory.length() );
        out.close();
    }
    //toDo: normal settings
    public String getWorkDirectory() throws FileNotFoundException {
        File workDirecotryFile = new File(c.getFilesDir().getPath() + "/workDirectoryPathFile");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(workDirecotryFile));
            return reader.readLine();
        }catch(Exception e){
            return WorkDirectory;//toDo: normal settings
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorText = findViewById(R.id.errorText);
        try {
            if (
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                Manifest.permission.INTERNET},
                        1);
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                Manifest.permission.ACCESS_NETWORK_STATE},
                        1);
            }
            c = new ContextWrapper(this);
            PathFiles = c.getFilesDir().getPath();
            WorkDirectory = getWorkDirectory();
            Log.d("XDAndroid","XD init");

            xd = initXD();
            Log.d("XDAndroid","XD inited");

            if (xd == null) {
                Log.d("XD-Activity", "Cant start app");
                restartXD();
                try {
                    wait(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //this.finishAffinity();
            } else {
                Log.d("XDAndroid","Start activity 2");
                Intent intent = new Intent(this, MainActivity2.class);
                startActivity(intent);
            }
        }catch(Throwable e){
            errorText.setText(e.toString());
            Log.d("errorXD",e.toString());
        }
    }
}
