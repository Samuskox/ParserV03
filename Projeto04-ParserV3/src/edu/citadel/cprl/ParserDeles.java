package edu.citadel.cprl;

import com.sun.source.tree.WhileLoopTree;
import edu.citadel.compiler.Position;
import edu.citadel.compiler.ParserException;
import edu.citadel.compiler.InternalCompilerException;
import edu.citadel.compiler.ErrorHandler;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import static edu.citadel.cprl.FirstFollowSets.*;
import edu.citadel.cprl.ast.*;

/**
 * Implementado por Aloísio & Guilherme
 */
public class ParserDeles {

    private Scanner scanner;
    private IdTable idTable;
    private LoopContext loopContext;
    private SubprogramContext subprogramContext;

    /**
     * Constrói um analisador sintático (parser) com um scanner especificado.
     */
    public ParserDeles(Scanner scanner) {

        this.scanner = scanner;
        this.idTable = new IdTable();
        this.loopContext = new LoopContext();
        this.subprogramContext = new SubprogramContext();

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * program = declarativePart statementPart "." .
     */
    public Program parseProgram() throws IOException {

        try {

            DeclarativePart declPart = parseDeclarativePart();
            StatementPart stmtPart = parseStatementPart();
            match(Symbol.dot);
            match(Symbol.EOF);
            return new Program(declPart, stmtPart);

        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("program"));
            return null;
        }

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * declarativePart = initialDecls subprogramDecls .
     */
    public DeclarativePart parseDeclarativePart() throws IOException {

        List<InitialDecl> initialDecls = parseInitialDecls();
        List<SubprogramDecl> subprogDecls = parseSubprogramDecls();

        return new DeclarativePart(initialDecls, subprogDecls);

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * initialDecls = ( initialDecl )* .
     */
    public List<InitialDecl> parseInitialDecls() throws IOException {

        List<InitialDecl> initialDecls = new ArrayList<>();

        while (scanner.getSymbol().isInitialDeclStarter()) {

            InitialDecl decl = parseInitialDecl();

            if (decl instanceof VarDecl) {
                VarDecl varDecl = (VarDecl) decl;
                for (SingleVarDecl singleVarDecl : varDecl.getSingleVarDecls()) {
                    initialDecls.add(singleVarDecl);
                }
            } else {
                initialDecls.add(decl);
            }

        }

        return initialDecls;

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * initialDecl = constDecl | arrayTypeDecl | varDecl .
     */
    public InitialDecl parseInitialDecl() throws IOException {

        // código do parser esperado para o Projeto 03
        if (scanner.getSymbol() == Symbol.constRW) {
            ConstDecl CD = parseConstDecl();
            return CD;
        } else if (scanner.getSymbol() == Symbol.typeRW) {
            ArrayTypeDecl AT = parseArrayTypeDecl();
            return AT;
        } else if (scanner.getSymbol() == Symbol.varRW) {
            VarDecl VD = parseVarDecl();
            return VD;
        } else {
            throw internalError("Invalid initial decl.");
        }
// <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * constDecl = "const" constId ":=" literal ";" .
     */
    public ConstDecl parseConstDecl() throws IOException {

        try {

            match(Symbol.constRW);
            Token constId = scanner.getToken();
            match(Symbol.identifier);
            match(Symbol.assign);
            Token literal = parseLiteral();
            match(Symbol.semicolon);

            Type constType = Type.UNKNOWN;
            if (literal != null) {
                constType = Type.getTypeOf(literal.getSymbol());
            }

            ConstDecl constDecl = new ConstDecl(constId, constType, literal);

            idTable.add(constDecl);

            return constDecl;

        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("constDecl"));
            return null;
        }

        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
    }

    /**
     * Analisa as regras gramaticais abaixo:
     *
     * literal = intLiteral | charLiteral | stringLiteral | booleanLiteral .
     * booleanLiteral = "true" | "false" .
     */
    public Token parseLiteral() throws IOException {

        try {

            if (scanner.getSymbol().isLiteral()) {
                Token literal = scanner.getToken();
                matchCurrentSymbol();
                return literal;
            } else {
                throw error("Invalid literal expression.");
            }

        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("literal"));
            return null;
        }

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * varDecl = "var" identifiers ":" typeName ";" .
     */
    public VarDecl parseVarDecl() throws IOException {

        try {

            match(Symbol.varRW);
            List<Token> identifiers = parseIdentifiers();
            match(Symbol.colon);
            Type typeName = parseTypeName();
            match(Symbol.semicolon);
            VarDecl varDecl = new VarDecl(identifiers, typeName, idTable.getScopeLevel());

            for (SingleVarDecl decl : varDecl.getSingleVarDecls()) {
                idTable.add(decl);
            }

            return varDecl;

        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("varDecl"));
            return null;
        }

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * identifiers = identifier ( "," identifier )* .
     *
     * @return uma lista de tokens do tipo identificador. Retorna uma lista
     * vazia caso a análise falhe.
     */
    public List<Token> parseIdentifiers() throws IOException {

        try {

            List<Token> identifiers = new ArrayList<>();
            Token idToken = scanner.getToken();
            match(Symbol.identifier);
            identifiers.add(idToken);

            while (scanner.getSymbol() == Symbol.comma) {
                matchCurrentSymbol();
                idToken = scanner.getToken();
                match(Symbol.identifier);
                identifiers.add(idToken);
            }

            return identifiers;

        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("identifiers"));
            return Collections.emptyList();
        }

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * arrayTypeDecl = "type" typeId "=" "array" "[" intConstValue "]" "of"
     * typeName ";" .
     */
    public ArrayTypeDecl parseArrayTypeDecl() throws IOException {

        // código do parser esperado para o Projeto 03
        try {
            match(Symbol.typeRW);
            Token typeId = scanner.getToken();
            match(Symbol.identifier);
            match(Symbol.equals);
            match(Symbol.arrayRW);
            match(Symbol.leftBracket);
            ConstValue numElements = parseConstValue();
            if (numElements == null) {
                numElements = new ConstValue(new Token(Symbol.intLiteral, scanner.getPosition(), "0"));
            }
            match(Symbol.rightBracket);
            match(Symbol.ofRW);
            Type typeName = parseTypeName();
            match(Symbol.semicolon);
            ArrayTypeDecl arrayTypeDecl = new ArrayTypeDecl(typeId, typeName, numElements);
            idTable.add(arrayTypeDecl);

            return arrayTypeDecl;
        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("arrayTypeDecl"));
            return null;
        }

        /* Dica: Se parseConstValue() returnar um valor null, crie um token 
         * "boneco" para o ConstValue evitando assim erros adicionais associados 
         * à NullPointerException. Por exemplo:
         * new ConstValue( new Token( Symbol.intLiteral, scanner.getPosition(), "0" ) );
         */
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * typeName = "Integer" | "Boolean" | "Char" | typeId .
     */
    public Type parseTypeName() throws IOException {

        Type type = Type.UNKNOWN;

        try {

            if (scanner.getSymbol() == Symbol.IntegerRW) {
                type = Type.Integer;
                matchCurrentSymbol();
            } else if (scanner.getSymbol() == Symbol.BooleanRW) {
                type = Type.Boolean;
                matchCurrentSymbol();
            } else if (scanner.getSymbol() == Symbol.CharRW) {
                type = Type.Char;
                matchCurrentSymbol();
            } else if (scanner.getSymbol() == Symbol.identifier) {

                Token typeId = scanner.getToken();
                matchCurrentSymbol();
                Declaration decl = idTable.get(typeId);

                if (decl != null) {
                    if (decl instanceof ArrayTypeDecl) {
                        type = decl.getType();
                    } else {
                        throw error(typeId.getPosition(), "Identifier \""
                                + typeId + "\" is not a valid type name.");
                    }
                } else {
                    throw error(typeId.getPosition(), "Identifier \""
                            + typeId + "\" has not been declared.");
                }

            } else {
                throw error("Invalid type name.");
            }

        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("typeName"));
        }

        return type;

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * subprogramDecls = ( subprogramDecl )* .
     */
    public List<SubprogramDecl> parseSubprogramDecls() throws IOException {

        // código do parser esperado para o Projeto 03
        List<SubprogramDecl> subprogramDecls = new ArrayList<>();
        while (scanner.getSymbol().isSubprogramDeclStarter()) {
            subprogramDecls.add(parseSubprogramDecl());
        }
        return subprogramDecls;
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * subprogramDecl = procedureDecl | functionDecl .
     */
    public SubprogramDecl parseSubprogramDecl() throws IOException {

        // código do parser esperado para o Projeto 03
        if (scanner.getSymbol() == Symbol.procedureRW) {

            ProcedureDecl PD = parseProcedureDecl();
            return PD;

        } else if (scanner.getSymbol() == Symbol.functionRW) {
            FunctionDecl FD = parseFunctionDecl();
            return FD;
        }
        //throw internalError( "Invalid subprogram decl." );

        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
        return null;

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * procedureDecl = "procedure" procId ( formalParameters )? "is"
     * initialDecls statementPart procId ";" .
     */
    public ProcedureDecl parseProcedureDecl() throws IOException {

        try {

            match(Symbol.procedureRW);
            Token procId = scanner.getToken();
            match(Symbol.identifier);
            ProcedureDecl procDecl = new ProcedureDecl(procId);
            idTable.add(procDecl);
            idTable.openScope();

            if (scanner.getSymbol() == Symbol.leftParen) {
                procDecl.setFormalParams(parseFormalParameters());
            }

            match(Symbol.isRW);
            procDecl.setInitialDecls(parseInitialDecls());

            subprogramContext.beginSubprogramDecl(procDecl);
            procDecl.setStatementPart(parseStatementPart());
            subprogramContext.endSubprogramDecl();
            idTable.closeScope();

            Token procId2 = scanner.getToken();
            match(Symbol.identifier);

            if (!procId.getText().equals(procId2.getText())) {
                throw error(procId2.getPosition(), "Procedure name mismatch.");
            }

            match(Symbol.semicolon);

            return procDecl;

        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("procedureDecl"));
            return null;
        }

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * functionDecl = "function" funcId ( formalParameters )? "return" typeName
     * "is" initialDecls statementPart funcId ";" .
     */
    public FunctionDecl parseFunctionDecl() throws IOException {

        // código do parser esperado para o Projeto 03
        try {

            match(Symbol.functionRW);
            Token funcId = scanner.getToken();
            match(Symbol.identifier);
            FunctionDecl funcDecl = new FunctionDecl(funcId);
            idTable.add(funcDecl);
            idTable.openScope();
            

            if (scanner.getSymbol() == Symbol.leftParen) {
                funcDecl.setFormalParams(parseFormalParameters());
            }
            

            match(Symbol.returnRW);

            funcDecl.setType(parseTypeName());

            match(Symbol.isRW);
            funcDecl.setInitialDecls(parseInitialDecls());

            subprogramContext.beginSubprogramDecl(funcDecl);
            funcDecl.setStatementPart(parseStatementPart());
            subprogramContext.endSubprogramDecl();
            idTable.closeScope();

            Token procId2 = scanner.getToken();
            match(Symbol.identifier);

            if (!funcId.getText().equals(procId2.getText())) {
                throw error(procId2.getPosition(), "Function name mismatch.");
            }

            match(Symbol.semicolon);

            return funcDecl;

        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("functionDecl"));
            return null;
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * formalParameters = "(" parameterDecl ( "," parameterDecl )* ")" .
     */
    public List<ParameterDecl> parseFormalParameters() throws IOException {

        // código do parser esperado para o Projeto 03
        try {
            List<ParameterDecl> parametersDecl = new ArrayList<>();

            match(Symbol.leftParen);

            parametersDecl.add(parseParameterDecl());

            while (scanner.getSymbol() == Symbol.comma) {
                matchCurrentSymbol();
                parametersDecl.add(parseParameterDecl());
            }

            match(Symbol.rightParen);
            return parametersDecl;
        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("formalParameters"));
            return null;
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * parameterDecl = ( "var" )? paramId ":" typeName .
     */
    public ParameterDecl parseParameterDecl() throws IOException {

        // código do parser esperado para o Projeto 03
        try {
            boolean isVar = false;
            if (scanner.getSymbol() == Symbol.varRW) {
                isVar = true;
                matchCurrentSymbol();
            }

            Token paramId = scanner.getToken();
            match(Symbol.identifier);

            match(Symbol.colon);

            Type typeName = parseTypeName();

            ParameterDecl parameterDecl = new ParameterDecl(paramId, typeName, isVar);
            idTable.add(parameterDecl);

            return parameterDecl;
        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("parameterDecl"));
            return null;
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * statementPart = "begin" statements "end" .
     */
    public StatementPart parseStatementPart() throws IOException {

        try {
            match(Symbol.beginRW);

            List<Statement> statements = parseStatements();
            match(Symbol.endRW);
            return new StatementPart(statements);
        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("statementPart"));
            return null;
        }

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * statements = ( statement )* .
     */
    public List<Statement> parseStatements() throws IOException {

        List<Statement> statements = new ArrayList<>();

        while (scanner.getSymbol().isStmtStarter()) {
            Statement statement = parseStatement();

            statements.add(statement);

        }

        return statements;
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * statement = assignmentStmt | ifStmt | loopStmt | exitStmt | readStmt |
     * writeStmt | writelnStmt | procedureCallStmt | returnStmt .
     */
    public Statement parseStatement() throws IOException {

        assert scanner.getSymbol().isStmtStarter() : "Invalid statement.";

        try {
            if (scanner.getSymbol() == Symbol.exitRW) {
                return parseExitStmt();
            } else if (scanner.getSymbol() == Symbol.identifier) {
                Declaration idType = idTable.get(scanner.getToken());

                if (idType == null) {
                    String errorMsg = "Identifier \"" + scanner.getToken()
                            + "\" has not been declared.";
                    throw error(scanner.getToken().getPosition(), errorMsg);
                } else if (idType instanceof NamedDecl) {

                    return parseAssignmentStmt();
                } else if (idType instanceof ProcedureDecl) {
                    return parseProcedureCallStmt();
                } else {
                    String errorMsg = ("Identifier \"" + scanner.getToken() + "\" cannot start a statement.");
                    throw error(scanner.getToken().getPosition(), errorMsg);
                }
            } else if (scanner.getSymbol() == Symbol.ifRW) {
                return parseIfStmt();
            } else if (scanner.getSymbol() == Symbol.loopRW || scanner.getSymbol() == Symbol.whileRW) {
                return parseLoopStmt();
            } else if (scanner.getSymbol() == Symbol.readRW) {
                return parseReadStmt();
            } else if (scanner.getSymbol() == Symbol.writeRW) {
                return parseWriteStmt();
            } else if (scanner.getSymbol() == Symbol.writelnRW) {
                return parseWritelnStmt();
            } else if (scanner.getSymbol() == Symbol.returnRW) {
                return parseReturnStmt();
            } else {
                throw internalError("Invalid statement.");
            }
        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            scanner.advanceTo(Symbol.semicolon);
            recover(FOLLOW_SETS.get("statement"));
            return null;
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * assignmentStmt = variable ":=" expression ";" .
     */
    public AssignmentStmt parseAssignmentStmt() throws IOException {

        // código do parser esperado para o Projeto 03
        try {
            Variable variable = parseVariable();
            Position pos = scanner.getPosition();

            try {

                match(Symbol.assign);

            } catch (ParserException e) {

                if (scanner.getSymbol() == Symbol.equals) {
                    ErrorHandler.getInstance().reportError(e);
                    matchCurrentSymbol();  // tratar "=" como ":="
                    // nesse contexto
                } else {
                    throw e;
                }

            }

            Expression expr = parseExpression();

            match(Symbol.semicolon);

            return new AssignmentStmt(variable, expr, pos);
        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("assignmentStmt"));
            return null;
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * ifStmt = "if" booleanExpr "then" statements ( "elsif" booleanExpr "then"
     * statements )* ( "else" statements )? "end" "if" ";" .
     */
    public IfStmt parseIfStmt() throws IOException {

        // código do parser esperado para o Projeto 03
        try {

            match(Symbol.ifRW);

            Expression booleanExpr = parseExpression();

            match(Symbol.thenRW);

            List<Statement> stmts = parseStatements();

            List<ElsifPart> elseIfPart = new ArrayList<>();
            List<Statement> elseStmts = new ArrayList<>();

            while (scanner.getSymbol() == Symbol.elsifRW) {
                match(Symbol.elsifRW);
                Expression elseIfExpr = parseExpression();
                match(Symbol.thenRW);
                List<Statement> elseIfStmt = parseStatements();

                elseIfPart.add(new ElsifPart(elseIfExpr, elseIfStmt));
            }

            if (scanner.getSymbol() == Symbol.elseRW) {
                match(Symbol.elseRW);
                elseStmts = parseStatements();
            }

            match(Symbol.endRW);
            match(Symbol.ifRW);
            match(Symbol.semicolon);

            return new IfStmt(booleanExpr, stmts, elseIfPart, elseStmts);

        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("ifStmt"));
            return null;
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * loopStmt = ( "while" booleanExpr )? "loop" statements "end" "loop" ";" .
     */
    public LoopStmt parseLoopStmt() throws IOException {

        // código do parser esperado para o Projeto 03
        try {
            LoopStmt stmt = new LoopStmt();
            if ( scanner.getSymbol() == Symbol.whileRW ) {
                matchCurrentSymbol();
                Expression whileExpr = parseExpression();
                stmt.setWhileExpr(whileExpr);
            }

            match( Symbol.loopRW );
            
            
            loopContext.beginLoop(stmt);
            stmt.setStatements(parseStatements());
            loopContext.endLoop();

            match( Symbol.endRW );
            match( Symbol.loopRW );
            match( Symbol.semicolon );
            
            return stmt;
        } catch ( ParserException e ) {
            ErrorHandler.getInstance().reportError( e );
            recover( FOLLOW_SETS.get( "loopStmt" ) );
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
        return null;

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * exitStmt = "exit" ( "when" booleanExpr )? ";" .
     */
    public ExitStmt parseExitStmt() throws IOException {

        // código do parser esperado para o Projeto 03
        try {
            Expression booleanExpr = null;
            Position exitPosition = scanner.getPosition();
            match( Symbol.exitRW );
            
            if ( scanner.getSymbol() == Symbol.whenRW ) {
                matchCurrentSymbol();
                booleanExpr = parseExpression();
               
            }
            
            match( Symbol.semicolon );
            
            LoopStmt loopStmt = loopContext.getLoopStmt();
            if (loopStmt == null) {
                throw error(exitPosition, "Exit statement is not nested within a loop.");
            }
            return new ExitStmt(booleanExpr, loopStmt);
            
        } catch ( ParserException e ) {
            ErrorHandler.getInstance().reportError( e );
            recover( FOLLOW_SETS.get( "exitStmt" ) );
            return null;
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
        

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * readStmt = "read" variable ";" .
     */
    public ReadStmt parseReadStmt() throws IOException {

        // código do parser esperado para o Projeto 03
        try {
            
            match( Symbol.readRW );
            Variable var = parseVariableExpr();
            match( Symbol.semicolon );
            return new ReadStmt(var);
        } catch ( ParserException e ) {
            ErrorHandler.getInstance().reportError( e );
            recover( FOLLOW_SETS.get( "readStmt" ) );
            return null;
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
        

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * writeStmt = "write" expressions ";" .
     */
    public WriteStmt parseWriteStmt() throws IOException {

        // código do parser esperado para o Projeto 03
        try {
            
            match( Symbol.writeRW );
            List<Expression> expressions = parseExpressions();
            match( Symbol.semicolon );
            return new WriteStmt(expressions);
            
        } catch ( ParserException e ) {
            ErrorHandler.getInstance().reportError( e );
            recover( FOLLOW_SETS.get( "writeStmt" ) );
            return null;
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
        

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * expressions = expression ( "," expression )* .
     */
    public List<Expression> parseExpressions() throws IOException {

        // código do parser esperado para o Projeto 03
        List<Expression> expressions = new ArrayList<>();
        expressions.add(parseExpression());
        
        
        while ( scanner.getSymbol() == Symbol.comma ) {
            matchCurrentSymbol();
            expressions.add(parseExpression());
        }
        return expressions;
        
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
        

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * writelnStmt = "writeln" ( expressions )? ";" .
     */
    public WritelnStmt parseWritelnStmt() throws IOException {

        try {

            match(Symbol.writelnRW);

            List<Expression> expressions;
            if (scanner.getSymbol().isExprStarter()) {

                expressions = parseExpressions();

            } else {
                expressions = Collections.emptyList();
            }
            
            match(Symbol.semicolon);

            return new WritelnStmt(expressions);

        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("writelnStmt"));
            return null;
        }

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * procedureCallStmt = procId ( actualParameters )? ";" .
     */
    public ProcedureCallStmt parseProcedureCallStmt() throws IOException {

        // código do parser esperado para o Projeto 03
        try {
            Token procId = scanner.getToken();
            List<Expression> actualParams = new ArrayList<>();
            match( Symbol.identifier );
            
            if ( scanner.getSymbol().isExprStarter() ) {
                actualParams = parseActualParameters();
            }
            
            match( Symbol.semicolon );
            ProcedureDecl procDecl = (ProcedureDecl) idTable.get(procId);
            return new ProcedureCallStmt(procId, actualParams, procDecl);
        } catch ( ParserException e ) {
            ErrorHandler.getInstance().reportError( e );
            recover( FOLLOW_SETS.get( "procedureCallStmt" ) );
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
        return null;

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * actualParameters = "(" expressions ")" .
     */
    public List<Expression> parseActualParameters() throws IOException {

        // código do parser esperado para o Projeto 03
        try {
            List<Expression> expressions = new ArrayList<>();
            match( Symbol.leftParen );
            expressions = parseExpressions();
            match( Symbol.rightParen );
            return expressions;
        } catch ( ParserException e ) {
            ErrorHandler.getInstance().reportError( e );
            recover( FOLLOW_SETS.get( "actualParameters" ) );
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
        return null;

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * returnStmt = "return" ( expression )? ";" .
     */
    public ReturnStmt parseReturnStmt() throws IOException {

        // código do parser esperado para o Projeto 03
        try {
            Position pos = scanner.getPosition();
            match( Symbol.returnRW );
            Expression exp = null;
            if ( scanner.getSymbol().isExprStarter() ) {
               exp = parseExpression();
            }
            
            match( Symbol.semicolon );
            
            
            
            SubprogramDecl subDecl = subprogramContext.getSubprogramDecl();
            if (subDecl == null) {
                throw error(pos, "Return statement is not nested within a subprogram.");
            }
            return new ReturnStmt(subDecl, exp, pos);
        } catch ( ParserException e ) {
            ErrorHandler.getInstance().reportError( e );
            recover( FOLLOW_SETS.get( "returnStmt" ) );
            return null;
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
        

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * variable = ( varId | paramId ) ( "[" expression "]" )* .
     *
     * Esse método auxiliar provê uma lógica comum aos métodos parseVariable() e
     * parseNamedValue(). Esse método não lida com quaisquer exceções geradas
     * pelo parser (ParseException), lançando-as ao método chamador para que
     * possam ser manipuladas apropriadamente.
     *
     * @throws ParserException se a análise falhar.
     * @see #parseVariable()
     * @see #parseNamedValue()
     */
    public Variable parseVariableExpr() throws IOException, ParserException {

        Token idToken = scanner.getToken();
        match(Symbol.identifier);
        Declaration decl = idTable.get(idToken);

        if (decl == null) {

            String errorMsg = "Identifier \"" + idToken
                    + "\" has not been declared.";
            throw error(idToken.getPosition(), errorMsg);

        } else if (!(decl instanceof NamedDecl)) {

            String errorMsg = "Identifier \"" + idToken
                    + "\" is not a variable.";
            throw error(idToken.getPosition(), errorMsg);

        }

        NamedDecl namedDecl = (NamedDecl) decl;
        List<Expression> indexExprs = new ArrayList<>();

        while (scanner.getSymbol() == Symbol.leftBracket) {
            matchCurrentSymbol();
            indexExprs.add(parseExpression());
            match(Symbol.rightBracket);
        }

        return new Variable(namedDecl, idToken.getPosition(), indexExprs);

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * variable = ( varId | paramId ) ( "[" expression "]" )* .
     */
    public Variable parseVariable() throws IOException {

        try {
            return parseVariableExpr();
        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("variable"));
            return null;
        }

    }

    /**
     * Analisa as regras gramaticais abaixo:
     *
     * expression = relation ( logicalOp relation )* . logicalOp = "and" | "or"
     * .
     */
    public Expression parseExpression() throws IOException {

        Expression expr = parseRelation();

        while (scanner.getSymbol().isLogicalOperator()) {
            Token operator = scanner.getToken();
            matchCurrentSymbol();
            Expression expr2 = parseRelation();
            expr = new LogicalExpr(expr, operator, expr2);
        }

        return expr;

    }

    /**
     * Analisa as regras gramaticais abaixo:
     *
     * relation = simpleExpr ( relationalOp simpleExpr )? . relationalOp = "=" |
     * "!=" | "<" | "<=" | ">" | ">=" .
     */
    public Expression parseRelation() throws IOException {

        // código do parser esperado para o Projeto 03
        Expression leftOp = parseSimpleExpr();

        if (scanner.getSymbol().isRelationalOperator()) {
            Token operator = scanner.getToken();
            matchCurrentSymbol();
            Expression rightOp = parseSimpleExpr();
            return new RelationalExpr(leftOp, operator, rightOp);
        } else {
            return leftOp;
        }

        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
    }

    /**
     * Analisa as regras gramaticais abaixo:
     *
     * simpleExpr = ( addingOp )? term ( addingOp term )* . addingOp = "+" | "-"
     * .
     */
    public Expression parseSimpleExpr() throws IOException {
        Token operator = null;

        // código do parser esperado para o Projeto 03
        if (scanner.getSymbol().isAddingOperator()) {
            operator = scanner.getToken();
            matchCurrentSymbol();
        }

        Expression leftExpr = parseTerm();

        if (operator != null) {
            leftExpr = new NegationExpr(operator, leftExpr);
        }

        while (scanner.getSymbol().isAddingOperator()) {
            operator = scanner.getToken();
            matchCurrentSymbol();
            Expression rightExpr = parseTerm();

            leftExpr = new AddingExpr(leftExpr, operator, rightExpr);
        }

        return leftExpr;

        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
    }

    /**
     * Analisa as regras gramaticais abaixo:
     *
     * term = factor ( multiplyingOp factor )* . multiplyingOp = "*" | "/" |
     * "mod" .
     */
    public Expression parseTerm() throws IOException {

        // código do parser esperado para o Projeto 03
        Expression leftFactor = parseFactor();

        Token operator = null;
        if (scanner.getSymbol().isMultiplyingOperator()) {
            while (scanner.getSymbol().isMultiplyingOperator()) {
                operator = scanner.getToken();
                matchCurrentSymbol();
                Expression rightFactor = parseFactor();

                leftFactor = new MultiplyingExpr(leftFactor, operator, rightFactor);
            }

            return leftFactor;
        }

        return leftFactor;
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * factor = "not" factor | constValue | namedValue | functionCall | "("
     * expression ")" .
     */
    public Expression parseFactor() throws IOException {

        try {

            Expression expr;

            if (scanner.getSymbol() == Symbol.notRW) {

                Token operator = scanner.getToken();
                matchCurrentSymbol();
                Expression factorExpr = parseFactor();
                expr = new NotExpr(operator, factorExpr);

            } else if (scanner.getSymbol().isLiteral()) {

                // lida com literais de constantes separadamente dos
                // identificadores de constantes
                expr = parseConstValue();

            } else if (scanner.getSymbol() == Symbol.identifier) {

                // lida com os identificadores baseando-se se eles foram
                // declarados como variáveis, constantes ou funções.
                Token idToken = scanner.getToken();
                Declaration decl = idTable.get(idToken);

                if (decl != null) {
                    if (decl instanceof ConstDecl) {
                        expr = parseConstValue();
                    } else if (decl instanceof NamedDecl) {
                        expr = parseNamedValue();
                    } else if (decl instanceof FunctionDecl) {
                        expr = parseFunctionCall();
                    } else {
                        throw error("Identifier \"" + scanner.getToken()
                                + "\" is not valid as an expression.");
                    }
                } else {
                    throw error("Identifier \"" + scanner.getToken()
                            + "\" has not been declared.");
                }

            } else if (scanner.getSymbol() == Symbol.leftParen) {
                matchCurrentSymbol();
                expr = parseExpression();
                match(Symbol.rightParen);
            } else {
                throw error("Invalid expression.");
            }

            return expr;

        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("factor"));
            return null;
        }

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * constValue = literal | constId .
     */
    public ConstValue parseConstValue() throws IOException {

        // código do parser esperado para o Projeto 03
        try {

            if (scanner.getSymbol().isLiteral()) {
                return new ConstValue(parseLiteral());
            } else if (scanner.getSymbol() == Symbol.identifier) {
                Token Id = scanner.getToken();
                matchCurrentSymbol();
                return new ConstValue(Id, (ConstDecl) idTable.get(Id));
            } else {
                throw error("Invalid constant.");
            }

        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("constValue"));
            return null;
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * namedValue = variable .
     */
    public NamedValue parseNamedValue() throws IOException {

        try {
            Variable variableExpr = parseVariableExpr();
            return new NamedValue(variableExpr);
        } catch (ParserException e) {
            ErrorHandler.getInstance().reportError(e);
            recover(FOLLOW_SETS.get("namedValue"));
            return null;
        }

    }

    /**
     * Analisa a regra gramatical abaixo:
     *
     * functionCall = funcId ( actualParameters )? .
     */
    public FunctionCall parseFunctionCall() throws IOException {

        // código do parser esperado para o Projeto 03
        try {
            Token funcId = scanner.getToken();
            List<Expression> actualParams = new ArrayList<>();
            match( Symbol.identifier );
            
            if ( scanner.getSymbol().isExprStarter() ) {
                actualParams = parseActualParameters();
            }
            FunctionDecl funcDecl = (FunctionDecl) idTable.get(funcId);
            return new FunctionCall(funcId, actualParams, funcDecl);
        } catch ( ParserException e ) {
            ErrorHandler.getInstance().reportError( e );
            recover( FOLLOW_SETS.get( "functionCall" ) );
        }
        // <editor-fold defaultstate="collapsed" desc="Implementação">
        // sua implementação aqui
        // </editor-fold>
        /* a linha abaixo está presente apenas para evitar erros
         * ela deve ser modificada para que o seja feito o que é esperado
         * seja inserindo-a em outra posição etc.
         */
        return null;

    }

    /**
     * *************************************************************************
     * Métodos utilitários de análise *
     * ************************************************************************
     */
    /**
     * Verifica se o símbolo atual do scanner é o símbolo esperado. Se for,
     * avança o scanner. Caso contrário, lança uma ParserException.
     */
    private void match(Symbol expectedSymbol) throws IOException, ParserException {

        if (scanner.getSymbol() == expectedSymbol) {
            scanner.advance();
        } else {
            String errorMsg = "Expecting \"" + expectedSymbol
                    + "\" but found \"" + scanner.getToken()
                    + "\" instead.";
            throw error(errorMsg);
        }

    }

    /**
     * Avança o scanner. Esse método representa uma correspondência
     * incondicional com o símbolo atual do scanner.
     */
    private void matchCurrentSymbol() throws IOException {
        scanner.advance();
    }

    /**
     * Avança o scanner até que o símbolo atual seja um dos símbolos
     * especificados no array de símbolos seguidores (follow).
     */
    private void recover(Symbol[] followers) throws IOException {
        scanner.advanceTo(followers);
    }

    /**
     * Cria uma ParserException com a mensagem especificada e a posição corrente
     * do scanner.
     */
    private ParserException error(String errMsg) {
        return new ParserException(scanner.getPosition(), errMsg);
    }

    /**
     * Cria uma ParserException especificando a posição atual do scanner e a
     * mensagem do erro.
     */
    private ParserException error(Position errPos, String errMsg) {
        return new ParserException(errPos, errMsg);
    }

    /**
     * Cria uma InternalCompilerException especificando a posição atual do
     * scanner e a mensagem do erro.
     */
    private InternalCompilerException internalError(String message) {
        return new InternalCompilerException(scanner.getPosition(), message);
    }

}
