package graph.util;

public enum Operation {
    // For SCC (Tarjan)
    DFS_VISIT,
    DFS_EDGE_TRAVERSAL,

    // For Topological Sort (Kahn)
    TOPO_QUEUE_PUSH,
    TOPO_QUEUE_POP,

    // For DAG Shortest Path
    DAG_RELAXATION
}