package com.alikhan_s.scc;

import com.alikhan_s.Graph;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Arrays;

public class TarjanSCC {

    private int V;
    private List<List<Integer>> adj;
    private int time;

    private int[] disc;
    private int[] lowLink;
    private boolean[] onStack;
    private Stack<Integer> stack;

    private List<List<Integer>> sccs;

    public TarjanSCC() {
    }

    public List<List<Integer>> findSccs(Graph graph) {
        this.V = graph.getV();
        this.adj = new ArrayList<>(V);
        for(int i=0; i<V; i++) {
            this.adj.add(graph.getAdj(i));
        }

        this.time = 0;
        this.disc = new int[V];
        this.lowLink = new int[V];
        this.onStack = new boolean[V];
        this.stack = new Stack<>();
        this.sccs = new ArrayList<>();

        Arrays.fill(disc, -1);
        Arrays.fill(lowLink, -1);

        for (int u = 0; u < V; u++) {
            if (disc[u] == -1) {
                dfs(u);
            }
        }

        return sccs;
    }

    private void dfs(int u) {
        disc[u] = lowLink[u] = time;
        time++;

        stack.push(u);
        onStack[u] = true;

        for (int v : adj.get(u)) {

            if (disc[v] == -1) {
                dfs(v);
                lowLink[u] = Math.min(lowLink[u], lowLink[v]);
            }
            else if (onStack[v]) {
                lowLink[u] = Math.min(lowLink[u], disc[v]);
            }
        }

        if (lowLink[u] == disc[u]) {
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
}