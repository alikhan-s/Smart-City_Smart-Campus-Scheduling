package com.alikhan_s;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class Graph {
    private final int V;
    private final List<List<Integer>> adj;

    private final List<Edge> edges;

    public Graph(int V) {
        this.V = V;

        adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adj.add(new LinkedList<>());
        }
        this.edges = new ArrayList<>();
    }

    public void addEdge(int u, int v, int w) {
        if (u >= 0 && u < V && v >= 0 && v < V) {
            adj.get(u).add(v);
            edges.add(new Edge(u, v, w));
        } else {
            System.err.println("Error: Vertices " + u + " or " + v + " go beyond the boundaries.");
        }
    }

    public List<Integer> getAdj(int u) {
        return adj.get(u);
    }

    public int getV() {
        return V;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}