package com.heckntarnation.rpgengine.heckscript.cons.nodes;

import com.heckntarnation.rpgengine.heckscript.cons.Token;

/**
 *
 * @author Ben
 */
public class UnOpNode extends Node {

    public Token opToken;
    public Node node;

    public UnOpNode(Token opToken, Node node) {
        this.opToken = opToken;
        this.node = node;
        this.start = opToken.start;
        this.end = node.end;
    }

    @Override
    public String toString() {
        return String.format("%1s, %2s", this.opToken, this.node);
    }

}
