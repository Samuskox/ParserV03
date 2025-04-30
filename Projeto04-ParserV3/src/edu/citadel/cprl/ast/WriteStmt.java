package edu.citadel.cprl.ast;

import java.util.List;
import test.cprl.gui.visitor.Visitor;

/**
 * The abstract syntax tree node for a write statement.
 */
public class WriteStmt extends OutputStmt {

    /**
     * Construct a write statement with the list of expressions.
     */
    public WriteStmt( List<Expression> expressions ) {
        super( expressions );
        assert expressions.size() > 0 : "A \"write\" statement must have an expression.";
    }

    @Override
    public void accept( Visitor v ) {
        v.visitConcreteElementWriteStmt( this );
    }
    
    // inherited checkConstraints() method is sufficient
    // inherited emit() method is sufficient
    
}
