package com.sdc.javascript;

import com.sdc.abstractLangauge.AbstractMethodVisitor;
import com.sdc.ast.controlflow.*;
import com.sdc.ast.controlflow.Invocation;
import com.sdc.ast.expressions.*;
import com.sdc.ast.expressions.identifiers.Field;
import com.sdc.ast.expressions.identifiers.Identifier;
import com.sdc.ast.expressions.identifiers.Variable;
import com.sdc.cfg.Edge;
import com.sdc.cfg.Node;
import org.objectweb.asm.*;
import org.objectweb.asm.util.Printer;

import java.util.*;

public class JSMethodVisitor extends AbstractMethodVisitor {
    private JSClassMethod myJSClassMethod;

    private Stack<Expression> myBodyDecompilationStack = new Stack<Expression>();
    private List<Statement> myStatements = new ArrayList<Statement>();

    private List<Node> myNodes = new ArrayList<Node>();
    private List<Edge> myEdges = new ArrayList<Edge>();

    private List<Label> myLabels = new ArrayList<Label>();
    private Map<Label, List<Integer>> myMap = new HashMap<Label, List<Integer>>();
    private List<Label> mySpecialLabels = new ArrayList<Label>();
    private List<Label> myDetectionLabels = new ArrayList<Label>();

    private boolean myHasDebugInformation = false;

    public JSMethodVisitor(JSClassMethod jsClassMethod) {
        super(Opcodes.ASM4, null);
        this.myJSClassMethod = jsClassMethod;
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        return null;
    }

    @Override
    public void visitAttribute(final Attribute attr) {

    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        return null;
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(final int parameter, final String desc, final boolean visible) {
        return null;
    }

    @Override
    public void visitCode() {
    }

    @Override
    public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {

    }

    @Override
    public void visitInsn(final int opcode) {
        final String opString = Printer.OPCODES[opcode];

        if (opString.contains("ADD") || opString.contains("SUB")
                || opString.contains("MUL") || opString.contains("DIV")) {
            Expression e1 = getTopOfBodyStack();
            Expression e2 = getTopOfBodyStack();
            Expression res = new BinaryExpression(BinaryExpression.OperationType.valueOf(opString.substring(1)), e2, e1);
            myBodyDecompilationStack.push(res);
        } else if (opString.contains("NEG")) {
            myBodyDecompilationStack.push(new UnaryExpression(UnaryExpression.OperationType.NEGATE, getTopOfBodyStack()));
        } else if (opString.contains("CONST_")) {
            myBodyDecompilationStack.push(new Constant(opString.substring(7), false));
        } else if (opString.equals("RETURN")) {
            myStatements.add(new Return());
        } else if (opString.contains("RETURN")) {
            myStatements.add(new Return(getTopOfBodyStack()));
        } else if (opString.contains("CMP")) {
            Expression e1 = getTopOfBodyStack();
            Expression e2 = getTopOfBodyStack();
            myBodyDecompilationStack.push(new BinaryExpression(e2, e1));
        } else if (opString.contains("ATHROW")) {
            myStatements.add(new Throw(getTopOfBodyStack()));
        }
    }

    @Override
    public void visitIntInsn(final int opcode, final int operand) {
        final String opString = Printer.OPCODES[opcode];

        if (opString.contains("IPUSH")) {
            myBodyDecompilationStack.push(new Constant(operand, false));
        }
    }

    @Override
    public void visitVarInsn(final int opcode, final int var) {
        final String opString = Printer.OPCODES[opcode];

        if (opString.contains("ALOAD") && var == 0) {
            return;
        } else if (opString.contains("LOAD")) {
            myBodyDecompilationStack.push(new Variable(var, null));
        } else if (opString.contains("STORE")) {
            Identifier v = new Variable(var, null);
            myStatements.add(new Assignment(v, getTopOfBodyStack()));
        }

        myJSClassMethod.addLocalVariableType(var, getDescriptor(opString.charAt(0)));
        String name;
        if (var <= myJSClassMethod.getLastLocalVariableIndex()) {
            name = "x" + var;
        } else {
            name = "y" + var;
        }
        myJSClassMethod.addLocalVariableName(var, name);
    }

    @Override
    public void visitTypeInsn(final int opcode, final String type) {
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        myBodyDecompilationStack.push(new Field(name));
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
        final String opString = Printer.OPCODES[opcode];

        List<Expression> arguments = new ArrayList<Expression>();
        for (int i = 1; i < desc.indexOf(')'); i++) {
            arguments.add(0, getTopOfBodyStack());
        }

        if (opString.contains("INVOKEVIRTUAL")) {
            if (myBodyDecompilationStack.isEmpty()) {
                myStatements.add(new Invocation(name, "", arguments));
            } else {
                myBodyDecompilationStack.push(new com.sdc.ast.expressions.Invocation(name, "", arguments));
            }
        } else if (opString.contains("INVOKESPECIAL")) {
            myBodyDecompilationStack.push(new New(new com.sdc.ast.expressions.Invocation(owner, "", arguments)));
        }
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
    }

    @Override
    public void visitJumpInsn(final int opcode, final Label label) {
        final String opString = Printer.OPCODES[opcode];

//        System.out.println(opstring + ": " + label);

        myLabels.add(label);

        if (opString.contains("IF")) {
            mySpecialLabels.add(label);
            applyNode();
            final Node node = new Node();
            myNodes.add(node);
            final int temp = getLeftEmptyNodeIndex();
            myEdges.add(new Edge(temp - 1, myNodes.size() - 1, Edge.EdgeType.TRUEEXIT));
            myEdges.add(new Edge(temp - 1, -1, Edge.EdgeType.FALSEEXIT));
        } else if (opString.contains("GOTO")) {
            if (!myMap.containsKey(label)) {
                List<Integer> list = new ArrayList<Integer>();
                list.add(getLeftEmptyNodeIndex());
                myMap.put(label, list);
            } else {
                myMap.get(label).add(getLeftEmptyNodeIndex());
            }
        }
    }

    @Override
    public void visitLabel(final Label label) {
        myDetectionLabels.add(label);
        if (myMap.containsKey(label)) {
            for (Integer i : myMap.get(label)) {
                final int last = myNodes.size() - 1;
                if (myNodes.get(last).isEmpty()) {
                    myEdges.add(new Edge(i, myNodes.size(), Edge.EdgeType.GOTO));
                } else {
                    myEdges.add(new Edge(i, myNodes.size() + 1, Edge.EdgeType.GOTO));
                }
            }
            myMap.remove(label);
        }

        if (mySpecialLabels.contains(label)) {
            Edge edge = getRightEmptyDestinationEdge();
            final int last = myNodes.size() - 1;
            if (myNodes.get(last).isEmpty()) {
                edge.setDestination(myNodes.size());
            } else {
                edge.setDestination(myNodes.size() + 1);
            }
        }

        if (myLabels.contains(label)) {
            applyNode();
            myLabels.remove(label);
        }

//        System.out.println(label);
    }

    @Override
    public void visitLdcInsn(final Object cst) {
        myBodyDecompilationStack.push(new Constant(cst, cst instanceof String));
    }

    @Override
    public void visitIincInsn(final int var, final int increment) {
    }

    @Override
    public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label... labels) {
    }

    @Override
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
    }

    @Override
    public void visitMultiANewArrayInsn(final String desc, final int dims) {
    }

    @Override
    public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
    }

    @Override
    public void visitLocalVariable(final String name, final String desc,
                                   final String signature, final Label start, final Label end,
                                   final int index) {
        if (!myHasDebugInformation) {
            myHasDebugInformation = true;
            myJSClassMethod.clearCollectedLocalVaribles();
        }

        myJSClassMethod.addLocalVariableName(index, name);
        myJSClassMethod.addLocalVariableType(index, getDescriptor(desc.charAt(0)));
    }

    @Override
    public void visitLineNumber(final int line, final Label start) {
    }

    @Override
    public void visitMaxs(final int maxStack, final int maxLocals) {
    }

    @Override
    public void visitEnd() {
        applyNode();

        boolean fl = true;

        for (final Label lbl : myMap.keySet()) {
            for (final Node node : myNodes) {
                if (node.containsLabel(lbl)) {
                    for (final Integer i : myMap.get(lbl)) {
                        myEdges.add(new Edge(i, myNodes.indexOf(node), Edge.EdgeType.GOTO));
                        if (i == (myNodes.size() - 2)) {
                            for (final Edge edge1 : myEdges) {
                                if ((edge1.getEdgeType() == Edge.EdgeType.FALSEEXIT)
                                        && (edge1.getSource() == myNodes.indexOf(node))
                                        && (edge1.getDestination() == (myNodes.size() - 1))) {
                                    fl = false;
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
        if (fl) {
            myEdges.add(new Edge(myNodes.size() - 2, myNodes.size() - 1));
        }

        for (int i = 0; i < myNodes.size() - 1; i++) {
            fl = false;
            for (final Edge edge : myEdges) {
                if (edge.getSource() == i) {
                    fl = true;
                    break;
                }
            }
            if (!fl) {
                myEdges.add(new Edge(i, i + 1));
            }
        }

        List<com.sdc.javascript.statements.Statement> statements =
                new ArrayList<com.sdc.javascript.statements.Statement>();
        for (Statement st : myStatements) {
            statements.add(Converter.convertStatement(st));
        }
        myJSClassMethod.setBody(statements);
        myJSClassMethod.setNodes(myNodes);
        myJSClassMethod.setEdges(myEdges);
        myJSClassMethod.drawCFG();
    }

    private Edge getRightEmptyDestinationEdge() {
        int k = 0;
        for (int i = myEdges.size() - 1; i >= 0; i--) {
            if (myEdges.get(i).getDestination() == -1) {
                k = i;
                break;
            }
        }
        return myEdges.get(k);
    }


    private Integer getLeftEmptyNodeIndex() {
        for (final Node node : myNodes) {
            if (node.isEmpty() && !node.isEmpty()) {
                return myNodes.indexOf(node);
            }
        }
        return myNodes.size();
    }


    private void applyNode() {
        Integer i = getLeftEmptyNodeIndex();
        if (i != myNodes.size()) {
            myNodes.get(i).setStatements(new ArrayList<Statement>(myStatements));
            myNodes.get(i).setInnerLabels(new ArrayList<Label>(myDetectionLabels));
            myDetectionLabels.clear();
            if (myNodes.get(i).getStatements().isEmpty()) {
                myNodes.get(i).setEmpty(true);
            }
        } else {
            Node node = new Node(new ArrayList<Statement>(myStatements), new ArrayList<Label>(myDetectionLabels));
            myDetectionLabels.clear();
            if (node.getStatements().isEmpty()) {
                node.setEmpty(true);
            }
            myNodes.add(node);
        }
//        myStatements.clear();
    }

    private String getDescriptor(final char descriptor) {
        switch (descriptor) {
            case 'B':
                return "byte ";
            case 'J':
                return "long ";
            case 'Z':
                return "boolean ";
            case 'I':
                return "int ";
            case 'S':
                return "short ";
            case 'C':
                return "char ";
            case 'F':
                return "float ";
            default:
                return "double ";
        }
    }

    private Expression getTopOfBodyStack() {
        if (myBodyDecompilationStack.isEmpty()) {
            final int lastIndex = myStatements.size() - 1;
            final Statement lastStatement = myStatements.get(lastIndex);

            if (lastStatement instanceof Invocation) {
                Invocation invoke = (Invocation) lastStatement;
                myStatements.remove(lastIndex);
                return new com.sdc.ast.expressions.Invocation(invoke.getFunction(), "", invoke.getArguments());
            } else if (lastStatement instanceof Assignment) {
                return ((Assignment) lastStatement).getRight();
            }
        }
        return myBodyDecompilationStack.pop();
    }
}
