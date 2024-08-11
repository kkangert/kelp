package top.kangert.elp.expression;

import java.util.Map;

public class ObjectKeyAccess extends Expression {
    private final String variableName;
    private final Expression keyExpression;

    public ObjectKeyAccess(String variableName, Expression keyExpression) {
        this.variableName = variableName;
        this.keyExpression = keyExpression;
    }

    @Override
    public Object evaluate(Environment env) throws Exception {
        Object object = env.getVariable(variableName);

        if (object instanceof String) {
            return (String) object;
        }
        
        if (!(object instanceof Map)) {
            throw new Exception("Expected an object but got " + object.getClass().getSimpleName());
        }

        Object key = keyExpression.evaluate(env);
        if (!(key instanceof String)) {
            throw new Exception("Expected a string key but got " + key.getClass().getSimpleName());
        }

        Map<String, ?> map = (Map<String, ?>) object;
        String keyStr = (String) key;
        if (!map.containsKey(keyStr)) {
            throw new Exception("Key not found: " + keyStr);
        }

        return map.get(keyStr);
    }

    @Override
    public String toString() {
        return variableName + "[" + keyExpression + "]";
    }
}