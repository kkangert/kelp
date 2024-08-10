package top.kangert.elp.expression;

import java.util.ArrayList;
import java.util.List;

/**
 * 表达式解析器
 */
public class Parser {
    private final List<Token> tokens;
    private int currentTokenIndex;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    /**
     * 构建抽象语法树
     * 
     * @return 表达式
     */
    public Expression buildAst() {
        return parseExpression();
    }

    /**
     * 解析表达式
     * 
     * @return 表达式
     */
    private Expression parseExpression() {
        Expression expr = parseTerm();

        while (currentTokenIndex < tokens.size() && isAddSubOp(tokens.get(currentTokenIndex))) {
            Token token = consumeToken();
            Expression right = parseTerm();
            Operator op = Operator.parse(token.getValue().toString());
            expr = new BinaryOperation(expr, op, right);
        }

        return expr;
    }

    /**
     * 解析加减法表达式
     * 
     * @return 表达式
     */
    private Expression parseTerm() {
        Expression expr = parseFactor();

        while (currentTokenIndex < tokens.size() && isMulDivOp(tokens.get(currentTokenIndex))) {
            Token token = consumeToken();
            Expression right = parseFactor();
            Operator op = Operator.parse(token.getValue().toString());
            expr = new BinaryOperation(expr, op, right);
        }

        return expr;
    }

    /**
     * 解析原子\单因子表达式
     * 
     * @return 表达式
     */
    private Expression parseFactor() {
        Token token = currentToken();
        if (token.getType() == TokenType.NUMBER || token.getType() == TokenType.FLOAT
                || token.getType() == TokenType.INTEGER) {
            consumeToken();
            Number numberValue = (Number) token.getValue();
            if (numberValue instanceof Integer) {
                return new IntegerLiteral(numberValue.intValue());
            } else if (numberValue instanceof Double) {
                return new FloatLiteral(numberValue.doubleValue());
            } else {
                throw new RuntimeException("Unexpected number type at position " + token.getValue());
            }
        } else if (token.getType() == TokenType.IDENTIFIER) {
            consumeToken();
            return parseChainableExpression(token.getValue().toString());
        } else if (token.getType() == TokenType.LPAREN) {
            consumeToken(); // Consume '('
            Expression expr = parseExpression();
            if (consumeToken().getType() != TokenType.RPAREN) {
                throw new RuntimeException("Expected ')'");
            }
            return expr;
        } else if (token.getType() == TokenType.LBRACKET) {
            return parseArrayOrMapAccess();
        }
        throw new RuntimeException("Invalid token at position " + token.getValue());
    }

    /**
     * 解析链式表达式
     *
     * @param baseIdentifier 基础标识符
     * @return 表达式
     */
    private Expression parseChainableExpression(String baseIdentifier) {
        Expression expr = new Variable(baseIdentifier);
        while (currentTokenIndex < tokens.size()) {
            Token token = currentToken();
            if (token.getType() == TokenType.PERIOD) {
                consumeToken(); // Consume '.'
                String methodName = consumeToken().getValue().toString(); // Consume method name
                expr = parseMethodCall(expr, methodName);
            } else if (token.getType() == TokenType.LBRACKET) {
                consumeToken();
                expr = parseArrayOrMapAccess();
            } else {
                break; // 如果不是点或方括号，则结束解析
            }
        }
        return expr;
    }

    /**
     * 解析方法调用
     * 
     * @param target     目标表达式
     * @param methodName 方法名
     * @return 表达式
     */
    private Expression parseMethodCall(Expression target, String methodName) {
        List<Expression> arguments = new ArrayList<>();

        if (currentToken().getType() == TokenType.LPAREN) {
            consumeToken(); // Consume '('
            if (currentToken().getType() != TokenType.RPAREN) {
                arguments.add(parseExpression());
                while (currentToken().getType() == TokenType.COMMA) {
                    consumeToken(); // Consume ','
                    arguments.add(parseExpression());
                }
            }
            if (consumeToken().getType() != TokenType.RPAREN) {
                throw new RuntimeException("Expected ')'");
            }
        }

        return new FunctionCall(target, methodName, arguments);
    }

    /**
     * 解析数组\Map访问
     * 
     * @return 表达式
     */
    private Expression parseArrayOrMapAccess() {
        Token identifierToken = previousToken();
        if (identifierToken.getType() != TokenType.IDENTIFIER) {
            throw new RuntimeException("Expected an identifier before '['");
        }

        // consumeToken(); // Consume '['
        Expression indexOrKeyExpr = parseExpression();
        if (consumeToken().getType() != TokenType.RBRACKET) {
            throw new RuntimeException("Expected ']' after array index expression");
        }

        // 检查是否有更多的方括号访问
        if (currentToken().getType() == TokenType.LBRACKET) {
            return new ArrayAccess(identifierToken.getValue().toString(), indexOrKeyExpr);
        }

        if (indexOrKeyExpr instanceof IntegerLiteral) {
            return new ArrayAccess(identifierToken.getValue().toString(), indexOrKeyExpr);
        } else {
            return new ObjectKeyAccess(identifierToken.getValue().toString(), indexOrKeyExpr);
        }
    }

    /**
     * 判断是否是加减法运算符
     * 
     * @param token Token
     * @return 是否是加减法运算符
     */
    private boolean isAddSubOp(Token token) {
        return token.getType() == TokenType.PLUS || token.getType() == TokenType.MINUS;
    }

    /**
     * 判断是否是乘除法运算符
     * 
     * @param token Token
     * @return 是否是乘除法运算符
     */
    private boolean isMulDivOp(Token token) {
        return token.getType() == TokenType.MULTIPLY || token.getType() == TokenType.DIVIDE;
    }

    /**
     * 获取下一个Token
     * 
     * @return Token
     */
    private Token consumeToken() {
        Token token = currentToken();
        currentTokenIndex++;
        return token;
    }

    /**
     * 获取解析器当前正在处理的标记(token)
     * 
     * @return 当前正在处理的token
     */
    private Token currentToken() {
        return tokens.get(currentTokenIndex);
    }

    /**
     * 获取解析器当前正在处理的标记(token)的前一个标记
     * 
     * @return 前一个token
     */
    private Token previousToken() {
        return tokens.get(currentTokenIndex - 1);
    }
}