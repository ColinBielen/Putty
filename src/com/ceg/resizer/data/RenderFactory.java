package com.ceg.resizer.data;


/**
 * Figures out what renderer you want to use.
 */
public class RenderFactory {

    private static ImageRenderer renderer = new DefaultImageRenderer();


    /**
     * Inject whatever renderer you want.
     * @param newRenderer
     */
    public static void setRenderer(ImageRenderer newRenderer) {
        renderer = newRenderer;
    }


    /**
     * Gets whatever renderer you specified.
     * Defualt behavior should return the DefaultImageRenderer.
     * @return
     */
	public static ImageRenderer getRenderer() {
		return renderer;
	}
}
