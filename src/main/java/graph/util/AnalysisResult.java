package graph.util;

import graph.util.Operation;
import java.util.Locale;

/**
 * A data class (POJO) to hold all computed metrics
 * for a single graph analysis run.
 */
public class AnalysisResult {
    // Graph Info
    public String fileName;
    public int n; // V
    public int e; // E
    public double loadTimeMs;

    // SCC
    public int sccCount;
    public double sccTimeMs;
    public long sccDfsVisits;
    public long sccDfsEdges;

    // Condensation Graph
    public int dagNodes;
    public int dagEdges;

    // Topological Sort
    public double topoTimeMs;
    public long topoPushes;
    public long topoPops;

    // Shortest Path
    public double spTimeMs;
    public long spRelaxations;

    // Longest Path
    public double lpTimeMs;
    public long lpRelaxations;
    public double criticalPathLength;

    public AnalysisResult(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return Title for CSV file
     */
    public static String getCsvHeader() {
        return "FileName,N,E,LoadTimeMs,SccCount,SccTimeMs,SccDfsVisits,SccDfsEdges," +
                "DagNodes,DagEdges,TopoTimeMs,TopoPushes,TopoPops," +
                "SpTimeMs,SpRelaxations,LpTimeMs,LpRelaxations,CriticalPathLength\n";
    }

    /**
     * @return Data string for CSV file
     */
    public String toCsvRow() {
        return String.format(Locale.US,
                "%s,%d,%d,%.4f,%d,%.4f,%d,%d,%d,%d,%.4f,%d,%d,%.4f,%d,%.4f,%d,%.2f\n",
                fileName, n, e, loadTimeMs, sccCount, sccTimeMs, sccDfsVisits, sccDfsEdges,
                dagNodes, dagEdges, topoTimeMs, topoPushes, topoPops,
                spTimeMs, spRelaxations, lpTimeMs, lpRelaxations, criticalPathLength
        );
    }

    /**
     * @return Header for Markdown table
     */
    public static String getMarkdownHeader() {
        return "| File             | N | E | SCCs | DAG V | DAG E | SCC Time (ms) | Topo Time (ms) | SP Time (ms) | LP Time (ms) | Crit. Path |";
    }

    /**
     * @return Separator for Markdown tables
     */
    public static String getMarkdownSeparator() {
        return "|:-----------------|--:|--:|-----:|------:|------:|--------------:|---------------:|-------------:|-------------:|-----------:|";
    }

    /**
     * @return Data row for Markdown table (abbreviated)
     */
    public String toMarkdownRow() {
        return String.format(Locale.US,
                "| %-16s | %d | %d | %d | %d | %d | %.4f | %.4f | %.4f | %.4f | %.2f |",
                fileName, n, e, sccCount, dagNodes, dagEdges,
                sccTimeMs, topoTimeMs, spTimeMs, lpTimeMs, criticalPathLength
        );
    }
}