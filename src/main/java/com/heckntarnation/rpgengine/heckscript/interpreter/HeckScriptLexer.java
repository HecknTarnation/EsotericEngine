package com.heckntarnation.rpgengine.heckscript.interpreter;

import static com.heckntarnation.rpgengine.handlers.HeckScriptHandler.*;
import com.heckntarnation.rpgengine.heckscript.HeckScript;
import com.heckntarnation.rpgengine.heckscript.cons.Position;
import com.heckntarnation.rpgengine.heckscript.cons.Token;
import com.heckntarnation.rpgengine.heckscript.exception.ExpectedCharException;
import com.heckntarnation.rpgengine.heckscript.exception.InvalidCharException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Ben
 */
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
            } else if (DIGITS.contains(currentChar + "")) {
                tokens.add(makeNumber());
            } else if (LETTERS.contains(currentChar + "")) {
                tokens.add(makeIdentifier());
            } else if (currentChar == '+') {
                tokens.add(new Token(T_PLUS).setStart(position));
                advance();
            } else if (currentChar == '-') {
                tokens.add(new Token(T_MINUS).setStart(position));
                advance();
            } else if (currentChar == '*') {
                tokens.add(new Token(T_MUL).setStart(position));
                advance();
            } else if (currentChar == '/') {
                tokens.add(new Token(T_DIV).setStart(position));
                advance();
            } else if (currentChar == '(') {
                tokens.add(new Token(T_LPAREN).setStart(position));
                advance();
            } else if (currentChar == ')') {
                tokens.add(new Token(T_RPAREN).setStart(position));
                advance();
            } else if (currentChar == '^') {
                tokens.add(new Token(T_POW).setStart(position));
                advance();
            } else if (currentChar == '!') {
                Object[] returns = makeNotEqual();
                Token tok = (Token) returns[0];
                if (returns[1] != null) {
                    throw (InvalidCharException) returns[1];
                }
                tokens.add(new Token(T_EQ).setStart(position));
            } else if (currentChar == '=') {
                tokens.add(makeEqual());
            } else if (currentChar == '<') {
                tokens.add(makeLessThan());
            } else if (currentChar == '>') {
                tokens.add(makeGreaterThan());
            } else {
                Position pos = position.copy();
                char errChar = currentChar;
                advance();
                throw new InvalidCharException("Invalid Character '" + errChar + "' Found in " + pos.fileName + ", Line: " + (pos.lineNum + 1));
            }
        }
        tokens.add(new Token(T_EOF).setStart(position));
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
        Position start = position.copy();
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
            return new Token(T_INT, numStr).setStart(start).setEnd(position);
        } else {
            return new Token(T_FLOAT, numStr).setStart(start).setEnd(position);
        }
    }

    private Token makeIdentifier() {
        String str = "";
        Position start = position.copy();

        while (currentChar != '`' && (LETTERS_DIGITS + "_").contains(currentChar + "")) {
            str += currentChar;
            advance();
        }

        String tokenType = (Arrays.asList(KEYWORDS).contains(str) ? T_KEYWORD : T_IDENTIFIER);
        return new Token(tokenType, str, start, position);
    }

    private Object[] makeNotEqual() {
        Position start = position.copy();
        advance();
        if (currentChar == '=') {
            advance();
            return new Object[]{new Token(T_NE).setStart(start).setEnd(position), null};
        }
        advance();
        return new Object[]{null, new ExpectedCharException("Expected '=' after '!'. Found in " + start.fileName + ", Line: " + (start.lineNum + 1))};
    }

    private Token makeEqual() {
        Position start = position.copy();
        String tokType = T_EQ;
        advance();
        if (currentChar == '=') {
            advance();
            tokType = T_EE;
        }

        return new Token(tokType).setStart(start).setEnd(position);
    }

    private Token makeLessThan() {
        Position start = position.copy();
        String tokType = T_LT;
        advance();
        if (currentChar == '=') {
            advance();
            tokType = T_LTE;
        }

        return new Token(tokType).setStart(start).setEnd(position);
    }

    private Token makeGreaterThan() {
        Position start = position.copy();
        String tokType = T_GT;
        advance();
        if (currentChar == '=') {
            advance();
            tokType = T_GTE;
        }

        return new Token(tokType).setStart(start).setEnd(position);
    }

}
