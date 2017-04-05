/* $Id: CycEdge.java,v 1.8 2002/05/23 22:35:04 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.cyc;

import com.cyc.blue.graph.GenericEdge;
import com.cyc.blue.graph.IsEdge;
import com.cyc.blue.graph.IsNode;

/**
 * An edge representing a binary Cyc GAF.
 *
 * @author John Jantos
 * @date 2001/08/06
 * @version $Id: CycEdge.java,v 1.8 2002/05/23 22:35:04 jantos Exp $
 */

public class CycEdge extends GenericEdge implements IsEdge {

  public CycEdge(IsNode _tailNode, IsNode _headNode, Object _relation) {
    super(_tailNode, _headNode, _relation);
  }

  public String getLabel() {
    return getRelation().toString();
  }

  public String toString() {
    return "CycEdge[" + getTail() + ", " + getHead() + ", " + getRelation() + "]";
  }

}
