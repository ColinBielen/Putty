package com.ceg.resizer.servlets;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.CoderResult;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ceg.resizer.data.ImageRenderer;
import com.ceg.resizer.util.URLFileGrabber;
import com.sun.net.httpserver.HttpPrincipal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceg.resizer.data.RenderFactory;
import com.ceg.resizer.util.ResizerOptions;

/**
 * Servlet that does the dynamic cropping and rescaling of ausr/l jpeg image.
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
public class RevisedImageServlet extends HttpServlet {


    private static final long serialVersionUID = -2296705384546611586L;

    private static Log log = LogFactory.getLog(ImageScalerServlet.class);

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

        log.debug("Servlet Path = '" + request.getPathInfo() + "'");
        String[] uriArray = request.getPathInfo().split("/");
        //String[] uriArray = url.getPath().split("/");
        ResizerOptions options = new ResizerOptions();
        /*
       Okay so if they follow our rules about url formatting we should end up with something like:

           http://thishost.com/images.eonline.com/eol_images/Entire_Site/2011522/reg_634.kim.k.lc.062211,90-90,0-0-293-293.jpg
           So splitting on "," means:
           [0] = images.eonline.com   (remote host the image is coming from)
           [1] = eol_images/Entire_Site/2011522/reg_634.kim.k.lc.062211 (path to file minus the extension)
           [2] = 90-90 (resize instructions)
           [3] = 0-0-293-293.jpg (crop instructions + extension).


        */
        //log.error("URI ARRAY SIZE ="+uriArray.length);
        String imageHost = "";
        StringBuffer pathBuffer = new StringBuffer();
        String fileName = "";
        for (int i = 1; i < uriArray.length; i++) {
            //     log.error("Array "+i+" = "+uriArray[i]);
            if (i == 1) {
                imageHost = uriArray[i];
            } else if (i + 1 == uriArray.length) {
                fileName = uriArray[i];
            } else {
                if (!uriArray[i].startsWith("/")) {
                    pathBuffer.append("/");
                }
                pathBuffer.append(uriArray[i]);
            }
        }

        String path = pathBuffer.toString();
        //reg_634.kim.k.lc.062211,90-90,0-0-293-293.jpg
        String fileNameArray[] = fileName.split(",");
        String fileBaseName = fileNameArray[0];
        String scaleInstructions = fileNameArray[1];
        String cropInstructionsAndExtension = fileNameArray[2];
        String cropInstructions = "";
        String fileExtension = "jpg";
        if (cropInstructionsAndExtension != null) {
            String[] tmpArray = cropInstructionsAndExtension.split("\\.");
            if (tmpArray.length > 0) {
                cropInstructions = tmpArray[0];
                fileExtension = tmpArray[1];
            }
        }

        log.debug("\tImage Host = '" + imageHost + "'");
        log.debug("\tPath = '" + path + "'");
        log.debug("\tfile base Name = '" + fileBaseName + "'");
        log.debug("\traw crop instructions (plus extension) = '" + cropInstructionsAndExtension + "'");
        log.debug("\tscale instructions = " + scaleInstructions);
        log.debug("\tcrop instructions = " + cropInstructions);
        log.debug("\tFile Extension = " + fileExtension);
        //String resizeArray[] = resizeInstructions.split("-");
        if (!scaleInstructions.isEmpty()) {
            String[] scaleAry = scaleInstructions.split("-");
            if (scaleAry.length > 1) {
                log.error("Scale instructions passed...");
                options.setWidth(scaleAry[0]);
                options.setHeight(scaleAry[1]);
            }
        }
        if (!cropInstructions.isEmpty()) {
            log.error("Crop instructions passed..");
            options.setCoordinates(cropInstructions);
        }
        // Reconstruct the URL of the original image sans formatting.
        StringBuffer imgURLBuffer = new StringBuffer();
        imgURLBuffer.append("http://")
                .append(imageHost)
                .append(path)
                .append("/")
                .append(fileBaseName).append(".")
                .append(fileExtension);

        String imgString = imgURLBuffer.toString();

        log.error("Original Image We're looking for: " + imgString);

        File imgFile = new URLFileGrabber().getFile(imgString);

        //File imgFile = new File(options.getFileRoot() + imgString);
        // Get the file locally and do a basic existence test:
        if (!imgFile.exists()) {
            log.error("Couldn't find image file at " + imgFile.getPath());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            response.flushBuffer();
            return;
        }

        // Leave all the file checking with the url object for now because it's
        // already working (hack, right?)
        if (!Pattern.matches(".*jpe?g", imgString)) {
            log.error("The extension " + imgString.substring(imgString.lastIndexOf(".")) + " for " + imgString
                    + " is not a jpg.");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            response.flushBuffer();
            return;
        }

        options.setEncodingQuality(request.getParameter("encodeQuality"));
        options.setRenderHint(request.getParameter("renderHint"));

        try {
            RenderFactory.getRenderer().resizeImage(imgFile, options, response.getOutputStream());
        } catch (InterruptedException ie) {
            throw new ServletException(ie.getMessage());
        }
    }
}
