package dice.chess.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;


public class MenuControllerWindow {
    
    @FXML
    private Button button1 = new Button("Human vs Human");
    
    @FXML
    void button1Handle(MouseEvent event) throws IOException {
        
        loadBoard("Human vs Human", "mode1");
    }
    
    @FXML
    void button2Handle(MouseEvent event) throws IOException {
        loadBoard("Human vs Computer", "mode2");
    }
    
    @FXML
    void button3Handle(MouseEvent event) throws IOException {
        loadBoard("Computer vs Computer", "mode3");
    }
    
    @FXML
    void button4Handle(MouseEvent event) {
        SettingsWindow.display();
    }
    
    private void loadBoard(String title, String mode) {
        
        ChessBoardWindow.display("Chess Board" + " (" + title + ")", mode);
    }
    
}
