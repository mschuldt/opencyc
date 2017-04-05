/* $Id: CycGraphCommand_AddEdge.java,v 1.5 2002/05/23 22:35:04 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.cyc;

import java.awt.Component;
import javax.swing.JOptionPane;
import com.cyc.blue.command.GraphCommandExecutor;
import com.cyc.blue.command.IsCommand;
import com.cyc.blue.command.IsCommandable;
import com.cyc.blue.command.IsCommandExecutor;
import com.cyc.blue.command.IsCommandManager;
import com.cyc.blue.command.IsGraphCommandExecutor;
import com.cyc.blue.graph.IsEdge;
import com.cyc.blue.graph.IsGraph;
import com.cyc.blue.graph.IsNode;
import com.cyc.blue.gui.IsApplicationFrame;
import com.cyc.blue.gui.IsGraphFrame;
import com.cyc.blue.gui.IsRenderableGraph;
import org.opencyc.cycobject.CycFort;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycSymbol;

/**
 * A cyc graph command that adds an edge to a cyc graph.
 *
 * @author John Jantos
 * @date 2001/08/15
 * @version $Id: CycGraphCommand_AddEdge.java,v 1.5 2002/05/23 22:35:04 jantos Exp $
 */

public class CycGraphCommand_AddEdge extends CycGraphCommand {
  private static final boolean DEBUG = false;

  private boolean isInteractive = false;

  private IsGraphFrame graphFrame;
  private IsApplicationFrame applicationFrame;
  private IsGraph graph;

  //CommandExecutionState executionState = CommandExecutionState.UNEXECUTED;
  boolean isReexecuted = false;
  
  CycFort pred;
  CycFort tailNodeCore;
  CycFort headNodeCore;
  CycFort mt;
  CycSymbol isTrue;
  CycSymbol isAsserted;
  CycSymbol strength;

  IsNode tailNode;
  IsNode headNode;
  IsEdge edge;

  public CycGraphCommand_AddEdge(IsEdge _edge) {
    edge = _edge;
  }

  public CycGraphCommand_AddEdge(CycList _paramList) {
    pred = (CycFort)_paramList.get(0);
    tailNodeCore = (CycFort)_paramList.get(1);
    headNodeCore = (CycFort)_paramList.get(2);
    //mt = (CycFort)_paramList.get(3);
    isTrue = (CycSymbol)_paramList.get(4);
    isAsserted = (CycSymbol)_paramList.get(5);
    strength = (CycSymbol)_paramList.get(6);
  }

  public CycGraphCommand_AddEdge(IsGraph _graph, CycList _paramList) {
    setGraph(_graph);
    if (_paramList.size() > 0 && _paramList.get(0) instanceof CycFort) {
      pred = (CycFort)_paramList.get(0);
    } else {
      pred = null;
    }
    if (_paramList.size() > 1 && _paramList.get(1) instanceof CycFort) {
      tailNodeCore = (CycFort)_paramList.get(1);
    } else {
      tailNodeCore = null;
    }
    if (_paramList.size() > 2 && _paramList.get(2) instanceof CycFort) {
      headNodeCore = (CycFort)_paramList.get(2);
    } else {
      headNodeCore = null;
    }
    //mt = (CycFort)_paramList.get(3);
    if (_paramList.size() > 4 && _paramList.get(4) instanceof CycSymbol) {
      isTrue = (CycSymbol)_paramList.get(4);
    } else {
      isTrue = null;
    }
    if (_paramList.size() > 5 && _paramList.get(5) instanceof CycSymbol) {
      isAsserted = (CycSymbol)_paramList.get(5);
    } else {
      isAsserted = null;
    }
    if (_paramList.size() > 6 && _paramList.get(6) instanceof CycSymbol) {
      strength = (CycSymbol)_paramList.get(6);
    } else {
      strength = null;
    }
    if (tailNodeCore != null) {
      tailNode = getGraph().instantiateNode(tailNodeCore);
    } else {
      tailNode = null;
    }
    if (tailNodeCore != null) {
      headNode = getGraph().instantiateNode(headNodeCore);
    } else {
      headNode = null;
    }
    if (tailNode != null && headNode != null && pred != null) {
      edge = getGraph().instantiateEdge(tailNode, headNode, pred);
    }
  }

  ////////////////////////////////////////////////////////////////////////////////

  public void processInternal(IsCommandManager _commandManager) {
    if (DEBUG) { System.out.println("TRACE: CycGraphCommand_AddEdge[" + this + "].process[IsCommandManager](" + _commandManager + ")"); }
    process(_commandManager.getCommandable());
  }

  public void processInternal(IsRenderableGraph _renderableGraph) {
    if (true) { System.out.println("TRACE: CycGraphCommand_AddEdge[" + this + "].process[IsRenderableGraph](" + _renderableGraph + ")"); }
    process(_renderableGraph.getGraphFrame());
  }
  
  public void processInternal(IsGraphFrame _graphFrame) {
    
    if (DEBUG) { System.out.println("TRACE: CycGraphCommand_AddEdge[" + this + "].process(" + _graphFrame + ")"); }

    if (getGraphFrame() == null) { setGraphFrame(_graphFrame); }
    if (getApplicationFrame() == null) { setApplicationFrame(_graphFrame.getApplicationFrame()); }
    if (getGraph() == null) { setGraph(_graphFrame.getGraph()); }

    if (tailNode == null) {
      tailNode = getGraph().instantiateNode(tailNodeCore);
    }

    if (headNode == null) {
      headNode = getGraph().instantiateNode(headNodeCore);
    }

    if (edge == null && tailNode != null && headNode != null && pred != null) {
      //System.out.println(getGraph());
      edge = getGraph().instantiateEdge(tailNode, headNode, pred);
    }

    if (edge == null && isInteractive == true) {
      // show a non-modal dialog on application frame
      //userProcess_FindEdgeDialog(getGraphFrame(), getApplicationFrame(), getGraph());
      JOptionPane.showInternalMessageDialog(_graphFrame.getComponent(),
					    "Error processing CycGraphCommand_AddEdge request", 
					    "Can't show dialog window yet (?)",
					    JOptionPane.ERROR_MESSAGE); 

    } else if (_graphFrame instanceof IsCommandable) {
      // can queue
      IsCommandManager commandManager = _graphFrame.getCommandManager();
      if (commandManager != null) {
	commandManager.queueCommand(this);
      } else {
	System.err.println("CycGraphCommand_AddEdge[" + this + "].processInternal(" + _graphFrame + "): no commandManager found.  Command not queued."); 
      }
    } else {
      // error: need IsCommandable graph frame
      JOptionPane.showInternalMessageDialog(_graphFrame.getComponent(),
					    "Error processing CycGraphCommand_AddEdge request", 
					    "IsGraphFrame instanceof IsCommandable == false",
					    JOptionPane.ERROR_MESSAGE); 
    }
  }

  public Object execute(IsCommandExecutor _commandExecutor) {
    if (DEBUG) System.err.println("CycGraphCommand_AddEdge.execute(" + _commandExecutor + ")");
    if (_commandExecutor instanceof IsGraphCommandExecutor) {
      IsGraphCommandExecutor graphCommandExecutor = (GraphCommandExecutor)_commandExecutor;
      //if (DEBUG) { System.out.println("--> CycGraphCommand_AddEdge[" + this + "].execute()"); }
      //if (DEBUG) { System.err.println("--- DDDDDDD edge = " + edge); }
      graphCommandExecutor.addEdge(this,edge);
      //if (DEBUG) { System.err.println("--- DDDDDDD edge added"); }
      return edge;
    } else {
      // @todo: throw command execution error?
      Object[] message = { "Error in execute(" + _commandExecutor + ")", 
			   "(_commandExecutor instanceof IsGraphCommandExecutor) is false" };
      JOptionPane.showInternalMessageDialog(getGraphFrame().getComponent(),
					    message,
					    "CycGraphCommand_AddEdge Error",
					    JOptionPane.ERROR_MESSAGE); 
      return null;
    }
    
  }

  //  public CommandExecutionState getExecutionState() { return executionState; }

  public boolean isPossiblyDestructive() { return false; }
  public boolean wasDestructive() { return false; }

  ////////////////////////////////////////////////////////////////////////////////

  public void setGraph(IsGraph _graph) { graph = _graph; }
  public IsGraph getGraph() { return graph; }

  public void setGraphFrame(IsGraphFrame _graphFrame) { graphFrame = _graphFrame; }
  public IsGraphFrame getGraphFrame() { return graphFrame; }

  public void setApplicationFrame(IsApplicationFrame _applicationFrame) { applicationFrame = _applicationFrame; }
  public IsApplicationFrame getApplicationFrame() { return applicationFrame; }


}
