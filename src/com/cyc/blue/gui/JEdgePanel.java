/* $Id: JEdgePanel.java,v 1.4 2002/05/23 22:30:15 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * An extension of JPanel to be used as the component for a renderable edge.
 *
 * @author John Jantos
 * @date 2002/02/18
 * @version $Id: JEdgePanel.java,v 1.4 2002/05/23 22:30:15 jantos Exp $
 */

public class JEdgePanel extends JPanel {
  
  private IsRenderableEdge renderableEdge;

  public JEdgePanel(IsRenderableEdge _renderableEdge) {
    setRendererableEdge(_renderableEdge);
    setVisible(true);
    setOpaque(false);
    setLayout(null);
    validate();
    //setDebugGraphicsOptions(DebugGraphics.FLASH_OPTION);
  }

  public void setRendererableEdge(IsRenderableEdge _renderableEdge) {
    renderableEdge = _renderableEdge;
  }

  public IsRenderableEdge getRenderableEdge() {
    return renderableEdge;
  }

  public void paintComponent(Graphics _g) {
    //super.paintComponent(_g);
    renderableEdge.getRenderer().render(renderableEdge, _g);
  }

  public int getX() {
    return (int)Math.round(renderableEdge.getNormalizedXD());
  }
  public int getY() {
    return (int)Math.round(renderableEdge.getNormalizedYD());
  }
  public int getWidth() {
    return (int)Math.round(renderableEdge.getNormalizedWidthD());
  }
  public int getHeight() {
    return (int)Math.round(renderableEdge.getNormalizedHeightD());
  }


}
