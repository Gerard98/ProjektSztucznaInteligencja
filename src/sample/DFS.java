package sample;

import javafx.scene.paint.Color;

import java.util.*;

public class DFS {

    private List<MineCircle> listOfCircles;
    private List<MineLine> listOflines;
    private int actuallyNode;
    private int startNode;
    private int endNode;
    private int numberOfNodes;

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
    }

    public void loadEdges(List<Edge> edges) {
        edges.forEach(m -> {
            listOfEdges[m.getNode1()].add(m.getNode2());
            listOfEdges[m.getNode2()].add(m.getNode1());
        });

    }

    public boolean step() {
        if (step == 0) {
            listOfEdges[actuallyNode].forEach(m -> {
                listOfCircles.forEach(n -> {
                    if (n.getIndex() == actuallyNode) {
                        n.setFill(Color.YELLOW);
                    } else if (n.getIndex() == m) {
                        n.setFill(Color.LIGHTBLUE);
                    }
                });
            });
            step = 1;
        } else {
            try {
                final int beforeNode = actuallyNode;
                actuallyNode = listOfEdges[actuallyNode]
                        .stream()
                        .min(Comparator.comparing(Integer::intValue))
                        .get();
                listOfEdges[actuallyNode].removeIf(m -> m == beforeNode);

                listOflines.forEach(m -> {
                    if (m.getEndIndex() == beforeNode && m.getStartIndex() == actuallyNode || m.getEndIndex() == actuallyNode && m.getStartIndex() == beforeNode) {
                        m.setStyle("-fx-stroke: red;");
                    }
                });
                step = 0;
                if (actuallyNode == endNode) {
                    return true;
                }
                return false;
            } catch (NoSuchElementException ex) {
                if (actuallyNode != endNode) {
                    final int beforeNode = actuallyNode - 1;

                    listOfCircles.forEach(n -> {
                        if(n.getIndex() == actuallyNode){
                            n.setFill(Color.LIGHTGREY);
                        }
                    });

                    listOflines.forEach(n -> {
                        if (n.getEndIndex() == beforeNode && n.getStartIndex() == actuallyNode || n.getEndIndex() == actuallyNode && n.getStartIndex() == beforeNode) {
                            n.setStyle("-fx-stroke: black;");
                        }
                    });

                    listOfEdges[beforeNode].removeIf(m -> m == actuallyNode);
                    actuallyNode--;
                }
            }

        }
        return false;
    }
}





