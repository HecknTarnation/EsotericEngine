package com.heckntarnation.rpgengine.handlers;

import com.heckntarnation.rpgengine.heckscript.HeckScript;
import com.heckntarnation.rpgengine.heckscript.InvalidCharException;
import com.heckntarnation.rpgengine.heckscript.InvalidSyntaxException;
import com.heckntarnation.rpgengine.heckscript.HeckRuntimeException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
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
    public static final String T_EOF = "EOF";
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
    public void runHeckScirpt(HeckScript script) throws InvalidCharException, Exception {
        HeckScriptLexer lexer = new HeckScriptLexer();
        HeckScriptParser parser = new HeckScriptParser();
        HeckScriptInterpreter interp = new HeckScriptInterpreter();
        Context rootContext = new Context("<program>", null, null);
        ArrayList<Token> tokens = lexer.tokinize(script);
        tokens.forEach(l -> {
            System.out.println(l);
        });
        Node parsedNode = parser.parse(tokens).node;
        System.out.println(parsedNode);
        Object result = interp.visit(parsedNode, rootContext);
        RuntimeResult res = (RuntimeResult) result;
        System.out.println(res.value);
    }

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
                } else if (DIGITS.contains(currentChar + "")) {
                    tokens.add(makeNumber());
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

    }

    public class HeckScriptParser {

        ArrayList<Token> tokens;
        int tokenIndex = -1;
        Token currentToken;

        private Token advance() {
            this.tokenIndex++;
            if (this.tokenIndex < tokens.size()) {
                this.currentToken = tokens.get(this.tokenIndex);
            }
            return this.currentToken;
        }

        private ParseResult factor() throws Exception {
            ParseResult result = new ParseResult();
            Token tok = this.currentToken;
            if (tok.type.equals(T_PLUS) || tok.type.equals(T_MINUS)) {

                result.register(this.advance());
                Node factor = result.register(this.factor());
                if (result.error != null) {
                    throw result.error;
                }
                return result.success(new UnOpNode(tok, factor));
            } else if (tok.type.equals(T_INT) || tok.type.equals(T_FLOAT)) {
                NumberNode node = new NumberNode(tok);
                result.register(this.advance());
                return result.success(node);
            } else if (tok.type.equals(T_LPAREN)) {
                result.register(this.advance());
                Node expression = result.register(this.expression());
                if (result.error != null) {
                    throw result.error;
                }
                if (this.currentToken.type.equals(T_RPAREN)) {
                    result.register(this.advance());
                    return result.success(expression);
                } else {
                    result.failure(new InvalidSyntaxException("Expected ')' at COL:" + this.currentToken.start.col + " on Line " + (this.currentToken.start.lineNum + 1)));
                }
            }
            return result.failure(new InvalidSyntaxException("Expected Int or Float at COL:" + this.currentToken.start.col + " on Line " + this.currentToken.start.lineNum + 1));
        }

        private ParseResult term() throws Exception {
            return binop(new String[]{T_MUL, T_DIV}, (Callable) () -> factor());
        }

        private ParseResult expression() throws Exception {
            return binop(new String[]{T_PLUS, T_MINUS}, (Callable) () -> term());
        }

        private ParseResult binop(String[] ops, Callable func) throws Exception {
            ParseResult result = new ParseResult();
            Node left = result.register((ParseResult) func.call());
            if (result.error != null) {
                throw result.error;
            }
            while (Arrays.asList(ops).contains(this.currentToken.type)) {
                Token op = this.currentToken;
                result.register(advance());
                Node right = result.register((ParseResult) func.call());
                if (result.error != null) {
                    throw result.error;
                }
                left = new BinOpNode(left, op, right);
            }
            return result.success(left);
        }

        public ParseResult parse(ArrayList<Token> toks) throws Exception {
            this.tokens = toks;
            this.advance();
            ParseResult result = this.expression();
            if (!this.currentToken.type.equals(T_EOF)) {
                throw new InvalidSyntaxException("Expected '+', '-', '*', '/' at COL:" + currentToken.start.col + " on Line " + this.currentToken.start.lineNum);
            }
            return result;
        }

    }

    public class HeckScriptInterpreter {

        public RuntimeResult visit(Node node, Context context) throws Exception {
            if (node instanceof NumberNode) {
                return visitNumberNode((NumberNode) node, context);
            } else if (node instanceof BinOpNode) {
                return visitBinOpNode((BinOpNode) node, context);
            } else if (node instanceof UnOpNode) {
                return visitUnOpNode((UnOpNode) node, context);
            } else {
                visitDefault(node);
            }
            return null;
        }

        private RuntimeResult visitNumberNode(NumberNode node, Context context) {
            return new RuntimeResult().success(new Number(node.token.value).setContext(context).setPosition(node.start, node.end));
        }

        private RuntimeResult visitBinOpNode(BinOpNode node, Context context) throws Exception {
            RuntimeResult result = new RuntimeResult();
            Number left = (Number) result.register(this.visit(node.leftNode, context));
            if (result.error != null) {
                throw result.error;
            }
            Number right = (Number) result.register(this.visit(node.rightNode, context));
            Number resultNumber = null;
            if (node.opToken.type.equals(T_PLUS)) {
                resultNumber = left.addedTo(right);
            } else if (node.opToken.type.equals(T_MINUS)) {
                resultNumber = left.subtractBy(right);
            } else if (node.opToken.type.equals(T_MUL)) {
                resultNumber = left.multiplyBy(right);
            } else if (node.opToken.type.equals(T_DIV)) {
                resultNumber = left.divideBy(right);
            }
            result.error = resultNumber.error;
            if (result.error != null) {
                throw result.error;
            }
            return result.success(resultNumber.setPosition(node.start, node.end));
        }

        private RuntimeResult visitUnOpNode(UnOpNode node, Context context) throws Exception {
            RuntimeResult result = new RuntimeResult();
            Number number = (Number) result.register(this.visit(node.node, context));
            if (node.opToken.type.equals(T_MINUS)) {
                number = number.multiplyBy(new Number(-1));
            }
            result.error = number.error;
            if (result.error != null) {
                throw result.error;
            }
            return result.success(number.setPosition(node.start, node.end));
        }

        private void visitDefault(Node node) {
            try {
                throw new HeckRuntimeException("No visit method defined for " + node.toString());
            } catch (Exception ex) {
                Logger.getLogger(HeckScriptHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

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

    public class Context {

        public String name;
        public Context parent;
        public Position parentEntry;

        public Context(String name, Context parent, Position parentEntry) {
            this.name = name;
            this.parent = parent;
            this.parentEntry = parentEntry;
        }
    }

    public class Number {

        public boolean isFloat = false;
        public String value;

        public Position start;
        public Position end;
        public Context context;

        public Exception error;

        public Number(String value) {
            this.value = value;
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                this.isFloat = true;
            }

        }

        public Number(int value) {
            this.value = value + "";
        }

        public Number(float value) {
            this.value = value + "";
            this.isFloat = true;
        }

        public Number setContext(Context context) {
            this.context = context;
            return this;
        }

        public Number setPosition(Position start, Position end) {
            this.start = start;
            this.end = end;
            return this;
        }

        //TODO: for some reason, this method returns a float number.
        public Number addedTo(Object other) {
            if (other instanceof Number) {
                Number oth = (Number) other;
                return new Number((this.isFloat ? Float.parseFloat(this.value) : Integer.parseInt(this.value))
                        + (oth.isFloat ? Float.parseFloat(oth.value) : Integer.parseInt(oth.value))).setContext(this.context);
            }
            return null;
        }

        public Number subtractBy(Object other) {
            if (other instanceof Number) {
                if (other instanceof Number) {
                    Number oth = (Number) other;
                    return new Number((this.isFloat ? Float.parseFloat(this.value) : Integer.parseInt(this.value))
                            - (oth.isFloat ? Float.parseFloat(oth.value) : Integer.parseInt(oth.value))).setContext(this.context);
                }
            }
            return null;
        }

        public Number multiplyBy(Object other) {
            if (other instanceof Number) {
                if (other instanceof Number) {
                    Number oth = (Number) other;
                    return new Number((this.isFloat ? Float.parseFloat(this.value) : Integer.parseInt(this.value))
                            * (oth.isFloat ? Float.parseFloat(oth.value) : Integer.parseInt(oth.value))).setContext(this.context);
                }
            }
            return null;
        }

        public Number divideBy(Object other) throws RuntimeExcpetion {
            if (other instanceof Number) {
                if (other instanceof Number) {
                    Number oth = (Number) other;
                    float a = Float.parseFloat(this.value) / Float.parseFloat(oth.value);
                    if (a == Float.POSITIVE_INFINITY || a == Float.NEGATIVE_INFINITY) {
                        this.error = HeckRuntimeException.contextRuntimeException("Divide by zero.", this.context, this.start);
                        return this;
                    }
                    return new Number((this.isFloat ? Float.parseFloat(this.value) : Integer.parseInt(this.value))
                            / (oth.isFloat ? Float.parseFloat(oth.value) : Integer.parseInt(oth.value))).setContext(this.context);
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

    public class ParseResult {

        public Exception error;
        public Node node;

        public ParseResult() {
        }

        public Node register(Object result) {
            if (result instanceof ParseResult) {
                if (((ParseResult) result).error != null) {
                    this.error = ((ParseResult) result).error;
                    return ((ParseResult) result).node;
                }
            }
            if (result instanceof Token) {
                return null;
            }
            return ((ParseResult) result).node;
        }

        public ParseResult success(Node node) {
            this.node = node;
            return this;
        }

        public ParseResult failure(Exception error) {
            this.error = error;
            return this;
        }
    }

    public class Node {

        public Token token;
        public Position start;
        public Position end;

    }

    public class NumberNode extends Node {

        public NumberNode(Token token) {
            this.token = token;
            this.start = token.start;
            this.end = token.end;
        }

        @Override
        public String toString() {
            return token.toString();
        }
    }

    public class BinOpNode extends Node {

        public Token opToken;
        public Node leftNode;
        public Node rightNode;

        public BinOpNode(Node leftNode, Token opToken, Node rightNode) {
            this.leftNode = leftNode;
            this.opToken = opToken;
            this.rightNode = rightNode;
            this.start = leftNode.start;
            this.end = rightNode.end;
        }

        @Override
        public String toString() {
            return String.format("(%1s, %2s, %3s)", this.leftNode, this.opToken, this.rightNode);
        }
    }

    public class UnOpNode extends Node {

        public Token opToken;
        public Node node;

        public UnOpNode(Token opToken, Node node) {
            this.opToken = opToken;
            this.node = node;
            this.start = opToken.start;
            this.end = node.end;
        }

        @Override
        public String toString() {
            return String.format("%1s, %2s", this.opToken, this.node);
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
