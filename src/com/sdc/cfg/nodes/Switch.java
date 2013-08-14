package com.sdc.cfg.nodes;

import com.sdc.ast.expressions.Expression;
import org.objectweb.asm.Label;

import java.util.*;

public class Switch extends Node {
    private final Expression myExpr;
    private final int[] myKeys;
    private List<Label> myLabels = new ArrayList<Label>();

    private List<SwitchCase> myCases;
    private SwitchCase myDefaultCase;

    public Switch(Expression myExpr, int[] myKeys, List<Label> labels, int index) {
        this.myExpr = myExpr;
        this.myKeys = myKeys;
        this.myLabels = labels;
        this.index = index;
    }

    public List<Label> getLabels() {
        return myLabels;
    }

    public Node getNodeByKeyIndex(final int keyIndex) {
        final int nodeIndex = keyIndex == -1 ? myNodeTails.size() - 1 : keyIndex;
        return myNodeTails.get(nodeIndex);
    }

    public int[] getKeys() {
        return myKeys;
    }

    public int getKey(final int index) {
        return myKeys[index];
    }

    public Expression getExpr() {
        return myExpr;
    }

    public List<SwitchCase> getCases() {
        return myCases;
    }

    public boolean hasRealDefaultCase() {
        calculateCases();

        if (isDefaultCaseLast()) {
            for (int i = 0; i < myCases.size() - 2; i++) {
                if (defaultHasLinkFromCase(myCases.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean defaultHasLinkFromCase(final SwitchCase switchCase) {
        final int caseIndex = myCases.indexOf(switchCase);
        final int leftBound = switchCase.getCaseBody().getIndex();
        final int rightBound = myCases.get(caseIndex + 1).getCaseBody().getIndex();

        for (final Node ancestor : myDefaultCase.getCaseBody().getAncestors()) {
            final int ancestorIndex = ancestor.getIndex();

            if (ancestorIndex >= leftBound && ancestorIndex < rightBound) {
                return true;
            }
        }
        return false;
    }

    private boolean isDefaultCaseLast() {
        return myCases.get(myCases.size() - 1).getKeys().contains(null);
    }

    public void removeFakeDefaultCase() {
        myCases.remove(myDefaultCase);
//        myDefaultCase.getKeys().remove(null);
    }

    private void calculateCases() {
        final int tailsCount = getListOfTails().size();
        Map<Node, SwitchCase> switchCases = new HashMap<Node, SwitchCase>();

        for (int i = 0; i < tailsCount; i++) {
            final Integer key = i == tailsCount - 1 ? null : getKey(i);
            final Node caseBody = getNodeByKeyIndex(i);

            SwitchCase switchCase;
            if (!switchCases.containsKey(caseBody)) {
                switchCase = new SwitchCase(caseBody);
                switchCase.addKey(key);
                switchCases.put(caseBody, switchCase);
            } else {
                switchCase = switchCases.get(caseBody);
                switchCase.addKey(key);
            }

            if (key == null) {
                myDefaultCase = switchCase;
            }
        }

        List<SwitchCase> result = new ArrayList<SwitchCase>(switchCases.values());
        Collections.sort(result);

        myCases = result;
    }
}
