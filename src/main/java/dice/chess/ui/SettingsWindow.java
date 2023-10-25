package dice.chess.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class SettingsWindow extends Group {
    
    @FXML
    private final VBox vBox;
    private final CheckBox checkBoxVar;
    private final VBox vBox2;
    private final CheckBox checkBoxVar2;
    private final VBox vBox5;
    private final CheckBox checkBoxVar5;
    private final VBox vBox3;
    private final CheckBox checkBoxVar3;
    private final CheckBox checkBoxVar4;


    public SettingsWindow()  {
        
        this.vBox = new VBox();
        this.checkBoxVar = new CheckBox("Highlight Possible Moves per Piece");
        this.checkBoxVar.setFont(Font.font("Copperplate", 16));
        this.checkBoxVar.getStyleClass().add("checkBox");

        this.vBox2 = new VBox();
        this.checkBoxVar2 = new CheckBox("Sound effects");
        this.checkBoxVar2.setFont(Font.font("Copperplate", 16));
        this.checkBoxVar2.getStyleClass().add("checkBox");

        this.vBox5 = new VBox();
        this.checkBoxVar5 = new CheckBox("Animate moves");
        this.checkBoxVar5.setFont(Font.font("Copperplate", 16));
        this.checkBoxVar5.getStyleClass().add("checkBox");

        this.vBox3 = new VBox();
        this.checkBoxVar3 = new CheckBox("Remove 2 pawns");
        this.checkBoxVar3.setFont(Font.font("Copperplate", 16));
        this.checkBoxVar3.getStyleClass().add("checkBox");

        this.checkBoxVar4 = new CheckBox("Remove 1 knight");
        this.checkBoxVar4.setFont(Font.font("Copperplate", 16));
        this.checkBoxVar4.getStyleClass().add("checkBox");
        renderMenu();
    }
    
    public static void display() {
        
        try {
            // Load the FXML for the chess board
            Parent root;
            URL settingsUrl = new File("src/main/java/dice/chess/ui/settingsWindow.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(settingsUrl);
            root = loader.load();
            Scene scene = new Scene(root, 400, 400);
            URL cssUrl = new File("src/main/java/dice/chess/ui/application.css").toURI().toURL();
            scene.getStylesheets().add(cssUrl.toString());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Settings");
            stage.show();
            root.getChildrenUnmodifiable();
            root.requestFocus();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void saveSettings(boolean isSelected,boolean isSelected2, boolean isSelected3,boolean isSelected4,boolean isSelected5) {
        
        Settings.setHighlightPieceMoves(isSelected);
        Settings.setSoundEffects(isSelected2);
        Settings.setAnimatesMoves(isSelected5);
        Settings.setChallenge1(isSelected3);
        Settings.setChallenge2(isSelected4);
    }
    
    private void renderMenu() {
        
        Button button = new Button();
        this.checkBoxVar.setSelected(Settings.isHighlightPieceMoves());
        this.vBox.getChildren().addAll(this.checkBoxVar, button);
        this.vBox.prefHeight(200);
        this.vBox.prefWidth(200);
        this.vBox.setSpacing(100);
        this.getChildren().add(this.vBox);

        this.checkBoxVar2.setSelected(Settings.isAddSoundEffects());
        this.checkBoxVar2.setTranslateY(14);
        this.vBox2.getChildren().addAll(this.checkBoxVar2);
        this.vBox2.prefHeight(200);
        this.vBox2.prefWidth(200);
        this.vBox2.setSpacing(100);
        this.vBox2.setTranslateY(14);
        this.getChildren().add(this.vBox2);

        this.checkBoxVar5.setSelected(Settings.isAnimateMoves());
        this.checkBoxVar5.setTranslateY(14);
        this.vBox5.getChildren().addAll(this.checkBoxVar5);
        this.vBox5.prefHeight(200);
        this.vBox5.prefWidth(200);
        this.vBox5.setSpacing(100);
        this.vBox5.setTranslateY(40);
        this.getChildren().add(this.vBox5);
        
        this.checkBoxVar3.setSelected(Settings.isChallenge1());
        this.vBox3.prefHeight(200);
        this.vBox3.prefWidth(200);
        this.vBox3.setSpacing(100);
        this.checkBoxVar4.setSelected(Settings.isChallenge2());
        this.checkBoxVar4.setTranslateY(30);
        this.vBox3.getChildren().addAll(this.checkBoxVar3, this.checkBoxVar4);

        Rectangle rectangle = new Rectangle(20.0d, 60.0d, 280.0d, 130.0d);
        rectangle.setFill(Color.WHITE);
        rectangle.setArcHeight(10.0d);
        rectangle.setArcWidth(10.0d);

        Image i = new Image("File:src/main/resources/menu/crown_48px.png");
        ImageView iw = new ImageView(i);
        Label icon = new Label("Challenge mode", iw);
        icon.setFont(Font.font("Copperplate", FontWeight.BOLD, FontPosture.REGULAR, 16));

        StackPane stack = new StackPane();
        stack.setTranslateX(60);
        stack.setTranslateY(100);
        stack.setAlignment(icon, Pos.TOP_LEFT);
        stack.getChildren().addAll(rectangle,vBox3, checkBoxVar3,checkBoxVar4,icon);
        stack.setVisible(true);
        this.getChildren().add(stack);

        button.setText("Save");
        button.setFont(Font.font("Copperplate", 20));
        button.setPrefHeight(50);
        button.setPrefWidth(200);
        button.setTranslateX(100);
        button.setTranslateY(120);
        button.getStyleClass().add("saveSettingsButton");
    
        button.setOnMouseClicked(event -> {
            saveSettings(this.checkBoxVar.isSelected(),this.checkBoxVar2.isSelected(), this.checkBoxVar3.isSelected(),this.checkBoxVar4.isSelected(),this.checkBoxVar5.isSelected());
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();});
    }
}
