/* $Id: GraphFrameMouseListener.java,v 1.4 2002/05/30 16:18:43 jantos Exp $
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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import com.cyc.blue.command.IsCommand;
import com.cyc.blue.cyc.BlueCycAccess;
import com.cyc.blue.cyc.CycGraphCommand_AddGraph;
import com.cyc.blue.graph.IsNode;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycSymbol;

/**
 * A single listener for a graph frame.  also handles events meant for
 * nodes/edges by using the component hierarchy.
 *
 * @author John Jantos
 * @date 2002/05/15
 * @version $Id: GraphFrameMouseListener.java,v 1.4 2002/05/30 16:18:43 jantos Exp $
 */

public class GraphFrameMouseListener implements MouseListener, MouseMotionListener {
  private static final boolean DEBUG = false;

  private IsGraphFrame graphFrame;

  private int savedX = 0, savedY = 0;

  //final static long DOUBLE_CLICK_TIME = 500;
  //private IsCommandManager commandManager;

//    public GraphFrameMouseListener(IsRenderableNode _renderableNode, IsCommandManager _commandManager) {
//      renderableNode = _renderableNode;
//      commandManager = _commandManager;
//    }

  public GraphFrameMouseListener(IsGraphFrame _graphFrame) {
    if (DEBUG) { System.out.println("TRACE: GraphFrameMouseListener[" + this + "](" + _graphFrame + ")"); }
    setGraphFrame(_graphFrame);
  }

  public void setGraphFrame(IsGraphFrame _graphFrame) {
    graphFrame = _graphFrame;
  }
  public IsGraphFrame getGraphFrame() {
    return graphFrame;
  }
    
  /**
   * Determine the "graph object" that we are manipulating -- an instance of IsGraph, IsEdge, or IsNode.
   * We depend on the fact that there is only one mouse cursor to save time calculating.
   */

  private IsRenderable currentRenderable = null;

  private void determineRenderable(MouseEvent e) {
    //System.out.println("event = " + e);
    Component eventComponent = e.getComponent();
    //System.out.println("eventComponent = " + eventComponent);
    Component inComponent;
    if (eventComponent instanceof Container) {
      inComponent = ((Container)eventComponent).findComponentAt(e.getX(), e.getY());
    } else {
      inComponent = eventComponent;
    }
    //System.out.println("inComponent = " + inComponent);
    if (inComponent instanceof JGraphPanel) {
      currentRenderable = getGraphFrame().getRenderableGraph();
    } else if (inComponent instanceof JNodePanel) {
      currentRenderable = ((JNodePanel)inComponent).getRenderableNode();
    } else if (inComponent instanceof JEdgePanel) {
      currentRenderable = ((JEdgePanel)inComponent).getRenderableEdge();
    }
    //System.out.println("currentRenderable = " + currentRenderable);
  }

  private void unDetermineRenderable() {
    currentRenderable = null;
  }
   
  private IsRenderable getCurrentRenderable() {
    return currentRenderable;
  }

  ////////////////////////////////////////////////////////////////////////////////
  
  JBoundingBox boundingBox = null;

  private class JBoundingBox extends JComponent {
    private IsRenderableGraph renderableGraph;

    public JBoundingBox(IsRenderableGraph _renderableGraph) {
      renderableGraph = _renderableGraph;
      setVisible(true);
      setOpaque(false);
      setLayout(null);
      setSize(1,1); // non zero initial size.
      validate();
    }

//      public void setLocation(int _x, int _y) {
//        super.setLocation(_x + (int)Math.round(renderableGraph.getNormalizedXD()),
//  			_y + (int)Math.round(renderableGraph.getNormalizedYD()));
//      }
    
    public void paintComponent(Graphics _g) {
      if (_g instanceof Graphics2D) {
	Graphics2D _g2d = (Graphics2D)_g;
	Shape shape = new Rectangle(0, 0, getWidth()-1, getHeight()-1);
	_g2d.setPaint(Color.red);
	_g2d.draw(shape);
      }
    }
  }

  /********************************************************************************
   * MouseListener
   ********************************************************************************
   */

  public void mouseClicked(MouseEvent e) {
    determineRenderable(e);
    int modifiers = e.getModifiers();
    if ((modifiers & MouseEvent.BUTTON1_MASK) != 0) {
      mouseClickedButton1(e);
    } else if ((modifiers & MouseEvent.BUTTON2_MASK) != 0) {
      mouseClickedButton2(e);
    } else if ((modifiers & MouseEvent.BUTTON3_MASK) != 0) {
      mouseClickedButton3(e);
    }
    unDetermineRenderable();
  }

// masks ALT_MASK, BUTTON1_MASK, BUTTON2__MASK, BUTTON3_MASK, CTRL_MASK, META_MASK, and SHIFT_MASK. 

  public void mouseClickedButton1(MouseEvent e) {
    int modifiers = e.getModifiers();

    if (getCurrentRenderable() instanceof IsRenderableNode) {
      IsRenderableNode renderableNode = (IsRenderableNode)getCurrentRenderable();

      if ((modifiers & MouseEvent.SHIFT_MASK) != 0) {
	//getRenderableNode().processCommand(new RenderableGraphCommand_ClearSelected(getRenderableNode().getRenderableGraph()));
	renderableNode.toggleSelected();
	renderableNode.render();
	
	
	//      } else if ((modifiers & MouseEvent.ALT_MASK) != 0) {
	//        toggleAnchored();
	
      } else {
      }
    }	
    
    if (e.getClickCount() == 2) {
      // double left click.
    }
  }

  public void mouseClickedButton2(MouseEvent e) {
  }
  
  public void mouseClickedButton3(MouseEvent e) {
  }
 
  public void mouseEntered(MouseEvent e) {
    //System.out.println("entered node");
  }

  public void mouseExited(MouseEvent e) {
    //System.out.println("exited node");
  }
  
  public void mousePressed(MouseEvent e) {
    determineRenderable(e);
    int modifiers = e.getModifiers();
    if ((modifiers & MouseEvent.BUTTON1_MASK) != 0) {
      mousePressedButton1(e);
    } else if ((modifiers & MouseEvent.BUTTON2_MASK) != 0) {
      mousePressedButton2(e);
    } else if ((modifiers & MouseEvent.BUTTON3_MASK) != 0) {
      mousePressedButton3(e);
    }
  }

  public void mousePressedButton1(MouseEvent e) {
   if (DEBUG) { System.out.println("TRACE: GraphFrameMouseListener[" + this + "].mousePressedButton1(" + e + ")"); }


    if (getCurrentRenderable() instanceof IsRenderableNode) {
      IsRenderableNode renderableNode = (IsRenderableNode)getCurrentRenderable();
      renderableNode.setLocked(true);
      savedX = e.getX();
      savedY = e.getY();
    } else if (getCurrentRenderable() != null) {
      // start bounding box
      savedX = e.getX();
      savedY = e.getY();

      boundingBox = new JBoundingBox(getGraphFrame().getRenderableGraph());
      boundingBox.setLocation(savedX, savedY);
      getGraphFrame().getRenderableGraph().getContainer().add(boundingBox);

    }


    //System.out.println("saved_x: " + saved_x);
    //System.out.println("saved_y: " + saved_y);
    //getRenderableNode().setMovable(false);
  }
  
  public void mousePressedButton2(MouseEvent e) {
  }


  public void mousePressedButton3(MouseEvent e) {
    // popup context menu
    JPopupMenu popup; 
    JMenuItem menuItem;

    //...where the GUI is constructed:
    //Create the popup menu.
    popup = new JPopupMenu();

    if (getCurrentRenderable() instanceof IsRenderableNode) {
      final IsRenderableNode renderableNode = (IsRenderableNode)getCurrentRenderable();
      // Lock/Unlock
      if (renderableNode.isLocked()) {
	menuItem = new JMenuItem("Unlock");
	menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ee) {
	      renderableNode.setLocked(false);
	    }
	  });
      } else {
	menuItem = new JMenuItem("Lock");
	menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ee) {
	      renderableNode.setLocked(true);
	    }
	  });
      }
      popup.add(menuItem);
      
      // Add Min Forward True links w/ genls(?)
      
      menuItem = new JMenuItem("Add min-genls");
      menuItem.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent ee) {
	    HashSet predSet = new HashSet();
	    try {
	      predSet.add(BlueCycAccess.genls);
	    } catch (Exception e) {
	    }
	    HashSet fortSet = new HashSet();
	    fortSet.add(((IsNode)renderableNode.getCore()).getCore());
	    HashSet filterFnSet = new HashSet();
	    filterFnSet.add(new CycSymbol("BFF-ARBITRARY-UNIONS"));
	    filterFnSet.add(new CycSymbol("BFF-CYC-KB-SUBSET-COLLECTIONS"));
	    //filterFnSet.add(new CycSymbol("BFF-RKF-IRRELEVANT-TERMS"));
	    CycList argList = new CycList();
	    argList.add(new CycSymbol(":PREDS"));
	    argList.add(new CycList(predSet));
	    argList.add(new CycSymbol(":FORTS"));
	    argList.add(new CycList(fortSet));
	    argList.add(new CycSymbol(":FILTER-FNS"));
	    argList.add(new CycList(filterFnSet));
	    argList.add(new CycSymbol(":DEPTH"));
	    argList.add(new Integer(1));
	    argList.add(new CycSymbol(":MT"));
	    try {
	      argList.add(BlueCycAccess.current().getConstantByName("BiologyMt"));
	    } catch (Exception e) {
	    }
	    //argList.add(_params);
	    CycList quoteList = new CycList();
	    quoteList.add(new CycSymbol("QUOTE"));
	    quoteList.add(argList);
	    CycList paramList = new CycList();
	    paramList.add(new CycSymbol("BBF-MIN-FORWARD-TRUE"));
	    paramList.add(quoteList);
	    getGraphFrame().processCommand(new CycGraphCommand_AddGraph(paramList));
	    //getRenderableGraph().processCommand(new CycGraphCommand_AddNode(getRenderableNode(), // genls, 1));
	    //));
	  }
	});
      popup.add(menuItem);

      menuItem = new JMenuItem("Add min-specs");
      menuItem.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent ee) {
	    HashSet predSet = new HashSet();
	    try {
	      predSet.add(BlueCycAccess.genls);
	    } catch (Exception e) {
	    }
	    HashSet fortSet = new HashSet();
	    fortSet.add(((IsNode)renderableNode.getCore()).getCore());
	    HashSet filterFnSet = new HashSet();
	    filterFnSet.add(new CycSymbol("BFF-ARBITRARY-UNIONS"));
	    filterFnSet.add(new CycSymbol("BFF-CYC-KB-SUBSET-COLLECTIONS"));
	    //filterFnSet.add(new CycSymbol("BFF-RKF-IRRELEVANT-TERMS"));
	    CycList argList = new CycList();
	    argList.add(new CycSymbol(":PREDS"));
	    argList.add(new CycList(predSet));
	    argList.add(new CycSymbol(":FORTS"));
	    argList.add(new CycList(fortSet));
	    argList.add(new CycSymbol(":FILTER-FNS"));
	    argList.add(new CycList(filterFnSet));
	    argList.add(new CycSymbol(":DEPTH"));
	    argList.add(new Integer(1));
	    argList.add(new CycSymbol(":MT"));
	    try {
	      argList.add(BlueCycAccess.current().getConstantByName("BiologyMt"));
	    } catch (Exception e) {
	    }
	    //argList.add(_params);
	    CycList quoteList = new CycList();
	    quoteList.add(new CycSymbol("QUOTE"));
	    quoteList.add(argList);
	    CycList paramList = new CycList();
	    paramList.add(new CycSymbol("BBF-MIN-BACKWARD-TRUE"));
	    paramList.add(quoteList);
	    getGraphFrame().processCommand(new CycGraphCommand_AddGraph(paramList));
	    //getRenderableGraph().processCommand(new CycGraphCommand_AddNode(getRenderableNode(), // genls, 1));
	    //));
	  }
	});
      popup.add(menuItem);


    }

//      menuItem = new JMenuItem("Add adjacent generalizations and specializations");
//      menuItem.addActionListener(new ActionListener() {
//  	public void actionPerformed(ActionEvent ee) {
//  	  // @todo: @hack: unforgivable hack:
//  	  CycAccessInitializer cycAccessInitializer = new CycAccessInitializer("localhost", 3640, CycConnection.BINARY_MODE);
//  	  try {
//  	    BlueCycAccess blueCycAccess = new BlueCycAccess(cycAccessInitializer);
//  	  } catch (IOException e) {
//  	  } catch (CycApiException e) {}
//  	  HashSet preds = new HashSet();
//  	  preds.add("genls");
//  	  String fortString = ((CycNode)getRenderableNode().getCore()).getCore().toString();
//  	  HashSet defaultFilterFns = new HashSet();
//  	  defaultFilterFns.add("BFF-ARBITRARY-UNIONS");
//  	  defaultFilterFns.add("BFF-RKF-IRRELEVANT-TERMS");
//  	  commandManager.enqueueCommand(new CycGraphCommand_AddMinForwardAndBackwardTrue(cycAccessInitializer, preds, fortString, "RKF-TKCP-DemoEnvironmentMt", defaultFilterFns, 1));
//  	  commandManager.enqueueCommand(new CycGraphCommand_FocusAtNode(cycAccessInitializer, fortString));
//  	}
//        });
//      popup.add(menuItem);

    // todo: make work better for edges and graphs
    popup.show(getCurrentRenderable().getComponent(),
	       getCurrentRenderable().getComponent().getWidth(), 0);
/////	       e.getX(), e.getY());
  }

  public void mouseReleased(MouseEvent e){
    int modifiers = e.getModifiers();
    if ((modifiers & MouseEvent.BUTTON1_MASK) != 0) {
      mouseReleasedButton1(e);
    } else if ((modifiers & MouseEvent.BUTTON2_MASK) != 0) {
      mouseReleasedButton2(e);
    } else if ((modifiers & MouseEvent.BUTTON3_MASK) != 0) {
      mouseReleasedButton3(e);
    }
    unDetermineRenderable();
  }

  public void mouseReleasedButton1(MouseEvent e) {
    if (getCurrentRenderable() instanceof IsRenderableNode) {
      //
    } else if (getCurrentRenderable() != null) {
      // modify bounding box
      boundingBox.setVisible(false);
      getGraphFrame().getRenderableGraph().getContainer().remove(boundingBox);
      
    }
  }

  public void mouseReleasedButton2(MouseEvent e) {
  }

  public void mouseReleasedButton3(MouseEvent e) {
  }

  /********************************************************************************
   * MouseMotionListener
   ********************************************************************************
   */

  public void mouseMoved(MouseEvent e) {
    if (DEBUG) { System.out.println("TRACE: GraphFrameMouseListener[" + this + "].mouseMoved(" + e + ")"); }
  }

  public void mouseDragged(MouseEvent e) {
    int modifiers = e.getModifiers();
    if ((modifiers & MouseEvent.BUTTON1_MASK) != 0) {
      mouseDraggedButton1(e);
    } else if ((modifiers & MouseEvent.BUTTON2_MASK) != 0) {
      mouseDraggedButton2(e);
    } else if ((modifiers & MouseEvent.BUTTON3_MASK) != 0) {
      mouseDraggedButton3(e);
    }
  }

  public void mouseDraggedButton1(MouseEvent e) {
    if (getCurrentRenderable() instanceof IsRenderableNode) {
      IsRenderableNode renderableNode = (IsRenderableNode)getCurrentRenderable();
//        System.out.println("renderableNode.getXD() = " + renderableNode.getXD());
//        System.out.println("e.getX() = " + e.getX());
//        System.out.println("savedX = " + savedX);

      renderableNode.setXYD(renderableNode.getXD() + e.getX() - savedX,
			    renderableNode.getYD() + e.getY() - savedY);
      savedX = e.getX();
      savedY = e.getY();
    } else if (getCurrentRenderable() != null) {
      // modify bounding box
      boundingBox.setSize(e.getX() - savedX, e.getY() - savedY);
      
    }
  }

  public void mouseDraggedButton2(MouseEvent e) {
  }

  public void mouseDraggedButton3(MouseEvent e) {
  }

}
