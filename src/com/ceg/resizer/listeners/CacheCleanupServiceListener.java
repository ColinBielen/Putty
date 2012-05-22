package com.ceg.resizer.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ceg.resizer.util.CacheCleanupTask;

/**
 * Listener that initializes the cache cleanup on a dedicated thread.
 * 'cuz cron is so 90s.
 * And because we're deploying this to a cloud; the less external stuff we need to set up the better ;)
 */
 public class CacheCleanupServiceListener implements ServletContextListener {


    private ScheduledExecutorService scheduler;

    /**
     * Set up the Cleanup task and initialize the values as specified in web.xml
     * @param servletContextEvent
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        //String cacheRoot = servletContextEvent.getServletContext().getInitParameter(CACHE_DIR_PARAM_NAME);
        CacheCleanupTask task = new CacheCleanupTask();
        task.setCacheDirectory(servletContextEvent.getServletContext().getInitParameter("LocalCacheDirectory"));
        task.setMaxFileAge(Integer.parseInt(servletContextEvent.getServletContext().getInitParameter("CacheMaxFileAge")));

        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.MINUTES);
    }

    /**
     * Shut down the internal scheduler.
     *
     * @param servletContextEvent
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
         scheduler.shutdown(); //As they say in Dark City: SHUT IT DOWN!
    }
}
