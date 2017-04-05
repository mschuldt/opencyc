/* $Id: GenericNodeRenderer.java,v 1.5 2002/05/22 01:33:22 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import com.cyc.blue.graph.IsGraph;
import com.cyc.blue.graph.IsNode;
import com.cyc.blue.layout.IsGraphLayoutStrategy;

/**
 * A basic implementation of a node renderer.  The nodes rendered by this class
 * are boxes with the node label inside them.  The box border is colored based
 * on the node's attributes.
 *
 * @author John Jantos
 * @date 2002/04/10
 * @version $Id: GenericNodeRenderer.java,v 1.5 2002/05/22 01:33:22 jantos Exp $
 */

public class GenericNodeRenderer implements IsNodeRenderer {
  private static final boolean DEBUG = false;

  private static Font nodeFont = new Font("sanserif",Font.PLAIN, 12);

  private static BasicStroke stroke4sqbev = new BasicStroke(4,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL);
  private static BasicStroke stroke4rndrnd = new BasicStroke(4,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
  private static BasicStroke stroke4 = new BasicStroke(4);
  private static BasicStroke stroke3 = new BasicStroke(3);
  private static BasicStroke stroke2 = new BasicStroke(2);
  private static BasicStroke stroke1 = new BasicStroke(1);

  private boolean isPendingRender = true;

  ////////////////////////////////////////////////////////////////////////////////
  // IsRenderer
  public void render(IsRenderable _renderable) {
    if (DEBUG) { System.out.println("--> " + this + ".render(" + _renderable + ")"); }
    if (_renderable instanceof IsRenderableNode) {
      // do this instead of repaint since can't fully subclass AWT components and override getX(),.
      _renderable.getComponent().setLocation((int)Math.round(_renderable.getNormalizedXD()), 
					     (int)Math.round(_renderable.getNormalizedYD()));
      if (isPendingRender == true) {
	return; // avoid calling repaint if node has pending repaint request
      }
      _renderable.getComponent().repaint(); // in case the node hasn't moved(!!!!)
      renderIncidentEdges((IsRenderableNode)_renderable);
    } else {
      _renderable.getComponent().repaint();
    }
    isPendingRender = true;
  }
  
  public void render(IsRenderable _renderable, Graphics _g) {
    if (DEBUG) { System.out.println("--> " + this + ".render(" + _renderable + ", " + _g + ")"); }
    if (_renderable instanceof IsRenderableNode &&
	_g instanceof Graphics2D) {
      renderNode((IsRenderableNode)_renderable, (Graphics2D)_g);
      isPendingRender = false;
    }
  }

  public void renderNode(IsRenderableNode _rNode, Graphics2D _g) {
    //System.out.println("--> " + this + ".renderNode(" + _rNode + ", " + _g + ")");

    int NODE_TEXT_OFFSET = 4;  //todo- don't assume height and width < certain number?
    
    String nodeString = _rNode.getLabel();
    _g.setFont(nodeFont);
    // @todo: fix this nasty callback somehow?
    _rNode.setWidthHeightD(_g.getFontMetrics().stringWidth(nodeString) + 2*NODE_TEXT_OFFSET,
			   _g.getFontMetrics().getHeight() + 2*NODE_TEXT_OFFSET);
    
    Shape shape = new Rectangle(0, 0, 
				(int)Math.round(_rNode.getNormalizedWidthD())-1, 
				(int)Math.round(_rNode.getNormalizedHeightD())-1);
    _g.setPaint(Color.white);
    _g.fill(shape);

    _g.setPaint(Color.black);
    _g.drawString(nodeString, NODE_TEXT_OFFSET, Math.round(_rNode.getNormalizedHeightD()-1.5*NODE_TEXT_OFFSET));
    
    if (_rNode.isLocked()) {
      _g.setStroke(stroke2);
    } else {
      _g.setStroke(stroke1);
    }

    if (_rNode.isSelected()) {
      _g.setPaint(Color.red);
    } else if (_rNode.isFocus()) {
      _g.setPaint(Color.blue);
    } else {
      _g.setPaint(Color.black);
    }
    _g.draw(shape);
    
    //renderForces(_rNode, _g);
  }

  public void renderForces(IsRenderableNode _rNode, Graphics2D g) {
    try {
      Iterator strategiesIterator = _rNode.getLastForceStrategiesIterator();
      if (strategiesIterator != null) {
	int foo = 0;
	while (strategiesIterator.hasNext()) {
	  foo++;
	  IsGraphLayoutStrategy strategy = (IsGraphLayoutStrategy)strategiesIterator.next();
	  double dx = _rNode.getLastXForceByStrategy(strategy);
	  double dy = _rNode.getLastYForceByStrategy(strategy);
	  double centerX = _rNode.getNormalizedWidthD() / 2;
	  double centerY = _rNode.getNormalizedHeightD() / 2;
	  double endX = centerX + dx;
	  double endY = centerY + dy;
	  Shape shape = new Line2D.Double(centerX, centerY, endX, endY);
	  //g.setStroke(stroke2);
	  g.setPaint(strategy.getColor());
	  g.draw(shape);
//  	    // do math
//  	    Polygon arrow = new Polygon();
//  	    arrow.addPoint((int)Math.round(endX), (int)Math.round(endY));
//  	    double slope = Math.atan((centerX - endX) /
//  				     (centerY - endY));
//  	    double adjust = (centerY - endY) < 0 ? 1 : -1;
//  	    double arrowAngle = Math.PI / 9;
//  	    double arrowLength = 14.0;
//  	    arrow.addPoint((int)Math.round(endX + adjust * arrowLength * Math.sin(slope + arrowAngle)),
//  			   (int)Math.round(endY + adjust * arrowLength * Math.cos(slope + arrowAngle)));
//  	    arrow.addPoint((int)Math.round(endX + adjust * (arrowLength * 0.7) * Math.sin(slope)),
//  			   (int)Math.round(endY + adjust * (arrowLength * 0.7) * Math.cos(slope)));
//  	    arrow.addPoint((int)Math.round(endX + adjust * arrowLength * Math.sin(slope - arrowAngle)),
//  			   (int)Math.round(endY + adjust * arrowLength * Math.cos(slope - arrowAngle)));
//  	    g.setPaint(Color.blue);
//  	    g.fill(arrow);
	}
      }  
    } catch (ConcurrentModificationException e) {
      System.err.println(e);
    }
  }

  private void renderIncidentEdges(IsRenderableNode _rNode) {
    //System.out.println("--> " + this + ".renderIncidentEdges()");
    if (_rNode.getRenderableGraph() != null) {
      Iterator edgesIterator = _rNode.getRenderableGraph().incidentEdgesIterator(_rNode);
      if (edgesIterator != null) {
 	while (edgesIterator.hasNext()) {
 	  IsRenderableEdge edge = (IsRenderableEdge)edgesIterator.next();
	  //  	  System.out.println(this + ".renderIncidentEdges(): checking " + edge);
	  //  	  if (redge.isVisible() == true) { 
	  //System.out.println("!!! " + this + ".renderIncidentEdges(): rendering " + edge);
	  edge.render(); // @todo: should be repaint??
	  //  	  }
 	}
      }
    } else {
      System.err.println(this + ".repaintIncidentEdges(): error: getGraph() failed.");
    }
  }

}
