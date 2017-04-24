package com.tonyodev.storagegrapher;

/**
 * Created by tonyofrancis on 4/23/17.
 *
 *
 * This class holds information about
 * a Storage volume.
 */
public final class StorageVolume {

    private final String path;
    private final long freeSpace;
    private final long usedSpace;
    private final long totalSpace;
    private final float usedSpacePercentage;
    private final float freeSpacePercentage;

    StorageVolume(String path, long freeSpace, long usedSpace,
                  long totalSpace, float usedSpacePercentage, float freeSpacePercentage) {
        this.path = path;
        this.freeSpace = freeSpace;
        this.usedSpace = usedSpace;
        this.totalSpace = totalSpace;
        this.usedSpacePercentage = usedSpacePercentage;
        this.freeSpacePercentage = freeSpacePercentage;
    }

    /**
     * @return  Volume Path
     * */
    public String getPath() {
        return path;
    }

    /**
     * @return  Volume free space in bytes
     * */
    public long getFreeSpace() {
        return freeSpace;
    }

    /**
     * @return  Volume used space in bytes
     * */
    public long getUsedSpace() {
        return usedSpace;
    }

    /**
     * @return  Volume total space in bytes
     * */
    public long getTotalSpace() {
        return totalSpace;
    }

    /**
     * @return  the percentage used space on the Volume
     * */
    public float getUsedSpacePercentage() {
        return usedSpacePercentage;
    }

    /**
     * @return  the percentage of free space available on the Volume
     * */
    public float getFreeSpacePercentage() {
        return freeSpacePercentage;
    }
}