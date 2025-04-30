package edu.citadel.cprl.ast;

import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.cprl.Type;
import test.cprl.gui.visitor.Visitor;

/**
 * The abstract syntax tree node for a read statement.
 */
public class ReadStmt extends Statement {

    private Variable variable;

    /**
     * Construct a read statement with the specified variable for storing the
     * input.
     */
    public ReadStmt( Variable variable ) {
        this.variable = variable;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public void accept( Visitor v ) {
        v.visitConcreteElementReadStmt( this );
    }
    
    @Override
    public void checkConstraints() {
        // a entrada é limitada à inteiros e caracteres
        // ...
    }

    @Override
    public void emit() throws CodeGenException {
        // ...
    }
    
}
