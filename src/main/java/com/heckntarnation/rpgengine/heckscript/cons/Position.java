package com.heckntarnation.rpgengine.heckscript.cons;

import com.heckntarnation.rpgengine.heckscript.HeckScript;

/**
 *
 * @author Ben
 */
public class Position {

    public int index;
    public int lineNum;
    public int col;
    public String fileName;
    public HeckScript script;

    public Position(int index, int lineNum, int col, String fileName, HeckScript script) {
        this.index = index;
        this.lineNum = lineNum;
        this.col = col;
        this.fileName = fileName;
        this.script = script;
    }

    public Position advance(char currentChar) {
        this.index++;
        this.col++;
        if (currentChar == '\n') {
            this.lineNum++;
            this.col = 0;
        }
        return this;
    }

    public Position copy() {
        return new Position(this.index, this.lineNum, this.col, this.fileName, this.script);
    }
}
