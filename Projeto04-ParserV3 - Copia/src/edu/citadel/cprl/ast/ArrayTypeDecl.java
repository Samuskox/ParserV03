package edu.citadel.cprl.ast;

import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.cprl.ArrayType;
import edu.citadel.cprl.Token;
import edu.citadel.cprl.Type;
import test.cprl.gui.visitor.Visitor;

/**
 * The abstract syntax tree node for an array type declaration.
 */
public class ArrayTypeDecl extends InitialDecl {

    private ConstValue numElements;

    /**
     * Construct an ArrayTypeDecl with its identifier, element type, and number
     * of elements. Note that the index type is always Integer in CPRL.
     */
    public ArrayTypeDecl( Token typeId, Type elemType, ConstValue numElements ) {
        super( typeId, new ArrayType( typeId.getText(), numElements.getLiteralIntValue(), elemType ) );
        this.numElements = numElements;
    }

    public ConstValue getNumElements() {
        return numElements;
    }
    
    @Override
    public void accept( Visitor v ) {
        v.visitConcreteElementArrayTypeDecl( this );
    }
    
    @Override
    public void checkConstraints() {
        // ...
    }
    
}
