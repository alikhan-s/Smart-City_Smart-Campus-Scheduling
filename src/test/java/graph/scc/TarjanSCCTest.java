package graph.scc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import graph.Graph;
import graph.util.Metrics;
import graph.util.SimpleMetrics;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * JUnit test for the TarjanSCC class.
 * Fulfills the "small deterministic cases" requirement.
 */
public class TarjanSCCTest {

    private TarjanSCC sccFinder;
    private Metrics metrics;

    /**
     * Set up common objects before each test.
     */
    @BeforeEach
    public void setUp() {
        sccFinder = new TarjanSCC();
        metrics = new SimpleMetrics();
    }

    /**
     * Tests a simple, deterministic graph with one cycle.
     * Graph structure:
     * 0 -> 1
     * 1 -> 2
     * 2 -> 1 (Cycle {1, 2})
     * 1 -> 3
     * 3 -> 4
     * Expected SCCs: {0}, {1, 2}, {3}, {4}
     */
    @Test
    public void testSmallGraphWithOneCycle() {
        // --- 1. Arrange ---
        Graph graph = new Graph(5, 0);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 1, 1); // Cycle
        graph.addEdge(1, 3, 1);
        graph.addEdge(3, 4, 1);

        // --- 2. Act ---
        List<List<Integer>> sccs = sccFinder.findSccs(graph, metrics);

        // --- 3. Assert ---

        assertEquals(4, sccs.size(), "Should find exactly 4 SCCs");


        Set<Set<Integer>> actualSccs = sccs.stream()
                .map(list -> new HashSet<>(list))
                .collect(Collectors.toSet());

        Set<Set<Integer>> expectedSccs = Set.of(
                Set.of(0),
                Set.of(1, 2), // The cycle
                Set.of(3),
                Set.of(4)
        );

        assertEquals(expectedSccs, actualSccs, "The content of the SCCs is incorrect");
    }

    /**
     * Tests a simple DAG (Directed Acyclic Graph).
     * All nodes should be in their own SCC of size 1.
     */
    @Test
    public void testSimpleDAG() {
        // --- 1. Arrange ---
        Graph graph = new Graph(4, 0);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        // --- 2. Act ---
        List<List<Integer>> sccs = sccFinder.findSccs(graph, metrics);

        // --- 3. Assert ---
        assertEquals(4, sccs.size(), "Should find 4 SCCs in a 4-node DAG");

        for (List<Integer> scc : sccs) {
            assertEquals(1, scc.size(), "Each SCC in a DAG should have size 1");
        }
    }
}