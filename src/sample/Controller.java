package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
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
    @FXML
    private RadioButton moveNodes;


    private List<Edge> listOfEdges = new LinkedList<>();
    private List<MineCircle> listOfCircles = new LinkedList<>();
    private List<MineLine> listOfLines = new LinkedList<>();
    private List<MineLabel> listOfLabels = new LinkedList<>();
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
                circle.setLayoutX(x+50);
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

            MineLine line = new MineLine();
            line.setStartX(circle1.getLayoutX());
            line.setStartY(circle1.getLayoutY());
            line.setStartIndex(circle1.getIndex());
            line.setEndX(circle2.getLayoutX());
            line.setEndY(circle2.getLayoutY());
            line.setEndIndex(circle2.getIndex());

            line.setFill(Color.BLACK);
            listOfLines.add(line);
            graphPane.getChildren().add(line);

        });

        listOfCircles.forEach(m -> {
            graphPane.getChildren().add(m);
            MineLabel label = new MineLabel(Integer.toString(m.getIndex()));
            label.setLayoutX(m.getLayoutX()-5);
            label.setLayoutY(m.getLayoutY()-12);
            label.setFont(Font.font(16));
            label.setCircleIndex(m.getIndex());
            listOfLabels.add(label);
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

    public void solveByDFS(){

        Graph graph = new Graph(numberOfNodes);
        listOfEdges.forEach(m -> {
            graph.addEdge(m.getNode1()-1,m.getNode2()-1);
        });
        System.out.println(graph.toString());
        StringBuilder result = new StringBuilder("Result: ");
        DFSPaths dfs1 = new DFSPaths(graph, startNode-1);
        for (int it : dfs1.getPathTo(endNode-1)) {
            result.append((it+1) + " -> ");
        }
        result.delete(result.length()-3,result.length());
        textArea.setText(result.toString());
    }

    public void solveByBFS(){
        Graph graph = new Graph(numberOfNodes);
        listOfEdges.forEach(m -> {
            graph.addEdge(m.getNode1()-1,m.getNode2()-1);
        });
        System.out.println(graph.toString());
        StringBuilder result = new StringBuilder("Result: ");
        BFSPaths bfs1 = new BFSPaths(graph, startNode-1);
        for (int it : bfs1.getPathTo(endNode-1)) {
            result.append((it+1) + " -> ");
        }
        result.delete(result.length()-3,result.length());
        textArea.setText(result.toString());
    }

    @FXML
    public void resolve(){
        String method = (String) methodPicker.getSelectionModel().getSelectedItem();
        //System.out.println(method);
        switch (method) {
            case "BFS":
                solveByBFS();
                break;
            case "DFS":
                solveByDFS();
                break;
        }
        try {

        }
        catch (NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Wybierz metodę przeszukiwania grafu!!");
            alert.show();
        }
    }

    private EventHandler<MouseEvent> moveNodesEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            listOfCircles.forEach(m -> {
                System.out.println(event.getX() + " "+event.getY());
                System.out.println(m.getBoundsInParent());
                Point2D point = new Point2D(event.getX(), event.getY());
                if(m.getBoundsInParent().contains(point)){
                    m.setLayoutX(event.getX());
                    m.setLayoutY(event.getY());

                    listOfLines.forEach(n -> {
                        if(n.getStartIndex() == m.getIndex()){
                            n.setStartX(event.getX());
                            n.setStartY(event.getY());
                        }
                        else if(n.getEndIndex() == m.getIndex()){
                            n.setEndX(event.getX());
                            n.setEndY(event.getY());
                        }
                    });

                    listOfLabels.forEach(e -> {
                        if(e.getCircleIndex() == m.getIndex()){
                            e.setLayoutX(m.getLayoutX()-5);
                            e.setLayoutY(m.getLayoutY()-12);
                        }
                    });

                }
            });

        }
    };


    @FXML
    public void moveNodesChanged(){

        if(moveNodes.isSelected()){
            graphPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, moveNodesEvent);
        }
        else {
            graphPane.removeEventHandler(MouseEvent.MOUSE_DRAGGED, moveNodesEvent);
        }
    }

}
