package graph;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class Graph {
    private final int V;
    private final int source;
    private final List<List<Edge>> adj;
    private final List<Edge> edges;

    public Graph(int V, int source) {
        this.V = V;
        this.source = source;
        this.adj = new ArrayList<>(V);
        this.edges = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            adj.add(new LinkedList<>());
        }
    }

    public void addEdge(int u, int v, int w) {
        if (u >= 0 && u < V && v >= 0 && v < V) {
            Edge edge = new Edge(u, v, w);
            adj.get(u).add(edge);
            edges.add(edge);
        } else {
            System.err.println("Error: Vertex " + u + " or " + v + " is out of bounds.");
        }
    }

    public List<Edge> getAdj(int u) {
        return adj.get(u);
    }

    public int getV() {
        return V;
    }

    public int getE() {
        return edges.size();
    }

    public int getSource() {
        return source;
    }

    public List<Edge> getEdges() {
        return edges;
    }

}