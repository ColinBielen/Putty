package com.ceg.resizer.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.TimerTask;

/**
 * Task that cleans up files older than 24 hours.
 * Override the 24 hours via the setMaxAge() method.
 */
public class CacheCleanupTask extends TimerTask {

    private static Log log = LogFactory.getLog(CacheCleanupTask.class);
    private String cacheDirectory = "/tmp";
    private Integer maxFileAge = 24;

    public CacheCleanupTask() {


    }

    public void setCacheDirectory(String cacheDirectory) {
        if(cacheDirectory != null) {
            log.info("Setting cache directory to '"+cacheDirectory+"'");
            this.cacheDirectory = cacheDirectory;
        }
    }

    /**
     * Set the max age for files before we zap them. Default is 24 hours.
     * No negative numbers.
     * @param hours
     */
    public void setMaxFileAge(Integer hours) {
        if(hours >=0 ) {
            log.info("Setting Cache max file age to '"+hours+"' hours.");
            this.maxFileAge = hours;
        }
    }


    /**
     * Recursively go though the specified cache directory and zap anything older than the specified
     * maxFileAge()
     */
    @Override
    public void run() {
        File cacheDir = new File(cacheDirectory);
        log.debug("Running CacheCleanupTask at directory '"+cacheDirectory+"'");
        if(!cacheDir.exists()) {
            log.warn("Cache Directory "+cacheDir.getPath()+" does not exist. No cleanup to run.");
            return;
        }
        recursivelyDeleteOldFiles(cacheDir);
    }


    private void recursivelyDeleteOldFiles(File f) {
        if(f.isDirectory()) {
            for(File c :  f.listFiles())
              recursivelyDeleteOldFiles(c);
        }
        if(f.lastModified() < (System.currentTimeMillis() - (1000*60*60*maxFileAge))) {
           //log.warn("Should Delete "+f.getPath()+" because it's old...");
           if(!f.delete()) {
                log.warn("Can't delete file "+f.getPath()+". Skipping ");
           }
        } else {
            log.debug("Skipping file '"+f.getPath()+"' because it's mod date isn't old enough");
        }

    } //end recursivelyDeleteOldFiles().


} //end class
