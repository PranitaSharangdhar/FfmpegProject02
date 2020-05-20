package com.pranita.ffmpegproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File ipfolder = new File(Environment.getExternalStorageDirectory() + "/IpVideos");

        if(!ipfolder.exists())
        {
            ipfolder.mkdir();
        }
        else
        {
            System.out.println("RTR input folder exists");
        }

        /*File ipFile = new File(Environment.getExternalStorageDirectory() + "/TrimVideos/apocalyptic.mp4");
        if(ipFile.exists())
        {
            System.out.println("RTR apocalyptic file is available");
        }
        else
        {
            System.out.println("RTR apocalyptic file is not available");
        }*/

        //Trim
        String ipPath = ipfolder.toString() + "/apocalyptic.mp4";
        String opFileName = "trim.mp4";
        int rc = trim(ipPath, "05", "10", opFileName);

        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "RTR Command execution completed successfully.");
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "RTR Command execution cancelled by user.");
        } else {
            Log.i(Config.TAG, String.format("RTR Command execution failed with rc=%d and the output below.", rc));
            Config.printLastCommandOutput(Log.INFO);
        }

        //Crop
        String opFileNameCrop = "crop.mp4";
        rc = crop(ipPath, 800,600, 0, 0, opFileNameCrop);
        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "RTR Command execution completed successfully.");
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "RTR Command execution cancelled by user.");
        } else {
            Log.i(Config.TAG, String.format("RTR Command execution failed with rc=%d and the output below.", rc));
            Config.printLastCommandOutput(Log.INFO);
        }

        //Concat
        String file1 = ipfolder.toString() + "/apocalyptic.mp4";
        String file2 = ipfolder.toString() + "/galaxy.mp4";
        String opFileNameConcat = "concat.mp4";
        rc = concat(file1, file2, opFileNameConcat);
        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "RTR Command execution completed successfully.");
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "RTR Command execution cancelled by user.");
        } else {
            Log.i(Config.TAG, String.format("RTR Command execution failed with rc=%d and the output below.", rc));
            Config.printLastCommandOutput(Log.INFO);
        }
    }

    public int concat(String filePath1, String filePath2, String OpFileName)
    {

        File opfolder = new File(Environment.getExternalStorageDirectory() + "/OpVideos");

        if(!opfolder.exists())
        {
            opfolder.mkdir();
        }
        else
        {
            System.out.println("RTR output folder exists");
        }

        try {
            FileWriter txtFile = new FileWriter(Environment.getExternalStorageDirectory() + "/OpVideos/mylist.txt");
            txtFile.write("file "+"'"+filePath1+"'"+"\n");
            txtFile.write("file "+"'"+filePath2+"'"+"\n");
            txtFile.close();
        }catch (IOException e1)
        {
            System.out.println("RTR IOException");
        }

        String concatPath = opfolder.toString() + "/mylist.txt";
        String command =  "-f concat -safe 0 -i "+concatPath+" -c copy "+opfolder.toString()+"/"+OpFileName;

        int rc = FFmpeg.execute(command);

        return rc;

    }

    public int trim( String ipFilePath, String startTime, String endTime, String OpFileName)
    {
        File opfolder = new File(Environment.getExternalStorageDirectory() + "/OpVideos");

        if(!opfolder.exists())
        {
            opfolder.mkdir();
        }
        else
        {
            System.out.println("RTR output folder exists");
        }

        String command = "-i " + ipFilePath +" -ss "+startTime+" -to "+endTime+" -c:v copy -c:a copy " + opfolder+"/"+OpFileName;

        int rc = FFmpeg.execute(command);

        return rc;
    }


    public int crop( String ipFilePath, int width, int height, int x, int y, String OpFileName)
    {
        File opfolder = new File(Environment.getExternalStorageDirectory() + "/OpVideos");

        if(!opfolder.exists())
        {
            opfolder.mkdir();
        }
        else
        {
            System.out.println("RTR output folder exists");
        }

        String commandCrop = "-i " + ipFilePath +" -filter:v \"crop="+width+":"+height+":"+x+":"+y+"\" " + opfolder + "/"+OpFileName;

        //val cmd = arrayOf("-i", audio!!.path, "-ss", startTime, "-t", endTime, "-c", "copy", outputLocation.path)

        int rc = FFmpeg.execute(commandCrop);

        return rc;
    }
}
