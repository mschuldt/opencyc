/* $Id: Blue.java,v 1.11 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue;
import java.io.*;
import org.opencyc.api.*;
import com.cyc.blue.cyc.CycAccessInitializer;
import com.cyc.blue.gui.BlueFrame;

/**
 * The grapher application entry point.  Currently requires being called with
 * two command line parameters: 1) the machine name a cyc image is running on
 * and 2) the base TCP port of that image.
 *
 * @author John Jantos
 * @date 2001/08/06
 * @version $Id: Blue.java,v 1.11 2002/05/23 22:30:14 jantos Exp $
 */

public class Blue {
  private static final boolean DEBUG = false;
  private BlueFrame blueframe;
  
  public Blue(CycAccessInitializer _cycAccessInitializer) throws IOException {
    blueframe = new BlueFrame(_cycAccessInitializer, 1);
  }

  public static void main (String[] args) throws IOException {
    if (DEBUG) { System.out.println("Starting Blue.main( " + args + ")"); }

    String hostName = args[0];
    int port = Integer.parseInt(args[1]);
    CycAccessInitializer cycAccessInitializer = new CycAccessInitializer(hostName, port, CycConnection.BINARY_MODE);

    if (DEBUG) { System.out.println("In Blue.main( " + args + "): After BlueCycAPI init"); }
    new Blue(cycAccessInitializer);
  }

}
