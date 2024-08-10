package top.kangert.elp.expression;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 函数调用表达式
 */
public class FunctionCall extends Expression {
    // 函数调用表达式的目标
    private final Expression target;

    // 函数名称
    private final String name;

    // 函数参数
    private final List<Expression> arguments;

    public FunctionCall(Expression target, String name, List<Expression> arguments) {
        this.target = target;
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public Object evaluate(Environment env) {
        // 解析参数
        List<Object> args = arguments.stream().map(e -> e.evaluate(env)).collect(Collectors.toList());

        // 获取目标对象或类
        Object targetObject = target.evaluate(env);

        try {
            // 获取目标类
            Class<?> targetClass = targetObject instanceof Class<?> ? (Class<?>) targetObject : targetObject.getClass();

            // 获取参数类型
            Class<?>[] argTypes = args.stream().map(Object::getClass).toArray(Class<?>[]::new);

            // 查找方法
            Method method = targetClass.getMethod(name, argTypes);

            // 设置方法可访问
            method.setAccessible(true);

            // 判断是否为静态方法
            boolean isStatic = Modifier.isStatic(method.getModifiers());

            // 调用方法
            return isStatic ? method.invoke(null, args.toArray()) : method.invoke(targetObject, args.toArray());
        } catch (Exception e) {
            throw new RuntimeException("Error invoking method " + name, e);
        }
    }
}