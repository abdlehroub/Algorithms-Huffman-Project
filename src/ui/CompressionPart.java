package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CompressionPart extends Card {
	private Button browseBtn;
	private Button statBtn;
	private Button infoBtn;
	private Button headerBtn;

	public CompressionPart() {
		Label compressL = new Label("Compression");
		compressL.getStyleClass().add("card-title");
		Label compressDescL = new Label(
				"Compress files using Huffman to reduce size by assigning shorter codes to frequently used data.\n\n");
		compressDescL.getStyleClass().add("subtitle-dark");
		browseBtn = new Button("Select File");
		statBtn = new Button("Statistics");
		infoBtn = new Button("Hufman Info");
		headerBtn = new Button("Hufman Header");

		browseBtn.setPrefWidth(200);
		statBtn.setPrefWidth(200);
		infoBtn.setPrefWidth(200);
		headerBtn.setPrefWidth(200);

		HBox upperHp = new HBox();
		HBox lowerHb = new HBox();
		upperHp.setSpacing(30);
		lowerHb.setSpacing(30);
		upperHp.getChildren().addAll(browseBtn, statBtn);
		lowerHb.getChildren().addAll(infoBtn, headerBtn);

		upperHp.setAlignment(Pos.CENTER);
		lowerHb.setAlignment(Pos.CENTER);

		this.getChildren().addAll(compressL,compressDescL ,upperHp, lowerHb);
	}

	public Button getBrowseBtn() {
		return browseBtn;
	}

	public void setBrowseBtn(Button browseBtn) {
		this.browseBtn = browseBtn;
	}

	public Button getStatBtn() {
		return statBtn;
	}

	public void setStatBtn(Button statBtn) {
		this.statBtn = statBtn;
	}

	public Button getInfoBtn() {
		return infoBtn;
	}

	public void setInfoBtn(Button infoBtn) {
		this.infoBtn = infoBtn;
	}

	public Button getHeaderBtn() {
		return headerBtn;
	}

	public void setHeaderBtn(Button headerBtn) {
		this.headerBtn = headerBtn;
	}
	
	

}
