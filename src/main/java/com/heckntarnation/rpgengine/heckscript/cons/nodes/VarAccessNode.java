package com.heckntarnation.rpgengine.heckscript.cons.nodes;

import com.heckntarnation.rpgengine.heckscript.cons.Token;

/**
 *
 * @author Ben
 */
public class VarAccessNode extends Node {

    public Token name;

    public VarAccessNode(Token name) {
        this.name = name;
        this.start = name.start;
        this.end = name.end;
    }
}
