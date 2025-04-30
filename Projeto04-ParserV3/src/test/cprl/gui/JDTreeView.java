/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.cprl.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import prefuse.Constants;
import prefuse.data.Tree;
import prefuse.data.io.TreeMLReader;

/**
 *
 * @author Prof. Dr. David Buzatto
 */
public class JDTreeView extends javax.swing.JDialog {

    private TreeView tView;
    
    private final Color BACKGROUND = Color.WHITE;
    private final Color FOREGROUND = Color.BLACK;
    
    public JDTreeView( JFrame pai, boolean modal ) {

        super( pai, modal );
        
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        setTitle( "Generated AST from Parsing" );
        
        setSize( Toolkit.getDefaultToolkit().getScreenSize() );

        Tree t = null;
        try {
            t = ( Tree ) new TreeMLReader().readGraph( "ast.xml" );
        } catch ( Exception exc ) {
            exc.printStackTrace();
        }

        tView = new TreeView( t, "nome" );
        tView.setBackground( BACKGROUND );
        tView.setForeground( FOREGROUND );
        tView.setSize( getWidth(), getHeight() );

        add( tView, BorderLayout.CENTER );

        JPanel painel = new JPanel();

        JRadioButton radioEsquerdaDireita = new JRadioButton( new OrientAction( Constants.ORIENT_LEFT_RIGHT ) );
        radioEsquerdaDireita.setText( "Left/Right" );
        JRadioButton radioCimaBaixo = new JRadioButton( new OrientAction( Constants.ORIENT_TOP_BOTTOM ) );
        radioCimaBaixo.setText( "Up/Down" );

        radioCimaBaixo.setSelected( true );

        ButtonGroup bg = new ButtonGroup();
        bg.add( radioEsquerdaDireita );
        bg.add( radioCimaBaixo );

        painel.add( radioEsquerdaDireita );
        painel.add( radioCimaBaixo );
        
        add( painel, BorderLayout.NORTH );

    }

    private class OrientAction extends AbstractAction {

        private int orientation;

        public OrientAction( int orientation ) {
            this.orientation = orientation;
        }

        @Override
        public void actionPerformed( ActionEvent evt ) {
            tView.setOrientation( orientation );
            tView.getVisualization().cancel( "orient" );
            tView.getVisualization().run( "treeLayout" );
            tView.getVisualization().run( "orient" );
        }
    }

}
