package ui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import algorithms.BitReader;
import algorithms.HuffmanCompress;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HeaderPart extends Stage {
	public HeaderPart(HuffmanCompress comp, File compressed) throws IOException {
		VBox root = new VBox(10);
		root.setPadding(new Insets(10));

		Label formTitleL = new Label("🖍 Huffman Header");
		formTitleL.getStyleClass().add("card-title");
		Label formDescL = new Label("View The header of Huffman File\r\n");
		formDescL.getStyleClass().add("subtitle-dark");

		Card formCard = new Card();

		TextArea ta = new TextArea();
		ta.setPrefHeight(200);
		ta.setEditable(false);
		
		ta.setText(comp.ReadableHeader());
		
		
		TextArea ta2 = new TextArea();
		ta.setEditable(false);
		ta.setWrapText(true);
		
		BitReader br = new BitReader(compressed);
		StringBuilder bits = new StringBuilder();

		for (int i = 0; i < comp.getHeaderSize(); i++) {
		    bits.append(br.readBit());
		}		

		ta2.setText(bits.toString());
		ta.getStyleClass().add("complexity-area");
		ta2.getStyleClass().add("equation-box");
		ta2.setWrapText(true);
		ta2.setEditable(false);
		formCard.getChildren().addAll(formTitleL, formDescL, ta, ta2);


		root.getChildren().addAll(formCard);
		Scene scene = new Scene(root, 700, 600);
		scene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());
		this.setScene(scene);
		this.setTitle("Huffman Header");
		this.initModality(Modality.APPLICATION_MODAL); // يمنع التفاعل مع النافذة الرئيسية
		this.setResizable(false);
	}

}
