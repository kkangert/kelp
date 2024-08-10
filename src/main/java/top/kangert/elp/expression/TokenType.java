package top.kangert.elp.expression;

public enum TokenType {
    // 整数
    INTEGER, 
    // 浮点数
    FLOAT, 
    // 标识符
    IDENTIFIER, 
    // 数字
    NUMBER, 
    // 加号
    PLUS, 
    // 减号
    MINUS, 
    // 乘号
    MULTIPLY, 
    // 除号
    DIVIDE,
    // 左括号
    LPAREN,
    // 右括号
    RPAREN,
    // 左方括号 [
    LBRACKET,  
    // 右方括号 ]    
    RBRACKET,      
    // 小数点
    PERIOD,
    // 逗号
    COMMA,
    // 双引号 "
    QUOTE,
    // 结束标记
    EOF
}