package com.github.simulatan.uncover;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public enum UncoverMode {
	LEFT_TO_RIGHT(((width, height, pixelReader, pixelWriter) -> {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color color = pixelReader.getColor(x, y);
				if (color.getOpacity() > 0.0) {
					pixelWriter.setColor(x, y, color);
				}
			}
			Thread.sleep(100);
		}
	})),
	CIRCULAR_TO_CENTER(((width, height, pixelReader, pixelWriter) -> {
		int centerX = width / 2;
		int centerY = height / 2;
		for (int r = 0; r < Math.min(centerX, centerY); r++) {
			for (int x = centerX - r; x <= centerX + r; x++) {
				for (int y = centerY - (int) (r * (double) centerY / centerX); y <= centerY + (int) (r * (double) centerY / centerX); y++) {
					if (x >= 0 && x < width && y >= 0 && y < height) {
						Color color = pixelReader.getColor(x, y);
						if (color.getOpacity() > 0.0) {
							pixelWriter.setColor(x, y, color);
						}
					}
				}
			}
			Thread.sleep(100);
		}
	})),
	RANDOM(((width, height, pixelReader, pixelWriter) -> {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color color = pixelReader.getColor(x, y);
				if (color.getOpacity() > 0.0) {
					pixelWriter.setColor(x, y, color);
				}
			}
			Thread.sleep(10);
		}
	}));

	private final Uncoverer uncoverer;

	UncoverMode(Uncoverer uncoverer) {
		this.uncoverer = uncoverer;
	}

	public void uncover(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter) {
		try {
			uncoverer.uncover(width, height, pixelReader, pixelWriter);
		} catch (InterruptedException ignored) {}
	}
}
