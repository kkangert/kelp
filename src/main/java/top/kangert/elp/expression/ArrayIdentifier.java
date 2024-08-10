package top.kangert.elp.expression;

public class ArrayIdentifier extends Expression {
    private final String identifier;

    public ArrayIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public Object evaluate(Environment env) {
        // 解析为数组
        return env.getArray(identifier);
    }

}
