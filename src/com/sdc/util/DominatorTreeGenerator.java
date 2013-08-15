package com.sdc.util;

import com.sdc.cfg.nodes.Node;

import java.util.*;

public class DominatorTreeGenerator {
    private List<Node> myNodes;
    private ArrayList<ArrayList<Integer>> myGraph;
    private ArrayList<ArrayList<Integer>> myDomiTree;
    private ArrayList<ArrayList<Integer>> myPostTree;
    private ArrayList<ArrayList<Boolean>> myMarkEdge;
    private boolean[] mark;
    private int[] id, semi, domi, post;
    private final int size;
    private int index;
    private int max;

    public DominatorTreeGenerator(List<Node> myNodes) {
        this.myNodes = myNodes;
        this.size = myNodes.size();
        if (size > 1) {
            this.domi = getDominators();
            this.post = getPostDominators();
            this.myDomiTree = getDomiTree();
            this.myPostTree = getPostTree();
        }
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
        for (int i = 0; i < size; i++) {
            myGraph.add(new ArrayList<Integer>());
            myMarkEdge.add(new ArrayList<Boolean>());
        }

        ListIterator li = myNodes.listIterator(isPostDominatorTree ? myNodes.size() : 0);

        while (isPostDominatorTree ? li.hasPrevious() : li.hasNext()) {
            Node node = (Node) (isPostDominatorTree ? li.previous() : li.next());
            List<Node> nodes = isPostDominatorTree ? node.getAncestors() : node.getListOfTails();
            for (Node tailNode : nodes) {
                myGraph.get(node.getIndex()).add(tailNode.getIndex());
                myMarkEdge.get(node.getIndex()).add(false);
            }
        }

        dfs(isPostDominatorTree ? size - 1 : 0);
        FindSemi();
        FindDomi();
    }

    private int[] getDominators() {
        return getTree(false);
    }

    private int[] getPostDominators() {
        return getTree(true);
    }

    private int[] getTree(boolean isPostDominatorTree) {
        build(isPostDominatorTree);
        return semi;
    }

    public int getRightIndexForLoop(int index) {
        Set<Integer> set = new HashSet<Integer>();
        max = -1;

        final int indexOfInnerLoopPart = myNodes.get(index).getListOfTails().get(0).getIndex();
        set.add(indexOfInnerLoopPart);

        Arrays.fill(mark, false);
        dfsDomiTree(myDomiTree, indexOfInnerLoopPart, set);

        Arrays.fill(mark, false);
        dfsPostTree(myPostTree, index, set);

        return max + 1;
    }

    private ArrayList<ArrayList<Integer>> getDomiTree() {
        ArrayList<ArrayList<Integer>> domiTree = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < size; i++) {
            domiTree.add(new ArrayList<Integer>());
        }

        for (int i = 1; i < domi.length; i++) {
            domiTree.get(domi[i]).add(i);
        }

        return domiTree;
    }

    private ArrayList<ArrayList<Integer>> getPostTree() {
        ArrayList<ArrayList<Integer>> postTree = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < size; i++) {
            postTree.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < post.length - 1; i++) {
            postTree.get(post[i]).add(i);
        }

        return postTree;
    }

    private void dfsDomiTree(ArrayList<ArrayList<Integer>> tree, int v, Set<Integer> set) {
        mark[v] = true;

        for (Integer tail : tree.get(v)) {
            if (!mark[tail]) {
                set.add(tail);
                dfsDomiTree(tree, tail, set);
            }
        }
    }

    private void dfsPostTree(ArrayList<ArrayList<Integer>> tree, int v, Set<Integer> set) {
        mark[v] = true;

        for (Integer tail : tree.get(v)) {
            if (!mark[tail]) {
                if (set.contains(tail) && max < tail) {
                    max = tail;
                }
                dfsPostTree(tree, tail, set);
            }
        }
    }

    public int[] getDomi() {
        return domi;
    }
}



