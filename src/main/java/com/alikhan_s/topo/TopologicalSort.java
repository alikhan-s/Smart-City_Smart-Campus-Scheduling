package com.alikhan_s.topo;

import com.alikhan_s.Graph;
import com.alikhan_s.util.Metrics;
import com.alikhan_s.util.Operation;

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;


public class TopologicalSort {

    private Metrics metrics;
    private List<Integer> sortedOrder;

    public List<Integer> sort(Graph dag, Metrics metrics) {
        this.metrics = metrics;
        this.metrics.reset();
        this.metrics.startTimer();

        int V = dag.getV();
        this.sortedOrder = new ArrayList<>(V);
        int[] inDegree = new int[V];

        for (int u = 0; u < V; u++) {
            for (int v : dag.getAdj(u)) {
                inDegree[v]++;
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

            for (int v : dag.getAdj(u)) {
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