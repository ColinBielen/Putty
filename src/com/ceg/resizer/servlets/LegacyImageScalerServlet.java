package com.ceg.resizer.servlets;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ceg.resizer.util.URLFileGrabber;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceg.resizer.data.RenderFactory;
import com.ceg.resizer.util.ResizerOptions;

/**
 *
 * Servlet that does the dynamic cropping and rescaling of a jpeg image.
 * It's the same code used by the current EOL site and is here for backward
 * compatibility purposes. New users should really use the DefaultImageServlet
 * To use this, the servlet should be mapped to /* in web.xml. Then the
 * correct URI to go to in your web browser would be
 * http://www.eonline.com/resize/66/66/foo/bar/baz.jpg or
 * http://www.eonline.com/resize/66/66/50-50-200-200/foo/bar/baz.jpg * , where
 * /foo/bar/baz.jpg is a valid uri on www.eonline.com. Invalid width/height
 * values will return the default size and non-jpg extensions will return a 404
 * status code.
 * @see DefaultImageServlet for the preferred Servlet.
 */
public class LegacyImageScalerServlet extends HttpServlet {

	private static final long serialVersionUID = -2296705384546611585L;

	private static Log log = LogFactory.getLog(LegacyImageScalerServlet.class);

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer urlBuff = request.getRequestURL();
		URL url = new URL(urlBuff.toString());
		String[] uriArray = url.getPath().split("/");
		ResizerOptions options = new ResizerOptions();
		options.setWidth(uriArray[2]);
		options.setHeight(uriArray[3]);

		if (uriArray.length > 5) {
			options.setCoordinates(uriArray[4]);
		}

		// Reconstruct the URL of the image
		// StringBuffer imgURLBuffer = new StringBuffer(hostname);
		StringBuffer imgURLBuffer = new StringBuffer();
		/*
		 * array[0] is empty array[1] is "resize", ie the name of the filters
		 * pattern array[2] and array[3] are width and height so start our loop
		 * at 4 because that's where the url info begins unless we crop then
		 * start it at 5.
		 */
		for (int i = (options.requireCrop()?5:4); i < uriArray.length; i++) {
			imgURLBuffer.append("/");
			imgURLBuffer.append(uriArray[i]);
		}

		String imgString = imgURLBuffer.toString();
		// Hack to avoid circular references:
		// Syndication's done this for some reason:
		if (imgString.contains("/resize/")) {
			log.warn("URL contains a link to /resize/. It's a potential circular ref so I'm going to abort.");
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			response.flushBuffer();
			return;

		}
		
		//For legacy purposes we're assuming all images live at "images.eonline.com"
		// because that's where they're all published to. The new revised images API lets you
        //define your own host in the file path.
        //this is not a hack: This is "Backwards Compatability" :P
        imgString = "http://images.eonline.com"+imgString;

        URLFileGrabber fileGrabber = new URLFileGrabber();
        if(this.getServletContext().getInitParameter("LocalCacheDirectory") != null) {
            fileGrabber.setDownloadDir(this.getServletContext().getInitParameter("LocalCacheDirectory"));
        }
        File imgFile = fileGrabber.getFile(imgString);


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
