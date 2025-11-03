package graph;

import graph.util.Metrics;
import graph.util.SimpleMetrics;
import graph.scc.CondensationGraph;
import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import graph.dagsp.DagShortestPath;
import graph.util.GraphParser;

import java.io.IOException;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        String[] testFiles = {
                "small_1.json", "small_2.json", "small_3.json",
                "medium_1.json", "medium_2.json", "medium_3.json",
                "large_1.json", "large_2.json", "large_3.json"
        };

        System.out.println("--- START OF FULL BATCH TESTING (SCC + Condensation + Topological Sort + Dag Shortest Path) ---");

        TarjanSCC sccFinder = new TarjanSCC();
        CondensationGraph dagBuilder = new CondensationGraph();
        TopologicalSort topoSorter = new TopologicalSort();
        DagShortestPath pathFinder = new DagShortestPath();

        for (String fileName : testFiles) {
            String filePath = "data/graphs/" + fileName;
            System.out.println("\n---------------------------------------------------");
            System.out.println("--- File name: " + fileName + " ---");

            try {
                Metrics loadMetrics = new SimpleMetrics();
                loadMetrics.startTimer();
                Graph graph = GraphParser.loadGraph(filePath);
                loadMetrics.stopTimer();
                System.out.println("Graph '" + fileName + "' loaded (Vertices: " + graph.getV() + ", Edges: " + graph.getEdges().size() + ")");
                System.out.println("  -> Current node (source): " + graph.getSource());
                System.out.println("  -> Metrics (Load): " + String.format("%.4f ms", loadMetrics.getElapsedTimeNano() / 1_000_000.0));


                System.out.println("\n[Find SCC (Tarjan)]");
                Metrics sccMetrics = new SimpleMetrics();
                List<List<Integer>> sccs = sccFinder.findSccs(graph, sccMetrics);
                System.out.println("Found SCC: " + sccs.size());
                System.out.println("Metrics (SCC): \n" + sccMetrics);

                System.out.println("\n[Building Condensation Graph]");
                Graph condensationDag = dagBuilder.build(graph, sccs);
                System.out.println("Condensation Graph (DAG) built.");
                System.out.println("  -> New vertices (SCC): " + condensationDag.getV());
                System.out.println("  -> New edges (between SCC): " + condensationDag.getEdges().size());

                System.out.println("\n[Topological Sort (Kahn)]");
                Metrics topoMetrics = new SimpleMetrics();
                List<Integer> sortedOrder = topoSorter.sort(condensationDag, topoMetrics);
                System.out.println("Topological order (SCC-nodes) found.");
                System.out.println("Metrics (Topo): \n" + topoMetrics);

                System.out.println("\n[Search Paths in DAG]");

                int originalSource = graph.getSource();
                int sccSourceNode = dagBuilder.getVertexToSccIdMap()[originalSource];
                System.out.println("  -> Original 'source' " + originalSource + " is located in the SCC node #" + sccSourceNode);

                Metrics spMetrics = new SimpleMetrics();
                pathFinder.findShortestPaths(condensationDag, sccSourceNode, sortedOrder, spMetrics);
                double[] shortestDists = pathFinder.getDistances();
                System.out.println("\n  --- Shortest Paths from SCC #" + sccSourceNode + " ---");
                for(int i=0; i<shortestDists.length && i < 10; i++) {
                    System.out.printf("  -> to SCC #%d: %s%n", i,
                            (shortestDists[i] == Double.POSITIVE_INFINITY) ? "UNREACHABLE" : shortestDists[i]);
                }
                System.out.println("  Metrics (ShortestPath): \n" + spMetrics);

                Metrics lpMetrics = new SimpleMetrics();
                pathFinder.findLongestPaths(condensationDag, sccSourceNode, sortedOrder, lpMetrics);
                double[] longestDists = pathFinder.getDistances();

                double criticalPathLength = 0;
                int criticalPathEndNode = -1;
                for(int i=0; i<longestDists.length; i++) {
                    if (longestDists[i] > criticalPathLength && longestDists[i] != Double.NEGATIVE_INFINITY) {
                        criticalPathLength = longestDists[i];
                        criticalPathEndNode = i;
                    }
                }
                System.out.println("\n  --- Longest (Classic) Path from SCC #" + sccSourceNode + " ---");
                System.out.println("  -> Critical Path Length: " + criticalPathLength);
                if(criticalPathEndNode != -1) {
                    System.out.println("  -> Path (in SCC-nodes): " + pathFinder.getPath(criticalPathEndNode));
                }
                System.out.println("  Metrics (LongestPath): \n" + lpMetrics);

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