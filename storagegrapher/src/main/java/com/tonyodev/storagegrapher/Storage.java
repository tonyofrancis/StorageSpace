package com.tonyodev.storagegrapher;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.os.EnvironmentCompat;
import android.text.format.Formatter;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by tonyofrancis on 4/21/17.
 *
 * Utility class used to get conversions, directories
 * and information about a Storage Volume
 */

public final class Storage {

    /**
     * Formats a content size to be in the form of bytes, kilobytes, megabytes, etc.
     * @param context context
     * @param bytes bytes
     * @return formatted string with conversion
     * */
    public static String getFormattedStorageAmount(Context context, long bytes) {

        if(context == null) {
            throw new NullPointerException("Context cannot be null");
        }

        return Formatter.formatFileSize(context,bytes);
    }

    /**
     * @param volume Storage Volume
     * @return available bytes on a storage volume
     * */
    public static long getAvailableBytesForVolume(File volume) {

        if(volume == null) {
            throw new IllegalArgumentException("File: cannot be null");
        }

        return getAvailableBytesForVolume(volume.getAbsolutePath());
    }

    /**
     * @param volumePath Storage Volume Path
     * @return available bytes on a storage volume
     * */
    public static long getAvailableBytesForVolume(String volumePath) {

        if(volumePath == null) {
            throw new IllegalArgumentException("Path: cannot be null");
        }

        StatFs stat = new StatFs(volumePath);

        long blockSize;
        long availableBlocks;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        }else {
            blockSize = (long) stat.getBlockSize();
            availableBlocks = (long) stat.getAvailableBlocks();
        }

        return availableBlocks * blockSize;
    }

    /**
     * @param volume Storage Volume
     * @return used bytes on a storage volume
     * */
    public static long getUsedBytesForVolume(File volume) {

        if(volume == null) {
            throw new IllegalArgumentException("File: cannot be null");
        }

        return getUsedBytesForVolume(volume.getAbsolutePath());
    }

    /**
     * @param volumePath Storage Volume Path
     * @return available bytes on a storage volume
     * */
    public static long getUsedBytesForVolume(String volumePath) {

        if(volumePath == null) {
            throw new IllegalArgumentException("Path: cannot be null");
        }

        StatFs stat = new StatFs(volumePath);
        long blockSize;
        long availableBlocks;
        long totalBlocks;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
            totalBlocks = stat.getBlockCountLong();
        }else {
            blockSize = (long) stat.getBlockSize();
            availableBlocks = (long) stat.getAvailableBlocks();
            totalBlocks = (long) stat.getBlockCount();
        }

        return (totalBlocks * blockSize) - (availableBlocks * blockSize);
    }

    /**
     * @param volume Storage Volume
     * @return total bytes on a storage volume
     * */
    public static long getTotalBytesForVolume(File volume) {

        if(volume == null) {
            throw new IllegalArgumentException("File: cannot be null");
        }

        return getTotalBytesForVolume(volume.getAbsolutePath());
    }

    /**
     * @param volumePath Storage Volume Path
     * @return available bytes on a storage volume
     * */
    public static long getTotalBytesForVolume(String volumePath) {

        if(volumePath == null) {
            throw new IllegalArgumentException("Path: cannot be null");
        }

        StatFs stat = new StatFs(volumePath);
        long blockSize;
        long totalBlocks;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            totalBlocks = stat.getBlockCountLong();
        }else {
            blockSize = (long) stat.getBlockSize();
            totalBlocks = (long) stat.getBlockCount();
        }

        return totalBlocks * blockSize;
    }

    /**
     * @return File - Primary storage directory. External SDCard. In some cases
     * this can be the internal storage.
     * */
    @Nullable
    public static File getPrimaryStorageDir() {

        String storage = System.getenv("EXTERNAL_STORAGE");

        if(storage != null) {
            return new File(storage);
        }

        return Environment.getExternalStorageDirectory();
    }

    /**
     *
     * @return File = Secondary storage directory. eg. SD Card
     * */
    @Nullable
    public static File getSecondaryStorageDir(Context context) {

        if(context == null) {
            throw new NullPointerException("Context cannot be null");
        }

        String storage = System.getenv("SECONDARY_STORAGE");

        if ((storage == null) || (storage.length() == 0)) {
            storage = System.getenv("EXTERNAL_SDCARD_STORAGE");
        }

        if(storage != null) {
            return new File(storage);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            List<android.os.storage.StorageVolume> volumes = storageManager.getStorageVolumes();

            for (android.os.storage.StorageVolume volume : volumes) {

                if(volume.getDescription(context).equalsIgnoreCase("SD card") && volume.isRemovable()) {
                    return new File("/storage/" + volume.getUuid());
                }
            }
        }else {

            File storageDir = new File("/storage");

            if(storageDir.exists() && storageDir.isDirectory()) {

                File[] files = storageDir.listFiles();

                if(files != null) {

                    for (File file : files) {

                        if(file != null && file.isDirectory()
                                && Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(file.getAbsoluteFile()))
                                && (isNewSdNameFormat(file.getName()) || file.getName().startsWith("sd"))) {
                            return file;
                        }
                    }
                }
            }
        }

        return null;
    }

    //eg 1D4C-1CE9
    private static boolean isNewSdNameFormat(String name) {

        if(name == null || name.isEmpty() || name.length() != 9 || name.charAt(4) != '-') {
            return false;
        }

        for (int i = 0; i < name.length(); i++) {

            int c = (int) name.charAt(i);

            if(c == (int)'-' && i == 4) {
                continue;
            }

            if(!((c >= ((int)'A') && c <= ((int)'Z')) || (c >= ((int)'0') && c <= ((int)'9')))){
                return false;
            }
        }

        return true;
    }

    /**
     * @return File - Application directory. Note: An application
     * can be install on an external Storage Volume.
     * */
    public static File getAppDir(@NonNull Context context) {

        if(context == null) {
            throw new NullPointerException("Context cannot be null");
        }

        return new File(context.getApplicationInfo().dataDir);
    }

    /**
     * @return File - App Files Data Directory on Primary Storage.
     * */
    public static File getPrimaryAppFilesDir(Context context) {

        if(context == null) {
            throw new NullPointerException("Context cannot be null");
        }

        File primaryDir = getPrimaryStorageDir();

        if(primaryDir != null) {

            String path = primaryDir.getAbsolutePath() +
                    "/Android/data/" +
                    context.getApplicationContext().getPackageName();

            File file = new File(path);

            if(file.exists()) {
                return file;
            }
        }

        return null;
    }

    /**
     * @return File - App Files Data Directory on Secondary Storage.
     * */
    @Nullable
    public static File getSecondaryAppFilesDir(@NonNull Context context) {

        if(context == null) {
            throw new NullPointerException("Context cannot be null");
        }

        File secondaryDir = getSecondaryStorageDir(context);

        if(secondaryDir != null) {

            String path = secondaryDir.getAbsolutePath() +
                    "/Android/data/" +
                    context.getApplicationContext().getPackageName();

            File file = new File(path);

            if(file.exists()) {
                return file;
            }
        }

        return null;
    }

    /**
     * @param context context
     * @return the size of the directory in bytes
     * */
    public static long getSecondaryAppFilesDirBytes(Context context) {

        if(context == null) {
            throw new NullPointerException("Context cannot be null");
        }

        File file = getSecondaryAppFilesDir(context);

        if(file == null || !file.exists()) {
            return 0;
        }

        return getDirectorySize(file);
    }

    /**
     * @param context context
     * @return the size of the directory in bytes
     * */
    public static long getPrimaryAppFilesDirBytes(Context context) {

        if(context == null) {
            throw new NullPointerException("Context cannot be null");
        }

        File file = getPrimaryAppFilesDir(context);

        if(file == null || !file.exists()) {
            return 0;
        }

        return getDirectorySize(file);
    }

    /**
     * @param bytesAmount bytes amount
     * @param bytesTotal total bytes
     * @return percentage of the bytes amount
     * */
    public static float getStoragePercentage(long bytesAmount, long bytesTotal) {
        return (float) ((double)bytesAmount / (double) bytesTotal) * 100;
    }

    /**
     * @param file file
     * @return Storage Volume for a file
     * */
    @Nullable
    public static StorageVolume getStorageVolume(File file) {

        if(file == null) {
            throw new NullPointerException("File cannot be null");
        }

        return getStorageVolume(file.getAbsolutePath());
    }

    /**
     * @param path filePath
     * @return Storage Volume for a file
     * */
    @Nullable
    public static StorageVolume getStorageVolume(String path) {

        if(path == null) {
            throw new NullPointerException("Path cannot be null");
        }

        long used = getUsedBytesForVolume(path);
        long free = getAvailableBytesForVolume(path);
        long total =getTotalBytesForVolume(path);
        float usedPercentage = getStoragePercentage(used,total);
        float freePercentage = getStoragePercentage(free,total);

        return new StorageVolume(path,free,used,total,
                usedPercentage,freePercentage);
    }

    /**
     * @return Storage Volume for primary storage directory
     * */
    @Nullable
    public static StorageVolume getPrimaryStorageVolume() {

        File file = getPrimaryStorageDir();

        if(file == null) {
            return null;
        }

        return getStorageVolume(file);
    }

    /**
     * @param context context
     * @return Storage Volume for secondary directory
     * */
    @Nullable
    public static StorageVolume getSecondaryStorageVolume(Context context) {

        if(context == null) {
            throw new NullPointerException("Context cannot be null");
        }

        File file = getSecondaryStorageDir(context);

        if(file == null) {
            return null;
        }

        return getStorageVolume(file);
    }

    /**
     * @param context context
     * @return the size of the application directory in bytes
     * */
    public static long getAppDirBytes(Context context){

        if(context == null) {
            throw new NullPointerException("Context cannot be null");
        }

        return getDirectorySize(getAppDir(context));
    }

    /**
     * Gets the size(bytes) of a directory or file
     *
     * @param file file
     * @return directory size in bytes
     * */
    public static long getDirectorySize(File file) {

        long size = 0;
        Queue<File> queue = new LinkedList<>();

        queue.add(file);

        while (!queue.isEmpty()) {

            File f = queue.remove();

            if(f != null && file.exists()) {

                size += f.length();

                if(f.isDirectory()) {

                    File[] subFiles = f.listFiles();

                    if(subFiles != null) {
                        queue.addAll(Arrays.asList(subFiles));
                    }
                }
            }
        }

        return size;
    }
}
