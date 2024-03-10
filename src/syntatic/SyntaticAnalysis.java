package syntatic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interpreter.command.AssertCommand;
import interpreter.command.AssignCommand;
import interpreter.command.BlocksCommand;
import interpreter.command.Command;
import interpreter.command.DoWhileCommand;
import interpreter.command.ForCommand;
import interpreter.command.IfCommand;
import interpreter.command.PrintCommand;
import interpreter.command.WhileCommand;
import interpreter.expr.*;
import interpreter.util.Utils;
import interpreter.value.*;
import lexical.*;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexema current;
    private Map<String,Variable> memory;

    public SyntaticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        this.current = lex.nextToken();
        memory = new HashMap<String,Variable>();
    }
    ////////////////////////////////////inicia o parsing
    public Command start() {
        Command cmd = procCode();
        eat(TokenType.END_OF_FILE);
        return cmd;
    }
    ////////////////////////////inicio dos procedimentos
    /*public void procCode(){//VERIFICAR ERRO NO ULTIMO for a partir do SUB, ou da primeira regra em geral
    while(current.type == TokenType.FINAL||current.type == TokenType.VAR||current.type == TokenType.PRINT
    ||current.type == TokenType.ASSERT||current.type == TokenType.IF||
            current.type == TokenType.DO||current.type == TokenType.WHILE||current.type == TokenType.FOR
            ||current.type == TokenType.SUB||current.type == TokenType.DENIAL||current.type == TokenType.DECREMENT
            ||current.type == TokenType.INCREMENT||current.type == TokenType.PARENTESES_L){
        procCmd();
    } */

    private BlocksCommand procCode() {
        //System.out.printf("proCode\n");
        int line = lex.getLine();
        List<Command> cmds = new ArrayList<Command>();
        while (current.type == TokenType.FINAL ||
                current.type == TokenType.VAR ||
                current.type == TokenType.PRINT ||
                current.type == TokenType.ASSERT ||
                current.type == TokenType.IF ||
                current.type == TokenType.WHILE ||
                current.type == TokenType.DO ||
                current.type == TokenType.FOR ||
                current.type == TokenType.DENIAL ||
                current.type == TokenType.SUB ||
                current.type == TokenType.INCREMENT ||
                current.type == TokenType.DECREMENT ||
                current.type == TokenType.PARENTESES_L ||
                current.type == TokenType.NULL ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NUMBER ||
                current.type == TokenType.TEXT ||
                current.type == TokenType.READ ||
                current.type == TokenType.RANDOM ||
                current.type == TokenType.LENGTH ||
                current.type == TokenType.KEYS ||
                current.type == TokenType.VALUES ||
                current.type == TokenType.TOBOOL ||
                current.type == TokenType.TOINT ||
                current.type == TokenType.TOSTR ||
                current.type == TokenType.NAME ||
                current.type == TokenType.SQUARE_BRACKETS_L ||
                current.type == TokenType.CURLY_BRACKETS_L) {
            Command c = procCmd();
            cmds.add(c);
        }

        BlocksCommand bc = new BlocksCommand(line, cmds);
        return bc;
    } 
    /*public void procCmd(){
        if(current.type == TokenType.FINAL||current.type == TokenType.VAR){
            procDecl();
        }
        else if(current.type == TokenType.PRINT){
            procPrint();
        }
        else if(current.type == TokenType.ASSERT){
            procAssert();
        }
        else if(current.type == TokenType.IF){
            procIf();
        }
        else if(current.type == TokenType.DO){
            procDowhile();
        }
        else if(current.type == TokenType.WHILE){
            procWhile();
        }
        else if(current.type == TokenType.FOR){
            procFor();
        }
        else if(current.type == TokenType.SUB||current.type == TokenType.DENIAL||current.type == TokenType.DECREMENT
                ||current.type == TokenType.INCREMENT||current.type == TokenType.PARENTESES_L){
            procAssign();
        }
        else{
            showError();
        }
    } */ 
    private Command procCmd() {
        //System.out.printf("proCmd, %s tipo: %s\n",current.token,current.type);
        Command cmd = null;
        switch (current.type) {
            case FINAL:
            case VAR:
                cmd = procDecl();
                break;
            case PRINT:
                cmd = procPrint();
                break;
            case ASSERT:
                cmd = procAssert();
                break;
            case IF:
                cmd = procIf();
                break;
            case WHILE:
                cmd = procWhile();
                break;
            case DO:
              cmd = procDowhile();
                break;
            case FOR:
                 cmd = procFor();
                break;
            case DENIAL:
            case SUB:
            case INCREMENT:
            case DECREMENT:
            case PARENTESES_L:
            case NULL:
            case FALSE:
            case TRUE:
            case NUMBER:
            case TEXT:
            case READ:
            case RANDOM:
            case LENGTH:
            case KEYS:
            case VALUES:
            case TOBOOL:
            case TOINT:
            case TOSTR:
            case NAME:
            case SQUARE_BRACKETS_L:
            case CURLY_BRACKETS_L:
                cmd = procAssign();
                break;
            default:
                showError();
                break;
        }

        return cmd;
    } 
    private BlocksCommand procDecl() {
        //System.out.printf("proDecl, %s tipo: %s\n",current.token,current.type);
        int line = lex.getLine();
        List<Command> cmds = new ArrayList<Command>();

        boolean constant = false;
        if (current.type == TokenType.FINAL) {
            advance();
            constant = true;
        }

        eat(TokenType.VAR);

        boolean nullable = false;
        if (current.type == TokenType.NULLABLE) {
            advance();
            nullable = true;
        }

        Variable var = procDeclarationName(constant, nullable);

        if (current.type == TokenType.ASSIGN) {
            line = lex.getLine(); 
            advance();

            Expr rhs = procExpr();

            AssignCommand acmd = new AssignCommand(line, rhs, var);
            cmds.add(acmd);
        }
        while(current.type==TokenType.COMMA){
            advance();
            var = procDeclarationName(constant, nullable);
            if (current.type == TokenType.ASSIGN) {
                line = lex.getLine();
                advance();

                Expr rhs = procExpr();

                AssignCommand acmd = new AssignCommand(line, rhs, var);
                cmds.add(acmd);
            }

        }

        //adicionar um loop while

        BlocksCommand bc = new BlocksCommand(line, cmds);
        eat(TokenType.SEMICOLON);
        return bc;
    }

    // <assert> ::= assert '(' <expr> [ ',' <expr> ] ')' ';'
    private AssertCommand procAssert() {
        eat(TokenType.ASSERT);
        eat(TokenType.PARENTESES_L);
        int line = lex.getLine();
        Expr expr =  procExpr();
        Expr expr2 =  null;
        if(current.type == TokenType.COMMA){
            advance();
            expr2 = procExpr();
        }
        eat(TokenType.PARENTESES_R);
        eat(TokenType.SEMICOLON);
        return new AssertCommand(line,expr,expr2);

    }
    private PrintCommand procPrint(){
        //System.out.printf("procPrint\n");
        eat(TokenType.PRINT);

        int line = lex.getLine();
        eat(TokenType.PARENTESES_L);
        //if do expr
        Expr expr = null;
        if (current.type == TokenType.DENIAL ||
                current.type == TokenType.SUB ||
                current.type == TokenType.INCREMENT ||
                current.type == TokenType.DECREMENT ||
                current.type == TokenType.PARENTESES_L ||
                current.type == TokenType.NULL ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NUMBER ||
                current.type == TokenType.TEXT ||
                current.type == TokenType.READ ||
                current.type == TokenType.RANDOM ||
                current.type == TokenType.LENGTH ||
                current.type == TokenType.KEYS ||
                current.type == TokenType.VALUES ||
                current.type == TokenType.TOBOOL ||
                current.type == TokenType.TOINT ||
                current.type == TokenType.TOSTR ||
                current.type == TokenType.NAME ||
                current.type == TokenType.SQUARE_BRACKETS_L ||
                current.type == TokenType.CURLY_BRACKETS_L) {
            expr = procExpr();
        }
        eat(TokenType.PARENTESES_R);
        eat(TokenType.SEMICOLON);

        PrintCommand pc = new PrintCommand(line, expr);
        return pc;
    }

    // <if> ::= if '(' <expr> ')' <body> [ else <body> ]
    private IfCommand procIf(){
        //System.out.printf("proIf, %s tipo: %s\n",current.token,current.type);
        eat(TokenType.IF);
        Command elseCmds = null;
        int line = lex.getLine();
        eat(TokenType.PARENTESES_L);
        Expr expr = procExpr();
        eat(TokenType.PARENTESES_R);
        Command thenCmds = procBody();
        if(current.type == TokenType.ELSE){
            advance();
            //System.out.printf("proElse, %s tipo: %s\n",current.token,current.type);
            elseCmds = procBody();
        }

        IfCommand ifcmd = new IfCommand(line, expr, thenCmds, elseCmds);
        return ifcmd;
    }
    // <while> ::= while '(' <expr> ')' <body>
    private WhileCommand procWhile() {
        eat(TokenType.WHILE);
        int line = lex.getLine();

        eat(TokenType.PARENTESES_L);
        Expr expr = procExpr();
        eat(TokenType.PARENTESES_R);
        //System.out.printf("prowhile, %s tipo: %s\n",current.token,current.type);
        Command cmds = procBody();

        WhileCommand wcmd = new WhileCommand(line, expr, cmds);
        return wcmd; 
    }

    // <dowhile> ::= do <body> while '(' <expr> ')' ';'
    private DoWhileCommand procDowhile(){
        eat(TokenType.DO);
        int line = lex.getLine();
        Command cmds = procBody();

        eat(TokenType.WHILE);
        eat(TokenType.PARENTESES_L);
        Expr cond = procExpr();
        eat(TokenType.PARENTESES_R);
        eat(TokenType.SEMICOLON) ;

        DoWhileCommand dwcmd = new DoWhileCommand(line, cmds, cond);
        return dwcmd;

    }
 // <for> ::= for '(' <name> in <expr> ')' <body>
    private ForCommand procFor(){//terminar o for
        eat(TokenType.FOR);
        int line = lex.getLine();
        eat(TokenType.PARENTESES_L);
        Variable name = procName();
        
        eat(TokenType.IN);
        Expr expr = procExpr();
        eat(TokenType.PARENTESES_R);
        Command cmds = procBody();
        //terminar o for
        
        ForCommand fcmd = new ForCommand(line, name, expr, cmds);
        return fcmd;
    }

    private Command procBody() {
        Command cmds = null;
        if (current.type == TokenType.CURLY_BRACKETS_L) {
            advance();
            cmds = procCode();
            eat(TokenType.CURLY_BRACKETS_R);
        } else {
            cmds = procCmd();
        }

        return cmds;
    }
   // <assign> ::= [ <expr> '=' ] <expr> ';'
   private AssignCommand procAssign() {
       //System.out.printf("proAssign, %s tipo: %s\n",current.token,current.type);
        Expr rhs = procExpr();
    SetExpr lhs = null;
    //Implementação pode estar errada
    int line = lex.getLine();
    if (current.type == TokenType.ASSIGN) {
        advance();
        //System.out.println(rhs.getClass().getName());
        if (!(rhs instanceof SetExpr))
            Utils.abort(line);

        lhs = (SetExpr) rhs;
        rhs = procExpr();
    }

    eat(TokenType.SEMICOLON);

    AssignCommand acmd = new AssignCommand(line, rhs, lhs);
    return acmd;
}

    private Expr procExpr() {
        int line = lex.getLine();
        Expr left = procCond();

        Expr right = null;
        BinaryOp op = null;
        if (current.type == TokenType.IF_NULL) {
            op = BinaryOp.IF_NULL;
            advance();
            right = procCond();
            BinaryExpr res = new BinaryExpr(line, left, op, right);
            return res;
        }
        else{
        //retornar expressão condicional
        return left;}
    }

    private Expr procCond() {
        Expr left = procRel();

        while (current.type == TokenType.AND ||
                current.type == TokenType.OR) {
            int line = lex.getLine();
            Expr right = null;

            BinaryOp op = null;
            if (current.type == TokenType.AND) {
                advance();
                op = BinaryOp.AND;
            } else {
                advance();
                op = BinaryOp.OR;
            }

            right = procRel();
            left = new BinaryExpr(line, left, op, right);
        }

        //resolver as expressões
        return left;
    }
    /*private void procRel(){// VERIFICAR O (), MOSTRAR SHOW ERROS NESSE CASO
        procArith();
        if(current.type == TokenType.LOWER||current.type == TokenType.GREATER||current.type == TokenType.GREATER_EQUAL
        ||current.type == TokenType.LOWER_EQUAL||current.type == TokenType.EQUAL||current.type == TokenType.NOT_EQUAL){
            advance();//colocar um showerror?
            procArith();
        }

    }*/
    private Expr procRel() {
        Expr left = procArith();

        if (current.type == TokenType.LOWER ||
                current.type == TokenType.GREATER ||
                current.type == TokenType.LOWER_EQUAL ||
                current.type == TokenType.GREATER_EQUAL ||
                current.type == TokenType.EQUAL ||
                current.type == TokenType.NOT_EQUAL) {
            BinaryOp op = null;
            switch (current.type) {
                case LOWER:
                    op = BinaryOp.LOWER_THAN;
                    advance();
                    break;
                case GREATER:
                    op = BinaryOp.GREATER_THAN;
                    advance();
                    break;
                case LOWER_EQUAL:
                    op = BinaryOp.LOWER_EQUAL;
                    advance();
                    break;
                case GREATER_EQUAL:
                    op = BinaryOp.GREATER_EQUAL;
                    advance();
                    break;
                case EQUAL:
                    op = BinaryOp.EQUAL;
                    advance();
                    break;
                default:
                    op = BinaryOp.NOT_EQUAL;
                    advance();
                    break;
            }

            int line = lex.getLine();
            Expr right = procArith();

            left = new BinaryExpr(line, left, op, right);
        }

        return left;
    }

    private Variable procDeclarationName(boolean constant, boolean nullable) {
        String name = current.token;
        eat(TokenType.NAME);
        int line = lex.getLine();

        if (memory.containsKey(name))
            Utils.abort(line);

        Variable var;
        if (nullable) {
            var = new UnsafeVariable(line, name, constant);
        } else {
            var = new SafeVariable(line, name, constant);
        }

        memory.put(name, var);

        return var;
    }
    private Expr procArith() {
        Expr left = procTerm();

        while (current.type == TokenType.ADD ||
                current.type == TokenType.SUB) {
            BinaryOp op = null;
            if (current.type == TokenType.ADD) {
                op = BinaryOp.ADD;
                advance();
            } else {
                op = BinaryOp.SUB;
                advance();
            }
            int line = lex.getLine();

            Expr right = procTerm();

            left = new BinaryExpr(line, left, op, right);//da merda
        }

        return left;
    }


    // <term> ::= <prefix> { ( '*' | '/' | '%' ) <prefix> }
    private Expr procTerm() {
        Expr left = procPrefix();

        while(current.type == TokenType.MUL||current.type == TokenType.DIV||current.type == TokenType.MOD){
            BinaryOp op = null;
            if(current.type == TokenType.MUL){
                 op = BinaryOp.MUL;
                advance();
            }
            else if (current.type == TokenType.DIV) {
                op = BinaryOp.DIV;
                advance();
            }
            else if (current.type == TokenType.MOD) {
                op = BinaryOp.MOD;
                advance();
            }
            else{
                showError();
            }
            int line = lex.getLine();
           Expr right = procPrefix();
            left = new BinaryExpr(line, left, op, right);
        }
        return left;
    }

        private Expr procFactor() {
           // System.out.printf("Factor Token: %s, tipo: %s\n",current.token,current.type);
            Expr expr = null;
            UnaryOp op  = null;
            int line = lex.getLine();
            if (current.type == TokenType.PARENTESES_L) {
                advance();
                expr = procExpr();
                eat(TokenType.PARENTESES_R);
            } else {
                expr = procRValue();
            }
            if (current.type == TokenType.INCREMENT ||
                    current.type == TokenType.DECREMENT) {
                if (current.type == TokenType.INCREMENT) {
                    op = UnaryOp.POS_INC;
                    advance();
                } else {
                    op = UnaryOp.POS_DEC;
                    advance();
                }
                expr = new UnaryExpr(line,expr,op);
            }
    
            return expr;
        }
    
    private Expr procPrefix() {

        UnaryOp op = null;
        if (current.type == TokenType.DENIAL ||
                current.type == TokenType.SUB ||
                current.type == TokenType.INCREMENT ||
                current.type == TokenType.DECREMENT) {
            switch (current.type) {
                case DENIAL:
                    op = UnaryOp.NOT;
                    advance();
                    break;
                case SUB:
                    op = UnaryOp.NEG;
                    advance();
                    break;
                case INCREMENT:
                    op = UnaryOp.PRE_INC;
                    advance();
                    break;
                default:
                    op = UnaryOp.POS_INC;
                    advance();
                    break;
            }
        }

        int line = lex.getLine();
        Expr expr = procFactor();

        if (op != null) {
            UnaryExpr ue = new UnaryExpr(line, expr, op);
            return ue;
        }

        return expr;
    }
    // <rvalue> ::= <const> | <function> | <lvalue> | <list> | <map>
    private Expr procRValue() {
       // System.out.printf("Rvalue Token: %s, tipo: %s\n",current.token,current.type);
        Expr expr = null;
        switch (current.type) {
            case NULL:
            case FALSE:
            case TRUE:
            case NUMBER:
            case TEXT:
                expr = procConst();
                break;
            case READ:
            case RANDOM:
            case LENGTH:
            case KEYS:
            case VALUES:
            case TOBOOL:
            case TOINT:
            case TOSTR:
                expr = procFunction();
                break;
            case NAME:
                expr = procLValue();
                break;
            case SQUARE_BRACKETS_L:
                //implementar para lista
                expr = procList();
                break;
            case CURLY_BRACKETS_L:
                expr = procMap();
                break;
            default:
                showError();
                break;
        }

        return expr;
    }

    // <const> ::= null | false | true | <number> | <text>
    private ConstExpr procConst() {
       // System.out.printf("const Token: %s, tipo: %s\n",current.token,current.type);
        Value<?> v = null;
        switch (current.type) {
            case NULL:
                advance();
                v = null;
                break;
            case FALSE:
                advance();
                v = new BoolValue(false);
                break;
            case TRUE:
                advance();
                v = new BoolValue(true);
                break;
            case NUMBER:
                v = procNumber();
                break;
            case TEXT:
                v = procText();
                break;
            default:
                showError();
                break;
        }

        int line = lex.getLine();
        ConstExpr ce = new ConstExpr(line, v);
        return ce;
    }

    private FunctionExpr procFunction() {
        FunctionOp op = null;
        switch (current.type) {
            case READ:
                advance();
                op = FunctionOp.READ;
                break;
            case RANDOM:
                advance();
                op = FunctionOp.RANDOM;
                break;
            case LENGTH:
                advance();
                op = FunctionOp.LENGTH;
                break;
            case KEYS:
                advance();
                op = FunctionOp.KEYS;
                break;
            case VALUES:
                advance();
                op = FunctionOp.VALUES;
                break;
            case TOBOOL:
                advance();
                op = FunctionOp.TOBOOL;
                break;
            case TOINT:
                advance();
                op = FunctionOp.TOINT;
                break;
            case TOSTR:
                advance();
                op = FunctionOp.TOSTR;
                break;
            default:
                showError();
                break;
        }
        int line = lex.getLine();

        eat(TokenType.PARENTESES_L);
        Expr expr = procExpr();
        eat(TokenType.PARENTESES_R);

        FunctionExpr fexpr = new FunctionExpr(line, op, expr);
        return fexpr;
    }

    private SetExpr procLValue() {
      //  System.out.printf("lvalue Token: %s, tipo: %s\n",current.token,current.type);
        SetExpr base = procName();

        while (current.type == TokenType.SQUARE_BRACKETS_L) {
            advance();
            int line = lex.getLine();

            Expr index = procExpr();

            base = new AccessExpr(line, base, index);

            eat(TokenType.SQUARE_BRACKETS_R);
        }


        return base;
    }

    private ListExpr procList(){
        eat(TokenType.SQUARE_BRACKETS_L);
        int line = lex.getLine();
        ListExpr list =  new ListExpr(line);
        if(current.type == TokenType.DENIAL ||
                current.type == TokenType.SUB ||
                current.type == TokenType.INCREMENT ||
                current.type == TokenType.DECREMENT ||
                current.type == TokenType.PARENTESES_L ||
                current.type == TokenType.NULL ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NUMBER ||
                current.type == TokenType.TEXT ||
                current.type == TokenType.READ ||
                current.type == TokenType.RANDOM ||
                current.type == TokenType.LENGTH ||
                current.type == TokenType.KEYS ||
                current.type == TokenType.VALUES ||
                current.type == TokenType.TOBOOL ||
                current.type == TokenType.TOINT ||
                current.type == TokenType.TOSTR ||
                current.type == TokenType.NAME ||
                current.type == TokenType.SQUARE_BRACKETS_L ||
                current.type == TokenType.CURLY_BRACKETS_L||
                        current.type == TokenType.IF||
                                current.type == TokenType.FOR||current.type == TokenType.THREE_POINT){
             ListItem Item = procL_element();
             list.addItem(Item);

            while(current.type == TokenType.COMMA){
                advance();
                Item = procL_element();
                list.addItem(Item);
            }

        }
        eat(TokenType.SQUARE_BRACKETS_R);
        return list;
    }
    private ListItem procL_element(){
        ListItem x = null;
        if(current.type == TokenType.DENIAL ||
                current.type == TokenType.SUB ||
                current.type == TokenType.INCREMENT ||
                current.type == TokenType.DECREMENT ||
                current.type == TokenType.PARENTESES_L ||
                current.type == TokenType.NULL ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NUMBER ||
                current.type == TokenType.TEXT ||
                current.type == TokenType.READ ||
                current.type == TokenType.RANDOM ||
                current.type == TokenType.LENGTH ||
                current.type == TokenType.KEYS ||
                current.type == TokenType.VALUES ||
                current.type == TokenType.TOBOOL ||
                current.type == TokenType.TOINT ||
                current.type == TokenType.TOSTR ||
                current.type == TokenType.NAME ||
                current.type == TokenType.SQUARE_BRACKETS_L ||
                current.type == TokenType.CURLY_BRACKETS_L){
            x = procL_single();
        }
        else if(current.type == TokenType.THREE_POINT){
            x = procL_spread();

        }
        else if(current.type == TokenType.IF){
            x = procL_if();

        }
        else if(current.type == TokenType.FOR){
            x = procL_for();

        }
        return x ;

    }
    private ListItem procL_single(){
        int line  =  lex.getLine();
        Expr expr = procExpr();
        SingleListItem Item =  new SingleListItem(line,expr);
        return Item;
    }
    private ListItem procL_spread(){
        eat(TokenType.THREE_POINT);
        int line  =  lex.getLine();
        Expr expr = procExpr();
        SpreadListItem Item =  new SpreadListItem(line,expr);
        return Item;

    }
    private ListItem procL_if(){
        int line  =  lex.getLine();
        eat(TokenType.IF);
        eat(TokenType.PARENTESES_L);
        Expr expr = procExpr();
        eat(TokenType.PARENTESES_R);
        ListItem x = procL_element();
        ListItem y = null;
        if(current.type == TokenType.ELSE){
            advance();
            y  = procL_element();
        }
        IfListItem a =  new IfListItem(line,expr,x,y);
        return a;
    }
 // <l-for> ::= for '(' <name> in <expr> ')' <l-elem>
    private ListItem procL_for(){
    	eat(TokenType.FOR);
    	int i =  lex.getLine();
        eat(TokenType.PARENTESES_L);
        Variable name  =  procName();//colocar como procedimento
        eat(TokenType.IN);
        Expr expr = procExpr();
        eat(TokenType.PARENTESES_R);
        ListItem  x = procL_element();
        ForListItem a = new ForListItem(i,expr,name,x);
        return a;
    }
    // <map> ::= '{' [ <m-elem> { ',' <m-elem> } ] '}'
    private MapExpr procMap() {
        eat(TokenType.CURLY_BRACKETS_L);
        int line = lex.getLine();

        MapExpr mexpr = new MapExpr(line);

        if (current.type == TokenType.DENIAL ||
                current.type == TokenType.SUB ||
                current.type == TokenType.INCREMENT ||
                current.type == TokenType.DECREMENT ||
                current.type == TokenType.PARENTESES_L ||
                current.type == TokenType.NULL ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NUMBER ||
                current.type == TokenType.TEXT ||
                current.type == TokenType.READ ||
                current.type == TokenType.RANDOM ||
                current.type == TokenType.LENGTH ||
                current.type == TokenType.KEYS ||
                current.type == TokenType.VALUES ||
                current.type == TokenType.TOBOOL ||
                current.type == TokenType.TOINT ||
                current.type == TokenType.TOSTR ||
                current.type == TokenType.NAME ||
                current.type == TokenType.SQUARE_BRACKETS_L ||
                current.type == TokenType.CURLY_BRACKETS_L) {
            MapItem item = procMElem();
            mexpr.addItem(item);

            while (current.type == TokenType.COMMA) {
                advance();
                item = procMElem();
                mexpr.addItem(item);
            }
        }
        eat(TokenType.CURLY_BRACKETS_R);

        return mexpr;
    }
    // <m-elem> ::= <expr> ':' <expr>
    private MapItem procMElem() {
        Expr key = procExpr();
        eat(TokenType.TWO_POINTS);
        Expr value = procExpr();

        MapItem item = new MapItem(key, value);
        return item;
    }
    private void advance() {
        // System.out.println("Advanced (\"" + current.token + "\", " +
        //     current.type + ")");
        current = lex.nextToken();
    }

    private void eat(TokenType type) {
        // System.out.println("Expected (..., " + type + "), found (\"" + 
        //     current.token + "\", " + current.type + ")");
        if (type == current.type) {
            current = lex.nextToken();
        } else {
            showError();
        }
    }
    private Variable procName() {
        //System.out.printf("Rvalue Token: %s, tipo: %s\n",current.token,current.type);
        String name = current.token;
        eat(TokenType.NAME);
        int line = lex.getLine();

        if (!memory.containsKey(name))
            Utils.abort(line);
        Variable var = memory.get(name);
        return var;
    }

    private NumberValue procNumber() {
        String txt = current.token;
        eat(TokenType.NUMBER);

        int n;
        try {
            n = Integer.parseInt(txt);
        } catch (Exception e) {
            n = 0;
        }

        NumberValue nv = new NumberValue(n);
        return nv;
    }

    private TextValue procText() {
        String txt = current.token;
        eat(TokenType.TEXT);

        TextValue tv = new TextValue(txt);
        return tv;
    }

    private void showError() {
        System.out.printf("%02d: ", lex.getLine());

        switch (current.type) {
            case INVALID_TOKEN:
                System.out.printf("Lexema inválido [%s]\n", current.token);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                System.out.printf("Fim de arquivo inesperado\n");
                break;
            default:
                System.out.printf("Lexema não esperado [%s]\n", current.token);
                break;
        }
        
        System.exit(1);
    }
    
    
}