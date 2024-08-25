package top.kangert.elp.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 词法分析器
 */
public class Lexer {
    private final String input;
    private int position;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
    }

    /**
     * 分词
     * 
     * @return 分词列表
     */
    public List<Token> tokenizer() {
        List<Token> tokens = new ArrayList<>();

        // 抽取表达式片段，除此之外的内容都按照字符串处理,抽取部分按照解析逻辑进行，保持先后顺序
        List<Integer> chuckPos = extractExpChuckPos(input);

        // 若无切片说明内部无合法表达式，按照字符串原样返回
        if (chuckPos.size() == 0) {
            tokens.add(new Token(TokenType.STRING, input));
        } else {
            // 切片分段处理
            for (int start : chuckPos) {
                // 当前位置i作为起始位置
                int startPos = position;
                // 第一个切片的开始位置作为第一段字符串的结束位置
                int endPos = start;

                // 添加字符串
                String val = input.substring(startPos, endPos);
                if (!"".equals(val) || val != null) {
                    tokens.add(new Token(TokenType.STRING, val));
                }

                // 添加表达式，跳过${
                position = start + 2;
                Token token;
                do {
                    token = nextToken();
                    tokens.add(token);
                } while (token.getType() != TokenType.EOF && input.charAt(position) != '}');

                // 跳过}字符
                position++;
            }
        }

        // 添加结束标记
        tokens.add(new Token(TokenType.EOF, null));

        return tokens;
    }

    /**
     * 获取下一个分词
     * 
     * @return 分词
     */
    private Token nextToken() {
        skipWhitespace();
        if (position >= input.length()) {
            return new Token(TokenType.EOF, null);
        }

        char ch = input.charAt(position);
        switch (ch) {
            case '+':
                position++;
                return new Token(TokenType.PLUS, "+");
            case '-':
                position++;
                return new Token(TokenType.MINUS, "-");
            case '*':
                position++;
                return new Token(TokenType.MULTIPLY, "*");
            case '/':
                position++;
                return new Token(TokenType.DIVIDE, "/");
            case '(':
                position++;
                return new Token(TokenType.LPAREN, "(");
            case ')':
                position++;
                return new Token(TokenType.RPAREN, ")");
            case '.':
                position++;
                return new Token(TokenType.PERIOD, ".");
            case ',':
                position++;
                return new Token(TokenType.COMMA, ",");
            case '[':
                position++;
                return new Token(TokenType.LBRACKET, "[");
            case ']':
                position++;
                return new Token(TokenType.RBRACKET, "]");
            case '"':
            case '\'':
                return parseQuotedString();
            default:
                if (Character.isDigit(ch)) {
                    return parseNumber();
                } else if (Character.isLetter(ch)) {
                    return parseIdentifier();
                }
                throw new RuntimeException("Invalid token at position " + position);
        }
    }

    /**
     * 解析数字
     * 
     * @return 数字
     */
    private Token parseNumber() {
        int startPos = position;
        while (position < input.length()
                && (Character.isDigit(input.charAt(position)) || input.charAt(position) == '.')) {
            position++;
        }
        String numberStr = input.substring(startPos, position);
        if (numberStr.contains(".")) {
            return new Token(TokenType.FLOAT, Double.parseDouble(numberStr));
        }
        return new Token(TokenType.INTEGER, Integer.parseInt(numberStr));
    }

    /**
     * 解析双\单引号字符串
     * 
     * @return 双\单引号字符串
     */
    private Token parseQuotedString() {
        int startPos = position;
        position++; // Skip the opening quote
        while (position < input.length() && input.charAt(position) != '"' && input.charAt(position) != '\'') {
            position++;
        }
        if (position >= input.length()) {
            throw new RuntimeException("Unterminated quoted string");
        }
        position++; // Skip the closing quote
        String value = input.substring(startPos, position - 1);
        return new Token(TokenType.QUOTE, value);
    }

    /**
     * 解析标识符
     * 
     * @return 标识符
     */
    private Token parseIdentifier() {
        int startPos = position;
        while (position < input.length()
                && (Character.isLetterOrDigit(input.charAt(position)) || input.charAt(position) == '_')) {
            position++;
        }
        String identifier = input.substring(startPos, position);
        return new Token(TokenType.IDENTIFIER, identifier);
    }

    /**
     * 抽取${*}中的内容，获得每个表达式切片的起始位置
     * 
     * @param input 含有表达式的字符串
     * @return 表达式切片的起始位置
     */
    private List<Integer> extractExpChuckPos(String input) {
        List<Integer> exps = new ArrayList<Integer>();
        Pattern pattern = Pattern.compile("\\$\\{([^}]*)\\}");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            // group(1) 获取括号内的内容
            String matcherStr = matcher.group(1);
            exps.add(start);
        }
        return exps;
    }

    /**
     * 跳过空白字符
     */
    private void skipWhitespace() {
        while (position < input.length() && Character.isWhitespace(input.charAt(position))) {
            position++;
        }
    }
}