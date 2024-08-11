package top.kangert.elp.expression;

public class IntegerLiteral extends Expression {
    private final int value;

    public IntegerLiteral(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Object evaluate(Environment env) {
        return value;
    }
}
