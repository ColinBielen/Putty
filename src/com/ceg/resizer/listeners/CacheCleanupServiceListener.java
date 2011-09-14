package com.ceg.resizer.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ceg.resizer.util.FileCacheCleanup;

/**
 * Listener that initializes the cache cleanup on a dedicated thread.
 * 'cuz cron is so 90s.
 * And because we're deploying this to a cloud; the less external stuff we need to set up the better ;)
 */
 public class CacheCleanupServiceListener implements ServletContextListener {

    /**
     * This is the name of the init param we're looking for. Currently it's set to "Cache-Directory".
     */
    protected final String CACHE_DIR_PARAM_NAME = "Cache-Directory";

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        String cacheRoot = servletContextEvent.getServletContext().getInitParameter(CACHE_DIR_PARAM_NAME);
        scheduler.scheduleAtFixedRate(new FileCacheCleanup(cacheRoot), 0, 1, TimeUnit.HOURS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
         scheduler.shutdown();
    }
}
