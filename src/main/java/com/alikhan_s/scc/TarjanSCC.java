package com.alikhan_s.scc;

import com.alikhan_s.Graph;
import com.alikhan_s.util.Metrics;
import com.alikhan_s.util.Operation;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Arrays;


public class TarjanSCC {

    private int V;
    private List<List<Integer>> adj;
    private int time;

    private int[] index;    // Renamed from 'disc'
    private int[] lowLink;
    private boolean[] onStack;
    private Stack<Integer> stack;

    private List<List<Integer>> sccs;

    private Metrics metrics;

    public TarjanSCC() {
    }

    public List<List<Integer>> findSccs(Graph graph, Metrics metrics) {
        this.metrics = metrics;
        this.metrics.reset();
        this.metrics.startTimer();

        this.V = graph.getV();
        this.adj = new ArrayList<>(V);
        for(int i=0; i<V; i++) {
            this.adj.add(graph.getAdj(i));
        }

        this.time = 0;
        this.index = new int[V];
        this.lowLink = new int[V];
        this.onStack = new boolean[V];
        this.stack = new Stack<>();
        this.sccs = new ArrayList<>();

        Arrays.fill(index, -1);
        Arrays.fill(lowLink, -1);

        for (int u = 0; u < V; u++) {
            if (index[u] == -1) {
                dfs(u);
            }
        }

        this.metrics.stopTimer();

        return sccs;
    }

    private void dfs(int u) {
        metrics.increment(Operation.DFS_VISIT);

        index[u] = lowLink[u] = time;
        time++;

        stack.push(u);
        onStack[u] = true;

        for (int v : adj.get(u)) {
            metrics.increment(Operation.DFS_EDGE_TRAVERSAL);

            if (index[v] == -1) {
                dfs(v);
                lowLink[u] = Math.min(lowLink[u], lowLink[v]);
            }
            else if (onStack[v]) {
                lowLink[u] = Math.min(lowLink[u], index[v]);
            }
        }

        if (lowLink[u] == index[u]) {
            List<Integer> currentScc = new ArrayList<>();
            int v;

            do {
                v = stack.pop();
                onStack[v] = false;
                currentScc.add(v);
            } while (u != v);

            sccs.add(currentScc);
        }
    }

    public List<Integer> getSccs(int idx) {
        return sccs.get(idx);
    }
}