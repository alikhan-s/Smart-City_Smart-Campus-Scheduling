package graph.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Utility class to export a list of AnalysisResult objects
 * to a CSV file and print a summary table to the console.
 */
public class ResultExporter {

    /**
     * Prints a formatted Markdown summary table to the console.
     * @param results The list of results to print.
     */
    public static void printAsMarkdownTable(List<AnalysisResult> results) {
        System.out.println("\n--- Summary Table of Results ---");
        System.out.println(AnalysisResult.getMarkdownHeader());
        System.out.println(AnalysisResult.getMarkdownSeparator());

        for (AnalysisResult result : results) {
            System.out.println(result.toMarkdownRow());
        }
        System.out.println("\n(N=Vertices, E=Edges, SCCs=Strong Con. Comp., DAG V/E=Vertices/Edges in condensation Graph)");
    }

    /**
     * Exports the full list of results to a CSV file.
     * @param results The list of results to export.
     * @param filePath The path for the output CSV file (e.g., "analysis_results.csv").
     */
    public static void exportToCSV(List<AnalysisResult> results, String filePath) {
        System.out.println("\n--- Export CSV ---");

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {

            writer.write(AnalysisResult.getCsvHeader());

            for (AnalysisResult result : results) {
                writer.write(result.toCsvRow());
            }

            System.out.println("  -> Successfully saved " + results.size() + " records to file " + filePath);

        } catch (IOException e) {
            System.err.println("  -> Error while writing CSV file: " + e.getMessage());
        }
    }
}