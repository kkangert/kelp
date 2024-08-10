package top.kangert.elp.expression;

public class ObjectIdentifier extends Expression {
    private final String identifier;

    public ObjectIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public Object evaluate(Environment env) {
        return env.getObject(identifier);
    }

}
