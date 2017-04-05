/* $Id: GenericEdgeRenderer.java,v 1.4 2002/05/22 01:33:22 jantos Exp $
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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import com.cyc.blue.graph.IsEdge;

/**
 * A basic implementation of an edge renderer.  The edges rendered by this class
 * connect two nodes with a straight line that has an arrow in the middle, and
 * some text sticking out to the right of the arrow.
 *
 * @author John Jantos
 * @date 2002/04/10
 * @version $Id: GenericEdgeRenderer.java,v 1.4 2002/05/22 01:33:22 jantos Exp $
 */

public class GenericEdgeRenderer implements IsEdgeRenderer {
  private static final boolean DEBUG = false;
  private static final Font FONT = new Font("sanserif", Font.PLAIN, 11);
  
  private static final Stroke THIN_STROKE = new BasicStroke();
  private static final Stroke MEDIUM_STROKE = new BasicStroke(2);
  private static final Stroke THICK_STROKE = new BasicStroke(3);
  private static final Stroke THICKEST_STROKE = new BasicStroke(4);

  ////////////////////////////////////////////////////////////////////////////////
  // IsRenderer
  public void render(IsRenderable _renderable) {
    _renderable.getComponent().setBounds((int)Math.round(_renderable.getNormalizedXD()), 
					 (int)Math.round(_renderable.getNormalizedYD()), 
					 (int)Math.round(_renderable.getNormalizedWidthD()), 
					 (int)Math.round(_renderable.getNormalizedHeightD()));
    // the above should be enough, but just in case:
    _renderable.getComponent().repaint();
  }

  public void render(IsRenderable _renderable, Graphics _g) {
    if (_renderable instanceof IsRenderableEdge &&
	_g instanceof Graphics2D) {
      renderEdge((IsRenderableEdge)_renderable, (Graphics2D)_g);
    }
  }

  

  public void renderEdge(IsRenderableEdge _rEdge, Graphics2D g) {
    //System.out.println("BBBBBBBBB " + g);

    if (DEBUG) {
      Shape shape1 = new Rectangle(0, 0, 
				   (int)Math.round(_rEdge.getNormalizedWidthD()) - 1, 
				   (int)Math.round(_rEdge.getNormalizedHeightD()) - 1);
      g.setPaint(Color.green);
      g.draw(shape1);
    }
    
    // call things like head.getAttachmentPoint(angle)?
    Point2D.Double tailPoint = (_rEdge.getRenderableTail()).getAttachmentPoint(_rEdge.getRenderableHead());
    tailPoint = new Point2D.Double(tailPoint.getX() - _rEdge.getNormalizedXD(),
				   tailPoint.getY() - _rEdge.getNormalizedYD());
    Point2D.Double headPoint = (_rEdge.getRenderableHead()).getAttachmentPoint(_rEdge.getRenderableTail());
    headPoint = new Point2D.Double(headPoint.getX() - _rEdge.getNormalizedXD(), 
				   headPoint.getY() - _rEdge.getNormalizedYD());
    Shape shape = new Line2D.Double(tailPoint, headPoint);
//      if (BlueCycAccess.genls.equals(getRelation())) {
//        g.setPaint(Color.green.darker());
//      } else if (BlueCycAccess.isa.equals(getRelation())) {
//        g.setPaint(Color.yellow.darker());
//      } else {
//        g.setPaint(Color.black);
//      }
    g.setPaint(Color.black);

    if (_rEdge.getGraph() != null) {
      int edgeWeight = Math.min(_rEdge.getGraph().incidentEdgesCount(_rEdge.getHead()),
				_rEdge.getGraph().incidentEdgesCount(_rEdge.getTail()));
      if (edgeWeight < 5) {
	g.setStroke(THIN_STROKE);
      } else if (edgeWeight < 10) {
	g.setStroke(MEDIUM_STROKE);
      } else if (edgeWeight < 20) {
	g.setStroke(THICK_STROKE);
      } else {
	g.setStroke(THICKEST_STROKE);
      }
    } else {
      g.setStroke(THIN_STROKE);
    }

    g.draw(shape);
    // do math
    double midPointX = (headPoint.getX() + tailPoint.getX()) / 2;
    double midPointY = (headPoint.getY() + tailPoint.getY()) / 2;
    Polygon arrow = new Polygon();
    arrow.addPoint((int)Math.round(midPointX), (int)Math.round(midPointY));
    double slope = Math.atan((headPoint.getX() - tailPoint.getX()) /
			     (headPoint.getY() - tailPoint.getY()));
    double adjust = (tailPoint.getY() - headPoint.getY()) <= 0 ? 1 : -1;
    double arrowAngle = Math.PI / 9;
    double arrowLength = 14.0;
    arrow.addPoint((int)Math.round(midPointX + adjust * arrowLength * Math.sin(slope + arrowAngle)),
		   (int)Math.round(midPointY + adjust * arrowLength * Math.cos(slope + arrowAngle)));
    arrow.addPoint((int)Math.round(midPointX + adjust * (arrowLength * 0.7) * Math.sin(slope)),
		   (int)Math.round(midPointY + adjust * (arrowLength * 0.7) * Math.cos(slope)));
    arrow.addPoint((int)Math.round(midPointX + adjust * arrowLength * Math.sin(slope - arrowAngle)),
		   (int)Math.round(midPointY + adjust * arrowLength * Math.cos(slope - arrowAngle)));
    g.fill(arrow);
    
    
    // show label
    String label = _rEdge.getRelation().toString();
    if (label != null) {
      double angle = - Math.PI/2 * adjust - slope;
      if (angle < -Math.PI) { angle += 2*Math.PI; }
      else if (angle > Math.PI) { angle -= 2*Math.PI; }
    
    // text is always rightside up
//      if (angle < Math.PI/2) { angle += Math.PI; }
//      else if (angle > Math.PI && angle < 3*Math.PI/4 ) { angle += Math.PI; }
//      angle += Math.PI;
      // text is always in direction of arrow
      //if (Math.sin(angle) >  0) { angle -= Math.PI; }
      AffineTransform rotate = AffineTransform.getRotateInstance(angle);
      Font thisFont = FONT; //FONT.deriveFont(rotate);
      g.setFont(thisFont);
      g.setPaint(Color.black);
      g.drawString(label, (int)Math.round(midPointX), (int)Math.round(midPointY));
    }
//      if (DEBUG) {
//        shape = new Rectangle(0, 0, getWidth()-1, getHeight()-1);
//        //g.setPaint(Color.yellow);
//        //g.fill(shape);
//        // g.setColor(Color.black);
//        //g.fillRect(x, y, x+w, y+h);
//        g.setPaint(Color.red);
//        g.draw(shape);
//      }
    
  }  

}
