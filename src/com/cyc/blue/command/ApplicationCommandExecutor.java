/* $Id: ApplicationCommandExecutor.java,v 1.7 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.command;

import javax.swing.*;
import com.cyc.blue.graph.*;
import com.cyc.blue.gui.*;
import com.cyc.blue.cyc.*; // @todo: remove
import com.cyc.blue.cyc.BlueCycEventListener;

/**
 * <description of this class or interface>
 *
 * @author John Jantos
 * @date 2001/10/18
 * @version $Id: ApplicationCommandExecutor.java,v 1.7 2002/05/23 22:30:14 jantos Exp $
 */

public class ApplicationCommandExecutor extends CommandExecutor implements IsApplicationCommandExecutor {
  private IsApplicationFrame applicationFrame;

  public ApplicationCommandExecutor(IsApplicationFrame _applicationFrame) {
    applicationFrame = _applicationFrame;
  }

  public IsApplicationFrame getApplicationFrame() {
    return applicationFrame;
  }

//    public IsApplicationInternalFrame newFrame(boolean _resizable, boolean _closable, boolean _maximizable, boolean _iconifiable) {
    
//      CycGraph cycGraph = new CycGraph();
//      IsApplicationInternalFrame frame = new BlueGraphFrame(cycGraph, _resizable, _closable, _maximizable, _iconifiable);
//      applicationFrame.addInternalFrame(frame);
    
//      return frame;
//    }

  public IsGraphFrame newFrame(CycAccessInitializer _cycAccessInitializer,
					     boolean _resizable, boolean _closable, boolean _maximizable, boolean _iconifiable) {
    
    // @todo: need CycGraphFrameType (IsGraphFrameType) (?)
    IsGraph graph = new CycGraph();
    IsRenderableGraph renderableGraph = new GenericSubscribingRenderableGraph(graph, GenericRenderableNode.class, GenericRenderableEdge.class);


    IsGraphCommandExecutor graphCommandExecutor = new CycGraphCommandExecutor(graph, _cycAccessInitializer);
 
    IsGraphFrame frame = new BlueGraphFrame(graph, renderableGraph, graphCommandExecutor, _resizable, _closable, _maximizable, _iconifiable);
    //    IsApplicationInternalFrame frame = new BlueGraphFrame(cycGraph, _resizable, _closable, _maximizable, _iconifiable);

    applicationFrame.addInternalFrame(frame);

    return frame;
  }

//    public IsApplicationInternalFrame newFrame(CycAccessInitializer _cycAccessInitializer, // @todo: move to CycApplicationCommandExecutor
//  					     String _signature,
//  					     boolean _resizable, boolean _closable, boolean _maximizable, boolean _iconifiable) {

//      // @todo: need CycGraphFrameType (IsGraphFrameType) (?)
//      IsApplicationInternalFrame frame = newFrame(_cycAccessInitializer, _resizable, _closable, _maximizable, _iconifiable);

  // /*** CYC-EVENT LISTENER THREAD ***/
 //        if (_signature != null) {
 //  	//System.err.println("starting BlueCycEventListener");
 //  	BlueCycEventListener blueCycEventListener = new BlueCycEventListener(frame.getCommandManager(), _signature, _cycAccessInitializer);
 //  	//blueCycEventListener.setPriority(3);
 //  	blueCycEventListener.start();
 //        }

//      return frame;
//    } 


}
