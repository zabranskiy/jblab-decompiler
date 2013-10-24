package com.sdc.util.graph;

import com.sdc.cfg.nodes.Node;

import java.io.File;
import java.util.List;

public class GraphDrawer {
    private final List<Node> myNodes;
    private final String myFileName;

    public GraphDrawer(final List<Node> myNodes, String myFileName) {
        this.myNodes = myNodes;
        this.myFileName = myFileName;
    }

    public void draw() {
        final GraphViz gv = new GraphViz();

        gv.addln(gv.start_graph());
        for (final Node node : myNodes) {
            for (final Node tail : node.getListOfTails()) {
                StringBuilder sb = new StringBuilder("");
                gv.addln(sb.append(node.getIndex())
                        .append(" -> ")
                        .append(tail.getIndex())
                        .append(";").toString());
            }
            System.out.println();
        }
        gv.addln(gv.end_graph());

        final String folder = "graphs\\";
        final File dir = new File(folder);
        dir.mkdir();
        final String type = "png";
        final File out = new File(folder + myFileName + "." + type);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
    }
}
