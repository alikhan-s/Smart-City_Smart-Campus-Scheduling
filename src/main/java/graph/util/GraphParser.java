package graph.util;

import graph.Graph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphParser {
    private static final Pattern N_PATTERN = Pattern.compile("\"n\":\\s*(\\d+)");
    private static final Pattern SOURCE_PATTERN = Pattern.compile("\"source\":\\s*(\\d+)");
    private static final Pattern EDGE_PATTERN = Pattern.compile(
            "\\{\"u\":\\s*(\\d+),\\s*\"v\":\\s*(\\d+),\\s*\"w\":\\s*(\\d+)\\}"
    );

    public static Graph loadGraph(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        String jsonContent = Files.readString(path);

        return parse(jsonContent);
    }

    private static Graph parse(String jsonContent) {
        int n = 0;
        int source = 0;

        Matcher nMatcher = N_PATTERN.matcher(jsonContent);
        if (nMatcher.find()) {
            n = Integer.parseInt(nMatcher.group(1));
        } else {
            throw new IllegalArgumentException("Could not find 'n' in JSON.");
        }

        Matcher sMatcher = SOURCE_PATTERN.matcher(jsonContent);
        if (sMatcher.find()) {
            source = Integer.parseInt(sMatcher.group(1));
        } else {
            System.err.println("Warning: 'source' not found in JSON, defaulting to 0.");
        }

        if (n <= 0) {
            throw new IllegalArgumentException("'n' must be > 0.");
        }

        Graph graph = new Graph(n, source);

        int edgesStartIndex = jsonContent.indexOf("\"edges\":");
        if (edgesStartIndex == -1) {
            return graph;
        }
        int edgesEndIndex = jsonContent.lastIndexOf("]");
        if (edgesEndIndex == -1 || edgesEndIndex < edgesStartIndex) {
            throw new IllegalArgumentException("Malformed 'edges' array in JSON.");
        }

        String edgesBlock = jsonContent.substring(edgesStartIndex, edgesEndIndex);

        Matcher edgeMatcher = EDGE_PATTERN.matcher(edgesBlock);
        while (edgeMatcher.find()) {
            int u = Integer.parseInt(edgeMatcher.group(1));
            int v = Integer.parseInt(edgeMatcher.group(2));
            int w = Integer.parseInt(edgeMatcher.group(3));

            graph.addEdge(u, v, w);
        }

        return graph;
    }
}