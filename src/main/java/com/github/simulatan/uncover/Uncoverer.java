package com.github.simulatan.uncover;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;

public interface Uncoverer {
	void uncover(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter) throws InterruptedException;
}
