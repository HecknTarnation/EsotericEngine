package com.heckntarnation.rpgengine.heckscript.interpreter;

import static com.heckntarnation.rpgengine.handlers.HeckScriptHandler.*;
import com.heckntarnation.rpgengine.heckscript.cons.ParseResult;
import com.heckntarnation.rpgengine.heckscript.cons.Token;
import com.heckntarnation.rpgengine.heckscript.cons.nodes.*;
import com.heckntarnation.rpgengine.heckscript.exception.InvalidSyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 *
 * @author Ben
 */
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

    private ParseResult atom() throws Exception {
        ParseResult result = new ParseResult();
        Token tok = this.currentToken;
        if (tok.type.equals(T_INT) || tok.type.equals(T_FLOAT)) {
            NumberNode node = new NumberNode(tok);
            result.registerAdvancement();
            this.advance();
            return result.success(node);
        } else if (tok.type.equals(T_IDENTIFIER)) {
            result.registerAdvancement();
            this.advance();
            return result.success(new VarAccessNode(tok));
        } else if (tok.type.equals(T_LPAREN)) {
            result.registerAdvancement();
            this.advance();
            Node expression = result.register(this.expression());
            if (result.error != null) {
                throw result.error;
            }
            if (this.currentToken.type.equals(T_RPAREN)) {
                result.registerAdvancement();
                this.advance();
                return result.success(expression);
            } else {
                result.failure(new InvalidSyntaxException("Expected ')' at COL:" + this.currentToken.start.col + " on Line " + (this.currentToken.start.lineNum + 1)));
            }
        } else if (tok.matches(T_KEYWORD, "if")) {
            Node if_expression = result.register(ifExpression());
            if (result.error != null) {
                throw result.error;
            }
            return result.success(if_expression);
        }
        return result.failure(new InvalidSyntaxException("Expected Int, Float, Identifier, '+', '-', or '(' at COL:" + this.currentToken.start.col + " on Line " + this.currentToken.start.lineNum + 1));
    }

    private ParseResult ifExpression() throws Exception {
        ParseResult result = new ParseResult();
        ArrayList<Node[]> cases = new ArrayList<>();
        Node elseCase = null;

        if (!this.currentToken.matches(T_KEYWORD, "if")) {
            return result.failure(new InvalidSyntaxException("Expected 'if' at COL:" + this.currentToken.start.col + " on Line " + this.currentToken.start.lineNum + 1));
        }
        result.registerAdvancement();
        this.advance();

        Node condition = result.register(this.expression());
        if (result.error != null) {
            throw result.error;
        }

        if (!this.currentToken.matches(T_KEYWORD, "then")) {
            return result.failure(new InvalidSyntaxException("Excepted 'then' at COL:" + this.currentToken.start.col + " on Line " + this.currentToken.start.lineNum + 1));
        }

        result.registerAdvancement();
        this.advance();

        Node expression = result.register(this.expression());
        if (result.error != null) {
            throw result.error;
        }
        cases.add(new Node[]{condition, expression});

        while (this.currentToken.matches(T_KEYWORD, "elseif")) {
            result.registerAdvancement();
            this.advance();

            condition = result.register(this.expression());
            if (result.error != null) {
                throw result.error;
            }

            if (!this.currentToken.matches(T_KEYWORD, "then")) {
                return result.failure(new InvalidSyntaxException("Excepted 'then' at COL:" + this.currentToken.start.col + " on Line " + this.currentToken.start.lineNum + 1));
            }

            result.registerAdvancement();
            this.advance();

            expression = result.register(this.expression());
            if (result.error != null) {
                throw result.error;
            }
            cases.add(new Node[]{condition, expression});
        }

        if (this.currentToken.matches(T_KEYWORD, "else")) {
            result.registerAdvancement();
            this.advance();

            elseCase = result.register(this.expression());
            if (result.error != null) {
                throw result.error;
            }
        }
        result.registerAdvancement();
        this.advance();

        return result.success(new IfNode(cases, elseCase));
    }

    private ParseResult factor() throws Exception {
        ParseResult result = new ParseResult();
        Token tok = this.currentToken;
        if (tok.type.equals(T_PLUS) || tok.type.equals(T_MINUS)) {
            result.registerAdvancement();
            this.advance();
            Node factor = result.register(this.factor());
            if (result.error != null) {
                throw result.error;
            }
            return result.success(new UnOpNode(tok, factor));
        }
        return this.power();
    }

    private ParseResult term() throws Exception {
        return binop(new String[]{T_MUL, T_DIV}, (Callable) () -> factor());
    }

    private ParseResult expression() throws Exception {
        ParseResult result = new ParseResult();
        if (currentToken.matches(T_KEYWORD, "var")) {
            result.registerAdvancement();
            this.advance();
            if (!currentToken.type.equals(T_IDENTIFIER)) {
                return result.failure(new InvalidSyntaxException("Expected identifier at COL:" + currentToken.start.col + " on Line " + currentToken.start.lineNum));
            }
            Token varName = currentToken;
            result.registerAdvancement();
            this.advance();

            if (!currentToken.type.equals(T_EQ)) {
                return result.failure(new InvalidSyntaxException("Expected '=' at COL:" + currentToken.start.col + " on Line " + currentToken.start.lineNum));
            }

            result.registerAdvancement();
            this.advance();
            Node expression = result.register(expression());
            if (result.error != null) {
                throw result.error;
            }
            return result.success(new VarAssignNode(varName, expression));
        }
        Node node = result.register(binop(new String[][]{{T_KEYWORD, "and"}, {T_KEYWORD, "or"}}, (Callable) () -> compExpress()));

        if (result.error != null) {
            return result.failure(new InvalidSyntaxException("Expected 'var', Int, Float, Identifier, '+', '-', or '(' at COL:" + this.currentToken.start.col + " on Line " + this.currentToken.start.lineNum + 1));
        }
        return result.success(node);
    }

    private ParseResult compExpress() throws Exception {
        ParseResult result = new ParseResult();
        Token tok;
        if (currentToken.matches(T_KEYWORD, "not")) {
            tok = currentToken;
            result.registerAdvancement();
            advance();

            Node node = result.register(compExpress());
            if (result.error != null) {
                throw result.error;
            }
            return result.success(new UnOpNode(tok, node));
        }

        Node node = result.register(binop(new String[]{T_EE, T_NE, T_LT, T_LTE, T_GT, T_GTE}, (Callable) () -> arithExpress()));
        if (result.error != null) {
            throw new InvalidSyntaxException("Expected Int, Float, Identifier, '+', '-', '(', or 'not' at COL:" + this.currentToken.start.col + " on Line " + this.currentToken.start.lineNum + 1);
        }
        return result.success(node);
    }

    private ParseResult arithExpress() throws Exception {
        return binop(new String[]{T_PLUS, T_MINUS}, (Callable) () -> term());
    }

    private ParseResult power() throws Exception {
        return binop(new String[]{T_POW}, (Callable) () -> atom(), (Callable) () -> factor());
    }

    private ParseResult binop(String[] ops, Callable func) throws Exception {
        return binop(ops, func, func);
    }

    private ParseResult binop(String[][] ops, Callable func) throws Exception {
        return binop(ops, func, func);
    }

    private ParseResult binop(String[][] ops, Callable funcA, Callable funcB) throws Exception {
        ParseResult result = new ParseResult();
        Node left = result.register((ParseResult) funcA.call());
        if (result.error != null) {
            throw result.error;
        }
        while (Arrays.asList(ops).contains(new String[]{this.currentToken.type, this.currentToken.value})) {
            Token op = this.currentToken;
            result.registerAdvancement();
            this.advance();
            Node right = result.register((ParseResult) funcB.call());
            if (result.error != null) {
                throw result.error;
            }
            left = new BinOpNode(left, op, right);
        }
        return result.success(left);
    }

    private ParseResult binop(String[] ops, Callable funcA, Callable funcB) throws Exception {
        ParseResult result = new ParseResult();
        Node left = result.register((ParseResult) funcA.call());
        if (result.error != null) {
            throw result.error;
        }
        while (Arrays.asList(ops).contains(this.currentToken.type)) {
            Token op = this.currentToken;
            result.registerAdvancement();
            this.advance();
            Node right = result.register((ParseResult) funcB.call());
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
        System.out.println(this.currentToken);
        if (!this.currentToken.type.equals(T_EOF)) {
            throw new InvalidSyntaxException("Expected '+', '-', '*', '/' at COL:" + currentToken.start.col + " on Line " + this.currentToken.start.lineNum);
        }
        return result;
    }

}
