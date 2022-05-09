package com.heckntarnation.rpgengine.heckscript.cons;

import java.util.HashMap;

/**
 *
 * @author Ben
 */
public class SymbolTable {

    public HashMap<String, Object> symbols;
    public SymbolTable parent;

    public SymbolTable() {
        this.symbols = new HashMap<>();
        this.parent = null;
    }

    public Object get(String var) {
        Object v = symbols.get(var);
        if (v == null && parent != null) {
            return parent.get(var);
        }
        return v;
    }

    public void set(String name, Object value) {
        symbols.put(name, value);
    }

    public void remove(String name) {
        symbols.remove(name);
    }
}
