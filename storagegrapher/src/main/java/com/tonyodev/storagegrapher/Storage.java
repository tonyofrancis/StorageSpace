package com.tonyodev.storagegrapher;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.Formatter;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by tonyofrancis on 4/21/17.
 *
 * Utility class used to get conversions, directories
 * and information about a Storage Volume
 */

public final class Storage {

    /**
     * Converts bytes to Gigabytes and returns a formatted string
     * @param bytes bytes
     *@return formatted string with conversion
     */
    public static String bytesToGigabytesString(long bytes) {
        return bytesToGigabytes(bytes) + " GB";
    }

    /**
     * Converts bytes to Megabytes and returns a formatted string
     * @param bytes bytes
     *@return formatted string with conversion
     */
    public static String bytesToMegabytesString(long bytes) {
        return bytesToMegabytes(bytes) + " MB";
    }

    /**
     * Converts bytes to Kilobytes and returns a formatted string
     * @param bytes bytes
     *@return formatted string with conversion
     */
    public static String bytesToKilobytesString(long bytes) {
        return bytesToKilobytes(bytes) + " KB";
    }

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

    public static long bytesToGigabytes(long bytes) {
        return bytes / 1073741824;
    }

    public static long bytesToMegabytes(long bytes) {
        return bytes / 1048576;
    }

    public static long bytesToKilobytes(long bytes) {
        return bytes / 1024;
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
     * @param bytes bytes
     * @return  a map that contains the GB,MB,KB,B after bytes is converted
     * */
    public static Map<String,Long> convertBytesToMap(long bytes) {

        HashMap<String,Long> map = new HashMap<>(4);

        long leftOverBytes = bytes;

        long gb = (int) bytesToGigabytes(leftOverBytes);
        leftOverBytes = subtractFromBytes(leftOverBytes,1073741824,gb);

        long mb = (int) bytesToMegabytes(leftOverBytes);
        leftOverBytes = subtractFromBytes(leftOverBytes,1048576,mb);

        long kb = (int) bytesToKilobytes(leftOverBytes);
        leftOverBytes = subtractFromBytes(leftOverBytes,1024,kb);

        map.put("GB",gb);
        map.put("MB",mb);
        map.put("KB",kb);
        map.put("B",leftOverBytes);

        return map;
    }

    private static long subtractFromBytes(long bytes,long amount,long multiplier) {

        if(multiplier <= 0) {
            return bytes;
        }

        return bytes - (amount * multiplier);
    }

    /**
     * @return File - Primary storage directory. Primary internal storage.
     * In some cases this could be an SD Card
     * */
    @Nullable
    public static File getPrimaryStorageDir() {

        String storage = System.getenv("EXTERNAL_STORAGE");

        if(storage != null) {
            return new File(storage);
        }

        return null;
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
        }

        return null;
    }

    /**
     * @return File - Internal Storage Directory
     * */
    @Nullable
    public static File getInternalStorageDir() {

        File internalStorage = Environment.getExternalStorageDirectory();

        if(internalStorage != null && Environment.isExternalStorageEmulated()
                && !Environment.isExternalStorageRemovable()) {
            return internalStorage;
        }

        return getPrimaryStorageDir();
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
     * @return File - External App Data Directory.
     * */
    @Nullable
    public static File getSecondaryAppDataDir(@NonNull Context context) {

        if(context == null) {
            throw new NullPointerException("Context cannot be null");
        }

        File secondaryDir = getSecondaryStorageDir(context);

        if(secondaryDir != null) {

            String path = secondaryDir.getAbsolutePath() +
                    "/Android/data/" +
                    context.getApplicationContext().getPackageName();

            return new File(path);
        }

        return null;
    }

    /**
     * @param context context
     * @return the size of the directory in bytes
     * */
    public static long getSecondaryAppDirBytes(Context context) {

        if(context == null) {
            throw new NullPointerException("Context cannot be null");
        }

        File file = getSecondaryAppDataDir(context);

        if(file == null) {
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
        return (float) Math.ceil(((double)bytesAmount / (double) bytesTotal) * 100);
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
        long total = getTotalBytesForVolume(path);
        float usedPercentage = getStoragePercentage(used,total);
        float freePercentage = getStoragePercentage(free,total);

        return new StorageVolume(path,free,used,total,
                usedPercentage,freePercentage);
    }

    /**
     * @return Storage Volume for internal storage directory
     * */
    @Nullable
    public static StorageVolume getInternalStorageVolume() {

        File file = getInternalStorageDir();

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
            return null;
        }

        File file = getSecondaryStorageDir(context);

        if(file != null) {
            return getStorageVolume(file);
        }

        return null;
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

        if(file == null) {
            throw new NullPointerException("File cannot be null");
        }

        LinkedList<File> queue = new LinkedList<>();
        long size = 0;

        queue.add(file);

        while (!queue.isEmpty()) {

            File f = queue.remove();

            if(f != null) {

                size += f.length();

                if(file.isDirectory()) {

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
