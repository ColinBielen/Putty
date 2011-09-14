package com.ceg.resizer.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * This Filter rejects requests that don't come from a specific set of hostnames or IP addresses.
 * The idea is that clients have proxies sitting in front of this service that do CDN/Varnish caching,
 * accellerator, and URL rewriting.
 *
 * We also don't want to open this up to the world. Yet :)

 */
public class AllowedHostnameFilter implements Filter {
    static final Log log = LogFactory.getLog(AllowedHostnameFilter.class);
    private Map allowedHostMap = new HashMap();


    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Initializing AllowedHostnameFilter..");
        String hostString = filterConfig.getInitParameter("allowedHosts");
        StringTokenizer st = new StringTokenizer(hostString, ",");
        while (st.hasMoreTokens()) {
            allowedHostMap.put(st.nextToken(), new Integer(1));
        }

    }


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String remoteHostname = servletRequest.getRemoteHost();
        if (!allowedHostMap.containsKey(remoteHostname)) {
            HttpServletResponse rs = (HttpServletResponse) servletResponse;
            rs.sendError(401, "Hostname '" + remoteHostname + "' is not authorized for this application");
            rs.flushBuffer();
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public void destroy() {
        //Umm... noop?
    }
}
