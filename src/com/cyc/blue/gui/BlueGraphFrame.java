/* $Id: BlueGraphFrame.java,v 1.9 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JInternalFrame;
import com.cyc.blue.IsPausable;
import com.cyc.blue.command.IsCommand;
import com.cyc.blue.command.IsCommandable;
import com.cyc.blue.command.IsCommandManager;
import com.cyc.blue.command.IsCommandProcessor;
import com.cyc.blue.command.IsGraphCommandExecutor;
import com.cyc.blue.command.SimpleCommandManagerThread;
import com.cyc.blue.graph.IsGraph;
import com.cyc.blue.layout.GraphLayoutStrategy_CenterGravity;
import com.cyc.blue.layout.GraphLayoutStrategy_MagneticFieldEdges;
import com.cyc.blue.layout.GraphLayoutStrategy_RepellingNodes;
import com.cyc.blue.layout.GraphLayoutStrategy_SpringEdges;
import com.cyc.blue.layout.JGraphLayoutManagerThread;

/**
 * An extension of ApplicationInternalFrame that also implements IsGraphFrame.
 *
 * @author John Jantos
 * @date 2001/10/18
 * @version $Id: BlueGraphFrame.java,v 1.9 2002/05/23 22:30:14 jantos Exp $
 */

public class BlueGraphFrame extends ApplicationInternalFrame implements IsGraphFrame, IsPausable {
  private static int openFrameCount = 0; //??

  private JGraphLayoutManagerThread renderableGraphLayoutManagerThread;
  private BlueOldToolBar toolbar;
  private IsGraphStatusBar statusBar;

  public BlueGraphFrame(IsGraph _graph, IsRenderableGraph _renderableGraph, IsGraphCommandExecutor _graphCommandExecutor, boolean _resizable, boolean _closable, boolean _maximizable, boolean _iconifiable) {
    super("Graph #" + (++openFrameCount), _resizable, _closable, _maximizable, _iconifiable);
    setGraph(_graph);
    setRenderableGraph(_renderableGraph);
    setCommandManager(new SimpleCommandManagerThread(this, _graphCommandExecutor));
    renderableGraphLayoutManagerThread = new JGraphLayoutManagerThread(renderableGraph);
    renderableGraphLayoutManagerThread.setPriority(Thread.MIN_PRIORITY);
    //renderableGraphLayoutManagerThread.addStrategy(new GraphLayoutStrategy_RepellingBorders());
    renderableGraphLayoutManagerThread.addStrategy(new GraphLayoutStrategy_CenterGravity());
    renderableGraphLayoutManagerThread.addStrategy(new GraphLayoutStrategy_RepellingNodes(2));
    renderableGraphLayoutManagerThread.addStrategy(new GraphLayoutStrategy_SpringEdges());
    renderableGraphLayoutManagerThread.addStrategy(new GraphLayoutStrategy_MagneticFieldEdges());
    
    _renderableGraph.setGraphFrame(this);
    // initialize
    setSize(640,480);

    getJInternalFrame().getContentPane().add(renderableGraph.getComponent());

    ////toolbar... @todo: redo
    toolbar = new BlueOldToolBar(renderableGraph, renderableGraphLayoutManagerThread);
    getJInternalFrame().getContentPane().add(toolbar, BorderLayout.NORTH);

    //// status bar
    statusBar = new GenericGraphStatusBar(getGraph());
    getJInternalFrame().getContentPane().add(statusBar.getComponent(), BorderLayout.SOUTH);
    
//      IsRenderableListener mouseInputListener = new RenderableGraphListener_Generic(_renderableGraph, this);
//      getComponent().addMouseListener(mouseInputListener);

    // display
    setVisible(true); //necessary as of kestrel
    validate();

    // start threads
    renderableGraphLayoutManagerThread.start();
    commandManager.start(); // @todo

     
    GraphFrameMouseListener mouseListener = new GraphFrameMouseListener(this);
    getRenderableGraph().getContainer().addMouseMotionListener(mouseListener);
    getRenderableGraph().getContainer().addMouseListener(mouseListener);
    //  // for some reason this prevents the frame from displaying at all:
//      try {
//        setMaximum(true);
//      } catch (java.beans.PropertyVetoException e) {
//        System.err.println("ERROR: BlueGraphFrame[" + this + "]: setMaximum failed");
//      }
  }
  
//    public BlueGraphFrame(IsGraph _graph, boolean _resizable, boolean _closable, boolean _maximizable, boolean _iconifiable) {
//      super("Graph #" + (++openFrameCount), _resizable, _closable, _maximizable, _iconifiable);
    
//      // own repaintManager, dodn't solve problem where mouseEvents are taking up too much bandwith and slowing repaint dramatically.
//      //RepaintManager.setCurrentManager(new RepaintManager());
//      ////set below to use setDebugGraphicsOptions
//      //RepaintManager.currentManager(this).setDoubleBufferingEnabled(false); 

//      graph = _graph;
//      renderableGraph = new GenericSubscribingRenderableGraph(graph, this, GenericRenderableNode.class, GenericRenderableEdge.class);
    
//      // ???
//  //      setCommandManager(new GraphCommandManager(graph));
//  //      graph.setGraphCommandManager((GraphCommandManager)getCommandManager());
//      setCommandManager(new SimpleCommandManagerThread(this, new GraphCommandExecutor(graph)));
//      if (getCommandManager() instanceof Thread) {
//        ((Thread)getCommandManager()).start();
//      }
//  //      setCommandExecutor(new GraphCommandExecutor(graph));
//  //      graph.setGraphCommandExecutor((GraphCommandExecutor)getCommandExecutor());
//      //renderableGraph.setGraphCommandExecutor(graphCommandExecutor); //?????

//      renderableGraphLayoutManagerThread = new JGraphLayoutManagerThread(renderableGraph);
//      renderableGraphLayoutManagerThread.setPriority(Thread.MIN_PRIORITY);
//      //renderableGraphLayoutManagerThread.addStrategy(new GraphLayoutStrategy_RepellingBorders());
//      renderableGraphLayoutManagerThread.addStrategy(new GraphLayoutStrategy_CenterGravity());
//      renderableGraphLayoutManagerThread.addStrategy(new GraphLayoutStrategy_RepellingNodes(2));
//      renderableGraphLayoutManagerThread.addStrategy(new GraphLayoutStrategy_SpringEdges());
//      renderableGraphLayoutManagerThread.addStrategy(new GraphLayoutStrategy_MagneticFieldEdges());

//      //...Create the GUI and put it in the window...
    
//      //...Then set the window size or call pack...
//      setSize(640,480);
//      //renderableGraph.setWidthHeightD(640,480);
    
//      //Set the window's location.
//      setLocation(0,0);
//      //renderableGraph.setXYD(0,0);
    
//      // contents
//      getJInternalFrame().getContentPane().add(renderableGraph.getContainer());
    
//      //getJInternalFrame().getContentPane().add(renderableGraph);
//      ////toolbar... @todo: redo
//      toolbar = new BlueOldToolBar(renderableGraph, renderableGraphLayoutManagerThread);
//      getJInternalFrame().getContentPane().add(toolbar, BorderLayout.SOUTH);

//      // properties
//      setVisible(true); //necessary as of kestrel

//      renderableGraphLayoutManagerThread.start();
//      //commandManager.start(); // @todo

//      validate();

//      //renderableGraph.validate();

//    }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsInternalFrame

  public void setJInternalFrame(JInternalFrame _internalFrame) { }
  public JInternalFrame getJInternalFrame() { return this; }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsGraphFrame

  private IsApplicationFrame applicationFrame;
  public void setApplicationFrame(IsApplicationFrame _applicationFrame) { applicationFrame = _applicationFrame; }
  public IsApplicationFrame getApplicationFrame() { return applicationFrame; }

  private IsGraph graph;
  public void setGraph(IsGraph _graph) { graph = _graph; }
  public IsGraph getGraph() { return graph; }

  private IsRenderableGraph renderableGraph;
  public void setRenderableGraph(IsRenderableGraph _renderableGraph) { renderableGraph = _renderableGraph; }
  public IsRenderableGraph getRenderableGraph() { return renderableGraph; }

  private IsGraphStatusBar graphStatusBar;
  public void setGraphStatusBar(IsGraphStatusBar _graphStatusBar) { graphStatusBar = _graphStatusBar; }
  public IsGraphStatusBar getGraphStatusBar() { return graphStatusBar; }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsPausable

  private boolean isPaused = false;
  
  public void setPaused(boolean _isPaused) {
    renderableGraphLayoutManagerThread.setPaused(_isPaused);
    isPaused = _isPaused; //synchronized (this) { notify(); }}
  }
  public boolean isPaused() { return isPaused; }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua ApplicationInternalFrame (spec)

//    public void validate() {
//      super.validate();
//      //renderableGraph.validate();
//    }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsCommandProcessor

  public void processCommand(IsCommand _command) {
    _command.process(this);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsCommandable

  private SimpleCommandManagerThread commandManager;

  public void setCommandManager(IsCommandManager _commandManager) {
    if (_commandManager instanceof SimpleCommandManagerThread) {
      commandManager = (SimpleCommandManagerThread)_commandManager;
    } else {
      System.err.println("ERROR: BlueGraphFrame[" + this + "].setCommandManager(" + _commandManager + ") failed: not an instance of SimpleCommandManagerThread");
    }
  }

  public IsCommandManager getCommandManager() {  
    return commandManager;
  }
  
  public void queueCommand(IsCommand _command) {
    if (getCommandManager() != null) {
      getCommandManager().queueCommand(_command);
    } else {
      System.err.println("ERROR: BlueGraphFrame[" + this + "].queueCommand(" + _command + "): has no commandManager"); 
    }
  }
  
}
