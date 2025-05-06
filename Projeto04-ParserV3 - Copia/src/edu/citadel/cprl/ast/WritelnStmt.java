package edu.citadel.cprl.ast;

import edu.citadel.compiler.CodeGenException;

import java.util.List;
import test.cprl.gui.visitor.Visitor;

/**
 * The abstract syntax tree node for a writeln statement.
 */
public class WritelnStmt extends OutputStmt {

    /**
     * Construct a writeln statement with the list of expressions.
     */
    public WritelnStmt( List<Expression> expressions ) {
        super( expressions );
    }

    @Override
    public void accept( Visitor v ) {
        v.visitConcreteElementWritelnStmt( this );
    }
    
    // inherited checkConstraints() method is sufficient
    @Override
    public void emit() throws CodeGenException {
        // ...
    }
    
}
