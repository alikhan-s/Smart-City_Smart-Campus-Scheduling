package com.alikhan_s;

import com.alikhan_s.util.Metrics;
import com.alikhan_s.util.SimpleMetrics;
import com.alikhan_s.scc.CondensationGraph;
import com.alikhan_s.scc.TarjanSCC;
import com.alikhan_s.util.GraphParser;
import java.io.IOException;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        String[] testFiles = {
                "small_1.json", "small_2.json", "small_3.json",
                "medium_1.json", "medium_2.json", "medium_3.json",
                "large_1.json", "large_2.json", "large_3.json"
        };

        System.out.println("--- START OF BATCH TESTING (SCC + Condensation) ---");

        TarjanSCC sccFinder = new TarjanSCC();
        CondensationGraph dagBuilder = new CondensationGraph();
        Metrics sccMetrics = new SimpleMetrics();

        for (String fileName : testFiles) {
            String filePath = "data/graphs/" + fileName;
            System.out.println("\n---------------------------------------------------");
            System.out.println("--- File name: " + fileName + " ---");

            try {
                Graph graph = GraphParser.loadGraph(filePath);
                System.out.println("Graph '" + fileName + "' loaded (Vertices: " + graph.getV() + ", Edges: " + graph.getEdges().size() + ")");

                System.out.println("\n[Search (Tarjan)]");
                List<List<Integer>> sccs = sccFinder.findSccs(graph, sccMetrics);

                System.out.println("Found SCC: " + sccs.size());
                System.out.println("Metrics (SCC): \n" + sccMetrics);

                System.out.println("\n[Building Condensation Graph]");
                // sccMetrics.reset();
                // sccMetrics.startTimer();
                Graph condensationDag = dagBuilder.build(graph, sccs);
                // sccMetrics.stopTimer();

                System.out.println("Condensation Graph Built.");
                System.out.println("  -> New vertices (SCC): " + condensationDag.getV());
                System.out.println("  -> New edges (between SCC): " + condensationDag.getEdges().size());

                // int[] sccSizes = dagBuilder.getSccSizes();
                // for (int i = 0; i < sccSizes.length; i++) {
                //    System.out.println("  -> Размер SCC #" + i + ": " + sccSizes[i]);
                // }


            } catch (IOException e) {
                System.err.println("Error: Could not read file " + filePath);
                System.err.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("Error: Could not parse file " + filePath);
                System.err.println(e.getMessage());
            }
        }

        System.out.println("\n---------------------------------------------------");
        System.out.println("--- BATCH TESTING COMPLETED ---");
    }
}