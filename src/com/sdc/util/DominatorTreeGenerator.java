package com.sdc.util;

import com.sdc.cfg.nodes.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class DominatorTreeGenerator {
    private List<Node> myNodes;
    private ArrayList<ArrayList<Integer>> myGraph;
    private ArrayList<ArrayList<Boolean>> myMarkEdge;
    private boolean[] mark;
    private int[] id, semi;
    private final int size;
    private int index;

    public DominatorTreeGenerator(List<Node> myNodes) {
        this.myNodes = myNodes;
        this.size = myNodes.size();
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

    private void build(boolean isPostDominatorTree) {
        index = 0;
        mark = new boolean[size];
        id = new int[size];
        semi = new int[size];
        myGraph = new ArrayList<ArrayList<Integer>>();
        myMarkEdge = new ArrayList<ArrayList<Boolean>>();

        Arrays.fill(semi, -1);

        ListIterator li = myNodes.listIterator(isPostDominatorTree ? myNodes.size() : -1);

        while (isPostDominatorTree ? li.hasPrevious() : li.hasNext()) {
            Node node = (Node) (isPostDominatorTree ? li.previous() : li.next());
            List<Node> nodes = isPostDominatorTree ? node.getAncestors() : node.getListOfTails();
            ArrayList<Integer> indexesOfTails = new ArrayList<Integer>();
            ArrayList<Boolean> markEdges = new ArrayList<Boolean>();
            for (Node tailNode : nodes) {
                indexesOfTails.add(myNodes.indexOf(tailNode));
                markEdges.add(false);
            }
            myGraph.add(indexesOfTails);
            myMarkEdge.add(markEdges);
        }

        dfs(isPostDominatorTree ? size - 1 : 0);
        FindSemi();
        FindDomi();
    }

    public int[] getDominators() {
        return getTree(false);
    }

    public int[] getPostDominators() {
        return getTree(true);
    }

    private int[] getTree(boolean isPostDominatorTree) {
        build(isPostDominatorTree);
        return semi;
    }
}



