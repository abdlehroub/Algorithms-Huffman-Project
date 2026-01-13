package ui;

import algorithms.HuffmanCompress;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HufmanInfoPage extends Stage {

	public HufmanInfoPage(HuffmanCompress comp) {
		VBox root = new VBox(10);
		root.setPadding(new Insets(10));

		Label formTitleL = new Label("🖍 Huffman Table");
		formTitleL.getStyleClass().add("card-title");
		Label formDescL = new Label("View The Table of Huffman Codes and Characters frequencies\r\n");
		formDescL.getStyleClass().add("subtitle-dark");

		Card formCard = new Card();

		TextArea ta = new TextArea();
		ta.setPrefHeight(1000);
		ta.setEditable(false);
		ta.getStyleClass().add("equation-box");

		formCard.getChildren().addAll(formTitleL, formDescL, ta);

		for (int i = -1; i < comp.getFreq().length; i++) {
			if (i == -1) {
				ta.setText(ta.getText().concat("Char\tFreq\tCode\t \t \t \t  Length\n"));
			} else {
				if (comp.getCodes()[i] != null) {
					String c = (char) i + "";
					if (i == 32)
						c = "Space";
					if (i == 9)
						c = "HT";
					if (i == 10)
						c = "LF";
					if (i == 13)
						c = "CR";
					ta.setText(ta.getText().concat(c + "\t" + comp.getFreq()[i] + "\t"
							+ String.format("%-20s", comp.getCodes()[i]) + "\t \t  " + comp.getCodes()[i].length())
							+ "\n");

				}
			}
		}

		root.getChildren().addAll(formCard);
		Scene scene = new Scene(root, 700, 600);
		scene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());
		this.setScene(scene);
		this.setTitle("Huffman Table");
		this.initModality(Modality.APPLICATION_MODAL); // يمنع التفاعل مع النافذة الرئيسية
		this.setResizable(false);
	}

}
