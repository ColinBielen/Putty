package com.ceg.resizer.filters;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Filter access via API keys.
 *
 * This is meant for use in places where we don't have an external API validation proxy (for example on a public or cloud environment)
 * Valid keys are registered in the filter config by defining a comma-delmited list as the "api-keys" config param.
 *
 *  Keys are passed either as a request parameter (called "api-key") or
 *  in a request header ("x-ptg-api-key")
 *
 *
 */

public class APIKeyFilter implements Filter {

    private static Log log = LogFactory.getLog(APIKeyFilter.class);

    public static Map apiKeyMap = new HashMap();

    public static final String CONFIG_PARAMETER_NAME = "api-keys";

    public static final String REQUEST_PARAM_NAME = "api-key";
    public static final String REQUEST_HEADER_NAME = "x-ptg-api-key";


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String keyString = (String)filterConfig.getInitParameter(CONFIG_PARAMETER_NAME);

        synchronized (filterConfig) {
            Map m = parseString(keyString);
            apiKeyMap.putAll(m);
        }

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String apiKey = getAPIKey(servletRequest);
        log.error("API KEY = "+apiKey);
        if(!isValid(apiKey)) {
          HttpServletResponse rs = (HttpServletResponse) servletResponse;
            rs.sendError(401, "Invalid api key");
            rs.flushBuffer();
        } else {
            filterChain.doFilter(servletRequest,servletResponse);
        }

    }

    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Grabs the API key.
     *
     * @param r
     */
    protected String getAPIKey(ServletRequest r) {
        String apiKey = null;
       //first look in the request param
       if(r.getParameter(REQUEST_PARAM_NAME) != null && (!r.getParameter(REQUEST_PARAM_NAME).isEmpty())) {
            apiKey = r.getParameter(REQUEST_PARAM_NAME);
       }
       if(apiKey == null) {
         //now check the request headers.
         try {
             HttpServletRequest cast = (HttpServletRequest)r;
             apiKey = cast.getHeader(REQUEST_HEADER_NAME);
         } catch (ClassCastException e) {
             //Really? What other subtypes of Serlvet are there?
         }
       }
       // not sure what else we might want to check yet...

       return apiKey;
    }

    protected boolean isValid(String apiKey) {
        return apiKeyMap.containsKey(apiKey);
    }


    /**
     * Parses the comma delimited string to a map for easy lookups.
     * @param s
     * @return
     */
    protected Map parseString(String s) {
        Map m = new HashMap();
        String[] result = s.split(",");
        for (int i=0; i<result.length; i++) {
            m.put(result[i],new Integer(i));
        }
        return m;
    }
}
