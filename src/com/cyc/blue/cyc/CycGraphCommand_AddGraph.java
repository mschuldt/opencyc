/* $Id: CycGraphCommand_AddGraph.java,v 1.5 2002/05/23 22:35:04 jantos Exp $
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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import com.cyc.blue.command.IsCommand;
import com.cyc.blue.command.IsCommandable;
import com.cyc.blue.command.IsCommandExecutor;
import com.cyc.blue.command.IsCommandManager;
import com.cyc.blue.command.IsGraphCommandExecutor;
import com.cyc.blue.graph.IsGraph;
import com.cyc.blue.gui.IsApplicationFrame;
import com.cyc.blue.gui.IsGraphFrame;
import com.cyc.blue.gui.IsRenderableGraph;
import org.opencyc.api.CycAccess;
import org.opencyc.api.CycApiException;
import org.opencyc.cycobject.CycFort;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycSymbol;

/**
 * A cyc graph command that adds a graph (possibly multiple nodes and edges) to a cyc graph.
 *
 * @author John Jantos
 * @date 2001/08/15
 * @version $Id: CycGraphCommand_AddGraph.java,v 1.5 2002/05/23 22:35:04 jantos Exp $
 */

public class CycGraphCommand_AddGraph extends CycGraphCommand implements IsCommand {
  private static final boolean DEBUG = false;
  private LinkedList subcommands;

  private CycSymbol _graphBuilderFn;
  private CycList _fortList;
  private CycList _predList;
  private CycFort _mt;
  private CycList _filterFns;
  private int _depth;
  private CycList _params;

  private CycList graphBuilderFnParamList = null;

  private Collection nodes;
  private Collection edges;

  public CycGraphCommand_AddGraph(CycList _graphBuilderFnParamList) {
    graphBuilderFnParamList = _graphBuilderFnParamList;
  }

  public String toString() {
    return "CycGraphCommand_AddGraph(" + graphBuilderFnParamList + ")";
  }

//    public CycGraphCommand_AddGraph(CycSymbol _graphBuilderFn,
//  				  CycList _preds,
//  				  CycList _forts,
//  				  CycFort _mt,
//  				  CycList _filterFns,
//  				  int _depth
//  ) {
//      //("bbf-min-forward-true" (#$Dog) (#$isa) #$RKF-TKCP-DemoEnvironmentMt (BFF-RKF-IRRELEVANT-TERMS BFF-ARBITRARY-UNIONS) (:depth 1 :closure? t))
//      graphBuilderFnParamList = makeGraphBuilderFnParamList(_graphBuilderFn,
//  							  _preds,
//  							  _forts,
//  							  _mt,
//  							  _filterFns,
//  							  _depth
//  							  );
//    }

//    private CycList makeGraphBuilderFnParamList(CycSymbol _graphBuilderFn,
//  					      CycList _preds,
//  					      CycList _forts,
//  					      CycFort _mt,
//  					      CycList _filterFns,
//  					      int _depth
//  					      ) {
//      CycList argList = new CycList();
//      argList.add(_preds);
//      argList.add(_forts);
//      argList.add(_mt);
//      argList.add(_filterFns);
//      argList.add(new Integer(_depth));
//      //argList.add(_params);
//      CycList quoteList = new CycList();
//      quoteList.add(new CycSymbol("QUOTE"));
//      quoteList.add(argList);
//      CycList paramList = new CycList();
//      paramList.add(_graphBuilderFn);
//      paramList.add(quoteList);
//      return paramList;
//    }

  private CycList callGraphBuilderFn() {
    try {
      System.out.println("callGraphBuilderFn evalling " + graphBuilderFnParamList);
      return BlueCycAccess.current().converseList(graphBuilderFnParamList);
    } catch (IOException e) { 
      System.err.println(e); 
      Thread.currentThread().dumpStack();
    } catch (CycApiException e) { 
      System.err.println(e); 
      Thread.currentThread().dumpStack(); 
    }
    return null;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsCommand

  public void processInternal(IsCommandManager _commandManager) {
    if (DEBUG) { System.out.println("TRACE: CycGraphCommand_AddGraph[" + this + "].process[IsCommandManager](" + _commandManager + ")"); }
    process(_commandManager.getCommandable());
  }

  public void processInternal(IsRenderableGraph _renderableGraph) {
    if (DEBUG) { System.out.println("TRACE: CycGraphCommand_AddGraph[" + this + "].process[IsRenderableGraph](" + _renderableGraph + ")"); }
    process(_renderableGraph.getGraphFrame());
  }
  
  public void processInternal(IsGraphFrame _graphFrame) {
    
    if (DEBUG) { System.out.println("TRACE: CycGraphCommand_AddGraph[" + this + "].process(" + _graphFrame + ")"); }

    if (getGraphFrame() == null) { setGraphFrame(_graphFrame); }
    if (getApplicationFrame() == null) { setApplicationFrame(_graphFrame.getApplicationFrame()); }
    if (getGraph() == null) { setGraph(_graphFrame.getGraph()); }

    if (_graphFrame instanceof IsCommandable) {
      // can queue
      IsCommandManager commandManager = _graphFrame.getCommandManager();
      if (commandManager != null) {
	commandManager.queueCommand(this);
      } else {
	System.err.println("CycGraphCommand_AddGraph[" + this + "].processInternal(" + _graphFrame + "): no commandManager found.  Command not queued."); 
      }
    } else {
      // error: need IsCommandable graph frame
      JOptionPane.showInternalMessageDialog(_graphFrame.getComponent(),
					    "Error processing CycGraphCommand_AddGraph request", 
					    "IsGraphFrame instanceof IsCommandable == false",
					    JOptionPane.ERROR_MESSAGE); 
    }
  }

  public Object execute(IsCommandExecutor _commandExecutor) {
    if (DEBUG) System.err.println("CycGraphCommand_AddGraph.execute(" + _commandExecutor + ")");
    if (_commandExecutor instanceof IsGraphCommandExecutor) {
      IsGraphCommandExecutor graphCommandExecutor = (IsGraphCommandExecutor)_commandExecutor;
      CycList resultEdges = callGraphBuilderFn();
      System.out.println("resultEdges = " + resultEdges);
      if (resultEdges != null) {
	Iterator resultEdgesIterator = resultEdges.iterator();
	if (resultEdgesIterator != null) {
	  while (resultEdgesIterator.hasNext()) {
	    CycList edgeDefList = (CycList)resultEdgesIterator.next();
	    if (DEBUG) System.err.println("calling new CycGraphCommand_AddEdge(" + getGraph() + ", " + edgeDefList + ")");	  
	    addSubCommand(new CycGraphCommand_AddEdge(getGraph(), edgeDefList));
	  }
	}
      }
    }
    return super.execute(_commandExecutor);
  }
  
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
  public CycGraphCommand_AddGraph(CycFort _pred,
					   CycFort _argIn,
					   CycFort _mt,
					   CycList _filterFns,
					   int _depth) {
    
    if (DEBUG) System.err.println("CycGraphCommand_AddGraph(" + 
				  _pred + ", " + 
				  _argIn + ", " + 
				  _mt + ", " + 
				  _filterFns + ", " + 
				  _depth);
    graphBuilderFnParamList = new CycList();
    graphBuilderFnParamList.add(_pred);
    graphBuilderFnParamList.add(_argIn);
    graphBuilderFnParamList.add(_mt);
    graphBuilderFnParamList.add(_filterFns);
    graphBuilderFnParamList.add(new Integer(_depth));
  }
*/
