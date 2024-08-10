package top.kangert.elp.expression;

public class NumericLiteral extends Expression {
    private final double value;

    public NumericLiteral(double value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Environment env) {
        return value;
    }
}
