package com.tonyodev.storagespace;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tonyodev.storagegrapher.Storage;
import com.tonyodev.storagegrapher.StorageGraphBar;
import com.tonyodev.storagegrapher.StorageVolume;
import com.tonyodev.storagegrapher.widget.StorageGraphView;

public class MainActivity extends AppCompatActivity {

    private StorageGraphView internalStorageGraphView;
    private StorageGraphView sdCardStorageGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        internalStorageGraphView = (StorageGraphView) findViewById(R.id.storageView);
        sdCardStorageGraphView = (StorageGraphView) findViewById(R.id.sdCardStorageView);
        setStorageGraphView();
        setSdCardStorageGraphView();
    }

    private void setStorageGraphView() {

        StorageVolume storageVolume = Storage.getPrimaryStorageVolume();

        if(storageVolume != null) {

            long appSize = Storage.getAppDirBytes(this) + Storage.getPrimaryAppFilesDirBytes(this);  //Get app size. The App is located on the internal storage

            StorageGraphBar appBar = new StorageGraphBar(
                    Storage.getStoragePercentage(appSize,storageVolume.getTotalSpace()),
                    ContextCompat.getColor(this,R.color.orange),
                    getString(R.string.app),
                    Storage.getFormattedStorageAmount(this,appSize)
            );

            StorageGraphBar usedBar = new StorageGraphBar(
                    Storage.getStoragePercentage(Math.abs(storageVolume.getUsedSpace()-appSize),storageVolume.getTotalSpace()), // App size is part of the volume used amount. Subtract it if displaying appSize
                    ContextCompat.getColor(this,R.color.light_blue),
                    getString(R.string.used),
                    Storage.getFormattedStorageAmount(this,Math.abs(storageVolume.getUsedSpace()) - appSize)
            );

            StorageGraphBar freeBar = new StorageGraphBar(
                    storageVolume.getFreeSpacePercentage(),
                    ContextCompat.getColor(this,R.color.gray),
                    getString(R.string.free),
                    Storage.getFormattedStorageAmount(this,storageVolume.getFreeSpace())
            );

            internalStorageGraphView.addBars(usedBar,appBar,freeBar);
            internalStorageGraphView.setVisibility(View.VISIBLE);
        }else {
            internalStorageGraphView.setVisibility(View.GONE);
        }
    }

    private void setSdCardStorageGraphView() {

        StorageVolume storageVolume = Storage.getSecondaryStorageVolume(this);

        if(storageVolume != null) {

            long appSize = Storage.getSecondaryAppFilesDirBytes(this);

            StorageGraphBar appBar = new StorageGraphBar(
                    Storage.getStoragePercentage(appSize,storageVolume.getTotalSpace()),
                    ContextCompat.getColor(this,R.color.orange),
                    getString(R.string.app),
                    Storage.getFormattedStorageAmount(this,appSize)
            );

            StorageGraphBar usedBar = new StorageGraphBar(
                    Storage.getStoragePercentage(storageVolume.getUsedSpace()-appSize,storageVolume.getTotalSpace()),
                    ContextCompat.getColor(this,R.color.light_blue),
                    getString(R.string.used),
                    Storage.getFormattedStorageAmount(this,storageVolume.getUsedSpace() - appSize)
            );

            StorageGraphBar freeBar = new StorageGraphBar(
                    storageVolume.getFreeSpacePercentage(),
                    ContextCompat.getColor(this,R.color.gray),
                    getString(R.string.free),
                    Storage.getFormattedStorageAmount(this,storageVolume.getFreeSpace())
            );

            sdCardStorageGraphView.addBars(usedBar,appBar,freeBar);
            sdCardStorageGraphView.setVisibility(View.VISIBLE);
        }else {
            sdCardStorageGraphView.setVisibility(View.GONE);
        }
    }
}
