package com.github.simulatan.uncover;

import javafx.beans.property.DoubleProperty;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;

public interface Uncoverer {
	void uncover(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter, DoubleProperty progress) throws InterruptedException;
}
