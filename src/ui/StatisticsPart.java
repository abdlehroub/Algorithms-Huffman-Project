package ui;

import java.io.File;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StatisticsPart extends Stage {
	public StatisticsPart(File original, File compressed, int headerSize, int bodySize) {
		VBox root = new VBox(10);
		root.setPadding(new Insets(10));

		Label formTitleL = new Label("🖍 Huffman Statistics");
		formTitleL.getStyleClass().add("card-title");
		Label formDescL = new Label("View Some Statistics About Files before and after compression.\r\n");
		formDescL.getStyleClass().add("subtitle-dark");

		TextArea dpRelationTa = new TextArea("Original Size = " + original.length() / 1024 + "KB\nCompressed Size = "
				+ compressed.length() / 1024 + "KB\nHeader Size = " + String.format("%.2f", headerSize / 1024.0)
				+ "KB\nData Size = " + String.format("%.2f", bodySize / 1024.0) + "KB\nCompression Ratio = "
				+ String.format("%.2f", (float) compressed.length() / original.length() * 100.0) + "%");
		dpRelationTa.setEditable(false);
		dpRelationTa.getStyleClass().add("text-area");

		Card formCard = new Card();
		formCard.getChildren().addAll(formTitleL, formDescL, dpRelationTa);

		root.getChildren().addAll(formCard);
		Scene scene = new Scene(root, 700, 300);
		scene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());
		this.setScene(scene);
		this.setTitle("Update TaskAdd Task");
		this.initModality(Modality.APPLICATION_MODAL); // يمنع التفاعل مع النافذة الرئيسية
		this.setResizable(false);
	}

}
