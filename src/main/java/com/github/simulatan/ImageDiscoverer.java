package com.github.simulatan;

import com.github.simulatan.uncover.UncoverMode;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageDiscoverer {
	private final WritableImage destImage;
	private final Image srcImage;
	private final int width;
	private final int height;
	private final PixelWriter pixelWriter;
	private final PixelReader pixelReader;

	private final UncoverMode uncoverMode = UncoverMode.CIRCULAR_TO_CENTER;

	private Thread thread;

	public ImageDiscoverer(Image srcImage) {
		this.srcImage = srcImage;
		this.width = (int) srcImage.getWidth();
		this.height = (int) srcImage.getHeight();
		this.destImage = new WritableImage(width, height);
		this.pixelWriter = destImage.getPixelWriter();
		this.pixelReader = srcImage.getPixelReader();
	}

	public Image getDestImage() {
		initializeDestImage();
		return this.destImage;
	}

	private void initializeDestImage() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pixelWriter.setColor(x, y, Color.WHITE);
			}
		}
	}

	public void reveal() {
		initializeDestImage();
		if (thread != null) {
			thread.interrupt();
		}
		thread = new Thread(() -> this.uncoverMode.uncover(width, height, pixelReader, pixelWriter));
		thread.start();
	}
}
