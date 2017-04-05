/* $Id: CycGraphCommand_FocusAtNode.java,v 1.6 2002/05/23 22:35:04 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.cyc;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import com.cyc.blue.command.IsCommand;
import com.cyc.blue.command.IsCommandable;
import com.cyc.blue.command.IsCommandExecutor;
import com.cyc.blue.command.IsCommandManager;
import com.cyc.blue.command.IsGraphCommand;
import com.cyc.blue.command.IsGraphCommandExecutor;
import com.cyc.blue.graph.IsGraph;
import com.cyc.blue.graph.IsNode;
import com.cyc.blue.gui.IsApplicationFrame;
import com.cyc.blue.gui.IsGraphFrame;
import com.cyc.blue.gui.IsRenderableGraph;
import org.opencyc.api.CycAccess;
import org.opencyc.api.CycApiException;
import org.opencyc.cycobject.CycFort;
import org.opencyc.cycobject.CycList;

/**
 * A command to focus a graph on a node.  Currently this just makes a node have
 * the isFocus attrubuter set to <code>true</code> but this command should call
 * a function in the graph command executor that shifts the graph (changes the
 * visible nodes) around the focus according to what predicates we're interested
 * in viewing.  Or probably the previous sentence describes a viewing mode that
 * should be set in the command executor and this command should just be aware
 * of itself "setting focus"?
 *
 * @author John Jantos
 * @date 2001/08/15
 * @version $Id: CycGraphCommand_FocusAtNode.java,v 1.6 2002/05/23 22:35:04 jantos Exp $
 */

public class CycGraphCommand_FocusAtNode extends CycGraphCommand implements IsGraphCommand {
  private static final boolean DEBUG = false;
  
  List nodeCores = new ArrayList();
  List cycNodes = new ArrayList();

  public CycGraphCommand_FocusAtNode(Object _nodeIndicator) {
    if (_nodeIndicator instanceof CycFort) {
      nodeCores.add((CycFort)_nodeIndicator);
    } else if (_nodeIndicator instanceof IsNode) {
      cycNodes.add((IsNode)_nodeIndicator);
    } else {
      // silently fail
    }
  }

  public CycGraphCommand_FocusAtNode(IsNode _cycNode) {
    cycNodes.add(_cycNode);
  }

  public CycGraphCommand_FocusAtNode(CycFort _cycFort) {
    nodeCores.add(_cycFort);
  }

  public CycGraphCommand_FocusAtNode(CycAccessInitializer _cycAccessInitializer, String _nodeString) {
    BlueCycAccess blueCycAccess = null;
    try {
      blueCycAccess = new BlueCycAccess(_cycAccessInitializer);
      nodeCores.add(BlueCycAccess.current().getConstantByName(_nodeString));
    } catch (IOException e) {
      Thread.currentThread().dumpStack();
    } catch (CycApiException e) {
      Thread.currentThread().dumpStack();
    } finally {
      if (blueCycAccess != null) {
	blueCycAccess.close();
      }
    }
    //importance = 0;
  }

  public CycGraphCommand_FocusAtNode(CycList _paramList) {
    nodeCores = (List)_paramList.get(1);
  }

  ////////////////////////////////////////////////////////////////////////////////

  public void processInternal(IsCommandManager _commandManager) {
    if (DEBUG) { System.out.println("TRACE: CycGraphCommand_FocusAtNode[" + this + "].process[IsCommandManager](" + _commandManager + ")"); }
    process(_commandManager.getCommandable());
  }

  public void processInternal(IsRenderableGraph _renderableGraph) {
    if (true) { System.out.println("TRACE: CycGraphCommand_FocusAtNode[" + this + "].process[IsRenderableGraph](" + _renderableGraph + ")"); }
    process(_renderableGraph.getGraphFrame());
  }
  
  public void processInternal(IsGraphFrame _graphFrame) {
    
    if (DEBUG) { System.out.println("TRACE: CycGraphCommand_FocusAtNode[" + this + "].process(" + _graphFrame + ")"); }

    if (getGraphFrame() == null) { setGraphFrame(_graphFrame); }
    if (getApplicationFrame() == null) { setApplicationFrame(_graphFrame.getApplicationFrame()); }
    if (getGraph() == null) { setGraph(_graphFrame.getGraph()); }

    Iterator listIterator = nodeCores.iterator();
    if (listIterator != null) {
      while (listIterator.hasNext()) {
	CycFort nodeCore = (CycFort)listIterator.next();
	IsNode cycNode = getGraph().instantiateNode(nodeCore);
	if (cycNode != null) {
	  cycNodes.add(cycNode);
	}
	if (_graphFrame instanceof IsCommandable) {
	  // can queue
	  IsCommandManager commandManager = _graphFrame.getCommandManager();
	  if (commandManager != null) {
	    commandManager.queueCommand(this);
	  } else {
	System.err.println("CycGraphCommand_FocusAtNode[" + this + "].processInternal(" + _graphFrame + "): no commandManager found.  Command not queued."); 
	  }
	} else {
	  // error: need IsCommandable graph frame
	  JOptionPane.showInternalMessageDialog(_graphFrame.getComponent(),
						"Error processing CycGraphCommand_FocusAtNode request", 
						"IsGraphFrame instanceof IsCommandable == false",
						JOptionPane.ERROR_MESSAGE); 
	}
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////////

  public Object execute(IsCommandExecutor _commandExecutor) {
    if (_commandExecutor instanceof IsGraphCommandExecutor) {
      return execute((IsGraphCommandExecutor)_commandExecutor);
    } else {
      return null;
    }
  }
 
  public Object execute(IsGraphCommandExecutor _commandExecutor) {
    System.out.println("CycGraphCommand_FocusAtNode -> focusing at " + cycNodes);
    Iterator listIterator = cycNodes.iterator();
    if (listIterator != null) {
      while (listIterator.hasNext()) {
	IsNode cycNode = (IsNode)listIterator.next();
	cycNode.setFocus(true);
      }
    }
    return null;
  }


  public boolean isPossiblyDestructive() { return false; }
  public boolean wasDestructive() { return false; }

  ////////////////////////////////////////////////////////////////////////////////

  private IsGraphFrame graphFrame;
  private IsApplicationFrame applicationFrame;
  private IsGraph graph;


  public void setGraph(IsGraph _graph) { graph = _graph; }
  public IsGraph getGraph() { return graph; }

  public void setGraphFrame(IsGraphFrame _graphFrame) { graphFrame = _graphFrame; }
  public IsGraphFrame getGraphFrame() { return graphFrame; }

  public void setApplicationFrame(IsApplicationFrame _applicationFrame) { applicationFrame = _applicationFrame; }
  public IsApplicationFrame getApplicationFrame() { return applicationFrame; }

}

  /*

  public Object executeOld(IsGraphCommandExecutor _commandExecutor) {
   if (DEBUG) { System.err.println(this + ".execute(" + _commandExecutor); }
    // undirected BFS
    if (_commandExecutor instanceof GraphCommandExecutor) { // was CycGraphCommandExecutor
      IsGraph graph = _commandExecutor.getGraph();
//        if (cycNode == null) {
//  	if (nodeCore != null) {
//  	  cycNode = (CycNode)graph.instantiateNode(nodeCore);
//  	}
//        }
      if (DEBUG) { System.err.println("CycGraphCommand_FocusAtNode: attempting focus at " + cycNode); }
      if (cycNode != null) {
	HashSet nextNodeSet = new HashSet();
	HashSet doneNodeSet = new HashSet();
	int depth = 0;
	nextNodeSet.add(cycNode);
	while (!nextNodeSet.isEmpty()) {
	  HashSet newNextNodeSet = new HashSet();
	  Iterator nextNodeSetIterator = nextNodeSet.iterator();
	  if (nextNodeSetIterator != null) {
	    while (nextNodeSetIterator.hasNext()) {
	      CycNode nextNode = (CycNode)nextNodeSetIterator.next();
	      if (!doneNodeSet.contains(nextNode)) {
		//System.out.println("AAAA: " + nextNode + " += " + depth);
		nextNode.addImportance(depth);
		doneNodeSet.add(nextNode);
		Iterator incidentEdgesInIterator = graph.incidentEdgesInIterator(nextNode);
		if (incidentEdgesInIterator != null) {
		  while (incidentEdgesInIterator.hasNext()) {
		    CycEdge incidentEdge = (CycEdge)incidentEdgesInIterator.next();
		    CycNode incidentEdgeOtherNode = (CycNode)incidentEdge.getTail();
		    newNextNodeSet.add(incidentEdgeOtherNode);
		  }
		}
		Iterator incidentEdgesOutIterator = graph.incidentEdgesOutIterator(nextNode);
		if (incidentEdgesOutIterator != null) {
		  while (incidentEdgesOutIterator.hasNext()) {
		    CycEdge incidentEdge = (CycEdge)incidentEdgesOutIterator.next();
		    CycNode incidentEdgeOtherNode = (CycNode)incidentEdge.getHead();
		    newNextNodeSet.add(incidentEdgeOtherNode);
		  }
		}
	      }
	    }
	  }
	  doneNodeSet.addAll(nextNodeSet);
	  nextNodeSet = newNextNodeSet;
	  nextNodeSet.removeAll(doneNodeSet);
	  depth--;
	}
      }
    }
    return null;
  }
  */
