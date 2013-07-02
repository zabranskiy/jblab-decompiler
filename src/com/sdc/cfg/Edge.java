package com.sdc.cfg;

public class Edge {
    private final int mySource;
    private int myDestination;
    private final EdgeType myType;

    public enum EdgeType {
        NONE, TRUEEXIT, FALSEEXIT, GOTO
    }

    public Edge(final int mySource, final int myDestination, final EdgeType type) {
        this.mySource = mySource;
        this.myDestination = myDestination;
        this.myType = type;
    }

    public Edge(final int src, final int dest) {
        this.mySource = src;
        this.myDestination = dest;
        this.myType = EdgeType.NONE;
    }

    public int getSource() {
        return mySource;
    }

    public int getDestination() {
        return myDestination;
    }

    public EdgeType getEdgeType() {
        return myType;
    }

    public void setDestination(final int dest) {
        this.myDestination = dest;
    }
}
