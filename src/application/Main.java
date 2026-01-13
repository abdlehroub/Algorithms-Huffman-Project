package application;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import algorithms.HuffmanCompress;
import algorithms.HuffmanDecompress;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.CompressionPart;
import ui.DecompressionPart;
import ui.HeaderPart;
import ui.HufmanInfoPage;
import ui.StatisticsPart;

public class Main extends Application {
	File file = null;
	File comp = null;

	@Override
	public void start(Stage stage) {

		// Header with a title and small description
		Label titleL = new Label("File Compression Application");
		titleL.getStyleClass().add("title");

		Label descriptionL = new Label("Compress and Decompress Files using Huffman Algorithm");
		descriptionL.getStyleClass().add("subtitle");

		VBox headerVb = new VBox(5, titleL, descriptionL);
		headerVb.getStyleClass().add("header-box");

		JFileChooser fileChooser = new JFileChooser();

		// Content container has all interface components
		VBox contentVb = new VBox(25);
		contentVb.setPadding(new Insets(30));
		contentVb.getStyleClass().add("content-wrapper");

//		----------------------------------------------------
		CompressionPart compPart = new CompressionPart();
		contentVb.getChildren().add(compPart);

//		----------------------------------------------------
		DecompressionPart decompPart = new DecompressionPart();
		contentVb.getChildren().add(decompPart);

//		----------------------------------------------------

		// Wrap root
		VBox root = new VBox();
		root.getChildren().addAll(headerVb, contentVb);

		// The main pane has all of the parts
		ScrollPane mainSp = new ScrollPane();
		mainSp.setContent(root);
		mainSp.setFitToWidth(true);

		Scene scene = new Scene(mainSp, 800, 700);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

		stage.setTitle("Daily Task Scheduling System");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();

		HuffmanCompress compress = new HuffmanCompress();
		compPart.getBrowseBtn().setOnAction(e -> {
			try {
				file = chooseFileForCompress(stage);
				boolean confirm = showConfirmation("Confirm Compression",
						"Are you sure you want to compress this file?\n\n" + file.getName());

				if (!confirm)
					return;

				comp = compress.compress(file);
				showSuccess("Compression Successful",
						"File compressed successfully!\n\nOutput file:\n" + comp.getName());

			} catch (IOException e1) {
				showError("IO Error!", e1.getMessage());
			} catch (NullPointerException e1) {
				showError("File Error!", "No File Selected!");
			}
		});

		compPart.getInfoBtn().setOnAction(e -> {
			if (comp != null) {
				HufmanInfoPage popup = new HufmanInfoPage(compress);
				popup.initOwner(stage);
				popup.showAndWait();
			} else {
				showError("IO Error!", "No File Selected!");
			}
		});

		compPart.getStatBtn().setOnAction(e -> {
			if (comp != null && file != null) {
				StatisticsPart p = new StatisticsPart(file, comp, compress.getHeaderSize(), compress.getDataSize());
				p.initOwner(stage);
				p.showAndWait();
			} else {
				showError("IO Error!", "No File Selected!");
			}
		});

		compPart.getHeaderBtn().setOnAction(e -> {
			if (comp != null) {
				try {
					HeaderPart hp = new HeaderPart(compress, comp);
					hp.initOwner(stage);
					hp.showAndWait();
				} catch (IOException e1) {
					showError("IO Error!", e1.getMessage());
				}
			} else {
				showError("IO Error!", "No File Selected!");
			}
		});

		decompPart.getBrowseBtn().setOnAction(e -> {
			try {
				File compressed = chooseFileForDecompress(stage);
				HuffmanDecompress decomp = new HuffmanDecompress();
				boolean confirm = showConfirmation("Confirm Decompression",
						"Are you sure you want to decompress this file?\n\n" + compressed.getName());

				if (!confirm)
					return;
				File result = decomp.decompress(compressed);
				showSuccess("Decompression Successful",
						"File decompressed successfully!\n\nOutput file:\n" + result.getName());
			} catch (IOException e1) {
				showError("IO Error!", e1.getMessage());
			}

		});

	}

	private File chooseFileForCompress(Stage owner) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select File to Compress");

		File file = chooser.showOpenDialog(owner);

		if (file == null)
			return null;

		String name = file.getName();
		int dot = name.lastIndexOf('.');

		if (dot == -1) {
			showError("Invalid File", "Selected file has no extension.");
			return null;
		}

		String ext = name.substring(dot + 1);

		if (ext.equalsIgnoreCase("huf")) {
			showError("Invalid File", "You cannot compress .huf files.");
			return null;
		}

		return file;
	}

	private File chooseFileForDecompress(Stage owner) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select .huf File to Decompress");

		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Huffman Files (*.huf)", "*.huf"));

		File file = chooser.showOpenDialog(owner);

		if (file == null)
			return null;

		if (!file.getName().toLowerCase().endsWith(".huf")) {
			showError("Invalid File", "Please select a .huf file only.");
			return null;
		}

		return file;
	}

	private void showError(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void showSuccess(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private boolean showConfirmation(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
	}

	public static void main(String[] args) {
		launch();
	}

}
