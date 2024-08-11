package top.kangert.elp.expression;

public class ArrayAccess extends Expression {
    private final String identifier;
    private final Expression indexExpression;

    public ArrayAccess(String identifier, Expression indexExpression) {
        this.identifier = identifier;
        this.indexExpression = indexExpression;
    }

    @Override
    public Object evaluate(Environment env) throws Exception {
        Object array = env.getVariable(identifier);
        if (!(array instanceof Object[] list)) {
            throw new Exception("Expected an array but got " + array.getClass().getSimpleName());
        }

        // 支持arr[exp]形式的访问
        Object index = indexExpression.evaluate(env);
        if (!(index instanceof Integer)) {
            throw new Exception("Expected an integer index but got " + index.getClass().getSimpleName());
        }

        int idx = (Integer) index;
        if (idx < 0 || idx >= list.length) {
            throw new Exception("Index out of bounds: " + idx);
        }

        return list[idx];
    }

    @Override
    public String toString() {
        return identifier + "[" + indexExpression + "]";
    }
}