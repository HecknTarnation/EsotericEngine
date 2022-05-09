package com.heckntarnation.rpgengine.heckscript.cons;

import com.heckntarnation.rpgengine.heckscript.cons.nodes.Node;

/**
 *
 * @author Ben
 */
public class ParseResult {

    public Exception error;
    public Node node;
    public int advanceCount = 0;

    public ParseResult() {
    }

    public Node registerAdvancement() {
        this.advanceCount++;
        return null;
    }

    public Node register(ParseResult result) {
        this.advanceCount += result.advanceCount;
        if (result.error != null) {
            this.error = result.error;
            return result.node;
        }
        return result.node;
    }

    public ParseResult success(Node node) {
        this.node = node;
        return this;
    }

    public ParseResult failure(Exception error) {
        if (this.error != null || this.advanceCount == 0) {
            this.error = error;
        }
        return this;
    }
}
