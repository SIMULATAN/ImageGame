package com.github.simulatan.uncover;

import javafx.application.Platform;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public enum UncoverMode {
	LEFT_TO_RIGHT(((width, height, pixelReader, pixelWriter) -> {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color color = pixelReader.getColor(x, y);
				if (color.getOpacity() > 0.0) {
					setPixel(x, y, color, pixelWriter);
				}
			}
			Thread.sleep(5000 / width);
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
						setPixel(x, y, color, pixelWriter);
					}
				}
				y = centerY + r;
				if (x >= 0 && x < width && y >= 0 && y < height) {
					Color color = pixelReader.getColor(x, y);
					if (color.getOpacity() > 0.0) {
						setPixel(x, y, color, pixelWriter);
					}
				}
			}
			for (int y = centerY - r + 1; y <= centerY + r - 1; y++) {
				int x = centerX - r;
				if (x >= 0 && x < width && y >= 0 && y < height) {
					Color color = pixelReader.getColor(x, y);
					if (color.getOpacity() > 0.0) {
						setPixel(x, y, color, pixelWriter);
					}
				}
				x = centerX + r;
				if (x >= 0 && x < width && y >= 0 && y < height) {
					Color color = pixelReader.getColor(x, y);
					if (color.getOpacity() > 0.0) {
						setPixel(x, y, color, pixelWriter);
					}
				}
			}
			Thread.sleep(5000 / width);
		}

		// Color the center pixel
		Color centerColor = pixelReader.getColor(centerX, centerY);
		if (centerColor.getOpacity() > 0.0) {
			pixelWriter.setColor(centerX, centerY, centerColor);
		}
	})),
	RANDOM(((width, height, pixelReader, pixelWriter) -> {
		Set<Integer> drawnPixels = new HashSet<>();
		for (int i = 0; i < width * height;) {
			int x = ThreadLocalRandom.current().nextInt(width);
			int y = ThreadLocalRandom.current().nextInt(height);
			int coord = x + y * width;
			if (!drawnPixels.contains(coord)) {
				Color color = pixelReader.getColor(x, y);
				if (color.getOpacity() > 0.0) {
					setPixel(x, y, color, pixelWriter);
				}
				drawnPixels.add(coord);
				i++;
				Thread.sleep(10000 / (width * height));
			}
		}
	}));

	private static void setPixel(int x, int y, Color color, PixelWriter pixelWriter) {
		Platform.runLater(() -> pixelWriter.setColor(x, y, color));
	}

	private final Uncoverer uncoverer;

	UncoverMode(Uncoverer uncoverer) {
		this.uncoverer = uncoverer;
	}

	public void uncover(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter) {
		try {
			uncoverer.uncover(width, height, pixelReader, pixelWriter);
		} catch (Exception ignored) {}
	}
}
