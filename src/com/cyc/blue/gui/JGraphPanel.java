/* $Id: JGraphPanel.java,v 1.3 2002/05/23 22:30:15 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * An extension of JPanel to be used as the component for a renderable graph.
 *
 * @author John Jantos
 * @date 2002/02/18
 * @version $Id: JGraphPanel.java,v 1.3 2002/05/23 22:30:15 jantos Exp $
 */

public class JGraphPanel extends JPanel {

  private IsRenderableGraph rGraph;

  public JGraphPanel(IsRenderableGraph _rGraph) {
    rGraph = _rGraph;
//      setBounds(1, 1, 2, 2);
//      setBackground(new Color(220,220,240));
    setOpaque(true);
    setLayout(null);
    setVisible(true);
    setBackground(new Color(255,255,128));
    setForeground(new Color(128,255,128));
    validate();
  }
  
  //public boolean isOptimizedDrawingEnabled() { return true; }

  public void paintComponent(Graphics _g) {
    super.paintComponent(_g);
    rGraph.getRenderer().render(rGraph, _g);

  }

//    public int getX() {
//      return (int)Math.round(rGraph.getNormalizedXD());
//    }
//    public int getY() {
//      return (int)Math.round(rGraph.getNormalizedYD());
//    }
//    public int getWidth() {
//      return (int)Math.round(rGraph.getNormalizedWidthD());
//    }
//    public int getHeight() {
//      return (int)Math.round(rGraph.getNormalizedHeightD());
//    }
  
  //getComponent().setLocation((int)Math.round(_x), (int)Math.round(_y));

}
