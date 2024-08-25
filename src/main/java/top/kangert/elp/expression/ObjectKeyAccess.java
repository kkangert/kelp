package top.kangert.elp.expression;

import java.util.Map;

public class ObjectKeyAccess extends Expression {
    private final String identifier;
    private final Expression keyExpression;

    public ObjectKeyAccess(String identifier, Expression keyExpression) {
        this.identifier = identifier;
        this.keyExpression = keyExpression;
    }

    @Override
    public Object evaluate(Environment env) throws Exception {
        Object object = env.getVariable(identifier);

        if (object instanceof String) {
            return (String) object;
        }

        if (!(object instanceof Map)) {
            throw new KelpException("Expected an object but got " + object.getClass().getSimpleName());
        }

        Object key = keyExpression.evaluate(env);
        if (!(key instanceof String)) {
            throw new KelpException("Expected a string key but got " + key.getClass().getSimpleName());
        }

        Map<String, ?> map = (Map<String, ?>) object;
        String keyStr = (String) key;
        if (!map.containsKey(keyStr)) {
            throw new KelpException("Cannot find the key for '" + keyStr + "' in the '" + identifier + "'");
        }

        return map.get(keyStr);
    }

    @Override
    public String toString() {
        return identifier + "[" + keyExpression + "]";
    }
}