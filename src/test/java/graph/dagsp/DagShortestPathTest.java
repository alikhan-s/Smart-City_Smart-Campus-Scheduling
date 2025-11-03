package graph.dagsp;

import graph.util.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import graph.Graph;
import graph.util.Metrics;
import graph.util.SimpleMetrics;

import java.util.List;

/**
 * JUnit test for the DagShortestPath class.
 * Tests both shortest path and longest path (critical path)
 * calculations on a small, deterministic DAG.
 */
public class DagShortestPathTest {

    private DagShortestPath pathFinder;
    private Metrics metrics;
    private Graph dag;
    private List<Integer> topoOrder;

    /**
     * Set up a common graph for both tests.
     * Graph structure:
     * (5) -> 1 --(3)--> 2
     * 0
     * (10)----------------> 2
     *
     * Source: 0
     * Shortest path to 2 is 0->1->2 (cost 8)
     * Longest path to 2 is 0->2 (cost 10)
     */
    @BeforeEach
    public void setUp() {
        pathFinder = new DagShortestPath();
        metrics = new SimpleMetrics();

        // 1. Arrange Graph
        dag = new Graph(3, 0); // 3 vertices, source = 0
        dag.addEdge(0, 1, 5);
        dag.addEdge(0, 2, 10);
        dag.addEdge(1, 2, 3);

        // 2. Arrange Topological Order
        topoOrder = List.of(0, 1, 2);
    }

    /**
     * Tests the findShortestPaths method.
     * Expected distances: dist[0]=0, dist[1]=5, dist[2]=8
     * Expected path (to 2): [0, 1, 2]
     */
    @Test
    public void testShortestPaths() {
        // --- 1. Act ---
        pathFinder.findShortestPaths(dag, 0, topoOrder, metrics);
        double[] dists = pathFinder.getDistances();
        List<Integer> path = pathFinder.getPath(2); // Path to node 2

        // --- 2. Assert ---
        assertEquals(0.0, dists[0], "Distance to source (0) should be 0.0");
        assertEquals(5.0, dists[1], "Distance to node 1 should be 5.0 (from 0->1)");
        assertEquals(8.0, dists[2], "Distance to node 2 should be 8.0 (from 0->1->2)");

        List<Integer> expectedPath = List.of(0, 1, 2);
        assertEquals(expectedPath, path, "Shortest path reconstruction is incorrect");

        assertEquals(3, metrics.getCount(Operation.DAG_RELAXATION), "Should perform 3 relaxations (one for each edge)");
    }

    /**
     * Tests the findLongestPaths (Critical Path) method.
     * Expected distances: dist[0]=0, dist[1]=5, dist[2]=10
     * Expected path (to 2): [0, 2]
     */
    @Test
    public void testLongestPaths() {
        // --- 1. Act ---
        pathFinder.findLongestPaths(dag, 0, topoOrder, metrics);
        double[] dists = pathFinder.getDistances();
        List<Integer> path = pathFinder.getPath(2); // Path to node 2

        // --- 2. Assert ---
        assertEquals(0.0, dists[0], "Distance to source (0) should be 0.0");
        assertEquals(5.0, dists[1], "Distance to node 1 should be 5.0 (from 0->1)");
        assertEquals(10.0, dists[2], "Distance to node 2 should be 10.0 (from 0->2)");

        List<Integer> expectedPath = List.of(0, 2);
        assertEquals(expectedPath, path, "Longest path reconstruction is incorrect");

        assertEquals(3, metrics.getCount(Operation.DAG_RELAXATION), "Should perform 3 relaxations (one for each edge)");
    }
}