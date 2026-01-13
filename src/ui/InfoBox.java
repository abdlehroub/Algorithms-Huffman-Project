package ui;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class InfoBox extends Pane {

	public InfoBox (String content) {
		TextArea whereTa = new TextArea(content);
		whereTa.setEditable(false);
		whereTa.setWrapText(true);
		whereTa.getStyleClass().add("info-box");
		this.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());

		
	}
}
