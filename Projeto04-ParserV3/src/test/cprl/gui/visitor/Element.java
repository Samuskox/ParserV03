/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.cprl.gui.visitor;

/**
 * Um elemento.
 * 
 * @author Prof. Dr. David Buzatto
 */
public interface Element {
    
    public void accept( Visitor v );
    
}
