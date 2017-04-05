/* $Id: GenericNode.java,v 1.6 2002/05/23 22:35:43 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.graph;

/**
 * A generic implementation of the IsNode interface.
 *
 * @author John Jantos
 * @date 2002/01/17
 * @version $Id: GenericNode.java,v 1.6 2002/05/23 22:35:43 jantos Exp $
 */

public class GenericNode implements IsNode {
  private static final boolean DEBUG = false;

  private IsGraph graph;
  private Object core = null;
  private boolean isFocus = false;
  private String label = "";
  
  public GenericNode() {
    setLabel("unlabeled");
  } 

  public GenericNode(Object _nodeCore) {
    setCore(_nodeCore);
    if (getCore() instanceof IsNode) {
      setLabel(((IsNode)getCore()).getLabel());
    } else {
      setLabel(getCore().toString());
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsNode

  public void setGraph(IsGraph _graph) { graph = _graph; }
  public IsGraph getGraph() { return graph; }

  public void setCore(Object _core) { core = _core; }
  public Object getCore() { return core; }

  public boolean isFocus() { return isFocus; }
  public void setFocus(boolean _isFocus) { isFocus = _isFocus; }

  public void setLabel(String _label) { label = _label; }
  public String getLabel() { return label; }
}
