package com.pranita.ffmpegproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import java.io.File;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File folder = new File(Environment.getExternalStorageDirectory() + "/TrimVideos");

        if(!folder.exists())
        {
            folder.mkdir();
        }
        else
        {
            System.out.println("RTR folder exists");
        }

        File ipFile = new File(Environment.getExternalStorageDirectory() + "/TrimVideos/apocalyptic.mp4");
        if(ipFile.exists())
        {
            System.out.println("RTR apocalyptic file is available");
        }
        else
        {
            System.out.println("RTR apocalyptic file is not available");
        }

        String ipPath = folder.toString() + "/apocalyptic.mp4";
        String opPath = folder.toString() + "/trim.mp4";

        String command = "-i " + ipPath +" -ss 05 -to 10 -c:v copy -c:a copy " + opPath;

        int rc = FFmpeg.execute(command);

        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "RTR Command execution completed successfully.");
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "RTR Command execution cancelled by user.");
        } else {
            Log.i(Config.TAG, String.format("RTR Command execution failed with rc=%d and the output below.", rc));
            Config.printLastCommandOutput(Log.INFO);
        }

        String opPathCrop = folder.toString() + "/crop.mp4";
        /*String commandCrop = "-i " + ipPath +" -filter:v \"crop=800:600:0:0\" " + opPathCrop;

        //val cmd = arrayOf("-i", audio!!.path, "-ss", startTime, "-t", endTime, "-c", "copy", outputLocation.path)

        int rc1 = FFmpeg.execute(commandCrop);

        if (rc1 == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "RTR Command execution completed successfully.");
        } else if (rc1 == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "RTR Command execution cancelled by user.");
        } else {
            Log.i(Config.TAG, String.format("RTR Command execution failed with rc=%d and the output below.", rc1));
            Config.printLastCommandOutput(Log.INFO);
        }*/

        crop(ipPath, 800,600, 0, 0, opPathCrop);

    }

    public void crop( String ipFileName, int width, int height, int x, int y, String OpFileName)
    {

        String commandCrop = "-i " + ipFileName +" -filter:v \"crop=800:600:0:0\" " + OpFileName;

        //val cmd = arrayOf("-i", audio!!.path, "-ss", startTime, "-t", endTime, "-c", "copy", outputLocation.path)

        int rc1 = FFmpeg.execute(commandCrop);

        if (rc1 == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "RTR Command execution completed successfully.");
        } else if (rc1 == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "RTR Command execution cancelled by user.");
        } else {
            Log.i(Config.TAG, String.format("RTR Command execution failed with rc=%d and the output below.", rc1));
            Config.printLastCommandOutput(Log.INFO);
        }
    }
}
