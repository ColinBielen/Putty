package com.ceg.resizer.data;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceg.resizer.util.ResizerOptions;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class DefaultImageRenderer implements ImageRenderer {
	
	private static Log log = LogFactory.getLog(DefaultImageRenderer.class);
	
        /**
         * Default Image Renderer. Uses the Graphics2D library. Very Basic.
         *
         *
         * @param file
         * @param width
         * @param height
         * @param x_coordinate
         * @param y_coordinate
         * @param x_offset
         * @param y_offset
         * @param encodeQuality
         * @param renderHint
         * @param output
         * @throws InterruptedException
         * @throws IOException
         */
        public void resizeImage(File file, ResizerOptions options, OutputStream output) throws InterruptedException, IOException {
                InputStream fis = new FileInputStream(file);
                try {
                        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(fis);
                        BufferedImage buffImg = decoder.decodeAsBufferedImage();
                        // crop image
                        if(options.requireCrop()) {
                        	buffImg = buffImg.getSubimage(options.getXCoordinate(),
                                        options.getYCoordinate(), options.getXOffset(), options.getYOffset());
                        }
                        Image cropImg = buffImg.getScaledInstance((int) (buffImg
                                        .getWidth()), (int) (buffImg.getHeight()),
                                        Image.SCALE_SMOOTH);
                        // resize image
                        BufferedImage resizeBuffImg = new BufferedImage(options.getWidth(), options.getHeight(), BufferedImage.TYPE_INT_RGB);
                        Graphics2D graphics2D = resizeBuffImg.createGraphics();
                        MediaTracker mediaTracker = new MediaTracker(new Container());
                        mediaTracker.addImage(cropImg, 0);
                        mediaTracker.waitForID(0);
                        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, options.getRenderHint());
                        graphics2D.drawImage(buffImg, 0, 0, options.getWidth(), options.getHeight(), null);
                        graphics2D.dispose();

                        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
                        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(resizeBuffImg);
                        param.setQuality(options.getEncodingQuality(), false);
                        encoder.setJPEGEncodeParam(param);
                        encoder.encode(resizeBuffImg);
                } catch (Exception e) {
                    log.error("Problem Rendering image from file '"+file+"'",e);
                } finally {
                        fis.close();
                }
                    log.debug("Generating a new cropped and resized image at " + options.getWidth()
                                + " x " + options.getHeight());
        }
}
