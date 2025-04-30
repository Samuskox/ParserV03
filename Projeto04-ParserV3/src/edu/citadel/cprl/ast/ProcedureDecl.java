package edu.citadel.cprl.ast;

import edu.citadel.compiler.CodeGenException;
import edu.citadel.cprl.Token;
import test.cprl.gui.visitor.Visitor;

/**
 * The abstract syntax tree node for a procedure declaration.
 */
public class ProcedureDecl extends SubprogramDecl {

    /**
     * Construct a procedure declaration with its name (an identifier).
     */
    public ProcedureDecl( Token procId ) {
        super( procId );
    }

    @Override
    public void accept( Visitor v ) {
        v.visitConcreteElementProcedureDecl( this );
    }
    
    // inherited checkConstraints() is sufficient
    @Override
    public void emit() throws CodeGenException {
        // ...
    }
    
}
