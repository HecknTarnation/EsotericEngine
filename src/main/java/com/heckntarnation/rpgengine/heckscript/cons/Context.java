package com.heckntarnation.rpgengine.heckscript.cons;

/**
 *
 * @author Ben
 */
public class Context {

    public String name;
    public Context parent;
    public Position parentEntry;
    public SymbolTable symbolTable;

    public Context(String name, Context parent, Position parentEntry) {
        this.name = name;
        this.parent = parent;
        this.parentEntry = parentEntry;
    }
}
