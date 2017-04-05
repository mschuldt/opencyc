/* $Id: CycNode.java,v 1.13 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.cyc;

import com.cyc.blue.graph.GenericNode;
import com.cyc.blue.graph.IsNode;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycFort;

/**
 * A node representing a CycFort.
 *
 * @author John Jantos
 * @date 2001/08/06
 * @version $Id: CycNode.java,v 1.13 2002/05/23 22:30:14 jantos Exp $
 */

public class CycNode extends GenericNode implements IsNode {
  private String label;
  private double importance;
  
//    public CycNode(Object _core) {
//      super(_core);
//      //label = _core.toString();
//      //label = BlueCycAccess.getParaphrase((CycFort)_core);
//    }

  public CycNode(Object _core) throws Exception {
    if (_core instanceof CycFort) {
      setCore(_core);
    } else if (_core instanceof String) {
      try {
	setCore(BlueCycAccess.current().getKnownConstantByName((String)_core));
      } catch (org.opencyc.api.CycApiException e) {
	throw new Exception("constructor CycNode(" + _core + ") failed with exception " + e);
      } catch (java.net.UnknownHostException e) {
	throw new Exception("constructor CycNode(" + _core + ") failed with exception " + e);
      } catch (java.io.IOException e) {
	throw new Exception("constructor CycNode(" + _core + ") failed with exception " + e);
      }
    }
    setLabel(getCore().toString());
    //setLabel(BlueCycAccess.getParaphrase((CycFort)getCore()));
  }

  public double getImportance() {
    return importance;
  }
  
  public void addImportance(double _importance) {
    importance += _importance;
  }
  
  public String toString() {
    return "CycNode[" + getCore().toString() + "]";
  }

}

/*
protected CycNode(Object _core, String _label) {
  core = _core;
  label = _label;
  //nodesByCores.put(new Integer(((CycFort)_core).getId()), this);
  synchronized (nodesByCores) {
    nodesByCores.put((CycFort)_core, this);
  }
}

public static IsNode instantiate(Object _core) {
  IsNode found = findNode(_core);
  if (found != null) {
    return found;
  } else {
    return new CycNode(_core);
  }
}

public static IsNode instantiate(Object _core, String _label) {
  CycNode found = findNode(_core);
  if (found != null) {
    return found;
  } else {
    return new CycNode(_core, _label);
  }
}

public void forget() {
  synchronized (nodesByCores) {
    nodesByCores.remove(core);
  }
}

public static CycNode findNode(Object _object) {
  //return (CycNode)nodesByCores.get(new Integer(((CycFort)_object).getId()));
  CycNode foundNode;
  synchronized (nodesByCores) {
    foundNode = (CycNode)nodesByCores.get(_object);
  }
  return foundNode;
}

public Iterator getNodes() {
  Iterator nodesIterator;
  synchronized (nodesByCores) {
    nodesIterator = nodesByCores.values().iterator();
  }
  return nodesIterator;
}


public Object getCore() { return core; }
public void setCore(Object _core) { core = _core; }
*/
