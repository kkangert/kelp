package top.kangert.elp.expression;

public class ArrayAccess extends Expression {
    private final String identifier;
    private final Expression index;

    public ArrayAccess(String identifier, Expression index) {
        this.identifier = identifier;
        this.index = index;
    }

    @Override
    public Object evaluate(Environment env) {
        // 访问数组的某个值
        Object[] array = (Object[]) env.getVariable(identifier);

        // 支持arr[exp]形式的访问
        int indexValue = (int) index.evaluate(env);

        if (indexValue < 0 || indexValue >= array.length) {
            throw new RuntimeException("Array index out of bounds: " + indexValue);
        }

        return array[indexValue];
    }

}
