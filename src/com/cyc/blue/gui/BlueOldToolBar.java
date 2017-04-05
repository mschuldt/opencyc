/* $Id: BlueOldToolBar.java,v 1.8 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JToolBar;
import com.cyc.blue.layout.JGraphLayoutManagerThread;

/**
 * A hacked up toolbar to add a few buttons.  This class was meant to be as
 * temporary as possible and stand in for a more general solution.
 *
 * @author John Jantos
 * @date 2001/10/17
 * @version $Id: BlueOldToolBar.java,v 1.8 2002/05/23 22:30:14 jantos Exp $
 */

public class BlueOldToolBar extends JToolBar {
  JButton random;
  JButton jiggle;
  JButton start;
  JButton stop;
  JButton clear;
  IsRenderableGraph renderableGraph;
  JGraphLayoutManagerThread renderableGraphLayoutManagerThread;

  public BlueOldToolBar(IsRenderableGraph _renderableGraph, JGraphLayoutManagerThread _renderableGraphLayoutManagerThread)  {
    super();
    renderableGraph = _renderableGraph;
    renderableGraphLayoutManagerThread = _renderableGraphLayoutManagerThread;
    random = new JButton("Random");
    //random.setToolTipText("Random the graph");
    random.addActionListener(new ActionListener() {
  	public void actionPerformed(ActionEvent e) {
	  Iterator nodes = renderableGraph.nodesIterator();
	  while (nodes.hasNext()) {
	    IsRenderableNode rNode = (IsRenderableNode)nodes.next();
	    rNode.setLocked(false);
	    rNode.setXYD((Math.random()-0.5)*1000 + rNode.getXD(),
			 (Math.random()-0.5)*1000 + rNode.getYD());
	  }
	  //renderableGraph.render();
  	}
      });
    jiggle = new JButton("Jiggle");
    //jiggle.setToolTipText("Jiggle the graph");
    jiggle.addActionListener(new ActionListener() {
  	public void actionPerformed(ActionEvent e) {
	  Iterator nodes = renderableGraph.nodesIterator();
	  while (nodes.hasNext()) {
	    IsRenderableNode rNode = (IsRenderableNode)nodes.next();
	    if (!rNode.isLocked()) {
	      rNode.setXYD((Math.random()-0.5)*100 + rNode.getXD(),
			   (Math.random()-0.5)*100 + rNode.getYD());
	    }
	  }
	  //renderableGraph.render();
  	}
      });
    start = new JButton("Auto Layout");
    start.addActionListener(new ActionListener() {
  	public void actionPerformed(ActionEvent e) {
	  renderableGraphLayoutManagerThread.setPaused(false);
  	}
      });
    stop = new JButton("Hold Still");
    stop.addActionListener(new ActionListener() {
  	public void actionPerformed(ActionEvent e) {
	  renderableGraph.render();
	  renderableGraphLayoutManagerThread.setPaused(true);
  	}
      });
    clear = new JButton("Clear");
    clear.addActionListener(new ActionListener() {
  	public void actionPerformed(ActionEvent e) {
	  renderableGraph.clear();
	  renderableGraph.render();
  	}
      });
    
    add(random);
    add(jiggle);
    add(start);
    add(stop);
    //add(clear);
    //setBackground(new Color(255,255,128));
    //setBackground(new Color(230,230,230));
    setFloatable(false);
  }

}
