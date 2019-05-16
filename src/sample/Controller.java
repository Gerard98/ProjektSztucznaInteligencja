package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Controller {

    @FXML
    private Pane mainPane,graphPane;
    @FXML
    private TextArea textArea;
    @FXML
    private ComboBox methodPicker;


    private List<Edge> listOfEdges = new LinkedList<>();
    private List<MineCircle> listOfCircles = new LinkedList<>();
    private int startNode;
    private int endNode;
    private int numberOfNodes;

    private boolean isFileLoaded = false;
    private File file;

    @FXML
    public void initialize() {
        graphPane.setStyle("-fx-background-color: white");
        textArea.setEditable(false);
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.add("BFS");
        observableList.add("DFS");
        methodPicker.setItems(observableList);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("CHUJ");
        //fileChooser.showOpenDialog(mainPane.getScene().getWindow());


    }


    public void loadEdgesFromFile(){
        try{
            Scanner in = new Scanner(file);

            numberOfNodes = in.nextInt();
            startNode = in.nextInt();
            endNode = in.nextInt();

            while(in.hasNext()){
                Edge edge = new Edge();
                edge.setNode1(in.nextInt());
                if(in.hasNext()){
                    edge.setNode2(in.nextInt());
                }
                listOfEdges.add(edge);
            }

        }
        catch (FileNotFoundException fx){

        }

    }

    public void drawGraph(){

        int y = 200;
        int x = 50;
        MineCircle startNode = new MineCircle(20, Color.GREEN);
        startNode.setIndex(1);
        startNode.setLayoutY(y+25);
        startNode.setLayoutX(x);
        listOfCircles.add(startNode);


        for(int i=2;i<numberOfNodes;i++){
            MineCircle circle = new MineCircle(15,Color.LIGHTGRAY);
            if(i%3 == 0){
                circle.setLayoutY(y + 75);
                circle.setLayoutX(x);
            }
            else if(i%3 == 1){
                circle.setLayoutY(y + 25);
                circle.setLayoutX(x);
            }
            else if(i%3 == 2){
                circle.setLayoutY(y - 25);
                x += 100;
                circle.setLayoutX(x);
            }
            circle.setIndex(i);
            listOfCircles.add(circle);
        }
        MineCircle endNode = new MineCircle(20, Color.RED);
        endNode.setIndex(8);
        endNode.setIndex(numberOfNodes);
        endNode.setLayoutY(y+25);
        endNode.setLayoutX(x+100);
        listOfCircles.add(endNode);



        listOfEdges.forEach(m -> {

            MineCircle circle1 = listOfCircles.stream()
                    .filter(n -> n.getIndex() == m.getNode1())
                    .findAny()
                    .orElse(null);

            MineCircle circle2 = listOfCircles.stream()
                    .filter(n -> n.getIndex() == m.getNode2())
                    .findAny()
                    .orElse(null);

            Line line = new Line();
            line.setStartX(circle1.getLayoutX());
            line.setStartY(circle1.getLayoutY());
            line.setEndX(circle2.getLayoutX());
            line.setEndY(circle2.getLayoutY());

            line.setFill(Color.BLACK);
            graphPane.getChildren().add(line);

        });

        listOfCircles.forEach(m -> {
            graphPane.getChildren().add(m);
            Label label = new Label(Integer.toString(m.getIndex()));
            label.setLayoutX(m.getLayoutX());
            label.setLayoutY(m.getLayoutY()-5);
            graphPane.getChildren().add(label);
        });


    }

    @FXML
    public void loadFileButton(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("CHUJ");
        file = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        if(file.getAbsolutePath().endsWith(".txt")){
            isFileLoaded = true;
            loadEdgesFromFile();
            drawGraph();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Plik musi być w formacie TXT");
            alert.show();
            isFileLoaded = false;
        }

    }

    public void DFS(){

    }

    public void BFS(){

    }

    @FXML
    public void resolve(){
        String method = (String) methodPicker.getSelectionModel().getSelectedItem();
        //System.out.println(method);
        try {
            switch (method) {
                case "BFS":

                    break;
                case "DFS":

                    break;
            }
        }
        catch (NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Wybierz metodę przeszukiwania grafu!!");
            alert.show();
        }
    }

}
