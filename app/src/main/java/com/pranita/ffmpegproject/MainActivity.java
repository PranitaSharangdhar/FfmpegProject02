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


    }
}
