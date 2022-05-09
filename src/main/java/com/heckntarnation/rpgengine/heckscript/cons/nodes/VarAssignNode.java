package com.heckntarnation.rpgengine.heckscript.cons.nodes;

import com.heckntarnation.rpgengine.heckscript.cons.Token;

/**
 *
 * @author Ben
 */
public class VarAssignNode extends Node {

    public Token name;
    public Node value;

    public VarAssignNode(Token name, Node node) {
        this.name = name;
        this.value = node;
        this.start = name.start;
        this.end = node.end;
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }

}
