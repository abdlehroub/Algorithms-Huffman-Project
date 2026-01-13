package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class DecompressionPart extends Card{
	private Button browseBtn;

	
	public DecompressionPart() {
		Label compressL = new Label("De-Compression");
		compressL.getStyleClass().add("card-title");
		
		Label decompressDescL = new Label(
				"Decompress .haf files by reversing Huffman encoding to restore the original data accurately.\n\n");
		decompressDescL.getStyleClass().add("subtitle-dark");
		browseBtn = new Button("Select File");

		
		browseBtn.setPrefWidth(200);


		
		HBox upperHp = new HBox();
		upperHp.getChildren().addAll(browseBtn);
		
		
		upperHp.setAlignment(Pos.CENTER);
		
		
		this.getChildren().addAll(compressL,decompressDescL, upperHp);
	}


	public Button getBrowseBtn() {
		return browseBtn;
	}


	public void setBrowseBtn(Button browseBtn) {
		this.browseBtn = browseBtn;
	}
	

}
