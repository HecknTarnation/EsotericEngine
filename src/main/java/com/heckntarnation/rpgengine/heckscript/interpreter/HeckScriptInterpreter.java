package com.heckntarnation.rpgengine.heckscript.interpreter;

import static com.heckntarnation.rpgengine.handlers.HeckScriptHandler.*;
import com.heckntarnation.rpgengine.heckscript.cons.*;
import com.heckntarnation.rpgengine.heckscript.cons.nodes.*;
import com.heckntarnation.rpgengine.heckscript.exception.HeckRuntimeException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ben
 */
public class HeckScriptInterpreter {

    public RuntimeResult visit(Node node, Context context) throws Exception {
        if (node instanceof NumberNode) {
            return visitNumberNode((NumberNode) node, context);
        } else if (node instanceof BinOpNode) {
            return visitBinOpNode((BinOpNode) node, context);
        } else if (node instanceof UnOpNode) {
            return visitUnOpNode((UnOpNode) node, context);
        } else if (node instanceof VarAccessNode) {
            return visitVarAccessNode((VarAccessNode) node, context);
        } else if (node instanceof VarAssignNode) {
            return visitVarAssignNode((VarAssignNode) node, context);
        } else if (node instanceof IfNode) {
            return visitIfNode((IfNode) node, context);
        } else {
            visitDefault(node);
        }
        return null;
    }

    private RuntimeResult visitNumberNode(NumberNode node, Context context) {
        return new RuntimeResult().success(new HeckNumber(node.token.value).setContext(context).setPosition(node.start, node.end));
    }

    private RuntimeResult visitBinOpNode(BinOpNode node, Context context) throws Exception {
        RuntimeResult result = new RuntimeResult();
        HeckNumber left = (HeckNumber) result.register(this.visit(node.leftNode, context));
        if (result.error != null) {
            throw result.error;
        }
        HeckNumber right = (HeckNumber) result.register(this.visit(node.rightNode, context));
        HeckNumber resultNumber = null;
        if (node.opToken.type.equals(T_PLUS)) {
            resultNumber = left.addedTo(right);
        } else if (node.opToken.type.equals(T_MINUS)) {
            resultNumber = left.subtractBy(right);
        } else if (node.opToken.type.equals(T_MUL)) {
            resultNumber = left.multiplyBy(right);
        } else if (node.opToken.type.equals(T_DIV)) {
            resultNumber = left.divideBy(right);
        } else if (node.opToken.type.equals(T_POW)) {
            resultNumber = left.powerBy(right);
        } else if (node.opToken.type.equals(T_EE)) {
            resultNumber = left.compareEQ(right);
        } else if (node.opToken.type.equals(T_NE)) {
            resultNumber = left.compareNE(right);
        } else if (node.opToken.type.equals(T_LT)) {
            resultNumber = left.compareLT(right);
        } else if (node.opToken.type.equals(T_GT)) {
            resultNumber = left.compareGT(right);
        } else if (node.opToken.type.equals(T_LTE)) {
            resultNumber = left.compareLTE(right);
        } else if (node.opToken.type.equals(T_GTE)) {
            resultNumber = left.compareGTE(right);
        } else if (node.opToken.matches(T_KEYWORD, "and")) {
            resultNumber = left.andBy(right);
        } else if (node.opToken.matches(T_KEYWORD, "or")) {
            resultNumber = left.orBy(right);
        }
        result.error = resultNumber.error;
        if (result.error != null) {
            throw result.error;
        }
        return result.success(resultNumber.setPosition(node.start, node.end));
    }

    private RuntimeResult visitUnOpNode(UnOpNode node, Context context) throws Exception {
        RuntimeResult result = new RuntimeResult();
        HeckNumber number = (HeckNumber) result.register(this.visit(node.node, context));
        if (node.opToken.type.equals(T_MINUS)) {
            number = number.multiplyBy(new HeckNumber(-1));
        } else if (node.opToken.matches(T_KEYWORD, "not")) {
            number = number.not();
        }
        result.error = number.error;
        if (result.error != null) {
            throw result.error;
        }
        return result.success(number.setPosition(node.start, node.end));
    }

    private RuntimeResult visitVarAccessNode(VarAccessNode node, Context context) {
        RuntimeResult result = new RuntimeResult();
        String varName = node.name.value;
        Object value = context.symbolTable.get(varName);

        if (value == null) {
            return result.failure(new HeckRuntimeException("Variable '" + varName + "' not defined at COL:" + context.parentEntry.col + " on Line " + (context.parentEntry.lineNum + 1)));
        }

        return result.success(value);
    }

    private RuntimeResult visitVarAssignNode(VarAssignNode node, Context context) throws Exception {
        RuntimeResult result = new RuntimeResult();
        String varName = node.name.value;
        Object value = result.register(visit(node.value, context));

        if (result.error != null) {
            throw result.error;
        }
        context.symbolTable.set(varName, value);
        return result.success(value);
    }

    private RuntimeResult visitIfNode(IfNode node, Context context) throws Exception {
        RuntimeResult result = new RuntimeResult();

        for (Node[] arr : node.cases) {
            Node condition = arr[0];
            Node expression = arr[1];
            HeckNumber conditionVal = (HeckNumber) result.register(this.visit(condition, context));
            if (result.error != null) {
                throw result.error;
            }
            if (conditionVal.isTrue()) {
                HeckNumber expressionVal = (HeckNumber) result.register(this.visit(expression, context));
                if (result.error != null) {
                    throw result.error;
                }
                return result.success(expressionVal);
            }

            if (node.elseCase != null) {
                HeckNumber elseVal = (HeckNumber) result.register(this.visit(node.elseCase, context));
                if (result.error != null) {
                    throw result.error;
                }
                return result.success(elseVal);
            }
            return result.success(null);
        }
        return null;
    }

    private void visitDefault(Node node) {
        try {
            throw new HeckRuntimeException("No visit method defined for " + node.toString());
        } catch (Exception ex) {
            Logger.getLogger(HeckScriptInterpreter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
