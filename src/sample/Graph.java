package sample;

import java.util.*;

class Graph {

    private StringBuilder result;
    private int e;
    private int v;

    private List<Integer>[] adjacencyList;

    public Graph(int v) {
        this.v = v;
        this.e = 0;
        adjacencyList = (List<Integer>[]) new List[v];
        for (int i = 0; i < v; i++) {
            adjacencyList[i] = new ArrayList<Integer>();
        }
    }

    public void addEdge(int v, int w) {
        adjacencyList[v].add(w);
        adjacencyList[w].add(v);
        e++;
    }

    public int getNumberOfEdges() {
        return e;
    }

    public int getNumberOfVertices() {
        return v;
    }

    public Iterable<Integer> getAdjacencyList(int v) {
        return adjacencyList[v];
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        s.append("wierzcholki: ").append(v).append("; krawedzie: ").append(e)
                .append(newLine);
        for (int i = 0; i < v; i++) {
            s.append(i).append(": ");
            for (int w : adjacencyList[i]) {
                s.append(w).append(" ");
            }
            s.append(newLine);
        }
        return s.toString();
    }
}

class DFSPaths {

    private int[] edgeTo;

    private boolean[] marked;

    private final int source;

    public DFSPaths(Graph graph, int source) {
        this.source = source;
        edgeTo = new int[graph.getNumberOfVertices()];
        marked = new boolean[graph.getNumberOfVertices()];
        dfs(graph, source);
    }



    public boolean hasPathTo(int vertex) {
        return marked[vertex];
    }

    public Iterable<Integer> getPathTo(int vertex) {
        Deque<Integer> path = new ArrayDeque<Integer>();

        if (!hasPathTo(vertex)) {
            return path;
        }

        for (int w = vertex; w != source; w = edgeTo[w]) {
            path.push(w);
        }

        path.push(source);
        return path;
    }

    private void dfs(Graph graph, int vertex) {

        marked[vertex] = true;

        for (int w : graph.getAdjacencyList(vertex)) {
            if (!marked[w]) {
                edgeTo[w] = vertex;
                dfs(graph, w);
            }
        }
    }
}

class BFSPaths {

    private int[] edgeTo;

    private boolean[] marked;

    private final int source;
    private Queue<Integer> priorityQueue;

    public BFSPaths(Graph graph, int source) {
        this.source = source;
        edgeTo = new int[graph.getNumberOfVertices()];
        marked = new boolean[graph.getNumberOfVertices()];
        priorityQueue = new PriorityQueue<Integer>(graph.getNumberOfVertices());
        priorityQueue.offer(source);
        bfs(graph, source);
    }

    public boolean hasPathTo(int vertex) {
        return marked[vertex];
    }

    public Iterable<Integer> getPathTo(int vertex) {
        Deque<Integer> path = new ArrayDeque<Integer>();

        if (!hasPathTo(vertex)) {
            return path;
        }

        for (int w = vertex; w != source; w = edgeTo[w]) {
            path.push(w);
        }

        path.push(source);
        return path;
    }

    private void bfs(Graph graph, int vertex) {

        marked[vertex] = true;

        priorityQueue.offer(vertex);

        while (!priorityQueue.isEmpty()) {
            int v = priorityQueue.remove();
            for (int w : graph.getAdjacencyList(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    marked[w] = true;
                    priorityQueue.offer(w);
                }
            }
        }
    }
}