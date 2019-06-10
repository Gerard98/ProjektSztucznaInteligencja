package sample;

import javafx.scene.paint.Color;

import java.util.*;

public class DFS {

    private List<MineCircle> listOfCircles;
    private List<MineLine> listOflines;
    private int actuallyNode;
    private int beforeNode;
    private int startNode;
    private int endNode;
    private int numberOfNodes;

    private List<Integer> visitedNodes = new LinkedList<>();

    private Stack<Integer> path = new Stack<>();

    private List<Integer> listOfEdges[];

    private int step = 0;

    public DFS(int startNode, int endNode, List<MineCircle> listOfCircles, List<MineLine> listOflines, int numberOfNodes, List<Edge> listOfEdges) {
        this.listOfCircles = listOfCircles;
        this.listOflines = listOflines;
        this.startNode = startNode;
        this.endNode = endNode;
        this.numberOfNodes = numberOfNodes;
        this.listOfEdges = (List<Integer>[]) new List[numberOfNodes + 1];
        for (int i = 0; i <= numberOfNodes; i++) {
            this.listOfEdges[i] = new ArrayList<Integer>();
        }
        actuallyNode = startNode;
        loadEdges(listOfEdges);
        path.push(startNode);
        visitedNodes.add(startNode);

    }

    public void loadEdges(List<Edge> edges) {
        edges.forEach(m -> {
            listOfEdges[m.getNode1()].add(m.getNode2());
            listOfEdges[m.getNode2()].add(m.getNode1());
        });

    }

    public boolean step() {
        if (step == 0) {
            listOfEdges[path.peek()].forEach(m -> {
                listOfCircles.forEach(n -> {
                    if (n.getIndex() == path.peek()) {
                        n.setFill(Color.YELLOW);
                        //n.setStyle("-fx-stroke: yellow; -fx-stroke-width: 5px");
                    } else if (n.getIndex() == m) {
                        n.setFill(Color.LIGHTBLUE);
                        //n.setStyle("-fx-stroke: lightblue; -fx-stroke-width: 5px");
                    }
                });
            });
            step = 1;
        } else {
            try {
                beforeNode = path.peek();

                boolean isLoopPosible = false;
                do {
                    actuallyNode = listOfEdges[path.peek()]
                            .stream()
                            .min(Comparator.comparing(Integer::intValue))
                            .get();

                    if (visitedNodes.stream().anyMatch(m -> m == actuallyNode)) {
                        listOfEdges[path.peek()].removeIf(n -> n == actuallyNode);
                        isLoopPosible = true;
                    }
                    else{
                        isLoopPosible = false;
                    }
                }while(isLoopPosible);
                visitedNodes.add(actuallyNode);
                path.push(actuallyNode);
                listOfEdges[path.peek()].removeIf(m -> m == beforeNode);

                listOflines.forEach(m -> {
                    if (m.getEndIndex() == beforeNode && m.getStartIndex() == actuallyNode || m.getEndIndex() == actuallyNode && m.getStartIndex() == beforeNode) {
                        m.setStyle("-fx-stroke: red; -fx-stroke-width: 2px");
                    }
                });

                listOfCircles.forEach(n -> {
                    listOfEdges[beforeNode].forEach(m -> {
                        if(n.getIndex() == m && m!= actuallyNode){
                            n.setFill(Color.LIGHTGREY);
                        }
                    });

                });

                step = 0;
                if (path.peek() == endNode) {
                    listOfCircles.forEach(m -> {
                        if(m.getIndex() == endNode){
                            m.setFill(Color.YELLOW);
                        }
                    });
                    return true;
                }
                return false;
            } catch (NoSuchElementException ex) {
                try {
                    if (path.peek() != endNode) {

                        listOfCircles.forEach(n -> {
                            if (n.getIndex() == path.peek()) {
                                n.setFill(Color.LIGHTGREY);
                            }
                        });

                        beforeNode = path.peek();
                        path.pop();

                        listOflines.forEach(n -> {
                            if (n.getEndIndex() == beforeNode && n.getStartIndex() == path.peek() || n.getEndIndex() == path.peek() && n.getStartIndex() == beforeNode) {
                                n.setStyle("-fx-stroke: black;");
                            }
                        });


                        listOfEdges[path.peek()].removeIf(m -> m == beforeNode);
                        actuallyNode = path.peek();
                        step = 0;
                    }
                }
                catch(EmptyStackException ex2){
                    return true;
                }
            }

        }
        return false;
    }

    public StringBuilder printResult(){

        StringBuilder result = new StringBuilder("Path: ");

        int k = path.size();
        if(k == 0){
            result.append("There is no available path");
        }
        else{
            for(int i=0;i<k;i++){
                result.append(path.firstElement() + " -> ");
                path.removeElementAt(0);
            }
            result.delete(result.length()-3, result.length());
        }

        return result;
    }

}





