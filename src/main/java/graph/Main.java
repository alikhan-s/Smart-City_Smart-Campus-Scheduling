package graph;

import graph.scc.CondensationGraph;
import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import graph.dagsp.DagShortestPath;
import graph.util.GraphParser;
import graph.util.AnalysisResult;
import graph.util.Operation;
import graph.util.ResultExporter;
import graph.util.Metrics;
import graph.util.SimpleMetrics;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        String[] testFiles = {
                "small_1.json", "small_2.json", "small_3.json",
                "medium_1.json", "medium_2.json", "medium_3.json",
                "large_1.json", "large_2.json", "large_3.json"
        };

        List<AnalysisResult> allResults = new ArrayList<>();

        System.out.println("--- START OF BATCH TESTING ---");

        TarjanSCC sccFinder = new TarjanSCC();
        CondensationGraph dagBuilder = new CondensationGraph();
        TopologicalSort topoSorter = new TopologicalSort();
        DagShortestPath pathFinder = new DagShortestPath();

        for (String fileName : testFiles) {
            System.out.println("  -> Analyse of file: " + fileName + "...");

            try {
                AnalysisResult result = new AnalysisResult(fileName);

                Metrics loadMetrics = new SimpleMetrics();
                loadMetrics.startTimer();
                Graph graph = GraphParser.loadGraph("data/graphs/" + fileName);
                loadMetrics.stopTimer();

                result.loadTimeMs = loadMetrics.getElapsedTimeMillis();
                result.n = graph.getV();
                result.e = graph.getE();

                Metrics sccMetrics = new SimpleMetrics();
                List<List<Integer>> sccs = sccFinder.findSccs(graph, sccMetrics);

                result.sccCount = sccs.size();
                result.sccTimeMs = sccMetrics.getElapsedTimeMillis();
                result.sccDfsVisits = sccMetrics.getCount(Operation.DFS_VISIT);
                result.sccDfsEdges = sccMetrics.getCount(Operation.DFS_EDGE_TRAVERSAL);

                Graph condensationDag = dagBuilder.build(graph, sccs);

                result.dagNodes = condensationDag.getV();
                result.dagEdges = condensationDag.getE();

                Metrics topoMetrics = new SimpleMetrics();
                List<Integer> sortedOrder = topoSorter.sort(condensationDag, topoMetrics);

                result.topoTimeMs = topoMetrics.getElapsedTimeMillis();
                result.topoPushes = topoMetrics.getCount(Operation.TOPO_QUEUE_PUSH);
                result.topoPops = topoMetrics.getCount(Operation.TOPO_QUEUE_POP);

                int sccSourceNode = condensationDag.getSource();

                Metrics spMetrics = new SimpleMetrics();
                pathFinder.findShortestPaths(condensationDag, sccSourceNode, sortedOrder, spMetrics);

                result.spTimeMs = spMetrics.getElapsedTimeMillis();
                result.spRelaxations = spMetrics.getCount(Operation.DAG_RELAXATION);

                Metrics lpMetrics = new SimpleMetrics();
                pathFinder.findLongestPaths(condensationDag, sccSourceNode, sortedOrder, lpMetrics);
                double[] longestDists = pathFinder.getDistances();

                result.lpTimeMs = lpMetrics.getElapsedTimeMillis();
                result.lpRelaxations = lpMetrics.getCount(Operation.DAG_RELAXATION);

                double criticalPathLength = 0;
                for (double dist : longestDists) {
                    if (dist != Double.NEGATIVE_INFINITY && dist > criticalPathLength) {
                        criticalPathLength = dist;
                    }
                }
                result.criticalPathLength = criticalPathLength;

                allResults.add(result);
                System.out.println("     ... OK");

            } catch (Exception e) {
                System.err.println("     ... Error during processing " + fileName + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("\n--- BATCH TESTING COMPLETED ---");

        ResultExporter.printAsMarkdownTable(allResults);
        ResultExporter.exportToCSV(allResults, "analysis_results.csv");
    }
}