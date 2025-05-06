/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.cprl.gui.visitor;

import edu.citadel.cprl.Token;
import edu.citadel.cprl.Type;
import edu.citadel.cprl.ast.*;
import org.apache.commons.text.StringEscapeUtils;

/**
 * Geração da árvore, implementando o Visitor.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class XMLArvoreVisitor extends Visitor {
    
    private static int level = 0;
    private static final String identationString = "  ";
    
    private String localIdentation = "";
    private StringBuilder sb = new StringBuilder();
    
    public String getXMLArvore() {
        
        StringBuilder intSb = new StringBuilder();
        
        intSb.append( "<tree>" ).append( "\n" );
        intSb.append( identationString ).append( "<declarations>" ).append( "\n" );
        intSb.append( identationString ).append( identationString ).append( "<attributeDecl name=\"nome\" type=\"String\"/>" ).append( "\n" );
        intSb.append( identationString ).append( identationString ).append( "<attributeDecl name=\"tipo\" type=\"String\"/>" ).append( "\n" );
        intSb.append( identationString ).append( "</declarations>" ).append( "\n" );
        intSb.append( sb );
        intSb.append( "</tree>" );
        
        return intSb.toString();
        
    }
    
    @Override
    public void visitConcreteElementAddingExpr( AddingExpr s ) {
        
        openBranch( s.getClass().getSimpleName() );

        if ( s.getLeftOperand() != null ) {
            s.getLeftOperand().accept( this );
        }
        
        if ( s.getOperator() != null ) {
            createTokenNode( s.getOperator() );
        }
        
        if ( s.getRightOperand() != null ) {
            s.getRightOperand().accept( this );
        }

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementArrayTypeDecl( ArrayTypeDecl s ) {
        
        openBranch( s.getClass().getSimpleName() );
        
        if ( s.getNumElements() != null ) {
            s.getNumElements().accept( this );
        }
        
        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementAssignmentStmt( AssignmentStmt s ) {
        
        openBranch( s.getClass().getSimpleName() );
        
        if ( s.getVariable() != null ) {
            s.getVariable().accept( this );
        }
        
        if ( s.getExpr() != null ) {
            s.getExpr().accept( this );
        }
        
        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementConstDecl( ConstDecl s ) {
        
        openBranch( s.getClass().getSimpleName() );
        
        if ( s.getIdToken() != null ) {
            createTokenNode( s.getIdToken() );
        }
        
        if ( s.getType() != null ) {
            createTypeNode( s.getType() );
        }
        
        if ( s.getLiteral() != null ) {
            createTokenNode( s.getLiteral() );
        }
        
        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementConstValue( ConstValue s ) {
        
        openBranch( s.getClass().getSimpleName() );
        
        if ( s.getLiteral() != null ) {
            createTokenNode( s.getLiteral() );
        }
        
        if ( s.getType() != null ) {
            createTypeNode( s.getType() );
        }
        
        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementDeclarativePart( DeclarativePart s ) {
        
        openBranch( s.getClass().getSimpleName() );

        for ( InitialDecl i : s.getInitialDecls() ) {
            if ( i != null ) {
                i.accept( this );
            }
        }
        
        for ( SubprogramDecl ss : s.getSubprogramDecls() ) {
            if ( ss != null ) {
                ss.accept( this );
            }
        }

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementElsifPart( ElsifPart s ) {
        
        openBranch( s.getClass().getSimpleName() );
        
        if ( s.getBooleanExpr() != null ) {
            s.getBooleanExpr().accept( this );
        }
        
        for ( Statement ss : s.getThenStmts() ) {
            ss.accept( this );
        }
        
        createLeaf( s.getL1() );
        createLeaf( s.getEndIfLabel() );

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementExitStmt( ExitStmt s ) {
        
        openBranch( s.getClass().getSimpleName() );
        
        if ( s.getWhenExpr() != null ) {
            s.getWhenExpr().accept( this );
        }
        
        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementFunctionCall( FunctionCall s ) {
        
        openBranch( s.getClass().getSimpleName() );

        if ( s.getFuncId() != null ) {
            createTokenNode( s.getFuncId() );
        }
        
        for ( Expression e : s.getActualParams() ) {
            if ( e != null ) {
                e.accept( this );
            }
        }

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementFunctionDecl( FunctionDecl s ) {
        
        openBranch( s.getClass().getSimpleName() );

        createTokenNode( s.getIdToken() );
        for ( ParameterDecl p : s.getFormalParams() ) {
            if ( p != null ) {
                p.accept( this );
            }
        }
        
        if ( s.getType() != null ) {
            createTypeNode( s.getType() );
        }
        
        for ( InitialDecl i : s.getInitialDecls() ) {
            if ( i != null ) {
                i.accept( this );
            }
        }
        
        if ( s.getStatementPart() != null ) {
            s.getStatementPart().accept( this );
        }
        
        createLeaf( s.getL1() );
        
        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementIfStmt( IfStmt s ) {
        
        openBranch( s.getClass().getSimpleName() );
        
        if ( s.getBooleanExpr() != null ) {
            s.getBooleanExpr().accept( this );
        }
        
        for ( Statement ss : s.getThenStmts() ) {
            if ( ss != null ) {
                ss.accept( this );
            }
        }
        
        for ( ElsifPart e : s.getElsifParts() ) {
            if ( e != null ) {
                e.accept( this );
            }
        }
        
        if ( s.getElseStmts() != null && !s.getElseStmts().isEmpty() ) {
            openBranch( "Else" );
            for ( Statement ss : s.getElseStmts() ) {
                if ( ss != null ) {
                    ss.accept( this );
                }
            }
            closeBranch();
        }

        createLeaf( s.getL1() );
        createLeaf( s.getL2() );
        
        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementLogicalExpr( LogicalExpr s ) {
        
        openBranch( s.getClass().getSimpleName() );

        if ( s.getLeftOperand() != null ) {
            s.getLeftOperand().accept( this );
        }
        
        if ( s.getOperator() != null ) {
            createTokenNode( s.getOperator() );
        }
        
        if ( s.getRightOperand() != null ) {
            s.getRightOperand().accept( this );
        }

        createLeaf( s.getL1() );
        createLeaf( s.getL2() );
        
        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementLoopStmt( LoopStmt s ) {
        
        openBranch( s.getClass().getSimpleName() );

        if ( s.getWhileExpr() != null ) {
            s.getWhileExpr().accept( this );
        }
        
        for ( Statement ss : s.getStatements() ) {
            if ( ss != null ) {
                ss.accept( this );
            }
        }

        createLeaf( s.getL1() );
        createLeaf( s.getL2() );
        
        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementMultiplyingExpr( MultiplyingExpr s ) {
        
        openBranch( s.getClass().getSimpleName() );

        if ( s.getLeftOperand() != null ) {
            s.getLeftOperand().accept( this );
        }
        
        if ( s.getOperator() != null ) {
            createTokenNode( s.getOperator() );
        }
        
        if ( s.getRightOperand() != null ) {
            s.getRightOperand().accept( this );
        }

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementNamedValue( NamedValue s ) {
        
        openBranch( s.getClass().getSimpleName() );
        
        if ( s.getDecl() != null ) {
            createNamedDeclarationNode( s.getDecl() );
        }
        
        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementNegationExpr( NegationExpr s ) {
        
        openBranch( s.getClass().getSimpleName() );

        if ( s.getOperator() != null ) {
            createTokenNode( s.getOperator() );
        }
        
        if ( s.getOperand() != null ) {
            s.getOperand().accept( this );
        }

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementNotExpr( NotExpr s ) {
        
        openBranch( s.getClass().getSimpleName() );

        if ( s.getOperator() != null ) {
            createTokenNode( s.getOperator() );
        }
        
        if ( s.getOperand() != null ) {
            s.getOperand().accept( this );
        }

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementParameterDecl( ParameterDecl s ) {
        
        openBranch( s.getClass().getSimpleName() );

        if ( s.getIdToken() != null ) {
            createTokenNode( s.getIdToken() );
        }
        
        if ( s.getType() != null ) {
            createTypeNode( s.getType() );
        }
        
        if ( s.getScopeLevel() != null ) {
            createLeaf( s.getScopeLevel().toString() );
        }

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementProcedureCallStmt( ProcedureCallStmt s ) {
        
        openBranch( s.getClass().getSimpleName() );

        if ( s.getProcId() != null ) {
            createTokenNode( s.getProcId() );
        }
        
        for ( Expression e : s.getActualParams() ) {
            if ( e != null ) {
                e.accept( this );
            }
        }

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementProcedureDecl( ProcedureDecl s ) {
        
        openBranch( s.getClass().getSimpleName() );

        if ( s.getIdToken() != null ) {
            createTokenNode( s.getIdToken() );
        }
        
        for ( ParameterDecl p : s.getFormalParams() ) {
            if ( p != null ) {
                p.accept( this );
            }
        }
        
        for ( InitialDecl i : s.getInitialDecls() ) {
            if ( i != null ) {
                i.accept( this );
            }
        }
        
        if ( s.getStatementPart() != null ) {
            s.getStatementPart().accept( this );
        }

        createLeaf( s.getL1() );

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementProgram( Program s ) {
        
        openBranch( s.getClass().getSimpleName(), "raiz" );
        
        if ( s.getDeclPart() != null ) {
            s.getDeclPart().accept( this );
        }
        
        if ( s.getStmtPart() != null ) {
            s.getStmtPart().accept( this );
        }
        
        createLeaf( s.getL1() );
        
        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementReadStmt( ReadStmt s ) {
        
        openBranch( s.getClass().getSimpleName() );
        
        if ( s.getVariable() != null ) {
            s.getVariable().accept( this );
        }
        
        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementRelationalExpr( RelationalExpr s ) {
        
        openBranch( s.getClass().getSimpleName() );

        if ( s.getLeftOperand() != null ) {
            s.getLeftOperand().accept( this );
        }
        
        if ( s.getOperator() != null ) {
            createTokenNode( s.getOperator() );
        }
        
        if ( s.getRightOperand() != null ) {
            s.getRightOperand().accept( this );
        }
        
        createLeaf( s.getL1() );
        createLeaf( s.getL2() );
        
        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementReturnStmt( ReturnStmt s ) {
        
        openBranch( s.getClass().getSimpleName() );

        if ( s.getReturnExpr() != null ) {
            s.getReturnExpr().accept( this );
        }

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementSingleVarDecl( SingleVarDecl s ) {
        
        openBranch( s.getClass().getSimpleName() );

        if ( s.getIdToken() != null ) {
            createTokenNode( s.getIdToken() );
        }
        
        if ( s.getType() != null ) {
            createTypeNode( s.getType() );
        }
        
        if ( s.getScopeLevel() != null ) {
            createLeaf( s.getScopeLevel().toString() );
        }
        
        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementStatementPart( StatementPart s ) {
        
        openBranch( s.getClass().getSimpleName() );

        for ( Statement ss : s.getStatements() ) {
            if ( ss != null ) {
                ss.accept( this );
            }
        }

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementVarDecl( VarDecl s ) {
        
        openBranch( s.getClass().getSimpleName() );
        
        if ( s.getIdToken() != null ) {
            createTokenNode( s.getIdToken() );
        }
        
        if ( s.getType() != null ) {
            createTypeNode( s.getType() );
        }
        
        for ( SingleVarDecl ss : s.getSingleVarDecls() ) {
            if ( ss != null ) {
                ss.accept( this );
            }
        }

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementVariable( Variable s ) {
        
        openBranch( s.getClass().getSimpleName() );

        if ( s.getDecl() != null ) {
            createNamedDeclarationNode( s.getDecl() );
        }
        
        if ( s.getIndexExprs() != null ) {
            for ( Expression e : s.getIndexExprs() ) {
                if ( e != null ) {
                    e.accept( this );
                }
            }
        }
        
        if ( s.getType() != null ) {
            createTypeNode( s.getType() );
        }

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementWriteStmt( WriteStmt s ) {
        
        openBranch( s.getClass().getSimpleName() );

        for ( Expression e : s.getExpressions() ) {
            if ( e != null ) {
                e.accept( this );
            }
        }

        closeBranch();
        
    }
    
    @Override
    public void visitConcreteElementWritelnStmt( WritelnStmt s ) {
        
        openBranch( s.getClass().getSimpleName() );

        for ( Expression e : s.getExpressions() ) {
            if ( e != null ) {
                e.accept( this );
            }
        }

        closeBranch();
        
    }

    private void openBranch( String branchName, String nodeType ) {
        level++;
        localIdentation = generateIdentation();
        sb.append( localIdentation ).append( "<branch>" ).append( "\n" );
        sb.append( localIdentation ).append( identationString ).append( "<attribute name=\"nome\" value=\" " ).append( branchName ).append( " \"/>" ).append( "\n" );
        sb.append( localIdentation ).append( identationString ).append( "<attribute name=\"tipo\" value=\"" ).append( nodeType ).append( "\"/>" ).append( "\n" );
    }
    
    private void openBranch( String branchName ) {
        openBranch( branchName, "galho" );
    }
    
    private void closeBranch() {
        sb.append( localIdentation ).append( "</branch>" ).append( "\n" );
        level--;
        localIdentation = generateIdentation();
    }
    
    private void createLeaf( String leafValue ) {
        sb.append( localIdentation ).append( "<leaf>" ).append( "\n" );
        sb.append( localIdentation ).append( identationString ).append( "<attribute name=\"nome\" value=\" " ).append( leafValue ).append( " \"/>" ).append( "\n" );
        sb.append( localIdentation ).append( identationString ).append( "<attribute name=\"tipo\" value=\"folha\"/>" ).append( "\n" );
        sb.append( localIdentation ).append( "</leaf>" ).append( "\n" );
    }
    
    private void createTokenNode( Token t ) {
        openBranch( "Token", "folha" );
        createLeaf( StringEscapeUtils.escapeHtml4( t.getText() ) );
        createLeaf( StringEscapeUtils.escapeHtml4( "\"" + t.getSymbol().toString() + "\"" ) );
        closeBranch();
    }
    
    private void createNamedDeclarationNode( NamedDecl n ) {
        
        openBranch( "NamedDecl" );
        
        if ( n instanceof ParameterDecl ) {
            ( (ParameterDecl) n ).accept( this );
        } else if ( n instanceof SingleVarDecl ) {
            ( (SingleVarDecl) n ).accept( this );
        } else {
            createLeaf( "???" );
        }
        
        closeBranch();
        
    }
    
    private void createTypeNode( Type t ) {
        createLeaf( t.getName() );
    }
    
    private String generateIdentation() {
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < level; i++ ) {
            sb.append( identationString );
        }
        return sb.toString();
    }
    
}
