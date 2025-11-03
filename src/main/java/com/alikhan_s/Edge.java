package com.alikhan_s;

public class Edge {
    public final int u;
    public final int v;
    public final int w; // Вес ребра

    public Edge(int u, int v, int w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    public int getW() {
        return w;
    }

    public int getV() {
        return v;
    }

    public int getU() {
        return u;
    }
}