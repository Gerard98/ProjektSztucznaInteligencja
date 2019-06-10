package sample;

import javafx.scene.paint.Color;

import java.util.*;

public class BFS {
    private List<MineCircle> listOfCircles;
    private List<MineLine> listOfLines;
    private int actuallyNode;
    private int beforeNode;
    private int startNode;
    private int startNode2;
    private int endNode;
    private int numberOfNodes;
    private boolean [] visited;
    private int [] comeFrom;
    private int v;

    private List<Integer> resultList = new LinkedList<>();

    private Queue<Integer> queue = new LinkedList<>();

    private List<Integer> listOfEdges[];

    private List<Integer> [] neighbours;

    Iterator<Integer> it;

    private int step = 0;

    private boolean markNextNode = false;

    public BFS(int startNode, int endNode, List<MineCircle> listOfCircles, List<MineLine> listOfLines, int numberOfNodes, List<Edge> listOfEdges) {
        this.listOfCircles = listOfCircles;
        this.listOfLines = listOfLines;
        this.startNode = startNode;
        this.startNode2 = startNode;
        this.endNode = endNode;
        this.numberOfNodes = numberOfNodes;
        this.listOfEdges = (List<Integer>[]) new List[numberOfNodes + 1];
        this.neighbours = new LinkedList[numberOfNodes+1];

        for (int i = 0; i <= numberOfNodes; i++) {
            this.listOfEdges[i] = new ArrayList<Integer>();
        }

        actuallyNode = startNode;
        loadEdges(listOfEdges);

        for(Edge e: listOfEdges) {
            if(neighbours[e.getNode1()] == null)
                neighbours[e.getNode1()] = new LinkedList<>();

            if(neighbours[e.getNode2()] == null)
                neighbours[e.getNode2()] = new LinkedList<>();

            neighbours[e.getNode1()].add(e.getNode2());
            neighbours[e.getNode2()].add(e.getNode1());
        }

        visited = new boolean[numberOfNodes+1];
        for(int i = 0; i < numberOfNodes; i++) visited[i] = false;
        comeFrom = new int[numberOfNodes+1];

        //queue.add(startNode);
    }

    public void loadEdges(List<Edge> edges) {
        edges.forEach(m -> {
            listOfEdges[m.getNode1()].add(m.getNode2());
            listOfEdges[m.getNode2()].add(m.getNode1());
        });
    }

    public boolean step() {
        if(step == 0) {
            visited[startNode2] = true;
            queue.add(startNode2);
            listOfEdges[queue.peek()].forEach(m -> {
                listOfCircles.forEach(n -> {
                    if (n.getIndex() == queue.peek()) {
                        n.setFill(Color.BLACK);
                        //n.setStyle("-fx-stroke: yellow; -fx-stroke-width: 5px");
                    }
                });
            });
            step = 1;

            startNode2 = queue.poll();
            it = neighbours[startNode2].listIterator();
        } else {
            if(!queue.isEmpty() || step == 1) {

                if (!markNextNode) {
                    while (it.hasNext()) {
                        int n = it.next();
                        if (!visited[n]) {
                            listOfEdges[n].forEach(m -> {
                                listOfCircles.forEach(z -> {
                                    if (z.getIndex() == n) {
                                        z.setFill(Color.YELLOW);
                                        //n.setStyle("-fx-stroke: yellow; -fx-stroke-width: 5px");
                                    }
                                });
                            });
                            visited[n] = true;
                            queue.add(n);
                        }
                    }
                    markNextNode = true;
                } else {
                    listOfEdges[startNode2].forEach(m -> {
                        listOfCircles.forEach(n -> {
                            if (n.getIndex() == startNode2) {
                                n.setFill(Color.YELLOW);
                                //n.setStyle("-fx-stroke: yellow; -fx-stroke-width: 5px");
                            }
                        });
                    });

                    startNode2 = queue.poll();
                    it = neighbours[startNode2].listIterator();

                    listOfEdges[startNode2].forEach(m -> {
                        listOfCircles.forEach(n -> {
                            if (n.getIndex() == startNode2) {
                                n.setFill(Color.BLACK);
                                //n.setStyle("-fx-stroke: yellow; -fx-stroke-width: 5px");
                            }
                        });
                    });

                    markNextNode = false;
                }
                 step++;
            }

        }

        return false;
    }


    public void shortestPath() {
        int v1 = startNode;
        int v2 = endNode;

        comeFrom[v1] = -1;
        queue.add(v1);
        visited[v1] = true;

        while (!queue.isEmpty()) {
            v = queue.poll();

            if (v == v2) break;

            for (int u : neighbours[v]) {
                if (!visited[u]) {
                    comeFrom[u] = v;
                    queue.add(u);
                    visited[u] = true;
                }
            }

        }

        while(v > -1) {
            resultList.add(0,v);
            v = comeFrom[v];
        }

        while (!queue.isEmpty()) {
            queue.remove();
        }

    }

    public StringBuilder printWholeResult(){

        StringBuilder result = new StringBuilder("Path: ");

        int k = resultList.size();
        if(k == 0){
            result.append("There is no available path");
        }
        else{
            listOfCircles.forEach(n -> {
                n.setFill(Color.LIGHTGREY);
            });
            for(int i=0;i<k;i++){
                result.append(resultList.get(i) + " -> ");
                int id = resultList.get(i);
                listOfEdges[id].forEach(m -> {
                    listOfCircles.forEach(z -> {
                        if (z.getIndex() == id) {
                            z.setFill(Color.YELLOW);
                        }
                    });
                });
            }
            for(int i=0;i<(k-1);i++) {
                int node1 = resultList.get(i);
                int node2 = resultList.get(i+1);
                listOfLines.forEach(m -> {
                    if (m.getEndIndex() == node1 && m.getStartIndex() == node2 || m.getEndIndex() == node2 && m.getStartIndex() == node1) {
                        m.setStyle("-fx-stroke: red; -fx-stroke-width: 2px");
                    }
                });
            }
            result.delete(result.length()-3, result.length());
        }

        return result;
    }

}
