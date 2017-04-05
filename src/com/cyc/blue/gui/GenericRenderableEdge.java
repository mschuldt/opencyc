/* $Id: GenericRenderableEdge.java,v 1.4 2002/05/22 01:33:22 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.Component;
import com.cyc.blue.graph.GenericEdge;
import com.cyc.blue.graph.IsEdge;
import com.cyc.blue.graph.IsNode;

/**
 * An edge implementation that extends a generic edge to also be a renderable edge.
 *
 * @author John Jantos
 * @date 2002/02/07
 * @version $Id: GenericRenderableEdge.java,v 1.4 2002/05/22 01:33:22 jantos Exp $
 */

public class GenericRenderableEdge extends GenericEdge implements IsEdge, IsRenderable, IsRenderableEdge {
  private static final boolean DEBUG = false;

  private Component edgeComponent;
  private IsRenderer nodeRenderer;

  private boolean isVisible = true;

  public GenericRenderableEdge(IsNode _head, IsNode _tail, Object _relation) {
    super(_head, _tail, _relation);
    setComponent(new JEdgePanel(this));
  }

  public String toString() {
    return "GenericEdge[" + getHead() + ", " + getTail() + ", " + getRelation() + "]";
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsEdge (inherited from GenericEdge)
  
  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsRenderable
  
  public void setComponent(Component _component) { edgeComponent = _component; }
  public Component getComponent() { return edgeComponent; }

  public void setRenderer(IsRenderer _renderer) { nodeRenderer = _renderer; }
  public IsRenderer getRenderer() { return nodeRenderer; }

  public double getXD() { 
    return 
      getRenderableHead().getXD() < getRenderableTail().getXD() ? 
      getRenderableHead().getXD() : getRenderableTail().getXD();
  }

  public double getYD() { 
    return 
      getRenderableHead().getYD() < getRenderableTail().getYD() ? 
      getRenderableHead().getYD() : getRenderableTail().getYD(); 
  }

  public double getHeightD() { 
    return 
      Math.max(getRenderableHead().getMaxYD(), getRenderableTail().getMaxYD()) -    
      Math.min(getRenderableHead().getMinYD(), getRenderableTail().getMinYD());
  }

  public double getWidthD() {
    return 
      Math.max(getRenderableHead().getMaxXD(), getRenderableTail().getMaxXD()) - 
      Math.min(getRenderableHead().getMinXD(), getRenderableTail().getMinXD());
  }

  public double getMinXD() { return getXD(); }
  public double getMinYD() { return getYD(); }

  public double getMaxXD() { return getXD() + getWidthD(); }
  public double getMaxYD() { return getYD() + getHeightD(); }

  public double getNormalizedXD() { 
    return 
      getRenderableHead().getNormalizedXD() < getRenderableTail().getNormalizedXD() ? 
      getRenderableHead().getNormalizedXD() : getRenderableTail().getNormalizedXD();
  }

  public double getNormalizedYD() { 
    return 
      getRenderableHead().getNormalizedYD() < getRenderableTail().getNormalizedYD() ? 
      getRenderableHead().getNormalizedYD() : getRenderableTail().getNormalizedYD(); 
  }

  public double getNormalizedHeightD() { 
    return getHeightD();
  }

  public double getNormalizedWidthD() {
    return getWidthD();
  }


//    public double getNormalizedXD() {
//      if (getRenderableGraph() != null) {
//        return getXD() - getRenderableGraph().getXD();
//      } else {
//        return getXD();
//      }
//    }
//    public double getNormalizedYD() {
//      if (getRenderableGraph() != null) {
//        return getYD() - getRenderableGraph().getYD();
//      } else {
//        return getYD();
//      }
//    }
//    public double getNormalizedWidthD() {
//      // account for zoom?
//      return getWidthD();
//    }
//    public double getNormalizedHeightD() {
//      // account for zoom?
//      return getHeightD();
//    }

//    public void validate() { 
//      getComponent().invalidate();
//      getComponent().validate();
//  //      if (getRenderableHead() != null && getRenderableTail() != null) {
//  //        getComponent().setSize(getWidth(), getHeight());
//  //        getComponent().setLocation(getXD(), getYD());
//  //      }
//    }

  public void render() {
    //System.out.println("AAAAAAAAAAA " + this);
    getRenderer().render(this);
  }

//    public void render(int _ms) { 
//      getComponent().setBounds((int)Math.round(getXD()), 
//  			     (int)Math.round(getYD()), 
//  			     (int)Math.round(getWidthD()), 
//  			     (int)Math.round(getHeightD()));
//      validate();
//      getRenderer().render(this,_ms);
//    }

  public void setXYD(double _x, double _y) { 
  }
  public void setWidthHeightD(double _width, double _height) {
  }

  public boolean isVisible() { // fancy look at node stuff?
    return isVisible;
  }
  public void setVisible(boolean _isVisible) { isVisible = _isVisible; }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsRenderableEdge

  public void setRenderableGraph(IsRenderableGraph _renderableGraph) {
    setGraph(_renderableGraph);
  }

  public IsRenderableGraph getRenderableGraph() {
    if (getGraph() instanceof IsRenderableGraph) {
      return (IsRenderableGraph)getGraph();
    } else {
      return null;
    }
  }

  public IsRenderableNode getRenderableHead() { return (IsRenderableNode)getHead(); }
  public IsRenderableNode getRenderableTail() { return (IsRenderableNode)getTail(); }
}
