package edu.citadel.cprl.ast;

import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.Position;
import edu.citadel.cprl.Type;

import java.io.PrintWriter;
import test.cprl.gui.visitor.Element;

/**
 * Classe base para todas as classes da árvore sinática abstrata.
 */
public abstract class AST implements Element {
    
    // número de espaços que serão impressos antes do opcode
    private static final String SPACES = "   ";

    private static PrintWriter out = new PrintWriter( System.out );

    // número da label atual para controle de fluxo
    private static int currentLabelNum = -1;
    
    /**
     * Configura o PrintWriter que será usado para a geração de código.
     */
    public static void setPrintWriter( PrintWriter out ) {
        AST.out = out;
    }

    /**
     * Cria/retorna uma nova ConstraintException com a posição específica e
     * uma mensagem.
     */
    protected ConstraintException error( Position errorPos, String errorMsg ) {
        return new ConstraintException( errorPos, errorMsg );
    }

    /**
     * Verifica as restrições semânticas/contextuais.
     */
    public abstract void checkConstraints();

    /**
     * Emite o código objeto da AST.
     *
     * @throws CodeGenException se o método for incapás de gerar o código alvo
     * apropriado.
     */
    public abstract void emit() throws CodeGenException;
    
    /**
     * Retorna um novo valor para o número de uma label. Esse método deve ser
     * chamado uma vez antes da geração do código de uma label.
     */
    protected String getNewLabel() {
        currentLabelNum++;
        return "L" + currentLabelNum;
    }

    /**
     * Retorna true se dois tipos são compatíveis em relação à atribuição.
     * Esse método trata também os tipos internos especiais do compilador.
     */
    protected boolean matchTypes( Type t1, Type t2 ) {
        return t1 == t2;
    }

    /**
     * Emite a instrução LOAD apropriada baseada no seu tipo.
     */
    protected void emitLoadInst( Type t ) {
        
        int numBytes = t.getSize();

        if ( numBytes == 4 ) {
            emit( "LOADW" );
        } else if ( numBytes == 2 ) {
            emit( "LOAD2B" );
        } else if ( numBytes == 1 ) {
            emit( "LOADB" );
        } else {
            emit( "LOAD " + numBytes );
        }
        
    }

    /**
     * Emite a instrução STORE apropriada baseada no seu tipo.
     */
    protected void emitStoreInst( Type t ) {
        
        int numBytes = t.getSize();

        if ( numBytes == 4 ) {
            emit( "STOREW" );
        } else if ( numBytes == 2 ) {
            emit( "STORE2B" );
        } else if ( numBytes == 1 ) {
            emit( "STOREB" );
        } else {
            emit( "STORE " + numBytes );
        }
        
    }

    /**
     * Emite uma label para uma instrução em assemply. Essa instrução adiciona
     * um caractere de dois pontos ao fim da label e escreve o resultado em uma
     * única linha.
     */
    protected void emitLabel( String label ) {
        out.println( label + ":" );
    }

    /**
     * Emite uma representação em String para uma instrução em assemply.
     */
    protected void emit( String instruction ) {
        out.println( SPACES + instruction );
    }
    
    /**
     * Reinicia o contador de labels, permitindo a execução dos testes de 
     * unidade que não podem manter o contexto desse contador nos testes prévios.
     * 
     * @author Prof. Dr. David Buzatto
     */
    public static void resetCurrentLabelNum() {
        currentLabelNum = -1;
    }
    
}
