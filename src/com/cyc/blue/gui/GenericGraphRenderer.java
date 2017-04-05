/* $Id: GenericGraphRenderer.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;

/**
 * A basic implementation of a graph renderer.  Since the nodes and edges are
 * subcomponents this class just draws a white box for the background.
 *
 * @author John Jantos
 * @date 2002/04/10
 * @version $Id: GenericGraphRenderer.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 */

public class GenericGraphRenderer implements IsRenderer {

  ////////////////////////////////////////////////////////////////////////////////
  // IsRenderer

  public void render(IsRenderable _rGraph) {
    _rGraph.getComponent().repaint();
  }

  public void render(IsRenderable _rGraph, Graphics _g) {
    if (_rGraph instanceof IsRenderableGraph &&
	_g instanceof Graphics2D) {
      renderGraph((IsRenderableGraph)_rGraph, (Graphics2D)_g);
    }
  }

  ////////////////////////////////////////////////////////////////////////////////

  public void renderGraph(IsRenderableGraph _rGraph, Graphics2D _g) {
    Shape shape = new Rectangle(0, 0,
  				(int)Math.round(_rGraph.getNormalizedWidthD() - 1), 
  				(int)Math.round(_rGraph.getNormalizedHeightD() - 1));
    _g.setPaint(Color.white);
    _g.fill(shape);
    
  }

}
