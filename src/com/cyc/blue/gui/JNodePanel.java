/* $Id: JNodePanel.java,v 1.4 2002/05/23 22:30:15 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * An extension of JPanel to be used as the component for a renderable node.
 *
 * @author John Jantos
 * @date 2002/02/18
 * @version $Id: JNodePanel.java,v 1.4 2002/05/23 22:30:15 jantos Exp $
 */

public class JNodePanel extends JPanel {

  private IsRenderableNode renderableNode;

  public JNodePanel(IsRenderableNode _renderableNode) {
    setRendererableNode(_renderableNode);
    setVisible(false);
    setOpaque(false);
    setLayout(null);
    setSize(1,1); // non zero initial size.
    validate();
  }

  public void setRendererableNode(IsRenderableNode _renderableNode) {
    renderableNode = _renderableNode;
  }
  public IsRenderableNode getRenderableNode() {
    return renderableNode;
  }

  public void paintComponent(Graphics _g) {
//      setBounds(1, 1, 2, 2);
//      validate();
    super.paintComponent(_g);
    getRenderableNode().getRenderer().render(getRenderableNode(), _g);
  }
  public int getX() {
    return (int)Math.round(getRenderableNode().getNormalizedXD());
  }
  public int getY() {
    return (int)Math.round(getRenderableNode().getNormalizedYD());
  }
  public int getWidth() {
    return (int)Math.round(getRenderableNode().getNormalizedWidthD());
  }
  public int getHeight() {
    return (int)Math.round(getRenderableNode().getNormalizedHeightD());
  }

  public void setLocation(int _x, int _y) {
    //System.out.println("--> " + this + ".setLocation(" + _x + ", " + _y + ")"); 
    super.setLocation(_x, _y);
  }
}
