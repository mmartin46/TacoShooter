module Game {
	requires javafx.controls;
	requires javafx.graphics;
	requires java.xml;
	
	opens application to javafx.graphics, javafx.fxml;
}
