package com.heckntarnation.rpgengine.heckscript.cons;

/**
 *
 * @author Ben
 */
public class Token {

    public String type;
    public String value;
    public Position start;
    public Position end;

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public Token(String type, String value, Position start, Position end) {
        this.type = type;
        this.value = value;
        this.start = start;
        this.end = end;
    }

    public Token(String type) {
        this.type = type;
    }

    public Token setStart(Position pos) {
        this.start = pos;
        return this;
    }

    public Token setEnd(Position pos) {
        this.end = pos;
        return this;
    }

    public boolean matches(String type, String value) {
        return this.type.equals(type) && this.value.equals(value);
    }

    @Override
    public String toString() {
        if (this.value != null) {
            return String.format("%1s:%2s", this.type, this.value);
        } else {
            return String.format("%1s", this.type);
        }
    }
}
