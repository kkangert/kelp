package top.kangert.elp.expression;

import java.util.Map;

public class ObjectKeyAccess extends Expression {
    private final String identifier;
    private final Expression key;

    public ObjectKeyAccess(String identifier, Expression key) {
        this.identifier = identifier;
        this.key = key;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Expression getKey() {
        return key;
    }

    @Override
    public Object evaluate(Environment env) {
        Object objKey = env.getVariable(identifier);
        Object keyValue = key.evaluate(env);
        if (!(keyValue instanceof String)) {
            throw new RuntimeException("Key must be a string, but got " + keyValue.getClass().getSimpleName());
        }
        Map<?, ?> map = (Map<?, ?>) env.getVariable((String) objKey);
        return map.get(keyValue);
    }

}
