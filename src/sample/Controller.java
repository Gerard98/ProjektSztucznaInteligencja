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
import java.util.*;
import java.util.List;

public class Controller {

    @FXML
    private Pane mainPane,graphPane;
    @FXML
    private TextArea textArea;
    @FXML
    private ComboBox methodPicker;
    @FXML
    private RadioButton moveNodes, addEdge, addNode;


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

    public void clean(){
        graphPane.getChildren().clear();
        listOfEdges.clear();
        listOfLabels.clear();
        listOfCircles.clear();
        listOfLines.clear();
    }

    public void loadEdgesFromFile(){
        try{

            clean();

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
        if(file != null) {
            if (file.getAbsolutePath().endsWith(".txt")) {
                isFileLoaded = true;
                loadEdgesFromFile();
                drawGraph();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Plik musi być w formacie TXT");
                alert.show();
                isFileLoaded = false;
            }
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
        try {
        switch (method) {
            case "BFS":
                if(listOfEdges.size() > 2){
                    solveByBFS();
                }
                break;
            case "DFS":
                if(listOfEdges.size() > 2){
                    solveByDFS();
                }
                break;
        }
        }
        catch (NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Wybierz metodę przeszukiwania grafu!!");
            alert.show();
        }
    }

    private boolean circleDragged = false;
    private EventHandler<MouseEvent> moveNodesEvent = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            for(MineCircle m : listOfCircles){
                Point2D point = new Point2D(event.getX(), event.getY());
                if(m.getBoundsInParent().contains(point) && !circleDragged){
                    circleDragged = true;
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
            }
            circleDragged = false;
        }
    };

    private EventHandler<MouseEvent> addNodeHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            MineCircle mineCircle = new MineCircle(15,Color.LIGHTGRAY);
            mineCircle.setLayoutX(event.getX());
            mineCircle.setLayoutY(event.getY());
            try {
                int maxIndex = listOfCircles
                        .stream()
                        .max(Comparator.comparing(MineCircle::getIndex))
                        .get()
                        .getIndex();

                mineCircle.setIndex(++maxIndex);
                listOfCircles.add(mineCircle);

                MineLabel mineLabel = new MineLabel(Integer.toString(maxIndex));
                mineLabel.setCircleIndex(maxIndex);
                mineLabel.setLayoutX(mineCircle.getLayoutX()-5);
                mineLabel.setLayoutY(mineCircle.getLayoutY()-12);
                mineLabel.setFont(Font.font(16));

                listOfLabels.add(mineLabel);
                graphPane.getChildren().add(mineCircle);
                graphPane.getChildren().add(mineLabel);


            }
            catch (NoSuchElementException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("You have to add start node firstly!!!");
                alert.show();
            }

        }
    };


    private boolean newLine = true;
    private MineLine creatingLine;
    private Edge edge;

    private EventHandler<MouseEvent> lineFollowCursor = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            creatingLine.setEndY(event.getY());
            creatingLine.setEndX(event.getX());
        }
    };
    private EventHandler<MouseEvent> addEdgeHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Point2D point = new Point2D(event.getX(), event.getY());
            boolean breakOut = false;
            for(MineCircle m : listOfCircles){
                if(m.getBoundsInParent().contains(point)){
                    if(newLine && !breakOut){
                        creatingLine = new MineLine();
                        creatingLine.setStartIndex(m.getIndex());
                        creatingLine.setStartX(m.getLayoutX());
                        creatingLine.setStartY(m.getLayoutY());

                        edge = new Edge();
                        edge.setNode1(m.getIndex());

                        creatingLine.setEndX(event.getX());
                        creatingLine.setEndY(event.getY());

                        newLine = false;
                        graphPane.getChildren().add(creatingLine);
                        graphPane.addEventHandler(MouseEvent.MOUSE_MOVED, lineFollowCursor);
                    }
                    else if(!newLine && !breakOut){
                        creatingLine.setEndY(m.getLayoutY());
                        creatingLine.setEndX(m.getLayoutX());

                        edge.setNode2(m.getIndex());
                        listOfEdges.add(edge);
                        numberOfNodes++;

                        creatingLine.toBack();
                        creatingLine.setEndIndex(m.getIndex());
                        newLine = true;
                        graphPane.removeEventHandler(MouseEvent.MOUSE_MOVED, lineFollowCursor);

                    }
                    breakOut = true;
                }

            }
            if(!breakOut){
                if(!newLine){
                    graphPane.getChildren().remove(creatingLine);
                    creatingLine = null;
                    edge = null;
                    newLine = true;
                    graphPane.removeEventHandler(MouseEvent.MOUSE_MOVED, lineFollowCursor);
                }
            }
    }};


    @FXML
    public void moveNodesChanged(){

        if(moveNodes.isSelected()){
            graphPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, moveNodesEvent);
            addEdge.setSelected(false);
            addEdgeChanged();
            addNode.setSelected(false);
            addNodeChanged();
        }
        else {
            graphPane.removeEventHandler(MouseEvent.MOUSE_DRAGGED, moveNodesEvent);
        }
    }

    @FXML
    public void addEdgeChanged(){
        if(addEdge.isSelected()){
            graphPane.addEventHandler(MouseEvent.MOUSE_CLICKED, addEdgeHandler);
            moveNodes.setSelected(false);
            moveNodesChanged();
            addNode.setSelected(false);
            addNodeChanged();
        }
        else {
            graphPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, addEdgeHandler);
        }
    }

    @FXML
    public void addNodeChanged(){
        if(addNode.isSelected()){
            graphPane.addEventHandler(MouseEvent.MOUSE_CLICKED, addNodeHandler);
            addEdge.setSelected(false);
            addEdgeChanged();
            moveNodes.setSelected(false);
            moveNodesChanged();
        }
        else {
            graphPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, addNodeHandler);
        }
    }

}
