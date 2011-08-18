package com.ceg.resizer.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet flushes a file from the local "cache" filesystem.
 * Normally a cron script would take care of this, but in case of emergency you can
 * use this.
 */
public class FlushLocalCacheServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
