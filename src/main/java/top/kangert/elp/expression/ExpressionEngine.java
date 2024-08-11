package top.kangert.elp.expression;

import java.util.List;

public class ExpressionEngine {
    private Environment env;

    private Lexer lexer;

    private Parser parser;

    private List<Token> tokens;

    public ExpressionEngine(Environment env) {
        this.env = env;

    }

    public ExpressionEngine(Environment env, String exp) {
        this.env = env;
        tokenizer(exp);
    }

    /**
     * 执行表达式
     * 
     * @return 执行结果
     * @throws Exception
     */
    public Object evaluate() throws Exception {
        Expression ast = parser.buildAst();
        return ast.evaluate(env);
    }

    /**
     * 执行表达式
     * 
     * @param exp 表达式
     * @return 执行结果
     * @throws Exception 异常
     */
    public Object evaluate(String exp) throws Exception {
        tokenizer(exp);
        Expression ast = parser.buildAst();
        return ast.evaluate(env);
    }

    /**
     * 分词
     * @param exp 表达式
     */
    private void tokenizer(String exp) {
        this.lexer = new Lexer(exp.substring(2, exp.length() - 1));
        this.tokens = lexer.tokenizer();
        this.parser = new Parser(tokens);
    }
}
