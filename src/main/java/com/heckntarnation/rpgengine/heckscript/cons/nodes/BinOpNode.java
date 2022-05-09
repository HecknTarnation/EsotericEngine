package com.heckntarnation.rpgengine.heckscript.cons.nodes;

import com.heckntarnation.rpgengine.heckscript.cons.Token;

/**
 *
 * @author Ben
 */
public class BinOpNode extends Node {

    public Token opToken;
    public Node leftNode;
    public Node rightNode;

    public BinOpNode(Node leftNode, Token opToken, Node rightNode) {
        this.leftNode = leftNode;
        this.opToken = opToken;
        this.rightNode = rightNode;
        this.start = leftNode.start;
        this.end = rightNode.end;
    }

    @Override
    public String toString() {
        return String.format("(%1s, %2s, %3s)", this.leftNode, this.opToken, this.rightNode);
    }
}
