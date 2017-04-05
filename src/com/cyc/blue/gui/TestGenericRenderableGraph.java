/* $Id: TestGenericRenderableGraph.java,v 1.4 2002/05/22 01:33:22 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import com.cyc.blue.graph.IsEdge;
import com.cyc.blue.graph.IsNode;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * A junit.framework.TestCase for testing the GenericRenderableGraph class.
 *
 * @author John Jantos
 * @version $Id: TestGenericRenderableGraph.java,v 1.4 2002/05/22 01:33:22 jantos Exp $
 */

public class TestGenericRenderableGraph extends TestCase {
  public static final String TEST_ALL_TEST_TYPE = "UNIT";

  /** Construct a new TestGenericRenderableGraph object.
   * @param name the test case name.
   */
  public TestGenericRenderableGraph(String name) {
    super(name);
  }
  
  /**
   * Test adding/removing a node to/from an empty graph.
   */
  public void testNode() {
    GenericRenderableGraph g = new GenericRenderableGraph(GenericRenderableNode.class, GenericRenderableEdge.class);
    IsNode a = g.instantiateNode("a");
    // just the node
    Assert.assertNotNull(a);
    Assert.assert("node a should be an instance of IsNode", a instanceof IsNode);
    Assert.assert("node a should be an instance of GenericRenderableNode", a instanceof GenericRenderableNode);
    Assert.assertEquals("node a's core should be \"a\"", "a", a.getCore());
    Assert.assertNull("node a's graph should be null", a.getGraph());
    // node in/out of graph
    Assert.assertEquals("graph g's findNode(\"a\") method should return node a", a, g.findNode("a"));
    g.addNode(a);
    Assert.assert("after g.add(a), graph g should contain node a", g.containsNode(a));
    g.removeNode(a);
    Assert.assert("after g.remove(a), graph g should not contain node a", !g.containsNode(a));
  }
  
  /**
   * Test adding/removing an edge to/from an empty graph.
   */
  public void testEdge() {
    GenericRenderableGraph g = new GenericRenderableGraph(GenericRenderableNode.class, GenericRenderableEdge.class);
    IsNode a = g.instantiateNode("a");
    IsNode b = g.instantiateNode("b");
    IsEdge e = g.instantiateEdge(a, b, "relation");
    // just the edge
    Assert.assertNotNull("edge e should not be null", e);
    Assert.assert("edge e should be an instance of IsEdge", e instanceof IsEdge);
    Assert.assert("edge e should be an instance of GenericRenderableEdge", e instanceof GenericRenderableEdge);
    Assert.assertEquals("edge e's tail should be node a", a, e.getTail());
    Assert.assertEquals("edge e's head should be node b", b, e.getHead());
    Assert.assertEquals("edge e's relation should be \"relation\"", "relation", e.getRelation());
    Assert.assertEquals("edge e's otherNode from node a should be node b", b, e.getOtherNode(a));
    Assert.assertEquals("edge e's otherNode from node b should be node a", a, e.getOtherNode(b));
    e.setHead(a);
    Assert.assertEquals(a, e.getHead());
    Assert.assertEquals(a, e.getOtherNode(a));
    Assert.assertEquals(null, e.getOtherNode(b));
    e.setHead(b);
    Assert.assertEquals(a, e.getTail());
    Assert.assertEquals(b, e.getHead());
    Assert.assertEquals(b, e.getOtherNode(a));
    Assert.assertEquals(a, e.getOtherNode(b));
    Assert.assertNull(e.getGraph());
    // edge in/out if graph
    Assert.assertEquals(e, g.findEdge(a, b, "relation"));
    g.addEdge(e);
    Assert.assert(g.containsEdge(e));
    Assert.assert(g.containsNode(a));
    Assert.assert(g.containsNode(b));
    g.removeEdge(e);
    Assert.assert(!g.containsEdge(e));
  }

  /**
   * Test creation of a graph with 6 nodes and 5 edges and checking of node/edge incidence.
   */
  public void testIncidence() {
    //         a
    //        /1\2
    //       b   c    (all edges pointing downward)
    //      /3\4  \5
    //     d   e   f 
    GenericRenderableGraph g = new GenericRenderableGraph(GenericRenderableNode.class, GenericRenderableEdge.class);
    IsNode a = g.instantiateNode("a");
    IsNode b = g.instantiateNode("b");
    IsNode c = g.instantiateNode("c");
    IsNode d = g.instantiateNode("d");
    IsNode e = g.instantiateNode("e");
    IsNode f = g.instantiateNode("f");
    IsEdge e1 = g.instantiateEdge(a, b, "relation");
    IsEdge e2 = g.instantiateEdge(a, c, "relation");
    IsEdge e3 = g.instantiateEdge(b, d, "relation");
    IsEdge e4 = g.instantiateEdge(b, e, "relation");
    IsEdge e5 = g.instantiateEdge(c, f, "relation");
    g.addEdge(e1);
    g.addEdge(e2);
    g.addEdge(e3);
    g.addEdge(e4);
    g.addEdge(e5);
    Assert.assertEquals("node a should have 2 incident edges", 2, g.incidentEdgesCount(a));
    Assert.assertEquals("node b should have 3 incident edges", 3, g.incidentEdgesCount(b));
    Assert.assertEquals("node c should have 2 incident edges", 2, g.incidentEdgesCount(c));
    Assert.assertEquals("node d should have 1 incident edges", 1, g.incidentEdgesCount(d));
    Assert.assertEquals("node e should have 1 incident edges", 1, g.incidentEdgesCount(e));
    Assert.assertEquals("node f should have 1 incident edges", 1, g.incidentEdgesCount(f));
  }

}
