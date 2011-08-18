package com.ceg.resizer.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Downloads the file from the passed URL and stores it locally for transformation and processing.
 * Also cache the file in the downloadDir.(/tmp). Cache files are *normally* accesses before doing a full remote lookup.
 * Currently cache files should be managed via cron or other system process to avoid becoming stale.
 */
public class URLFileGrabber
    implements FileGrabber{
    static final Log log = LogFactory.getLog(URLFileGrabber.class);

    static final int BUFF_SIZE = 100000;
    static final byte[] buffer = new byte[BUFF_SIZE];
    static int defaultReadtimeout = 1000;


    private String imageHost = "localhost";

    private String downloadDir = "/tmp";

    public URLFileGrabber() {

           downloadDir = "/tmp";

    }


    /**
     * Optionally override the directory where you want your file(s) downloaded. This is also the cache directory, so
     * clean it in accordance with your disk space preferences :)
     * @param dir
     */
    public void setDownloadDir(String dir) {
        downloadDir = dir;
    }

    /**
     * Gets a file from a url and caches it locally.
     * Local cache can be cleared externallay via cron or other service.
     * @param urlString the full url to the file you want to grab
     * @return File object pointing to a local copy of the file.
     */
    public File getFile(String urlString) {
        return getFile(urlString,true);
    }


    /**
     * Gets a file from a url. set the cacheLocally flag to "false" to force a fresh (and full) http read for each access.
     * Note that setting this to false still downloads and manipulates a local copy:
     * It will just be re downloaded freash with each call.
     * @param urlString the full url to the file you want to grab
     * @param cacheLocally - set to "false" to force a fresh (and full) http read for each access.
     * @return File object pointing to the local copy of the remote file.
     */
    public File getFile(String urlString,boolean cacheLocally) {
        File outputFile = null;
        try {

            URL url = new URL(urlString);

            outputFile = new File(downloadDir+"/"+imageHost,url.getPath());
            log.debug("Output File = "+outputFile.toString());
            if(!outputFile.exists()) {
               try {
                   URLConnection conn = url.openConnection();
                   conn.setReadTimeout(defaultReadtimeout);
                   InputStream in = conn.getInputStream();

                outputFile.getParentFile().mkdirs();
                outputFile.createNewFile();

                FileOutputStream fio = new FileOutputStream(outputFile);
                    copy(in,fio);
                } catch(MalformedURLException mfe) {
                     log.error(mfe.getMessage(),mfe);
                } catch(IOException e) {
                    log.error(e.getMessage(),e);
                }
            } else {
                log.debug("output file already exists. using cache version");
            }

        } catch(MalformedURLException mf) {
            log.error("Problem with URL: ",mf);
        }
        return outputFile;
    }



    /**
     * Byte-level copy from the URL to the local file.
     * This method closes both the input and output streams when it's done, so don't try to re use them.
     * @param in InputStream we're reading from
     * @param out OutputStream we're writing to.
     * @throws IOException
     */
protected static void copy(InputStream in,OutputStream out) throws IOException{

    try {
      while (true) {
         synchronized (buffer) {
            int amountRead = in.read(buffer);
            if (amountRead == -1) {
               break;
            }
            out.write(buffer, 0, amountRead);
         }
      }
   } finally {
      if (in != null) {
         in.close();
      }
      if (out != null) {
         out.close();
      }
   }
}





}
