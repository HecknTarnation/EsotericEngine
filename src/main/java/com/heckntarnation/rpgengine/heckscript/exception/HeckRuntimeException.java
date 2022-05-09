package com.heckntarnation.rpgengine.heckscript.exception;

import com.heckntarnation.rpgengine.heckscript.cons.Context;
import com.heckntarnation.rpgengine.heckscript.cons.Position;

/**
 *
 * @author Ben
 */
public class HeckRuntimeException extends Exception {

    public HeckRuntimeException(String err) {
        super(err);
    }

    public static HeckRuntimeException contextRuntimeException(String err, Context context, Position pos) {
        String result = "";
        while (context != null) {
            result = String.format(" File %1s, line %1d in %2s\n", pos.fileName, pos.lineNum + 1, context.name) + result;
            pos = context.parentEntry;
            context = context.parent;
        }
        err += "\nTraceback:\n" + result;
        return new HeckRuntimeException(err);
    }

}
