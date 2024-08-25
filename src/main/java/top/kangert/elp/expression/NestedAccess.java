package top.kangert.elp.expression;

import java.util.List;
import java.util.Map;

public class NestedAccess extends Expression {
    private final Expression baseExpression;
    private final Expression nestedExpression;
    private final String variableName = "NestedAccessKey";

    public NestedAccess(Expression baseExpression, Expression nestedExpression) {
        this.baseExpression = baseExpression;
        this.nestedExpression = nestedExpression;
    }

    @Override
    public Object evaluate(Environment env) throws Exception {
        Object baseValue = baseExpression.evaluate(env);

        // 构造虚拟环境
        Environment virtualEnv = env.clone();
        virtualEnv.setVariable(variableName, baseValue);

        if (baseValue instanceof List) {
            // 如果基础值是列表，则使用 ArrayAccess 评估
            return new ArrayAccess(variableName, nestedExpression).evaluate(virtualEnv);
        } else if (baseValue instanceof Map) {
            // 如果基础值是映射，则使用 ObjectKeyAccess 评估
            return new ObjectKeyAccess(variableName, nestedExpression).evaluate(virtualEnv);
        } else {
            throw new KelpException("Base value is neither a list nor a map: " + baseValue.getClass().getSimpleName());
        }
    }

    @Override
    public String toString() {
        return "(" + baseExpression + "[" + nestedExpression + "])";
    }
}