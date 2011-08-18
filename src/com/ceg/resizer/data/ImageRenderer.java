package com.ceg.resizer.data;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.ceg.resizer.util.ResizerOptions;

public interface ImageRenderer {
    public void resizeImage(File file, ResizerOptions options, OutputStream output) throws InterruptedException, IOException;
}
