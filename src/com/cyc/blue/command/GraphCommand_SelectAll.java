/* $Id: GraphCommand_SelectAll.java,v 1.3 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.command;

import java.util.*;
import java.io.*;
import org.opencyc.api.*;
import org.opencyc.cycobject.*;
import com.cyc.blue.command.*;
import java.awt.Component;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import com.cyc.blue.command.GenericGraphCommand;
import com.cyc.blue.command.GraphCommandExecutor;
import com.cyc.blue.command.IsCommand;
import com.cyc.blue.command.IsCommandable;
import com.cyc.blue.command.IsCommandExecutor;
import com.cyc.blue.command.IsCommandManager;
import com.cyc.blue.command.IsCommandProcessor;
import com.cyc.blue.command.IsGraphCommand;
import com.cyc.blue.command.IsGraphCommandExecutor;
import com.cyc.blue.graph.IsGraph;
import com.cyc.blue.graph.IsNode;
import com.cyc.blue.graph.*;
import com.cyc.blue.gui.*;
import com.cyc.blue.gui.IsGraphFrame;
import com.cyc.blue.gui.IsRenderableGraph;
import org.opencyc.cycobject.CycList;

/**
 * Select all visible nodes in graph.
 *
 * @author John Jantos
 * @date 2002/04/29
 * @version $Id: GraphCommand_SelectAll.java,v 1.3 2002/05/23 22:30:14 jantos Exp $
 */

public class GraphCommand_SelectAll extends GenericGraphCommand {
  private static final boolean DEBUG = false;
  private Set previousNodeSelection = new HashSet();
  
  public GraphCommand_SelectAll() {
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsCommand

  public void processInternal(IsCommandManager _commandManager) {
    if (DEBUG) { System.out.println("TRACE: GraphCommand_SelectAll[" + this + "].process[IsCommandManager](" + _commandManager + ")"); }
    process(_commandManager.getCommandable());
  }

  public void processInternal(IsApplicationFrame _renderableGraph) {
    if (DEBUG) { System.out.println("TRACE: GraphCommand_SelectAll[" + this + "].process[IsApplicationFrame](" + _renderableGraph + ")"); }
    process((IsGraphFrame)_renderableGraph.getActiveInternalFrame());
  }

  public void processInternal(IsRenderableGraph _renderableGraph) {
    if (DEBUG) { System.out.println("TRACE: GraphCommand_SelectAll[" + this + "].process[IsRenderableGraph](" + _renderableGraph + ")"); }
    process(_renderableGraph.getGraphFrame());
  }
  
  public void processInternal(IsGraphFrame _graphFrame) {
    
    if (DEBUG) { System.out.println("TRACE: GraphCommand_SelectAll[" + this + "].process(" + _graphFrame + ")"); }

    if (getGraphFrame() == null) { setGraphFrame(_graphFrame); }
    if (getApplicationFrame() == null) { setApplicationFrame(_graphFrame.getApplicationFrame()); }
    if (getGraph() == null) { setGraph(_graphFrame.getGraph()); }
    if (getRenderableGraph() == null) { setRenderableGraph(_graphFrame.getRenderableGraph()); }

    if (_graphFrame instanceof IsCommandable) {
      // can queue
      IsCommandManager commandManager = _graphFrame.getCommandManager();
      if (commandManager != null) {
	commandManager.queueCommand(this);
      } else {
	System.err.println("GraphCommand_SelectAll[" + this + "].processInternal(" + _graphFrame + "): no commandManager found.  Command not queued."); 
      }      //System.out.println("QUEUEING~~~~!");
    } else {
      // error: need IsCommandable graph frame
      JOptionPane.showInternalMessageDialog(_graphFrame.getComponent(),
					    "Error processing GraphCommand_SelectAll request", 
					    "IsGraphFrame instanceof IsCommandable == false",
					    JOptionPane.ERROR_MESSAGE); 
    }
  }

  public Object execute(IsCommandExecutor _commandExecutor) {
    if (DEBUG) System.out.println("GraphCommand_SelectAll.execute(" + _commandExecutor + ")");
    if (_commandExecutor instanceof IsGraphCommandExecutor) {
      IsGraphCommandExecutor graphCommandExecutor = (IsGraphCommandExecutor)_commandExecutor;
      if (getRenderableGraph() != null) {
	Iterator nodesIterator = getRenderableGraph().nodesIterator();
	if (nodesIterator != null) {
	  while (nodesIterator.hasNext()) {
	    IsRenderableNode renderableNode = (IsRenderableNode)nodesIterator.next();
	    if (renderableNode.isSelected()) {
	      previousNodeSelection.add(renderableNode);
	    } else {
	      renderableNode.setSelected(true);
	    }
	  }
	}
      } else {
	System.err.println("ERROR: GraphCommand_SelectAll.execute(" + _commandExecutor + "): getRenderableGraph() was null!");
      }
    }
    return super.execute(_commandExecutor);
  }
  
  public boolean unexecute(IsCommandExecutor _commandExecutor) {
    if (DEBUG) System.out.println("GraphCommand_SelectAll.unexecute(" + _commandExecutor + ")");
    if (_commandExecutor instanceof IsGraphCommandExecutor) {
      IsGraphCommandExecutor graphCommandExecutor = (IsGraphCommandExecutor)_commandExecutor;
      if (getRenderableGraph() != null) {
	Iterator nodesIterator = getRenderableGraph().nodesIterator();
	if (nodesIterator != null) {
	  while (nodesIterator.hasNext()) {
	    IsRenderableNode renderableNode = (IsRenderableNode)nodesIterator.next();
	    if (!previousNodeSelection.contains(renderableNode)) {
	      renderableNode.setSelected(false);
	    }
	  }
	}
      } else {
	System.err.println("ERROR: GraphCommand_SelectAll.unexecute(" + _commandExecutor + "): getRenderableGraph() was null!");
      }
    }
    return super.unexecute(_commandExecutor);
  }
  
  public Object reexecute(IsCommandExecutor _commandExecutor) {
    if (DEBUG) System.out.println("GraphCommand_SelectAll.reexecute(" + _commandExecutor + ")");
    if (_commandExecutor instanceof IsGraphCommandExecutor) {
      IsGraphCommandExecutor graphCommandExecutor = (IsGraphCommandExecutor)_commandExecutor;
      if (getRenderableGraph() != null) {
	Iterator nodesIterator = getRenderableGraph().nodesIterator();
	if (nodesIterator != null) {
	  while (nodesIterator.hasNext()) {
	    IsRenderableNode renderableNode = (IsRenderableNode)nodesIterator.next();
	    if (renderableNode.isSelected()) {
	      //previousNodeSelection.add(renderableNode); //??
	    } else {
	      renderableNode.setSelected(true);
	    }
	  }
	}
      } else {
	System.err.println("ERROR: GraphCommand_SelectAll.reexecute(" + _commandExecutor + "): getRenderableGraph() was null!");
      }
    }
    return super.reexecute(_commandExecutor);
  }


  ////////////////////////////////////////////////////////////////////////////////


}
