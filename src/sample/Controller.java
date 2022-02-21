package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class Controller {

    @FXML
    private GridPane gridPane;

    @FXML
    private Label timer;

    @FXML
    private Label numBombs;


    private boolean isInit = false;
    private int NumClosedCells;
    private long time;
    private Timeline timeline;


    private void bindToTime() {

        time = 0;

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),  ae -> {

                    time++;
                    if (time <= 999){
                        timer.setText("" + time);
                    }

                }), new KeyFrame(Duration.millis(1000)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void removeEmptyCell(
            StackPane[][] stackPane, Button[][] buttons, Label[][] lbl, Set<String> numbs, int i, int j){
        try{
            if(lbl[i][j].getText().equals("0") && stackPane[i][j].getChildren().size() > 1){

                stackPane[i][j].getChildren().remove(buttons[i][j]);
                //NumClosedCells--;
                //System.out.println("NumClosedCells: " + NumClosedCells + "  " + i + " " + j);
                findAdjacentEmptyCells(stackPane, buttons, lbl, numbs, i, j);

            }
            else if (numbs.contains(lbl[i][j].getText())) {
                stackPane[i][j].getChildren().remove(buttons[i][j]);
                //NumClosedCells--;
                //System.out.println("NumClosedCells: " + NumClosedCells + "  " + i + " " + j);
            }
        }catch (Exception ex){}
    }

    private void findAdjacentEmptyCells(
            StackPane[][] stackPane, Button[][] buttons, Label[][] lbl, Set<String> numbs, int i, int j){

        if (lbl[i][j].getText().equals("0")){

            removeEmptyCell(stackPane, buttons, lbl, numbs,i - 1, j);
            removeEmptyCell(stackPane, buttons, lbl, numbs,i + 1, j);
            removeEmptyCell(stackPane, buttons, lbl, numbs, i, j - 1);
            removeEmptyCell(stackPane, buttons, lbl, numbs, i, j + 1);

        }

    }

    private void restartGame(){

        numBombs.setText("" + GameField.getNumBombs());
        NumClosedCells = 0;
        gridPane.getChildren().remove(1, gridPane.getChildren().size());
        timer.setText("0");
        initialize();

    }

    private Button defInitBtn(Button button, double minWidth, double minHeight){

        button = new Button("");
        button.setMinWidth(minWidth);
        button.setMinHeight(minHeight);

        return button;
    }

    private Label defInitLbl(Label label, double minWidth, double minHeight){

        label = new Label();
        label.setMinWidth(minWidth);
        label.setMinHeight(minHeight);
        label.setAlignment(Pos.CENTER);

        return label;
    }

    private void messageBox(String Title, String ContentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Title);
        alert.setHeaderText("");
        alert.setContentText(ContentText);
        alert.showAndWait();
    }

    private void CountClosedCells(StackPane[][] stackPane){
        NumClosedCells = 0;
        for(int i1 = 0; i1 < stackPane.length; i1++){
            for(int j1 = 0; j1 < stackPane[0].length; j1++){
                if (stackPane[i1][j1].getChildren().size() > 1)
                    NumClosedCells++;
            }
        }
    }

    private void RightClickOnMouse(Button[][] buttons, int index_i, int index_j){

        int NumNotFindBombs = 0;
        try {
            NumNotFindBombs = Integer.parseInt(numBombs.getText());
        }catch (Exception ex){}

        if(!buttons[index_i][index_j].getText().equals("F") && NumNotFindBombs > 0){

            buttons[index_i][index_j].setText("F");
            buttons[index_i][index_j].setStyle("-fx-background-color:#FFE4B5;");
            numBombs.setText("" + (NumNotFindBombs - 1) );

        }else if(buttons[index_i][index_j].getText().equals("F") && NumNotFindBombs < 10){

            buttons[index_i][index_j].setText("");
            buttons[index_i][index_j].setStyle("-fx-background-color:");
            numBombs.setText("" + (NumNotFindBombs + 1) );

        }
    }

    @FXML
    private void ClickbtnExit(){
        System.exit(0);
    }

    @FXML
    private void ClickbtnRepeat(){

        timeline.stop();
        restartGame();

    }

    @FXML
    private void initialize(){

        final int col = GameField.getColField();
        final int row = GameField.getRowField();
        final int NBombs = GameField.getNumBombs();

        isInit = false;

        Button[][] buttons = new Button[row][col];
        Label[][] lbl = new Label[row][col];
        StackPane[][] stackPane = new StackPane[row][col];

        Set<String> numbs = new HashSet<>();
        for(int i = 1; i < 9; i++)
            numbs.add("" + i);

        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){

                buttons[i][j] = defInitBtn(buttons[i][j], 33, 33);
                lbl[i][j] = defInitLbl(lbl[i][j], 36, 36);

                stackPane[i][j] = new StackPane(lbl[i][j], buttons[i][j]);

                gridPane.add(stackPane[i][j], i, j, 1,1);

                final int index_i = i;
                final int index_j = j;

                buttons[i][j].setOnMouseClicked(event -> {

                    if(event.getButton().equals(MouseButton.PRIMARY)){

                        if (!isInit) {

                            bindToTime();//init Timer

                            GameField gf = new GameField(index_i, index_j);

                            for (int it = 0; it < row; it++) {
                                for (int jt = 0; jt < col; jt++) {
                                    lbl[it][jt].setText("" + gf.getField()[it][jt]);
                                }
                            }

                            isInit = true;
                        }

                        stackPane[index_i][index_j].getChildren().remove(buttons[index_i][index_j]);

                        boolean isThisBomb = lbl[index_i][index_j].getText().equals("X");
                        if (isThisBomb){

                            try {
                                TimeUnit.SECONDS.sleep(1);
                            }
                            catch (Exception ex){}

                            timeline.stop();
                            messageBox( "Information",  "GAME OVER");
                            restartGame();
                        }
                        else if (lbl[index_i][index_j].getText().equals("0")){

                            findAdjacentEmptyCells(stackPane, buttons, lbl, numbs, index_i, index_j);

                        }

                        CountClosedCells(stackPane);

                        System.out.println("NumClosedCells: " + (NumClosedCells - NBombs));



                        if(NumClosedCells - NBombs == 0 && !isThisBomb){
                            timeline.stop();
                            messageBox( "Information",  "Congratz, bro!!!");
                            restartGame();

                        }

                    }
                    else if(event.getButton().equals(MouseButton.SECONDARY)){

                        RightClickOnMouse(buttons, index_i, index_j);

                    }
                });

            }
        }

    }


}
