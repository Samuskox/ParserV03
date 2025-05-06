package edu.citadel.cprl;

import edu.citadel.cprl.ast.LoopStmt;
import java.util.Stack;

/**
 * This class provides access to an enclosing loop context required by exit
 * statements for constraint analysis code generation.
 */
public final class LoopContext {

    private Stack<LoopStmt> loopStack;

    public LoopContext() {
        loopStack = new Stack<LoopStmt>();
    }

    /**
     * Retorna a instrução de laço que está sendo atualmente analisada.
     * Retorna null se não houver uma instrução de laço.
     */
    public LoopStmt getLoopStmt() {
        if ( !loopStack.empty() ) {
            return loopStack.peek();
        } else {
            return null;
        }
    }

    /**
     * Chamado no início da análise de uma instrução de laço.
     */
    public void beginLoop( LoopStmt stmt ) {
        loopStack.push( stmt );
    }

    /**
     * Chamado ao se terminar a análise de uma instrução de laço.
     */
    public void endLoop() {
        if ( !loopStack.empty() ) {
            loopStack.pop();
        }
    }
    
}
