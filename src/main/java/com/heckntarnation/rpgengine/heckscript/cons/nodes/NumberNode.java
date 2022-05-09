package com.heckntarnation.rpgengine.heckscript.cons.nodes;

import com.heckntarnation.rpgengine.heckscript.cons.Token;

/**
 *
 * @author Ben
 */
public class NumberNode extends Node {

    public NumberNode(Token token) {
        this.token = token;
        this.start = token.start;
        this.end = token.end;
    }

    @Override
    public String toString() {
        return token.toString();
    }
}
