/* $Id: CycGraphCommand_AddNode.java,v 1.5 2002/05/23 22:35:04 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.cyc;

import java.awt.Component;
import java.awt.Dialog;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import com.cyc.blue.command.GenericCommand;
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
import com.cyc.blue.gui.IsApplicationFrame;
import com.cyc.blue.gui.IsGraphFrame;
import com.cyc.blue.gui.IsRenderableGraph;
import org.opencyc.cycobject.CycList;

/**
 * A graph command to adda node to a graph.  If the node isn't specified during
 * processing a dialog window will query the user for the node.
 *
 * @author John Jantos
 * @date 2001/08/15
 * @version $Id: CycGraphCommand_AddNode.java,v 1.5 2002/05/23 22:35:04 jantos Exp $
 */

public class CycGraphCommand_AddNode extends GenericGraphCommand implements IsGraphCommand {
  private static final boolean DEBUG = false;

  private IsGraphFrame graphFrame;
  private IsApplicationFrame applicationFrame;
  private IsGraph graph;

  private Object nodeCore = null;
  private IsNode node;

  private boolean isInteractive = false;

  public CycGraphCommand_AddNode() {
  }

  public CycGraphCommand_AddNode(IsGraph _graph, Object _nodeCore) {
    graph = _graph;
    nodeCore = _nodeCore;
  }

  public CycGraphCommand_AddNode(Object _nodeCore) {
    nodeCore = _nodeCore;
  }

  public CycGraphCommand_AddNode(Object _nodeCore, boolean _isInteractive) {
    nodeCore = _nodeCore;
    isInteractive = _isInteractive;
  }

//    public CycGraphCommand_AddNode(CycAccessInitializer _cycAccessInitializer, IsGraph _cycGraph, Object _nodeCore) {
//      this(_nodeCore, false);
//      setGraph(_cycGraph);
//      try {
//        new BlueCycAccess(_cycAccessInitializer);
//        //node = _cycGraph.instantiateNode(_nodeCore);
//      } catch (Exception e) {
//        System.err.println(e); Thread.currentThread().dumpStack(); 
//      }
//    }

  public CycGraphCommand_AddNode(CycList _paramList) {
    nodeCore = _paramList.get(0);
    //importance = ((Integer)_paramList.get(1)).intValue() + 1;
    //generalityEstimate = (Integer)_paramList.get(2);
    //fortTypes = (CycList)_paramList.get(3);
  }

//    public String toString() {
//      return "CycGraphCommand_AddNode(" + nodeFort + ")";
//    }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsCommand

  // using GenericCommands's dispatching process method

  public void processInternal(IsCommandManager _commandManager) {
    if (DEBUG) { System.out.println("TRACE: CycGraphCommand_AddNode[" + this + "].process[IsCommandManager](" + _commandManager + ")"); }
    process(_commandManager.getCommandable());
  }

  public void processInternal(IsRenderableGraph _renderableGraph) {
    if (DEBUG) { System.out.println("TRACE: CycGraphCommand_AddNode[" + this + "].process[IsRenderableGraph](" + _renderableGraph + ")"); }
    process(_renderableGraph.getGraphFrame());
  }
  
  public void processInternal(IsGraphFrame _graphFrame) {
    
    if (DEBUG) { System.out.println("TRACE: CycGraphCommand_AddNode[" + this + "].process(" + _graphFrame + ")"); }

    if (getGraphFrame() == null) { setGraphFrame(_graphFrame); }
    if (getApplicationFrame() == null) { setApplicationFrame(_graphFrame.getApplicationFrame()); }
    if (getGraph() == null) { setGraph(_graphFrame.getGraph()); }

    if (node == null && nodeCore != null) {
      //System.out.println(getGraph());
      node = getGraph().instantiateNode(nodeCore);
      nodeCore = null;
    }

    if (node == null && isInteractive == true) {
      // show a non-modal dialog on application frame
      userProcess_FindNodeDialog(getGraphFrame(), getApplicationFrame(), getGraph());

    } else if (_graphFrame instanceof IsCommandable) {
      // can queue
      IsCommandManager commandManager = _graphFrame.getCommandManager();
      if (commandManager != null) {
	commandManager.queueCommand(this);
      } else {
	System.err.println("CycGraphCommand_AddNode[" + this + "].processInternal(" + _graphFrame + "): no commandManager found.  Command not queued."); 
      }
    } else {
      // error: need IsCommandable graph frame
      JOptionPane.showInternalMessageDialog(_graphFrame.getComponent(),
					    "Error processing CycGraphCommand_AddNode request", 
					    "IsGraphFrame instanceof IsCommandable == false",
					    JOptionPane.ERROR_MESSAGE); 
    }
  }


  private void userProcess_FindNodeDialog(IsCommandProcessor _nextCommandProcessor, IsApplicationFrame _applicationFrame, IsGraph _graph) {
    
    final JDialog DIALOG = new JDialog();
    DIALOG.setTitle("Add Node");

    final String QUESTION_STRING = "What term would you like to add?";
    final JTextField FORT_NAME_FIELD = new JTextField(40);
    final JLabel STATUS_LABEL = new JLabel("");
    final Object[] QUESTION_ARRAY = { QUESTION_STRING, FORT_NAME_FIELD, STATUS_LABEL };
    
    final String COMPLETE_BUTTON_STRING = "Complete";
    final String ADD_BUTTON_STRING = "Add";
    final String CANCEL_BUTTON_STRING = "Cancel";
    final Object[] BUTTON_STRING_ARRAY = { COMPLETE_BUTTON_STRING, CANCEL_BUTTON_STRING, ADD_BUTTON_STRING };
    
    final JOptionPane OPTION_PANE = new JOptionPane(QUESTION_ARRAY, 
						    JOptionPane.PLAIN_MESSAGE,
						    0,
						    null,
						    BUTTON_STRING_ARRAY,
						    ADD_BUTTON_STRING);
    final IsGraph GRAPH = _graph;
    final IsCommandProcessor NEXT_COMMAND_PROCESSOR = _nextCommandProcessor;
    
    DIALOG.setContentPane(OPTION_PANE);
    DIALOG.setLocationRelativeTo(_applicationFrame.getComponent()); // ??
    DIALOG.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    OPTION_PANE.addPropertyChangeListener(new PropertyChangeListener() {
	public void propertyChange(PropertyChangeEvent e) {
	  String prop = e.getPropertyName();
	  //System.err.println("--- CCCCCCCC " + e);
	  STATUS_LABEL.setText("");
	  if (DIALOG.isVisible() 
	      && (e.getSource() == OPTION_PANE)
	      && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
		  prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
	    {
	      if (OPTION_PANE.getValue() == CANCEL_BUTTON_STRING) {
		DIALOG.setVisible(false);
	      } else {
		IsNode node = GRAPH.instantiateNode(FORT_NAME_FIELD.getText());
		if (node != null) {
		  STATUS_LABEL.setText(FORT_NAME_FIELD.getText() + " is good (" + node.toString() + ")");
		  if (OPTION_PANE.getValue() == ADD_BUTTON_STRING) {
		    DIALOG.setVisible(false);
		    userProcess_FindNodeDialogDone(NEXT_COMMAND_PROCESSOR, node);
		  }
		} else {
		  STATUS_LABEL.setText(FORT_NAME_FIELD.getText() + " is invalid.");
		}
	      }
	    }
	  DIALOG.validate();
	}
      });
    DIALOG.pack();
    DIALOG.setVisible(true);
  }

  private void userProcess_FindNodeDialogDone(IsCommandProcessor _nextProcessArg, IsNode _node) {
    node = _node;
    process(_nextProcessArg);
  }

  public boolean unexecute(IsCommandExecutor _commandExecutor) {
    if (_commandExecutor instanceof IsGraphCommandExecutor) {
      return unexecute((IsGraphCommandExecutor)_commandExecutor);
    } else {
      return false;
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  
  public Object execute(IsCommandExecutor _commandExecutor) {
    if (_commandExecutor instanceof IsGraphCommandExecutor) {
      IsGraphCommandExecutor graphCommandExecutor = (GraphCommandExecutor)_commandExecutor;
      //if (DEBUG) { System.out.println("--> CycGraphCommand_AddNode[" + this + "].execute()"); }
      //if (DEBUG) { System.err.println("--- DDDDDDD node = " + node); }
      graphCommandExecutor.addNode(this,node);
      //if (DEBUG) { System.err.println("--- DDDDDDD node added"); }
      //graphCommandExecutor.setNodeFocus(true);
      return node;
    } else {
      // @todo: throw command execution error?
      Object[] message = { "Error in execute(" + _commandExecutor + ")", 
			   "(_commandExecutor instanceof IsGraphCommandExecutor) is false" };
      JOptionPane.showInternalMessageDialog(getGraphFrame().getComponent(),
					    message,
					    "CycGraphCommand_AddNode Error",
					    JOptionPane.ERROR_MESSAGE); 
      return null;
    }
  }

  public boolean unexecute(IsGraphCommandExecutor _commandExecutor) {
    return _commandExecutor.removeNode(this, node);
  }

  
  public Object reexecute(IsCommandExecutor _commandExecutor) {
    if (_commandExecutor instanceof IsGraphCommandExecutor) {
      return reexecute((IsGraphCommandExecutor)_commandExecutor);
    } else {
      return null;
    }
  }
  public Object reexecute(IsGraphCommandExecutor _commandExecutor) {
    return execute(_commandExecutor);
  }

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
