package top.kangert.elp.expression;

import java.util.List;

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

        if (array instanceof Object[]) {
            // 数据强转
            Object[] list = (Object[]) array;
            // 检查下标
            int idx = checkIndex(env, list.length);
            // 获取元素
            return list[idx];
        } else if (array instanceof List) {
            // 数据强转
            List<Object> list = (List<Object>) array;
            // 检查下标
            int idx = checkIndex(env, list.size());
            // 获取元素
            return list.get(idx);
        } else {
            throw new KelpException("Expected an array but got " + array.getClass().getSimpleName());
        }
    }

    /**
     * 检查下标合法性
     * 
     * @param env     上下文环境
     * @param arrSize 数组大小
     * @return 合法的下标
     * @throws Exception
     */
    private int checkIndex(Environment env, int arrSize) throws Exception {
        // 支持arr[exp]形式的访问
        Object index = indexExpression.evaluate(env);
        if (!(index instanceof Integer)) {
            throw new KelpException("Expected an integer index but got " + index.getClass().getSimpleName());
        }

        int idx = (Integer) index;
        if (idx < 0 || idx >= arrSize) {
            throw new KelpException("Index out of bounds: " + idx);
        }
        return idx;
    }

    @Override
    public String toString() {
        return identifier + "[" + indexExpression + "]";
    }
}
