package top.kangert.elp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import top.kangert.elp.expression.Environment;
import top.kangert.elp.expression.Expression;
import top.kangert.elp.expression.Lexer;
import top.kangert.elp.expression.Parser;
import top.kangert.elp.expression.Token;
import top.kangert.elp.expression.TokenType;

@SpringBootApplication
public class ElpApplication {

    class StrTest {
        public static String subString(String str, Integer start, Integer end) {
            return str.substring(start, end);
        }
    }

    public static void main(String[] args) {
        Environment env = new Environment();
        // 定义变量
        env.setVariable("test", new Object[] { new HashMap<>() {
            {
                put("a", "b");
            }
        } });
        env.setVariable("keyName", "a");

        env.setVariable("str", StrTest.class);

        env.setVariable("testStr", "kangert@qq.com");

        env.setVariable("test", new Object[] { 1, new HashMap<>() {
            {
                put("a", "kangert");
            }
        } });

        env.setVariable("obj", new HashMap<>() {
            {
                put("a", "kangert");
            }
        });
        String input = "${test[1][keyName]}";

        // input = "${str.subString(testStr, 7, 14)}";

        // input = "${test[1].keyName}";

        input = "${obj[keyName]}";

        Lexer lexer = new Lexer(input.substring(2, input.length() - 1));
        List<Token> tokens = lexer.tokenizer();

        Parser parser = new Parser(tokens);
        Expression expr = parser.buildAst();

        Object result = expr.evaluate(env);
        System.out.println(result);
    }

}
