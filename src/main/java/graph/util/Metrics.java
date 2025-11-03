package graph.util;

import java.util.Map;


public interface Metrics {
    void startTimer();
    void stopTimer();
    void increment(Operation op);
    long getElapsedTimeNano();
    long getCount(Operation op);
    Map<Operation, Long> getAllCounts();
    void reset();
    double getElapsedTimeMillis();
}