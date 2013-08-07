package com.sdc.cfg;

import com.sdc.cfg.nodes.Node;

import java.io.File;
import java.util.List;

public class GraphDrawer {
    private final List<Node> myNodes;
    //   private final List<Edge> myEdges;

    private GraphViz myGraphViz;

    private final int myNestSize;
    private final int myTextWidth;

    public GraphDrawer(final List<Node> myNodes, final int nestSize, final int textWidth) {
        this.myNodes = myNodes;
        //this.myEdges = myEdges;
        this.myNestSize = nestSize;
        this.myTextWidth = textWidth;
    }

    public void simplyDraw() {
        myGraphViz = new GraphViz();

        myGraphViz.addln(myGraphViz.start_graph());
        drawEdges();
        myGraphViz.addln(myGraphViz.end_graph());

        final String type = "gif";
        final File out = new File("simple." + type);
        myGraphViz.writeGraphToFile(myGraphViz.getGraph(myGraphViz.getDotSource(), type), out);
    }

    public void draw() {
        myGraphViz = new GraphViz();

        myGraphViz.addln(myGraphViz.start_graph());
        myGraphViz.addln("graph [splines=ortho];");
        myGraphViz.addln("node [shape=record];");

 /*       for (int i = 0; i < myNodes.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Statement statement : myNodes.get(i).getStatements()) {
                stringBuilder.append(
                        PrettyPackage.pretty(
                                myTextWidth
                                , JavaClassPrinterPackage.printStatement(statement, myNestSize)
                        )
                ).append(";\\l");
            }
            myGraphViz.addln(i + " [label=" + '"' + stringBuilder + '"' + "];");
        }
*/
        drawEdges();
        myGraphViz.addln(myGraphViz.end_graph());
    //    System.out.println(myGraphViz.getDotSource());

        final String type = "gif";
        final File out = new File("extended." + type);
        myGraphViz.writeGraphToFile(myGraphViz.getGraph(myGraphViz.getDotSource(), type), out);
    }

    private void drawEdges() {
        for (int i = 0; i < myNodes.size(); i++) {
            for (Node node : myNodes.get(i).getListOfTails()) {
                StringBuilder sb = new StringBuilder("");
                myGraphViz.addln(sb.append(i)
                        .append(" -> ")
                        .append(myNodes.indexOf(node))
                        .append(";").toString());
            }
        }

    /*    for (Node node : myNodes) {
            String sl;
           *//**//* switch (edge.getEdgeType()) {
                case TRUEEXIT: {
                    sl = "[color=azure4]";
                    break;
                }
                case FALSEEXIT: {
                    sl = "[color=navy]";
                    break;
                }
                case GOTO: {
                    sl = "[color=red2]";
                    break;
                }
                default: {
                    sl = "";
                    break;
                }*//**//*
            }
            StringBuilder sb = new StringBuilder("");
            myGraphViz.addln(sb.append(String.valueOf(edge.getSource()))
                    .append(" -> ")
                    .append(edge.getDestination())
                    .append(sl)
                    .append(";").toString());
        }*/
    }
}
