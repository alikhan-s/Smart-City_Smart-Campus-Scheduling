package com.alikhan_s;

import com.alikhan_s.scc.TarjanSCC;
import com.alikhan_s.util.GraphParser;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String[] testFiles = {
                "small_1.json",
                "small_2.json",
                "small_3.json",
                "medium_1.json",
                "medium_2.json",
                "medium_3.json",
                "large_1.json",
                "large_2.json",
                "large_3.json"
        };

        System.out.println("--- SCC TESTING START ---");

        for (String fileName : testFiles) {
            String filePath = "data/graphs/" + fileName;
            System.out.println("\n-------------------------------------------");
            System.out.println("--- File name: " + fileName + " ---");

            try {
                Graph graph = GraphParser.loadGraph(filePath);
                System.out.println("Graph successfully loaded, vertices: " + graph.getV());

                TarjanSCC sccFinder = new TarjanSCC();
                List<List<Integer>> sccs = sccFinder.findSccs(graph);

                System.out.println("Strong Connectivity Component Found (SCC): " + sccs.size());

                int sccIndex = 1;
                for (List<Integer> scc : sccs) {
                    scc.sort(Integer::compareTo);
                    System.out.println("  -> SCC #" + sccIndex + " (Size: " + scc.size() + "): " + scc);
                    sccIndex++;
                }

            } catch (IOException e) {
                System.err.println("Error: could not read file " + filePath);
                System.err.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("Error: could not parse file " + filePath);
                System.err.println(e.getMessage());
            }
        }

        System.out.println("\n-------------------------------------------");
        System.out.println("--- TESTING FINISH ---");
    }
}