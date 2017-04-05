/* $Id: GenericGraphStatusBar.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.cyc.blue.event.BlueEventService;
import com.cyc.blue.event.EventFilter_AllowGraphCommandEvents;
import com.cyc.blue.event.GraphCommandEvent;
import com.cyc.blue.event.GraphCommandEvent_Begin;
import com.cyc.blue.event.GraphCommandEvent_End;
import com.cyc.blue.event.GraphCommandEvent_Fail;
import com.cyc.blue.graph.IsGraph;
import com.cyc.event.IsEvent;
import com.cyc.event.IsEventService;
import com.cyc.event.IsEventServiceSubscriber;

/**
 * An implementation of a graph status bar that is also an even service
 * subscriber that listens to events published by the graph command executor.
 *
 * @author John Jantos
 * @date 2002/04/26
 * @version $Id: GenericGraphStatusBar.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 */

public class GenericGraphStatusBar extends JPanel implements IsGraphStatusBar, IsEventServiceSubscriber {
  private static final boolean DEBUG = false;

  JLabel messageLabel = new JLabel("Welcome to the BlueGrapher.");

  public GenericGraphStatusBar(IsGraph _graph) {
    setGraph(_graph);
    setVisible(true);
    setOpaque(true);
    setLayout(new BorderLayout());
    //setSize(100,100); // non zero initial size.

    add(messageLabel, BorderLayout.WEST);
    validate();

    IsEventService blueEventService = BlueEventService.current();
    if (blueEventService != null) { 
      blueEventService.subscribe(GraphCommandEvent.class, 
				 new EventFilter_AllowGraphCommandEvents(getGraph()),
				 this);
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // IsComponent

  public void setComponent(Component _component) { }
  public Component getComponent() { return this; }

  ////////////////////////////////////////////////////////////////////////////////
  // IsGraphStatusBar
  
  private IsGraph graph;
  public void setGraph(IsGraph _graph) { graph = _graph; }
  public IsGraph getGraph() { return graph; }

  private Map objectToMessageListMap = new HashMap();

  public void addObjectMessage(Object _object, String _message) {
    messageLabel.setText(_message);
  }
    
  public void addObjectAlert(Object _object, String _alert) {
    messageLabel.setText(_alert);
  }

  public void addObject(Object _object) {
  }

  ////////////////////////////////////////////////////////////////////////////////
  // IsEventServiceSubscriber

  public void inform(IsEvent _event) {
    if (DEBUG) { System.out.println("-->" + this + ".inform(" + _event + ")"); }
    if (_event instanceof GraphCommandEvent &&
	((GraphCommandEvent)_event).getGraph() == getGraph()) { // somewhat redundant since we filtered
      // recast
      GraphCommandEvent graphCommandEvent = (GraphCommandEvent)_event;
      if (graphCommandEvent instanceof GraphCommandEvent_Begin) {
	addObjectMessage(graphCommandEvent.getCommand(), "Running: " + graphCommandEvent.getCommand().toString());
      } else if (graphCommandEvent instanceof GraphCommandEvent_End) {
	addObjectMessage(null, "Ready.");
      } else if (graphCommandEvent instanceof GraphCommandEvent_Fail) {
	addObjectMessage(null, "Failure!");
      }
    }
  }

}
