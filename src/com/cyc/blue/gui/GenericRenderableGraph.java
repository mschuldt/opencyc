/* $Id: GenericRenderableGraph.java,v 1.5 2002/05/22 01:33:22 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import com.cyc.blue.graph.GenericGraph;
import com.cyc.blue.graph.IsEdge;
import com.cyc.blue.graph.IsNode;
 
/**
 * An graph implementation that extends a generic graph to also be a renderable graph.
 *
 * @author John Jantos
 * @date 2002/02/07
 * @version $Id: GenericRenderableGraph.java,v 1.5 2002/05/22 01:33:22 jantos Exp $
 */

public class GenericRenderableGraph extends GenericGraph implements IsRenderableGraph {
  private static final boolean DEBUG = false;
  
  private IsGraphFrame graphFrame;
  private IsRenderer graphRenderer;
  private IsNodeRenderer nodeRenderer = new GenericNodeRenderer();
  private IsEdgeRenderer edgeRenderer = new GenericEdgeRenderer();

  private boolean isVisible = true; //??

  private double x = 0, y = 0;
  private double width = 6400, height = 4800;
  private double normalizedX = width / 2, normalizedY = height / 2;

  private JScrollPane scrollPane;
  private JViewport viewport;

  public GenericRenderableGraph(Class _nodeClass, Class _edgeClass) {
    super(_nodeClass, _edgeClass);

    JGraphPanel graphPanel = new JGraphPanel(this);

    // no scroll
    //setComponent(graphPanel);

    // scroll
    graphPanel.setPreferredSize(new Dimension((int)Math.round(width),
					      (int)Math.round(height)));
    scrollPane = new JScrollPane(graphPanel);
    viewport = scrollPane.getViewport();
    viewport.setBackground(new Color(220,220,240));
    //scrollPane.setPreferredSize(new Dimension(320,240));
    setComponent(scrollPane);
    viewport.setViewPosition(new Point((int)Math.round(width / 2) - 320, 
				       (int)Math.round(height / 2) - 240));

    setContainer(graphPanel);

    setRenderer(new GenericGraphRenderer());
    //getComponent().setBounds(0, 0, 2000, 2000);
    getComponent().repaint();
  }

  public GenericRenderableGraph(IsGraphFrame _graphFrame, Class _nodeClass, Class _edgeClass) {
    this(_nodeClass, _edgeClass);
    setGraphFrame(_graphFrame);
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsGraph

  public void addNode(IsNode _node) {
    if (DEBUG) { System.out.println(this + ".addNode(" + _node + ")"); }
    if (_node instanceof IsRenderableNode) {
      if (!containsNode(_node)) {
	if (getContainer() != null) {
	  if (DEBUG) { System.out.println("--> addNode: getContainer() = " + getContainer()); }
	  if (DEBUG) { System.out.println("--> addNode: _node.getComponent() = " + ((IsRenderableNode)_node).getComponent()); }
	  ((IsRenderableNode)_node).setRenderer(nodeRenderer);
	  super.addNode(_node);
	  getContainer().add(((IsRenderableNode)_node).getComponent(), 0); // @todo: unsafe
//  	  IsRenderableNodeListener mouseInputListener = new RenderableNodeListener_Generic((IsRenderableNode)_node, getGraphFrame());
//  	  ((IsRenderableNode)_node).getComponent().addMouseMotionListener(mouseInputListener);
//  	  ((IsRenderableNode)_node).getComponent().addMouseListener(mouseInputListener);
	  
	  getContainer().validate();
	} else {
	  System.err.println("ERROR: GenericRenderableGraph[" + this + "].addNode(" + _node + "): getContainer() returns + " + getContainer());
	}
      }
    } else {
      if (DEBUG) { System.out.println("--> addNode: failed to add Component!"); }
    }
  }
  public void removeNode(IsNode _node) {
    super.removeNode(_node);
    if (_node instanceof IsRenderableNode) {
      if (getContainer() != null) {
	getContainer().remove(((IsRenderableNode)_node).getComponent());
      }
    }
  }

  public void addEdge(IsEdge _edge) {
    if (DEBUG) { System.out.println(this + ".addEdge(" + _edge + ")"); }
    if (_edge instanceof IsRenderableEdge) {
      if (!containsEdge(_edge)) {
	if (getContainer() != null) {
	  if (DEBUG) { System.out.println("--> addEdge: getContainer() = " + getContainer()); }
	  if (DEBUG) { System.out.println("--> addEdge: _edge.getComponent() = " + ((IsRenderableEdge)_edge).getComponent()); }
	  ((IsRenderableEdge)_edge).setRenderer(edgeRenderer);
	  super.addEdge(_edge);
	  getContainer().add(((IsRenderableEdge)_edge).getComponent(), -1);
	} else {
	  System.err.println("ERROR: GenericRenderableGraph[" + this + "].addEdge(" + _edge + "): getContainer() returns + " + getContainer());
	}
      } else {
	if (DEBUG) { System.out.println("--> addEdge: failed to add Component!"); }
      }
    }
  }
  public void removeEdge(IsEdge _edge) {
    super.removeEdge(_edge);
    if (_edge instanceof IsRenderableEdge) {
      if (getContainer() != null) {
	getContainer().remove(((IsRenderableEdge)_edge).getComponent());
      }
    }
  }
		
  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsRenderable

  private Component graphComponent;
  
  public void setComponent(Component _component) {
    graphComponent = _component;
  }
  public Component getComponent() {
    return graphComponent;
  }

  public void setRenderer(IsRenderer _renderer) { graphRenderer = _renderer; }
  public IsRenderer getRenderer() { return graphRenderer; }

  public double getXD() { 
    //if (DEBUG) { System.out.println("TRACE: GenericRenderableGraph[" + this + "].getXD() --> " + x); }
    return x;
  }
  public double getYD() {     
    //if (DEBUG) { System.out.println("TRACE: GenericRenderableGraph[" + this + "].getYD() --> " + y); }
    return y;
  }
  public double getWidthD() { 
    //if (DEBUG) { System.out.println("TRACE: GenericRenderableGraph[" + this + "].getWidthD() --> " + width); }
    double possWidth = 0;
    if (getGraphFrame() != null &&
	getGraphFrame().getJInternalFrame() != null) {
      possWidth =  getGraphFrame().getJInternalFrame().getContentPane().getWidth();
    }
    if (possWidth > width) {
      setXYD(-possWidth / 2, getYD());
      return possWidth;
    } else {
      return width;
    }
  }
  public double getHeightD() { 
    //if (DEBUG) { System.out.println("TRACE: GenericRenderableGraph[" + this + "].getHeightD() --> " + height); }
    double possHeight = 0;
    if (getGraphFrame() != null &&
	getGraphFrame().getJInternalFrame() != null) {
      possHeight =  getGraphFrame().getJInternalFrame().getContentPane().getHeight();
    }
    if (possHeight > height) {
      setXYD(getXD(), -possHeight / 2);
      return possHeight;
    } else {
      return height;
    }
  }
  
  public double getNormalizedXD() {
    return normalizedX;
  }
  public double getNormalizedYD() {
    return normalizedY;
  }
  public double getNormalizedWidthD() {
    // account for zoom?
    return getWidthD();
  }
  public double getNormalizedHeightD() {
    // account for zoom?
    return getHeightD();
  }

  public void setXYD(double _x, double _y) { 
    x = _x; y = _y;
    //getComponent().setLocation((int)Math.round(_x), (int)Math.round(_y));
  }

  public void setWidthHeightD(double _width, double _height) {
    width = _width; height = _height;
    //if (DEBUG) { System.err.println("--> " + this + ".setWidthHeightD(" + _width + ", " + _height + ")"); }
    //getComponent().setSize((int)Math.round(_width), (int)Math.round(_height));
  }

  public void setPreferredSizeForScroller() {
    double lastMaxX = getMinXD(), lastMinX = getMaxXD();
    double lastMaxY = getMinYD(), lastMinY = getMaxYD();
    double maxX = getMinXD(), minX = getMaxXD();
    double maxY = getMinYD(), minY = getMaxYD();
    Iterator nodesIterator = visibleNodesIterator();
    if (nodesIterator != null) {
      while (nodesIterator.hasNext()) {
	IsRenderableNode renderableNode = (IsRenderableNode)nodesIterator.next();
	double nodeMaxX = renderableNode.getMaxXD(), nodeMinX = renderableNode.getMinXD();
	double nodeMaxY = renderableNode.getMaxYD(), nodeMinY = renderableNode.getMinYD();
	if (nodeMaxX > maxX) { maxX = nodeMaxX; }
	if (nodeMinX < minX) { minX = nodeMinX; }
	if (nodeMaxY > maxY) { maxY = nodeMaxY; }
	if (nodeMinY < minY) { minY = nodeMinY; }
      }
      normalizedX = ((maxX - minX) - getComponent().getWidth()) / 2;
      normalizedY = ((maxY - minY) - getComponent().getHeight()) / 2;
      // ??? setXYD(minX, minY);
      setWidthHeightD(maxX - minX, maxY - minY);
      ((JComponent)getComponent()).setPreferredSize(new Dimension((int)Math.round(getNormalizedWidthD()), 
								  (int)Math.round(getNormalizedHeightD())));
      ((JComponent)getComponent()).revalidate();
    }
  }

  public void setPreferredSizeForScrollerOLD() {
    double maxX = getMinXD(), minX = getMaxXD();
    double maxY = getMinYD(), minY = getMaxYD();
    Iterator nodesIterator = visibleNodesIterator();
    if (nodesIterator != null) {
      while (nodesIterator.hasNext()) {
	IsRenderableNode renderableNode = (IsRenderableNode)nodesIterator.next();
	double nodeMaxX = renderableNode.getMaxXD(), nodeMinX = renderableNode.getMinXD();
	double nodeMaxY = renderableNode.getMaxYD(), nodeMinY = renderableNode.getMinYD();
	if (nodeMaxX > maxX) { maxX = nodeMaxX; }
	if (nodeMinX < minX) { minX = nodeMinX; }
	if (nodeMaxY > maxY) { maxY = nodeMaxY; }
	if (nodeMinY < minY) { minY = nodeMinY; }
      }
      setXYD((maxX - minX) / (getComponent().getWidth() / 2), 
	     (maxX - minY) / (getComponent().getHeight() / 2));
      setWidthHeightD(maxX - minX, maxY - minY);
      ((JComponent)getComponent()).setPreferredSize(new Dimension((int)Math.round(getNormalizedWidthD()), 
								  (int)Math.round(getNormalizedHeightD())));
      ((JComponent)getComponent()).revalidate();
    }
  }

  public void render() { 
    //setPreferredSizeForScroller(); 
    getComponent().repaint();
  }
  public void render(int _ms) { 
    //setPreferredSizeForScroller(); 
    getComponent().repaint(_ms); 
  }

  public boolean isVisible() { return isVisible; }
  public void setVisible(boolean _isVisible) { isVisible = _isVisible; }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsRenderableGraph

  private Container graphContainer;;

  public void setGraphFrame(IsGraphFrame _graphFrame) { graphFrame = _graphFrame; }
  public IsGraphFrame getGraphFrame() { return graphFrame; }

  public double getMinXD() { return getXD(); }
  public double getMinYD() { return getYD(); }
  public double getMaxXD() { return getXD() + getWidthD(); }
  public double getMaxYD() { return getYD() + getHeightD(); }

  public Set getVisibleNodes() {
    HashSet visibleNodes = new HashSet();
    Iterator nodesIterator = nodesIterator();
    if (nodesIterator != null) {
      while (nodesIterator.hasNext()) {
	IsRenderableNode node = (IsRenderableNode)nodesIterator.next();
	if (node.isVisible()) {
	  visibleNodes.add(node);
	}
      }
    }
    return visibleNodes;
  }

  public Iterator visibleNodesIterator() {
    return getVisibleNodes().iterator();
  }

  public Set getVisibleEdges() {
    HashSet visibleEdges = new HashSet();
    Iterator edgesIterator = edgesIterator();
    if (edgesIterator != null) {
      while (edgesIterator.hasNext()) {
	Object possibleRenderableEdge = edgesIterator.next();
	if (possibleRenderableEdge instanceof IsRenderableEdge) {
	  IsRenderableEdge edge = (IsRenderableEdge)possibleRenderableEdge;
	  //if (edge.isVisible()) {
	    visibleEdges.add(edge);
	    // }
	}
      }
    }
    return visibleEdges;
  }

  public Iterator visibleEdgesIterator() {
    return getVisibleEdges().iterator();
  }

  public Set getVisibleIncidentEdges(IsRenderableNode _node) {
    HashSet edges = new HashSet();
    Iterator incidentEdgesIterator = incidentEdgesIterator(_node);
    if (incidentEdgesIterator != null) {
      while (incidentEdgesIterator.hasNext()) {
	//if (edge.isVisible()) {
	  IsEdge edge = (IsEdge)incidentEdgesIterator.next();
	  edges.add(edge);
	  //}
      }
    }
    return edges;
  }
  
  public Iterator visibleIncidentEdgesIterator(IsRenderableNode _node) {
    return getVisibleIncidentEdges(_node).iterator();
  }
  
  public boolean isNodeVisible(IsNode _node) {
    return true;
  }

//      IsNode coreNode = (IsNode)_node.getCore();
//      if (coreNode == null) {
//        return true;
//      } else {
//        return sourceGraph.isNodeVisible(coreNode);
//      }
//    }

//    // put in interface?
//    public boolean isNodeFocus(IsNode _node) {
//      IsNode coreNode = (IsNode)_node.getCore();
//      if (coreNode == null) {
//        return false;
//      } else {
//        return sourceGraph.isNodeFocus(coreNode);
//      }
//    }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsEventServiceSubscriber

//    private IsEdge instantiateEdge(IsEdge _edge) {
//      IsNode head = _edge.getHead();
//      IsNode tail = _edge.getTail();
//      Object relation = _edge.getRelation(); // no mapping??
//      IsNode newHead = instantiateNode(head);
//      IsNode newTail = instantiateNode(tail);
//      return instantiateEdge(newHead, newTail, relation);
//    }

//    public void inform(IsEvent _event) {
//      if (DEBUG) { System.out.println(this + ".inform(" + _event + ")"); }
//      System.out.println("************ALL HELL***************");
//      if (_event instanceof GraphChangeEvent &&
//  	((GraphChangeEvent)_event).getGraph() == sourceGraph) { // somewhat redundant since we filtered
//        System.out.println("************BREAKING LOOSE***************");
//        // recast
//        GraphChangeEvent graphChangeEvent = (GraphChangeEvent)_event;

//        Iterator removedEdgesIterator = graphChangeEvent.removedEdgesIterator();
//        while (removedEdgesIterator.hasNext()) {
//  	IsEdge removedEdge = (IsEdge)removedEdgesIterator.next();
//  	if (removedEdge != null) {
//  	  IsEdge newEdge = instantiateEdge(removedEdge);
//  	  if (newEdge != null) {
//  	    removeEdge(newEdge);
//  	  }
//  	}
//        }

//        Iterator removedNodesIterator = graphChangeEvent.removedNodesIterator();
//        while (removedNodesIterator.hasNext()) {
//  	IsNode removedNode = (IsNode)removedNodesIterator.next();
//  	if (removedNode != null) {
//  	  IsNode newNode = instantiateNode(removedNode);
//  	  if (newNode != null) {
//  	    removeNode(newNode);
//  	  }
//  	}
//        }

//        Iterator addedNodesIterator = graphChangeEvent.addedNodesIterator();
//        while (addedNodesIterator.hasNext()) {
//  	IsNode addedNode = (IsNode)addedNodesIterator.next();
//  	if (DEBUG) { System.out.println("--> inform: addedNodesIterator.next() = " + addedNode); }
//  	if (addedNode != null) {
//  	  IsNode newNode = instantiateNode(addedNode);
//  	  if (DEBUG) { System.out.println("--> inform: newNode = " + newNode); }
//  	  if (newNode != null) {
//  	    if (DEBUG) { System.out.println("--> inform: DIE MONKEY DIE"); }
//  	    addNode(newNode);
//  	    if (DEBUG) { System.out.println("--> inform: LIVE MONKEY LIVE"); }
//  	  }
//  	}
//        }
      

//        Iterator addedEdgesIterator = graphChangeEvent.addedEdgesIterator();
//        while (addedEdgesIterator.hasNext()) {
//  	IsEdge addedEdge = (IsEdge)addedEdgesIterator.next();
//  	if (DEBUG) { System.out.println("--> inform: addedEdgesIterator.next() = " + addedEdge); }
//  	if (addedEdge != null) {
//  	  IsEdge newEdge = instantiateEdge(addedEdge);
//  	  if (DEBUG) { System.out.println("--> inform: newEdge = " + newEdge); }
//  	  if (newEdge != null) {
//  	    addEdge(newEdge);
//  	  }
//  	}
//        }
      
//      }
//    }

  public Set getSelectedNodes() {
    HashSet selectedNodes = new HashSet();
    Iterator nodesIterator = nodesIterator();
    if (nodesIterator != null) {
      while (nodesIterator.hasNext()) {
	IsRenderableNode node = (IsRenderableNode)nodesIterator.next();
	if (node.isSelected()) {
	  selectedNodes.add(node);
	}
      }
    }
    return selectedNodes;
  }

  public Iterator selectedNodesIterator() {
    return getSelectedNodes().iterator();
  }

  public Set getFocusNodes() {
    HashSet focusNodes = new HashSet();
    Iterator nodesIterator = nodesIterator();
    if (nodesIterator != null) {
      while (nodesIterator.hasNext()) {
	IsRenderableNode node = (IsRenderableNode)nodesIterator.next();
	if (node.isFocus()) {
	  focusNodes.add(node);
	}
      }
    }
    return focusNodes;
  }

  public Iterator focusNodesIterator() {
    return getFocusNodes().iterator();
  }

  ////////////////////////////////////////////////////////////////////////////////
  // IsContainer

  public void setContainer(Container _container) {
    graphContainer = _container;
  }

  public Container getContainer() {
    return graphContainer;
  }

}

/*

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsCommandProcessor

  public void processCommand(IsCommand _command) {
    if (true) { System.out.println("TRACE: GenericRenderableGraph[" + this + "].processCommand(" + _command + ")"); }
    _command.process(this);
  }
//    public void processCommand(IsCommand _command) {
//      IsCommandable nextCommandable = _command.process(this);
//      if (nextCommandable == null) {
//        if (getCommandManager() != null) {
//  	getCommandManager.queue(_command);
//        } else {
//  	System.err.println("ERROR: GenericRenderableGraph[" + this + "].processCommand(" + _command + "): getCommandManager() is null!");
//        }
//      } else {
//        // @todo: do some infinite recursion checking?
//        nextCommandable.processCommand(_command);
//      }
//    }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsCommandable

  IsCommandManager commandManager;
  public void setCommandManager(IsCommandManager _commandManager) { commandManager = _commandManager; }
  public IsCommandManager getCommandManager() { return commandManager; }
  public void queueCommand(IsCommand _command) {
    getCommandManager().queueCommand(_command);
  }

*/
