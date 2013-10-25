package com.sdc.util.graph;

import com.decompiler.Settings;
import com.sdc.cfg.nodes.Node;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class GraphDrawer {
    private final List<Node> myNodes;
    private final String myFileName;

    public GraphDrawer(final @NotNull List<Node> myNodes, final @NotNull String myFileName) {
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

        final String path = Settings.getInstance().getPath();
        final String folder = (path == null) ? "graphs\\" : path + "\\graphs\\";
        final File dir = new File(folder);
        dir.mkdir();
        final String type = "png";
        final File out = new File(folder + myFileName + "." + type);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
    }
}
