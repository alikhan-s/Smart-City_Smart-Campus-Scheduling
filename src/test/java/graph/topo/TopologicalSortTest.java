package graph.topo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import graph.Graph;
import graph.util.Metrics;
import graph.util.SimpleMetrics;

import java.util.List;

/**
 * JUnit test for the TopologicalSort class (Kahn's Algorithm).
 * Tests for correct order in a DAG and for cycle detection.
 */
public class TopologicalSortTest {

    private TopologicalSort topoSorter;
    private Metrics metrics;

    /**
     * Set up common objects before each test.
     */
    @BeforeEach
    public void setUp() {
        topoSorter = new TopologicalSort();
        metrics = new SimpleMetrics();
    }

    /**
     * Tests a simple, deterministic DAG.
     * Graph structure (a simple line):
     * 3 -> 1 -> 2 -> 0 -> 4
     *
     * Expected order: [3, 1, 2, 0, 4]
     */
    @Test
    public void testSimpleDAGOrder() {
        // --- 1. Arrange ---
        Graph dag = new Graph(5, 3); // 5 вершин, source = 3
        dag.addEdge(3, 1, 1);
        dag.addEdge(1, 2, 1);
        dag.addEdge(2, 0, 1);
        dag.addEdge(0, 4, 1);

        List<Integer> expectedOrder = List.of(3, 1, 2, 0, 4);

        // --- 2. Act ---
        List<Integer> actualOrder = topoSorter.sort(dag, metrics);

        // --- 3. Assert ---
        assertEquals(expectedOrder, actualOrder, "Topological order is incorrect for the simple DAG");
    }

    /**
     * Tests that the sort method correctly throws an exception
     * when the graph contains a cycle.
     *
     * Graph structure:
     * 0 -> 1 -> 2 -> 0 (Cycle)
     */
    @Test
    public void testGraphWithCycleThrowsException() {
        // --- 1. Arrange ---
        Graph cyclicGraph = new Graph(3, 0); // 3-node cycle
        cyclicGraph.addEdge(0, 1, 1);
        cyclicGraph.addEdge(1, 2, 1);
        cyclicGraph.addEdge(2, 0, 1); // Cycle back to 0

        // --- 2. Act & 3. Assert ---
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            topoSorter.sort(cyclicGraph, metrics);
        }, "The sort method should throw an exception for cyclic graphs");

        assertTrue(exception.getMessage().contains("Graph contains a cycle"));
    }
}