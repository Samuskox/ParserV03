/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.cprl.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.LocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.filter.FisheyeTreeFilter;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.controls.FocusControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Tree;
import prefuse.data.tuple.TupleSet;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.sort.TreeDepthItemSorter;

/**
 * Demonstration of a node-link tree viewer
 *
 * @version 1.0
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * 
 * Modificado por Prof. Dr. David Buzatto
 */
public class TreeView extends Display {

    private static final String tree = "tree";
    private static final String treeNodes = "tree.nodes";
    private static final String treeEdges = "tree.edges";
    private LabelRenderer nodeRenderer;
    private EdgeRenderer edgeRenderer;
    private String label = "label";
    private int orientation = Constants.ORIENT_TOP_BOTTOM;

    private Color corRaiz;
    private Color corGalho;
    private Color corFolha;

    public TreeView( Tree t, String label ) {

        super( new Visualization() );
        this.label = label;

        corRaiz = new Color( 255, 139, 115 );
        corGalho = new Color( 255, 207, 115 );
        corFolha = new Color( 95, 192, 206 );

        m_vis.add( tree, t );

        nodeRenderer = new LabelRenderer( this.label );
        nodeRenderer.setHorizontalPadding( 5 );
        nodeRenderer.setVerticalPadding( 5 );
        nodeRenderer.setRenderType( AbstractShapeRenderer.RENDER_TYPE_FILL );
        nodeRenderer.setHorizontalAlignment( Constants.LEFT );
        nodeRenderer.setRoundedCorner( 8, 8 );
        edgeRenderer = new EdgeRenderer( Constants.EDGE_TYPE_LINE );

        DefaultRendererFactory rf = new DefaultRendererFactory( nodeRenderer );
        rf.add( new InGroupPredicate( treeEdges ), edgeRenderer );
        m_vis.setRendererFactory( rf );

        // colors
        ItemAction nodeColor = new NodeColorAction( treeNodes );

        ItemAction textColor = new ColorAction( treeNodes,
                VisualItem.TEXTCOLOR, ColorLib.rgb( 0, 0, 0 ) );
        m_vis.putAction( "textColor", textColor );

        ItemAction edgeColor = new EdgeColorAction( treeEdges );

        // quick repaint
        ActionList repaint = new ActionList();
        repaint.add( nodeColor );
        repaint.add( new RepaintAction() );
        m_vis.putAction( "repaint", repaint );

        // full paint
        ActionList fullPaint = new ActionList();
        fullPaint.add( nodeColor );
        m_vis.putAction( "fullPaint", fullPaint );

        // animate paint change
        ActionList animatePaint = new ActionList( 400 );
        animatePaint.add( new ColorAnimator( treeNodes ) );
        animatePaint.add( new RepaintAction() );
        m_vis.putAction( "animatePaint", animatePaint );

        // create the tree layout action
        NodeLinkTreeLayout treeLayout = new NodeLinkTreeLayout( tree,
                orientation, 50, 10, 8 );
        treeLayout.setLayoutAnchor( new Point2D.Double( 25, 300 ) );
        m_vis.putAction( "treeLayout", treeLayout );

        CollapsedSubtreeLayout subLayout =
                new CollapsedSubtreeLayout( tree, orientation );
        m_vis.putAction( "subLayout", subLayout );

        AutoPanAction autoPan = new AutoPanAction();

        // create the filtering and layout
        ActionList filter = new ActionList();
        filter.add( new FisheyeTreeFilter( tree, 30 ) );
        filter.add( new FontAction( treeNodes, FontLib.getFont( "Tahoma", 16 ) ) );
        filter.add( treeLayout );
        filter.add( subLayout );
        filter.add( textColor );
        filter.add( nodeColor );
        filter.add( edgeColor );
        m_vis.putAction( "filter", filter );

        // animated transition
        ActionList animate = new ActionList( 1000 );
        animate.setPacingFunction( new SlowInSlowOutPacer() );
        animate.add( autoPan );
        animate.add( new QualityControlAnimator() );
        animate.add( new VisibilityAnimator( tree ) );
        animate.add( new LocationAnimator( treeNodes ) );
        animate.add( new ColorAnimator( treeNodes ) );
        animate.add( new RepaintAction() );
        m_vis.putAction( "animate", animate );
        m_vis.alwaysRunAfter( "filter", "animate" );

        // create animator for orientation changes
        ActionList orient = new ActionList( 2000 );
        orient.setPacingFunction( new SlowInSlowOutPacer() );
        orient.add( autoPan );
        orient.add( new QualityControlAnimator() );
        orient.add( new LocationAnimator( treeNodes ) );
        orient.add( new RepaintAction() );
        m_vis.putAction( "orient", orient );

        // initialize the display
        setSize( 700, 600 );
        setItemSorter( new TreeDepthItemSorter() );
        addControlListener( new ZoomToFitControl() );
        addControlListener( new ZoomControl() );
        addControlListener( new WheelZoomControl() );
        addControlListener( new PanControl() );
        addControlListener( new FocusControl( 1, "filter" ) );
        
        // filter graph and perform layout
        setOrientation( orientation );
        m_vis.run( "filter" );
        
    }

    public void setOrientation( int orientation ) {
        NodeLinkTreeLayout rtl = ( NodeLinkTreeLayout ) m_vis.getAction( "treeLayout" );
        CollapsedSubtreeLayout stl = ( CollapsedSubtreeLayout ) m_vis.getAction( "subLayout" );
        switch ( orientation ) {
            case Constants.ORIENT_LEFT_RIGHT:
                nodeRenderer.setHorizontalAlignment( Constants.LEFT );
                edgeRenderer.setHorizontalAlignment1( Constants.RIGHT );
                edgeRenderer.setHorizontalAlignment2( Constants.LEFT );
                edgeRenderer.setVerticalAlignment1( Constants.CENTER );
                edgeRenderer.setVerticalAlignment2( Constants.CENTER );
                break;
            case Constants.ORIENT_RIGHT_LEFT:
                nodeRenderer.setHorizontalAlignment( Constants.RIGHT );
                edgeRenderer.setHorizontalAlignment1( Constants.LEFT );
                edgeRenderer.setHorizontalAlignment2( Constants.RIGHT );
                edgeRenderer.setVerticalAlignment1( Constants.CENTER );
                edgeRenderer.setVerticalAlignment2( Constants.CENTER );
                break;
            case Constants.ORIENT_TOP_BOTTOM:
                nodeRenderer.setHorizontalAlignment( Constants.CENTER );
                edgeRenderer.setHorizontalAlignment1( Constants.CENTER );
                edgeRenderer.setHorizontalAlignment2( Constants.CENTER );
                edgeRenderer.setVerticalAlignment1( Constants.BOTTOM );
                edgeRenderer.setVerticalAlignment2( Constants.TOP );
                break;
            case Constants.ORIENT_BOTTOM_TOP:
                nodeRenderer.setHorizontalAlignment( Constants.CENTER );
                edgeRenderer.setHorizontalAlignment1( Constants.CENTER );
                edgeRenderer.setHorizontalAlignment2( Constants.CENTER );
                edgeRenderer.setVerticalAlignment1( Constants.TOP );
                edgeRenderer.setVerticalAlignment2( Constants.BOTTOM );
                break;
            default:
                throw new IllegalArgumentException(
                        "Unrecognized orientation value: " + orientation );
        }
        this.orientation = orientation;
        rtl.setOrientation( orientation );
        stl.setOrientation( orientation );
    }

    public int getOrientation() {
        return orientation;
    }

    private class AutoPanAction extends Action {

        private Point2D m_start = new Point2D.Double();
        private Point2D m_end = new Point2D.Double();
        private Point2D m_cur = new Point2D.Double();
        private int m_bias = 150;

        public void run( double frac ) {
            
            TupleSet ts = m_vis.getFocusGroup( Visualization.FOCUS_ITEMS );
            if ( ts.getTupleCount() == 0 ) {
                return;
            }

            if ( frac == 0.0 ) {
                int xbias = 0, ybias = 0;
                switch ( orientation ) {
                    case Constants.ORIENT_LEFT_RIGHT:
                        xbias = m_bias;
                        break;
                    case Constants.ORIENT_RIGHT_LEFT:
                        xbias = -m_bias;
                        break;
                    case Constants.ORIENT_TOP_BOTTOM:
                        ybias = m_bias;
                        break;
                    case Constants.ORIENT_BOTTOM_TOP:
                        ybias = -m_bias;
                        break;
                }

                VisualItem vi = ( VisualItem ) ts.tuples().next();
                m_cur.setLocation( getWidth() / 2, getHeight() / 2 );
                getAbsoluteCoordinate( m_cur, m_start );
                m_end.setLocation( vi.getX() + xbias, vi.getY() + ybias );
                
            } else {
                m_cur.setLocation( m_start.getX() + frac * ( m_end.getX() - m_start.getX() ),
                        m_start.getY() + frac * ( m_end.getY() - m_start.getY() ) );
                panToAbs( m_cur );
            }
        }
    }

    private class NodeColorAction extends ColorAction {

        public NodeColorAction( String group ) {
            super( group, VisualItem.FILLCOLOR );
        }

        public int getColor( VisualItem item ) {

            String tipo = item.getString( "tipo" );
            
            if ( tipo.equals( "raiz" ) ) {
                return corRaiz.getRGB();
            } else if ( tipo.equals( "galho" ) ) {
                return corGalho.getRGB();
            }else if ( tipo.equals( "folha" ) ) {
                return corFolha.getRGB();
            } else {
                return ColorLib.rgb( 255, 255, 255 );
            }

        }
        
    }

    private class EdgeColorAction extends ColorAction {

        public EdgeColorAction( String group ) {
            super( group, VisualItem.STROKECOLOR );
        }

        public int getColor( VisualItem item ) {

            item.setStroke( new BasicStroke( 1.5f ) );
            return ColorLib.rgb( 0, 0, 0 );

        }
    }

} 

