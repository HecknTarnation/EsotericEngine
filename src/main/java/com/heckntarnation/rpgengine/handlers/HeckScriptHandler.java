package com.heckntarnation.rpgengine.handlers;

import com.heckntarnation.rpgengine.heckscript.HeckScript;
import com.heckntarnation.rpgengine.heckscript.cons.*;
import com.heckntarnation.rpgengine.heckscript.cons.nodes.Node;
import com.heckntarnation.rpgengine.heckscript.interpreter.*;
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
    public static final String T_POW = "POW";
    public static final String T_EOF = "EOF";
    public static final String T_EQ = "EQ";
    public static final String T_IDENTIFIER = "IDENTIFIER";
    public static final String T_KEYWORD = "KEYWORD";
    public static final String T_EE = "EE";
    public static final String T_NE = "NE";
    public static final String T_LT = "LT";
    public static final String T_GT = "GT";
    public static final String T_LTE = "LTE";
    public static final String T_GTE = "GTE";

    public static final String DIGITS = "0123456789";
    public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LETTERS_DIGITS = DIGITS + LETTERS;

    public static final String[] KEYWORDS = {
        "var",
        "function",
        "and",
        "or",
        "not"
    };

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
     * @throws
     * com.heckntarnation.rpgengine.heckscript.exception.InvalidCharException
     */
    public void runHeckScirpt(HeckScript script) throws Exception {
        HeckScriptLexer lexer = new HeckScriptLexer();
        HeckScriptParser parser = new HeckScriptParser();
        HeckScriptInterpreter interp = new HeckScriptInterpreter();
        Context rootContext = new Context("<program>", null, null);
        SymbolTable rootTable = new SymbolTable();
        rootTable.set("null", new HeckNumber(0));
        rootTable.set("true", new HeckNumber(1));
        rootTable.set("false", new HeckNumber(0));
        rootTable.set("a", new HeckNumber(5));
        rootContext.symbolTable = rootTable;

        ArrayList<Token> tokens = lexer.tokinize(script);
        System.out.println("Tokens:");
        tokens.forEach(l -> {
            System.out.println(l);
        });
        Node parsedNode = parser.parse(tokens).node;
        System.out.println("Parsed Expression:");
        System.out.println(parsedNode);
        Object result = interp.visit(parsedNode, rootContext);
        RuntimeResult res = (RuntimeResult) result;
        System.out.println("Expression Result:");
        System.out.println(res.value);
    }

}
