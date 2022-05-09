package com.heckntarnation.rpgengine.heckscript.cons;

/**
 *
 * @author Ben
 */
public class RuntimeResult {

    public Object value;
    public Exception error;

    public Object register(RuntimeResult result) {
        if (result.error != null) {
            this.error = result.error;
        }
        return result.value;
    }

    public RuntimeResult success(Object value) {
        this.value = value;
        return this;
    }

    public RuntimeResult failure(Exception error) {
        this.error = error;
        return this;
    }

}
