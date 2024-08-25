package top.kangert.elp;

import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import top.kangert.elp.expression.Environment;
import top.kangert.elp.expression.ExpressionEngine;

@SpringBootApplication
public class ElpApplication {

    class StrTest {
        public static String subString(String str, Integer start, Integer end) {
            return str.substring(start, end);
        }
    }

    public static void main(String[] args) throws Exception {
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
        String input = "${str.subString(test[1][keyName], 0, 3)}";

        input = "${str.subString(str.subString(testStr, 7, 14).toString(), 0, 2)}";

        // input = "${obj[keyName]}";

        // input = "${(1 + 1) * 3 / 2}";
        // input = "https://www.xxx.com/${testStr}/q?=keyword=${keyName}";
        // input = "https://www.xxx.com/";
        input = "${obj[\"a\"]}";
        input = "${obj['a']}";


        ExpressionEngine engine = new ExpressionEngine(env);
        Object result = engine.evaluate(input);
        System.out.println(result);
    }

}
