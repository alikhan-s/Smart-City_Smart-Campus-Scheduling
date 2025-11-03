package com.alikhan_s.util;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class SimpleMetrics implements Metrics {

    private long startTime;
    private long elapsedTime;
    private final Map<Operation, Long> counts;

    public SimpleMetrics() {
        this.counts = new EnumMap<>(Operation.class);
        reset();
    }

    @Override
    public void startTimer() {
        this.startTime = System.nanoTime();
    }

    @Override
    public void stopTimer() {
        this.elapsedTime = System.nanoTime() - this.startTime;
    }

    @Override
    public void increment(Operation op) {
        counts.put(op, counts.getOrDefault(op, 0L) + 1);
    }

    @Override
    public long getElapsedTimeNano() {
        return this.elapsedTime;
    }

    public double getElapsedTimeMillis() {
        return (double) this.elapsedTime / TimeUnit.MILLISECONDS.toNanos(1);
    }

    @Override
    public long getCount(Operation op) {
        return counts.getOrDefault(op, 0L);
    }

    @Override
    public Map<Operation, Long> getAllCounts() {
        return new EnumMap<>(counts); // Return a copy
    }

    @Override
    public void reset() {
        this.startTime = 0;
        this.elapsedTime = 0;
        this.counts.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("  Time: %.4f ms%n", getElapsedTimeMillis()));
        if (counts.isEmpty()) {
            sb.append("  Counters: (none)");
        } else {
            sb.append("  Counters: {\n");
            counts.forEach((op, count) ->
                    sb.append(String.format("    %s: %d%n", op.name(), count))
            );
            sb.append("  }");
        }
        return sb.toString();
    }
}