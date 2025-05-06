/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.cprl.gui.visitor;

import edu.citadel.cprl.ast.*;

/**
 * Contrato do Visitor.
 * 
 * @author Prof. Dr. David Buzatto
 */
public abstract class Visitor {
    
    public abstract void visitConcreteElementAddingExpr( AddingExpr s );
    public abstract void visitConcreteElementArrayTypeDecl( ArrayTypeDecl s );
    public abstract void visitConcreteElementAssignmentStmt( AssignmentStmt s );
    public abstract void visitConcreteElementConstDecl( ConstDecl s );
    public abstract void visitConcreteElementConstValue( ConstValue s );
    public abstract void visitConcreteElementDeclarativePart( DeclarativePart s );
    public abstract void visitConcreteElementElsifPart( ElsifPart s );
    public abstract void visitConcreteElementExitStmt( ExitStmt s );
    public abstract void visitConcreteElementFunctionCall( FunctionCall s );
    public abstract void visitConcreteElementFunctionDecl( FunctionDecl s );
    public abstract void visitConcreteElementIfStmt( IfStmt s );
    public abstract void visitConcreteElementLogicalExpr( LogicalExpr s );
    public abstract void visitConcreteElementLoopStmt( LoopStmt s );
    public abstract void visitConcreteElementMultiplyingExpr( MultiplyingExpr s );
    public abstract void visitConcreteElementNamedValue( NamedValue s );
    public abstract void visitConcreteElementNegationExpr( NegationExpr s );
    public abstract void visitConcreteElementNotExpr( NotExpr s );
    public abstract void visitConcreteElementParameterDecl( ParameterDecl s );
    public abstract void visitConcreteElementProcedureCallStmt( ProcedureCallStmt s );
    public abstract void visitConcreteElementProcedureDecl( ProcedureDecl s );
    public abstract void visitConcreteElementProgram( Program s );
    public abstract void visitConcreteElementReadStmt( ReadStmt s );
    public abstract void visitConcreteElementRelationalExpr( RelationalExpr s );
    public abstract void visitConcreteElementReturnStmt( ReturnStmt s );
    public abstract void visitConcreteElementSingleVarDecl( SingleVarDecl s );
    public abstract void visitConcreteElementStatementPart( StatementPart s );
    public abstract void visitConcreteElementVarDecl( VarDecl s );
    public abstract void visitConcreteElementVariable( Variable s );
    public abstract void visitConcreteElementWriteStmt( WriteStmt s );
    public abstract void visitConcreteElementWritelnStmt( WritelnStmt s );
    
}
