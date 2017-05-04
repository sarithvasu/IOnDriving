package com.ion.iondriving;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.ion.iondriving.macro.MacroConstant;
import com.ion.iondriving.utilities.DPUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DPMonitoringArchiveList extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ListView mMonitorTripArchListView;
    private ImageView mIvBack, mIvDelete;
    String path;
    File[] listfiles;
    ArrayList<String> archies;
    private static final String TAG = "MEDIA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpmonitoring_archive_list);
        mMonitorTripArchListView = (ListView) findViewById(R.id.monitored_trip_list);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvDelete = (ImageView) findViewById(R.id.iv_delete);
        mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFiles();
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        archies = getFileInArchies();
    if(totalFiels().length!=0) {

    for (int i = 0; i < totalFiels().length; i++)
        writeToSDFile(totalFiels()[i]);
    }
        if (archies != null && archies.size() > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, archies);
            mMonitorTripArchListView.setAdapter(adapter);
            mMonitorTripArchListView.setOnItemClickListener(this);
            mMonitorTripArchListView.setOnItemLongClickListener(this);
        }

    }

    private void deleteFiles() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (deleteAllFileInArchies())
                            mMonitorTripArchListView.setAdapter(null);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete All the files?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private ArrayList<String> getFileInArchies() {
        File mydirMain = DPUtility.getGlobalApplicationContext().getDir(MacroConstant.APPLICATION_DATA_FOLDER, Context.MODE_APPEND);
        System.out.println("DIRECTORY........" + mydirMain);
        String folderName = "Trip";
        File mydir = new File(mydirMain, folderName);


		/*mySubDir.mkdir();
        File mySubDir2 = new File(mydir, "INTERRUPT");
		mySubDir2.mkdir();*/

        System.out.println("DIRECTORY......." + mydir);

        // TODO Auto-generated method stub
        /*File mydir = new File(Environment.getExternalStorageDirectory()
				+ "/"+MacroConstant.APPLICATION_DATA_FOLDER+"/"+folderName);*/
        if (!mydir.exists()) {
            return null;

        } else {


            path = Environment.getDataDirectory().getPath() + "/data/" + DPUtility.getGlobalApplicationContext().getPackageName() + "/app_DRIVER PROFILE DATA" + "/" + folderName + "/";

            //	String path = "/"+Environment.getDataDirectory()+"/"+MacroConstant.APPLICATION_DATA_FOLDER+"/"+folderName+"/";
            String tripFilePath = MacroConstant.APPLICATION_DATA_FOLDER + "/" + folderName + "/";
            //	File sdcard = Environment.getExternalStorageDirectory();
            //file= new File(mydir, tripFilePath);
            listfiles = mydir.listFiles();
            ArrayList<String> Alfilenames = new ArrayList<String>();
            System.out.println("FileLength" + listfiles.length);
            for (int i = 0; i < listfiles.length; i++) {
                Alfilenames.add(listfiles[i].getName());


            }
            return Alfilenames;

            //file = new File(_update_path);
            //file.createNewFile();
        }

    }

    public File[] totalFiels() {
        File mydirMain = DPUtility.getGlobalApplicationContext().getDir(MacroConstant.APPLICATION_DATA_FOLDER, Context.MODE_PRIVATE);
        System.out.println("DIRECTORY........" + mydirMain);
        String folderName = "Trip";
        File mydir = new File(mydirMain, folderName);
        listfiles = mydir.listFiles();
        return listfiles;
    }

    private void checkExternalMedia() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

    }

    /**
     * Method to write ascii text characters to file on SD card. In earlier versions of Android a
     * WRITE_EXTERNAL_STORAGE permission must be added to the manifest file or this method will throw
     * a FileNotFound Exception because you won't have write permission. But not true after
     * API 18 for files in storage area of app (then no write permission required).
     *
     * @param filename
     */

    private void writeToSDFile(File filename) {

        // Root of the external file system

        File root0 = android.os.Environment.getExternalStorageDirectory();

        /* Now find the root of the external storage for this app (where the app can place
        * persistent files that it owns internal to the application and not typically visible
        * to the user as media).  See
        *
        *    http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
        *
        * The method getExternalFilesDir (string) returns the user storage associated with the
        * app, which doesn't require write permissions after API 18.  The string argument specifies various
        * regions of this storage.  For example,
        *
        * - null specifies the root of the storage for this app
        * - Environment.DIRECTORY_NOTIFICATIONS specifies the Notifications directory of app storage
        * - Environvment.DIRECTORY_DOWNLOADS specifies standard directory for files downloaded by user
        * - Environment.DIRECTORY_PICTURES specifies standard directory for pictures available to the user
        * - Environment.DIRECTORY_DOCUMENTS specifies standard directory for documents produced by user
        * etc.
        *
        * See the fields of the Environment class at
        *    https://developer.android.com/reference/android/os/Environment.html
        * for other possibilities.  For example, on my phone (running Android 6.0.1) the root of
        * the user storage for this specific app is found at
        *
        *    /storage/emulated/0/Android/data/com.lightcone.writesdcard/files
        * */

        // Root of the data directories Documents subdirectory specific to this app, for which no write
        // permission is required for Android 4.4 and later.

        File root = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);


        // Create a Documents/download subdirectory in the data area for this app
        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File(root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, filename.getName());

        InputStream in = null;

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            FileOutputStream f = new FileOutputStream(file);
            while ((line = br.readLine()) != null) {
                f.write(line.getBytes());

            }
            f.close();
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        MediaScannerConnection.scanFile(this,new String[]{file.getAbsolutePath().toString()},null,null);

    }


    private boolean deleteAllFileInArchies() {
        File mydirMain = DPUtility.getGlobalApplicationContext().getDir(MacroConstant.APPLICATION_DATA_FOLDER, Context.MODE_PRIVATE);
        System.out.println("DIRECTORY........" + mydirMain);
        String folderName = "Trip";
        File mydir = new File(mydirMain, folderName);
        if (!mydir.exists()) {
            return false;

        } else {
            path = Environment.getDataDirectory().getPath() + "/data/" + DPUtility.getGlobalApplicationContext().getPackageName() + "/app_DRIVER PROFILE DATA" + "/" + folderName + "/";
            listfiles = mydir.listFiles();
            for (int i = 0; i < listfiles.length; i++) {
                listfiles[i].delete();
            }
            return true;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            String mSelectedFilePath = listfiles[position].getPath();
            Intent mIntentPath = new Intent(this, CsvDisplayController.class);
            mIntentPath.putExtra("path", mSelectedFilePath);
            startActivity(mIntentPath);


        } catch (Exception e) {

        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        String mSelectedFilePath = listfiles[position].getPath();
                        File file = new File(mSelectedFilePath);
                        boolean deleted = file.delete();
                        if (deleted) {
                            ArrayList<String> archies = getFileInArchies();
                            if (archies != null && archies.size() > 0) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DPMonitoringArchiveList.this, R.layout.list_item, archies);
                                mMonitorTripArchListView.setAdapter(adapter);

                            } else {
                                mMonitorTripArchListView.setAdapter(null);
                            }
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete a file ?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
        return true;
    }
}
