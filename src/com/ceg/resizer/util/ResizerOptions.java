package com.ceg.resizer.util;

import java.awt.RenderingHints;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceg.properties.beans.data.Property;
import com.ceg.properties.helpers.PropertyHelper;

public class ResizerOptions {
	private static Log log = LogFactory.getLog(ResizerOptions.class);
	private static final String DEFAULT_IMAGE_FILE_ROOT = "/www/www.eonline.com/images/";
	private static final int DEFAULT_WIDTH = 66;
	private static final int DEFAULT_HEIGHT = 66;
	private static final float DEFAULT_ENCODE_QUALITY = .90f;
	
	private int width,height,x_coordinate,y_coordinate,x_offset,y_offset;
	private float encodingQuality;
	private boolean requireCrop;
	private String fileRoot;
	private Object renderHint;
	
	public Object getRenderHint() {
		return renderHint;
	}

	public void setRenderHint(String renderHint) {
		if (renderHint == null) {
			this.renderHint = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
		} else if (renderHint.equals("bicubic")) {
			this.renderHint = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
		} else if (renderHint.equals("bilinear")) {
			this.renderHint = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
		} else {
			// This one really isn't very good.
			this.renderHint = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
		}
	}

	public String getFileRoot() {
		return fileRoot;
	}

	public void setFileRoot(String fileRoot) {
		if(isNullOrEmpty(fileRoot)) this.fileRoot = fileRoot;
		else log.error("Attempted to pass empty file root.  Defaulting to "+this.fileRoot);
	}

	public ResizerOptions() {
		// Some default settings
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		x_coordinate = 0;
		y_coordinate = 0;
		x_offset = 0;
		y_offset = 0;
		encodingQuality = DEFAULT_ENCODE_QUALITY;
		fileRoot = DEFAULT_IMAGE_FILE_ROOT;
		requireCrop = false;
		Property fileRootProp = null;
		try {
			fileRootProp = PropertyHelper.getPropertyByName("image.resizer.file.root");
			fileRoot = fileRootProp.getValue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Property: 'image.resizer.file.root' not found.  Defaulting to "+DEFAULT_IMAGE_FILE_ROOT);
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(String width) {
		try {
			this.width = Integer.parseInt(width);
			if (this.width <= 0) {
				log.error("Width " + width
						+ " is less than 0; reverting to default size "
						+ DEFAULT_WIDTH);
				this.width = DEFAULT_WIDTH;
			}
		} catch (NumberFormatException nfe) {
			log.error("Width " + width
				+ " is an invalid size; reverting to default size "
				+ width);
		}
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(String height) {
		try {
			this.height = Integer.parseInt(height);
			if (this.height <= 0) {
				log.error("Height " + height
						+ " is less than 0; reverting to default size "
						+ DEFAULT_HEIGHT);
				this.height = DEFAULT_HEIGHT;
			}
		} catch (NumberFormatException nfe) {
			log.error("Height " + height
				+ " is an invalid size; reverting to default size "
				+ height);
		}
	}

	public int getXCoordinate() {
		return x_coordinate;
	}

	public void setXCoordinate(String x_coordinate) {
		try {
			this.x_coordinate = Integer.parseInt(x_coordinate);
		} catch (NumberFormatException nfe) {
			requireCrop = false;
			log.error("X Coordinate " + x_coordinate
					+ " is an invalid size; reverting to default size "
					+ this.x_coordinate);
		}
	}

	public int getYCoordinate() {
		return y_coordinate;
	}

	public void setYCoordinate(String y_coordinate) {
		try {
			this.y_coordinate = Integer.parseInt(y_coordinate);
		} catch (NumberFormatException nfe) {
			requireCrop = false;
			log.error("Y Coordinate " + y_coordinate
					+ " is an invalid size; reverting to default size "
					+ this.y_coordinate);
		}
	}
	
	public void setCoordinates(String coordinates) {
		if(coordinates.indexOf("-") != -1) {
			String[] x_y_coor = coordinates.split("-");
			if (x_y_coor != null && x_y_coor.length == 4) {
				this.requireCrop = true;
				setXCoordinate(x_y_coor[0]);
				setYCoordinate(x_y_coor[1]);
				setXOffset(x_y_coor[2]);
				setYOffset(x_y_coor[3]);
			}
		}
	}

	public int getXOffset() {
		return x_offset;
	}

	public void setXOffset(String x_offset) {
		try {
			this.x_offset = Integer.parseInt(x_offset);
		} catch (NumberFormatException nfe) {
			log.error("X offset " + x_offset
					+ " is an invalid size; use width as default size "
					+ width);
			this.x_offset = width;
		}
	}

	public int getYOffset() {
		return y_offset;
	}

	public void setYOffset(String y_offset) {
		try {
			this.y_offset = Integer.parseInt(y_offset);
		} catch (NumberFormatException nfe) {
			log
					.error("Y offset "
							+ y_offset
							+ " is an invalid size; use height as default size "
							+ height);
			this.y_offset = height;
		}

	}

	public float getEncodingQuality() {
		return encodingQuality;
	}

	public void setEncodingQuality(String encodeHint) {
		try {
			this.encodingQuality = Float.parseFloat(encodeHint);
		} catch (Exception e) {
			log.warn("Bad encodeQuality param passed. Sticking with default");
		}
	}

	public boolean requireCrop() {
		return requireCrop;
	}

	public void setRequireCrop(boolean requireCrop) {
		this.requireCrop = requireCrop;
	}

    private boolean isNullOrEmpty(String fileRoot) {
        return (fileRoot == null) || (fileRoot.isEmpty());
    }
}
