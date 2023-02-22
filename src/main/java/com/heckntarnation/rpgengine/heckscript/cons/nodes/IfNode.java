package com.heckntarnation.rpgengine.heckscript.cons.nodes;

import java.util.ArrayList;

/**
 *
 * @author Ben
 */
public class IfNode extends Node {

    public ArrayList<Node[]> cases;
    public Node elseCase;

    public IfNode(ArrayList<Node[]> cases, Node elseCase) {
        this.cases = cases;
        this.elseCase = elseCase;
        this.start = this.cases.get(0)[0].start;
        this.end = this.elseCase != null ? this.elseCase.end : this.cases.get(this.cases.size() - 1)[0].end;
    }

}
