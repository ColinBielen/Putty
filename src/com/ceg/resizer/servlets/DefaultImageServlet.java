package com.ceg.resizer.servlets;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.Element;

import com.ceg.resizer.data.ImageRenderer;
import com.ceg.resizer.util.URLFileGrabber;
import com.sun.tools.internal.ws.processor.model.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceg.resizer.data.RenderFactory;
import com.ceg.resizer.util.ResizerOptions;

/**
 * Servlet that does the dynamic cropping and rescaling of a jpeg image.
 * <p/>
 * This uses an updated URL format that can service multiple hosts and is easier to maintain if you're
 * using a CDN or other cache in between this app and any user-end traffic (which you SHOULD be doing!)
 * Here's the new format:
 * <pre>
 * http:[host]/[orignal_image_host]/[image_path]/[file_name].[scaling_instructions],[cropping_instructions].[file_extension]
 * where:
 * host = the host this code will be running on.
 * original_image_host = host server serving the original image we're processing.
 * image_path = path to the image on the original server
 * scaling instructions = [width]-[height] resize (e.g. 90_90)
 * cropping instructions = [x_origin]-[y_origin]-[x_offset]-[y_offset]
 *      (e.g. 100-100-193-193 => "start at position 100,100 on the image and crop it at position 293,293)
 * file extension= ".jpg",etc/ Only jpg is supported right now :/
 * </pre>
 */
public class DefaultImageServlet extends HttpServlet {


    private static final long serialVersionUID = -2296705384546611586L;

    private static Log log = LogFactory.getLog(LegacyImageScalerServlet.class);

    //We're making this configurable now because the default impl is kind of rough.
    private ImageRenderer renderer = null;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //Check and see  if they specified an ImageRenderer class:
        String renderClassName = config.getInitParameter("ImageRendererClass");
        if ((renderClassName != null) && (!renderClassName.isEmpty())) {
            try {
                //Load the thing into the ClassLoader.
                Class renderClazz = Class.forName(renderClassName);
                RenderFactory.setRenderer((ImageRenderer) renderClazz.newInstance());
            } catch (Exception e) {
                log.error("Problem adding Image Renderer " + renderClassName + "'");
            }
        } else {
            log.info("No Image Renderer defined. Using default...");
        }

    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        StringTokenizer st = new StringTokenizer(request.getPathInfo(),"\\/");
        int position =0;
        StringBuffer imgURLBuffer = new StringBuffer("http://");
        while(st.hasMoreTokens()) {
            if(position==0) {
                imgURLBuffer.append(st.nextToken());
                ++position;
            } else {
                imgURLBuffer.append("/"+st.nextToken());
            }
        }

        String imgString = imgURLBuffer.toString();

        log.error("Original Image We're looking for: " + imgString);


        URLFileGrabber fileGrabber = new URLFileGrabber();
        if(this.getServletContext().getInitParameter("LocalCacheDirectory") != null) {
            fileGrabber.setDownloadDir(this.getServletContext().getInitParameter("LocalCacheDirectory"));
        }
        File imgFile = fileGrabber.getFile(imgString);



        //File imgFile = new File(options.getFileRoot() + imgString);
        // Get the file locally and do a basic existence test:
        if (!imgFile.exists()) {
            log.error("Couldn't find image file at " + imgFile.getPath());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("message","Couldn't find a file at '"+imgString+"'");
            response.flushBuffer();
            return;
        }

        // Leave all the file checking with the url object for now because it's
        // already working (hack, right?)
        if (!Pattern.matches(".*jpe?g", imgString)) {
            log.error("The extension " + imgString.substring(imgString.lastIndexOf(".")) + " for " + imgString
                    + " is not a jpg.");
            response.addHeader("X-Putty-Rendering-Notes"," File extension is not .jpg. This image format isn't currently supported.");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            response.flushBuffer();
            return;
        }

        ResizerOptions options = this.parseOptions(request);

        options.setEncodingQuality(request.getParameter("encodeQuality"));
        options.setRenderHint(request.getParameter("renderHint"));
        log.info("Rendering here!!");
        try {
           response.addHeader("X-Putty-Rendering-Notes",
                    "Image resized to "+options.getWidth()+"x"+options.getHeight()+","
                    +" and cropped to "+options.getXCoordinate()+","+options.getYCoordinate()+","
                    +options.getXOffset()+","+options.getYOffset());
            RenderFactory.getRenderer().resizeImage(imgFile, options, response.getOutputStream());
            //Senda response header telling them what we did in case it's different than
            //what was expected.

        } catch (InterruptedException ie) {
            throw new ServletException(ie.getMessage());
        }
    }

    /**
     * Parses the resizer options.
     * Pre-factored to its own method so we can modify it.
     * @return
     */
    protected ResizerOptions parseOptions(HttpServletRequest request) {


               String[] uriArray = request.getPathInfo().split("/");
               //String[] uriArray = url.getPath().split("/");
               ResizerOptions options = new ResizerOptions();

                    if(hasValidResizeOptions(request)) {
                        options.setWidth(request.getParameter("width"));
                        options.setHeight(request.getParameter("height"));
                    }
                    if(hasValidCropOptions(request)) {
                        options.setCoordinates(
                                request.getParameter("x1")+"-"+
                                request.getParameter("y1")+"-"+
                                request.getParameter("x2")+"-"+
                                request.getParameter("x2")+"-"
                        );
                    }


        return options;

    }

    protected boolean hasValidResizeOptions(HttpServletRequest r) {
            if(isNullOrEmpty(r.getParameter(("width")))) { return false; }
            if(isNullOrEmpty(r.getParameter(("width")))) { return false; }
            return true;
    }

    protected boolean hasValidCropOptions(HttpServletRequest r) {
            if(isNullOrEmpty(r.getParameter(("x1")))) { return false; }
            if(isNullOrEmpty(r.getParameter(("x2")))) { return false; }
            if(isNullOrEmpty(r.getParameter(("y1")))) { return false; }
            if(isNullOrEmpty(r.getParameter(("y2")))) { return false; }
            return true;

    }

    private boolean isNullOrEmpty(String s) {
        if(s == null) {
            return true;
        } else {
            return s.isEmpty();
        }
    }

} //end servlet
