package com.alikhan_s.scc;

import com.alikhan_s.Edge;
import com.alikhan_s.Graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CondensationGraph {
    private int[] vertexToSccId;
    private int[] sccSizes;
    private Graph condensationGraph;
    private int numSccs;


    public Graph build(Graph originalGraph, List<List<Integer>> sccs) {
        this.numSccs = sccs.size();
        this.condensationGraph = new Graph(numSccs);
        this.vertexToSccId = new int[originalGraph.getV()];
        this.sccSizes = new int[numSccs];

        for (int sccId = 0; sccId < numSccs; sccId++) {
            List<Integer> scc = sccs.get(sccId);
            sccSizes[sccId] = scc.size(); // Store the size
            for (int vertex : scc) {
                vertexToSccId[vertex] = sccId;
            }
        }

        Map<String, Integer> dagEdges = new HashMap<>();

        for (Edge edge : originalGraph.getEdges()) {
            int u = edge.u;
            int v = edge.v;
            int w = edge.w;

            int sccU = vertexToSccId[u];
            int sccV = vertexToSccId[v];

            if (sccU != sccV) {
                String edgeKey = sccU + ":" + sccV;

                int currentMinWeight = dagEdges.getOrDefault(edgeKey, Integer.MAX_VALUE);
                if (w < currentMinWeight) {
                    dagEdges.put(edgeKey, w);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : dagEdges.entrySet()) {
            String[] sccIds = entry.getKey().split(":");
            int sccU = Integer.parseInt(sccIds[0]);
            int sccV = Integer.parseInt(sccIds[1]);
            int minWeight = entry.getValue();

            condensationGraph.addEdge(sccU, sccV, minWeight);
        }

        return condensationGraph;
    }

    public int[] getVertexToSccIdMap() {
        return vertexToSccId;
    }

    public int[] getSccSizes() {
        return sccSizes;
    }
}