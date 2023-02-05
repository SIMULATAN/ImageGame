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
	CIRCULAR_FROM_CENTER(((width, height, pixelReader, pixelWriter) -> {
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
	CIRCULAR_TO_CENTER(((width, height, pixelReader, pixelWriter) -> {
		int centerX = width / 2;
		int centerY = height / 2;
		int radius = (int) Math.min(centerX / Math.cos(Math.toRadians(45)), centerY / Math.cos(Math.toRadians(45)));
		for (int r = radius; r > 0; r--) {
			for (int x = centerX - r; x <= centerX + r; x++) {
				int y = centerY - r;
				if (x >= 0 && x < width && y >= 0 && y < height) {
					Color color = pixelReader.getColor(x, y);
					if (color.getOpacity() > 0.0) {
						pixelWriter.setColor(x, y, color);
					}
				}
				y = centerY + r;
				if (x >= 0 && x < width && y >= 0 && y < height) {
					Color color = pixelReader.getColor(x, y);
					if (color.getOpacity() > 0.0) {
						pixelWriter.setColor(x, y, color);
					}
				}
			}
			for (int y = centerY - r + 1; y <= centerY + r - 1; y++) {
				int x = centerX - r;
				if (x >= 0 && x < width && y >= 0 && y < height) {
					Color color = pixelReader.getColor(x, y);
					if (color.getOpacity() > 0.0) {
						pixelWriter.setColor(x, y, color);
					}
				}
				x = centerX + r;
				if (x >= 0 && x < width && y >= 0 && y < height) {
					Color color = pixelReader.getColor(x, y);
					if (color.getOpacity() > 0.0) {
						pixelWriter.setColor(x, y, color);
					}
				}
			}
			Thread.sleep(100);
		}

		// Color the center pixel
		Color centerColor = pixelReader.getColor(centerX, centerY);
		if (centerColor.getOpacity() > 0.0) {
			pixelWriter.setColor(centerX, centerY, centerColor);
		}
	})),
	RANDOM(((width, height, pixelReader, pixelWriter) -> {
		for (int i = 0; i < width * height; i++) {
			int x = (int) (Math.random() * width);
			int y = (int) (Math.random() * height);
			Color color = pixelReader.getColor(x, y);
			if (color.getOpacity() > 0.0) {
				pixelWriter.setColor(x, y, color);
			}
			Thread.sleep(20);
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
