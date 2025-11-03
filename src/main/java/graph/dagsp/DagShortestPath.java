package graph.dagsp;

import graph.Edge;
import graph.Graph;
import graph.util.Metrics;
import graph.util.Operation;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class DagShortestPath {
    private double[] dist;  // Use double for Infinity
    private int[] parent;   // For path reconstruction [cite: 23]
    private Metrics metrics;
    private int V;
    private static final double POS_INF = Double.POSITIVE_INFINITY;
    private static final double NEG_INF = Double.NEGATIVE_INFINITY;

    public DagShortestPath() {}


    public void findShortestPaths(Graph dag, int source, List<Integer> topoOrder, Metrics metrics) {
        setup(dag, metrics);

        for (int i = 0; i < V; i++) {
            dist[i] = POS_INF;
        }
        dist[source] = 0;

        runRelaxation(dag, topoOrder, false);
    }

    public void findLongestPaths(Graph dag, int source, List<Integer> topoOrder, Metrics metrics) {
        setup(dag, metrics);

        for (int i = 0; i < V; i++) {
            dist[i] = NEG_INF;
        }
        dist[source] = 0;

        runRelaxation(dag, topoOrder, true);
    }

    private void setup(Graph dag, Metrics metrics) {
        this.V = dag.getV();
        this.metrics = metrics;
        this.metrics.reset();

        this.dist = new double[V];
        this.parent = new int[V];
        for (int i = 0; i < V; i++) {
            parent[i] = -1;
        }
    }

    private void runRelaxation(Graph dag, List<Integer> topoOrder, boolean findLongest) {
        this.metrics.startTimer();

        for (int u : topoOrder) {
            if (dist[u] == POS_INF || dist[u] == NEG_INF) {
                continue;
            }

            for (Edge edge : dag.getAdj(u)) {
                int v = edge.v;
                int w = edge.w;

                metrics.increment(Operation.DAG_RELAXATION);

                if (findLongest) {
                    if (dist[v] < dist[u] + w) {
                        dist[v] = dist[u] + w;
                        parent[v] = u;
                    }
                } else {
                    if (dist[v] > dist[u] + w) {
                        dist[v] = dist[u] + w;
                        parent[v] = u;
                    }
                }
            }
        }
        this.metrics.stopTimer();
    }

    public double[] getDistances() {
        return dist;
    }

    public List<Integer> getPath(int destination) {
        List<Integer> path = new ArrayList<>();
        if (destination < 0 || destination >= V || dist[destination] == POS_INF || dist[destination] == NEG_INF) {
            return path;
        }

        int curr = destination;
        while (curr != -1) {
            path.add(curr);
            curr = parent[curr];
        }

        Collections.reverse(path);

        return path;
    }
}