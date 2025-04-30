package edu.citadel.cprl.ast;

import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.compiler.Position;
import test.cprl.gui.visitor.Visitor;

/**
 * The abstract syntax tree node for a return statement.
 */
public class ReturnStmt extends Statement {

    private SubprogramDecl subprogramDecl;
    private Expression returnExpr;          // may be null
    
    // position of the return token (needed for error reporting)
    private Position returnPosition;

    /**
     * Construct a return statement with a reference to the enclosing subprogram
     * and the expression for the value being returned, which may be null.
     */
    public ReturnStmt( SubprogramDecl subprogramDecl, Expression returnExpr, Position returnPosition ) {
        this.subprogramDecl = subprogramDecl;
        this.returnExpr = returnExpr;
        this.returnPosition = returnPosition;
    }

    public SubprogramDecl getSubprogramDecl() {
        return subprogramDecl;
    }

    public Expression getReturnExpr() {
        return returnExpr;
    }

    public Position getReturnPosition() {
        return returnPosition;
    }

    @Override
    public void accept( Visitor v ) {
        v.visitConcreteElementReturnStmt( this );
    }
    
    @Override
    public void checkConstraints() {
        
        assert subprogramDecl != null : "Return statement must be nested within a subprogram.";

        // ...
        
    }

    @Override
    public void emit() throws CodeGenException {
        // ...
    }
    
}
