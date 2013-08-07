package com.sdc.cfg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DominatorTreeGenerator {
    private ArrayList<ArrayList<Integer>> myGraph;
    private ArrayList<ArrayList<Boolean>> myMarkEdge;
    private boolean[] mark;
    private int[] id, semi;
    private final int size;
    private int index = 0;

    public DominatorTreeGenerator(List<Node> myNodes) {
        this.size = myNodes.size();
        this.mark = new boolean[size];
        this.id = new int[size];
        this.semi = new int[size];
        this.myGraph = new ArrayList<ArrayList<Integer>>();
        this.myMarkEdge = new ArrayList<ArrayList<Boolean>>();
        // init
        for (Node node : myNodes) {
            ArrayList<Integer> indexesOfTails = new ArrayList<Integer>();
            ArrayList<Boolean> markEdges = new ArrayList<Boolean>();
            for (Node tailNode : node.getListOfTails()) {
                indexesOfTails.add(myNodes.indexOf(tailNode));
                markEdges.add(false);
            }
            myGraph.add(indexesOfTails);
            myMarkEdge.add(markEdges);
        }
        Arrays.fill(semi, -1);
    }

    // walk graph
    private void dfs(int v) {
        id[v] = index++;
        mark[v] = true;

        for (Integer tail : myGraph.get(v)) {
            if (!mark[tail]) {
                myMarkEdge.get(v).set(myGraph.get(v).indexOf(tail), true);
                dfs(tail);
            }
        }
    }

    private void FindSemi(int curVertex, int waypoint) {
        if (mark[curVertex]) return;
        mark[curVertex] = true;

        if (id[curVertex] < id[waypoint]) {
            if (semi[waypoint] == -1 || id[curVertex] < id[semi[waypoint]]) {
                semi[waypoint] = curVertex;
            }
            return;
        }
        for (int i = 0; i < size; i++) {
            for (int tail : myGraph.get(i))
                if (tail == curVertex) {
                    FindSemi(i, waypoint);
                }
        }
    }

    private void FindSemi() {
        int[] indexes = new int[size];
        for (int i = 0; i < size; i++) {
            indexes[id[i]] = i;
        }
        for (int i = size - 1; i > 0; i--) {
            Arrays.fill(mark, false);
            FindSemi(indexes[i], indexes[i]);
        }
    }

    private boolean FindDomi(int from, int to) {
        if (from == to) return true;
        boolean res = false;

        for (int i = 0; i < size; i++) {
            for (int j : myGraph.get(i)) {
                if (myMarkEdge.get(i).get(myGraph.get(i).indexOf(j)) && i == from && FindDomi(j, to)) {
                    res = true;
                    if (id[semi[j]] < id[semi[to]]) {
                        semi[to] = semi[j];
                    }
                }
            }
        }
        return res;
    }

    private void FindDomi() {
        for (int i = 0; i < size; i++) {
            FindDomi(semi[i], i);
        }
    }

    private void build() {
        dfs(0);
        FindSemi();
        FindDomi();
    }

    public int[] getDominatorTreeArray() {
        build();
        return semi;
    }

}



