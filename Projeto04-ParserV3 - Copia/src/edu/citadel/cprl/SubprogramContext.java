package edu.citadel.cprl;

import edu.citadel.cprl.ast.SubprogramDecl;

/**
 * This class provides access to an enclosing subprogram context required by
 * return statements for constraint analysis and code generation.
 */
public final class SubprogramContext {

    private SubprogramDecl subprogDecl;

    public SubprogramContext() {
        subprogDecl = null;
    }

    /**
     * Retorna a declaração de subprograma que está sendo atualmente analisada.
     * Retorna null se não houver tal procedimento.
     */
    public SubprogramDecl getSubprogramDecl() {
        return subprogDecl;
    }

    /**
     * Chamado no início da análise de uma declaração de subprograma.
     */
    public void beginSubprogramDecl( SubprogramDecl subprogDecl ) {
        this.subprogDecl = subprogDecl;
    }

    /**
     * Chamado ao se terminar a análise de uma declaração de subprograma.
     */
    public void endSubprogramDecl() {
        this.subprogDecl = null;
    }
    
}
