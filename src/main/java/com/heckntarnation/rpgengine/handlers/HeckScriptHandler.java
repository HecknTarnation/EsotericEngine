package com.heckntarnation.rpgengine.handlers;

import com.heckntarnation.rpgengine.heckscript.HeckScript;
import com.heckntarnation.rpgengine.heckscript.InvalidCharException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles dialog, mainly by implementing the HeckScript language.
 *
 * @author Ben
 */
public class HeckScriptHandler {

    public static final String T_INT = "INT";
    public static final String T_FLOAT = "FLOAT";
    public static final String T_PLUS = "PLUS";
    public static final String T_MINUS = "MINUS";
    public static final String T_MUL = "MUL";
    public static final String T_DIV = "DIV";
    public static final String T_LPAREN = "LPAREN";
    public static final String T_RPAREN = "RPAREN";
    public static final String DIGITS = "0123456789";

    /**
     * Parses a HeckScript file, returning a ready-to-use HeckScript object.
     *
     * @param file
     * @return
     */
    public HeckScript readHeckScriptFile(File file) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HeckScriptHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        //array list to hold the script
        ArrayList<String> lines = new ArrayList<>();
        try {
            //reads the script file into the array
            String line = br.readLine();
            while (line != null) {
                lines.add(line + " ");
                line = br.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(HeckScriptHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new HeckScript(file.getName(), lines);
    }

    /**
     * Runs a HeckScript object.
     *
     * @param script
     * @throws com.heckntarnation.rpgengine.heckscript.InvalidCharException
     */
    public void runHeckScirpt(HeckScript script) throws InvalidCharException {
        HeckScriptLexer lexer = new HeckScriptLexer();
        ArrayList<Token> tokens = lexer.tokinize(script);
        tokens.forEach(l -> {
            System.out.println(l);
        });
    }

    public class Token {

        public String type;
        public String value;

        public Token(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public Token(String type) {
            this.type = type;
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

    public class HeckScriptLexer {

        public Position position;
        public char currentChar = '`';
        public char[] code;

        public ArrayList<Token> tokinize(HeckScript script) throws InvalidCharException {
            position = new Position(-1, 0, -1, script.name, script);
            StringBuilder sb = new StringBuilder();
            script.lines.forEach(l -> {
                sb.append(l).append("\n");
            });
            code = sb.toString().toCharArray();
            advance();
            ArrayList<Token> tokens = new ArrayList<>();
            while (currentChar != '`') {
                if (currentChar == ' ' || currentChar == '\t') {
                    advance();
                } else if (currentChar == '+') {
                    tokens.add(new Token(T_PLUS));
                    advance();
                } else if (currentChar == '-') {
                    tokens.add(new Token(T_MINUS));
                    advance();
                } else if (currentChar == '*') {
                    tokens.add(new Token(T_MUL));
                    advance();
                } else if (currentChar == '/') {
                    tokens.add(new Token(T_DIV));
                    advance();
                } else if (currentChar == '(') {
                    tokens.add(new Token(T_LPAREN));
                    advance();
                } else if (currentChar == ')') {
                    tokens.add(new Token(T_RPAREN));
                    advance();
                } else if (DIGITS.contains(currentChar + "")) {
                    tokens.add(makeNumber());
                } else {
                    Position pos = position.copy();
                    char errChar = currentChar;
                    advance();
                    throw new InvalidCharException("Invalid Character '" + errChar + "' Found in " + pos.fileName + ", Line: " + (pos.lineNum + 1));
                }
            }
            return tokens;
        }

        private void advance() {
            position.advance(currentChar);
            if (position.index >= code.length - 1) {
                currentChar = '`';
                return;
            }
            currentChar = code[position.index];
        }

        private Token makeNumber() {
            String numStr = "";
            short dotCount = 0;
            while ((DIGITS + ".").contains(currentChar + "")) {
                if (currentChar == '.') {
                    if (dotCount == 1) {
                        break;
                    }
                    dotCount++;
                    numStr += ".";
                } else {
                    numStr += currentChar;
                }
                advance();
            }
            if (dotCount == 0) {
                return new Token(T_INT, numStr);
            } else {
                return new Token(T_FLOAT, numStr);
            }
        }

    }

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
}
