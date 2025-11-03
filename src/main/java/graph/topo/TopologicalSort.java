package graph.topo;

import graph.Graph;
import graph.Edge;
import graph.util.Metrics;
import graph.util.Operation;

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;


public class TopologicalSort {

    private Metrics metrics;
    private List<Integer> sortedOrder;

    public TopologicalSort() {}

    public List<Integer> sort(Graph dag, Metrics metrics) {
        this.metrics = metrics;
        this.metrics.reset();
        this.metrics.startTimer();

        int V = dag.getV();
        this.sortedOrder = new ArrayList<>(V);
        int[] inDegree = new int[V];

        for (int u = 0; u < V; u++) {
            for (Edge edge : dag.getAdj(u)) {
                inDegree[edge.v]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int u = 0; u < V; u++) {
            if (inDegree[u] == 0) {
                queue.add(u);
                metrics.increment(Operation.TOPO_QUEUE_PUSH);
            }
        }

        int visitedCount = 0;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            metrics.increment(Operation.TOPO_QUEUE_POP);

            sortedOrder.add(u);
            visitedCount++;

            for (Edge edge : dag.getAdj(u)) {
                int v = edge.v;
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.add(v);
                    metrics.increment(Operation.TOPO_QUEUE_PUSH);
                }
            }
        }

        this.metrics.stopTimer();

        if (visitedCount < V) {
            throw new IllegalArgumentException(
                    "Error: Graph contains a cycle. A topological sort is not possible."
            );
        }

        return sortedOrder;
    }

    public List<Integer> getSortedOrder() {
        return this.sortedOrder;
    }
}