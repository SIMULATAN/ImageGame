package com.github.simulatan;

import com.github.simulatan.uncover.UncoverMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class FxmlDocumentController {

	@FXML
	private ImageView imageView;
	@FXML
	public Button btnLoad;
	@FXML
	public Button btnStart;
	@FXML
	public Label labelPath;
	@FXML
	public ComboBox<UncoverMode> uncoverMode;
	private ImageDiscoverer discoverer;


	public void handleStartButton(ActionEvent actionEvent) {
		if (discoverer == null) {
			System.err.println("No image loaded - shouldn't happen because the button is disabled");
			return;
		}

		discoverer.reveal(uncoverMode.getValue());
	}


	public void handleLoadButton(ActionEvent actionEvent) {
		FileChooser fc = new FileChooser();

		fc.setTitle("Select Image");
		fc.setInitialDirectory(new File("."));
		FileChooser.ExtensionFilter efj = new FileChooser.ExtensionFilter("JPG files *.jpg", "*.jpg");
		FileChooser.ExtensionFilter efp = new FileChooser.ExtensionFilter("PNG files *.png", "*.png");

		fc.getExtensionFilters().add(efp);
		fc.getExtensionFilters().add(efj);

		File imageFile = fc.showOpenDialog(null);
		if (imageFile == null) return;

		labelPath.setText(imageFile.getName());

		Image img = new Image(imageFile.toURI().toString());
		setImage(img);
	}

	private void setImage(Image img) {
		if (discoverer != null) discoverer.shutdown();
		discoverer = new ImageDiscoverer(img);
		imageView.setImage(discoverer.getDestImage());
		btnStart.setDisable(false);
	}
}
