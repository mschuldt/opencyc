/* $Id: SingleGraphApplet.java,v 1.6 2002/05/30 16:18:16 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.io.IOException;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JToolBar;
import javax.swing.RootPaneContainer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.cyc.blue.command.ApplicationCommandExecutor;
import com.cyc.blue.command.IsCommand;
import com.cyc.blue.command.IsCommandable;
import com.cyc.blue.command.IsCommandManager;
import com.cyc.blue.command.IsCommandProcessor;
import com.cyc.blue.command.SimpleCommandManagerThread;
import com.cyc.blue.cyc.BlueCycAccess;
import com.cyc.blue.cyc.BlueCycEventListener;
import com.cyc.blue.cyc.CycAccessInitializer;
import com.cyc.blue.event.BlueEventService;
import com.cyc.event.IsEventService;
import org.opencyc.api.CycAccess;
import org.opencyc.api.CycConnection;
import org.opencyc.cycobject.CycFort;

/**
 * An implementation of an application frame that is meant to have one graph
 * frame as an internal frame.
 * @see IsApplicationFrame
 * @see IsGraphFrame
 *
 * @author John Jantos
 * @date 2002/04/10
 * @version $Id: SingleGraphApplet.java,v 1.6 2002/05/30 16:18:16 jantos Exp $
 */

public class SingleGraphApplet extends JApplet implements IsApplicationFrame {
  private static final boolean DEBUG = false;

  // serves threadgroup
  private BlueCycEventListener blueCycEventListener;
  private IsEventService blueEventService;
  // serves thread
  private BlueCycAccess blueCycAccess;
  // serves this
  private SimpleCommandManagerThread commandManager;
  private ApplicationCommandExecutor applicationCommandExecutor;
  private IsGraphFrame applicationInternalFrame;
  // applet params
  private CycFort domainMt;
  private CycFort parsingMt;
  private CycFort generationMt;
  private String signature;

  /**
   * cleans up any CycAccess connections that may still be active.
   */
  protected void finalize() throws Throwable { // throws Throwable because java.awt.Frame does.
    super.finalize();
    if (blueCycAccess != null) {
      blueCycAccess.close();
    }
  }

  /**
   * Called by the browser or applet viewer to inform this Applet that it
   * has been loaded into the system. It is always called before the first 
   * time that the start method is called.
   */
  public void init() {
    // set system look and feel
    String laf = UIManager.getSystemLookAndFeelClassName();
    try {
      UIManager.setLookAndFeel(laf);
      // If you want the Cross Platform L&F instead, comment out the above line and
      // uncomment the following:
      // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (UnsupportedLookAndFeelException exc) {
      System.err.println("Blue: Warning: UnsupportedLookAndFeel: " + laf);
    } catch (Exception exc) {
      System.err.println("Blue: Error loading " + laf + ": " + exc);
    }
    
    // get parameters
    // cyc server
    String hostName = getParameter("hostname");
    //System.err.println("hostName: " + hostName);
    //System.err.println("CodeBase: " + this.getCodeBase());
    if (hostName == null) { hostName = this.getCodeBase().getHost(); }
    //System.err.println("hostName = " + hostName);
    int basePort = 3600;
    String basePortString = getParameter("baseport");
    try {
      basePort = Integer.parseInt(basePortString);
    } catch (NumberFormatException e) {
      System.err.println("Blue: " + this + ": param " + "baseport" + " error: " + basePortString + " not a number");
    }

    // ASCII fi port (legacy support)
    int port = 0;
    String portString = getParameter("port");
    try {
      port = Integer.parseInt(portString);
      if (port > 0) {
	basePort = port - 1; // ASCII fi port offset
	System.out.println("basePort = " + basePort + " (set from ASCII port param of " + port + ")");
      }
    } catch (NumberFormatException e) {
    }

    CycAccessInitializer cycAccessInitializer = new CycAccessInitializer(hostName, basePort, CycConnection.BINARY_MODE);
    
    {
      blueCycAccess = null; // slight kluge to avoid error
      try {
	//System.err.println("before new BlueCycAccess");
	blueCycAccess = new BlueCycAccess(cycAccessInitializer);
	//System.err.println("after new BlueCycAccess");
      } catch (IOException e) {
	System.err.println("Blue: " + e);
	Thread.currentThread().dumpStack();
	//stop();//Runtime.getRuntime().exit(-1);
      } catch (org.opencyc.api.CycApiException e) {
	System.err.println("Blue: " + e);
	Thread.currentThread().dumpStack();
	//stop();//Runtime.getRuntime().exit(-1);
      }

      String domainMtParamName = "domain_mt";
      String domainMtString = getParameter(domainMtParamName);
      domainMt = BlueCycAccess.baseKB;
      //System.err.println(domainMtString);
      if (domainMtString != null) {
	try {
	  domainMt = blueCycAccess.getConstantByName(domainMtString);	  
	  if (domainMt != null) {
	    blueCycAccess.setDomainMt(domainMt);
	  }
	} catch (IOException e) {
	  System.err.println("Blue: " + this + ": param " + domainMtParamName + " error: " + domainMtString + " not found");
	  Thread.currentThread().dumpStack();
	} catch (org.opencyc.api.CycApiException e) {
	  System.err.println("Blue: " + this + ": param " + domainMtParamName + " error: " + domainMtString + " not found");
	  Thread.currentThread().dumpStack();
	}
      }

      String parsingMtParamName = "parsing_mt";
      String parsingMtString = getParameter(parsingMtParamName);
      parsingMt = BlueCycAccess.baseKB;
      //System.err.println(parsingMtString);
      if (parsingMtString != null) {
	try {
	  parsingMt = blueCycAccess.getConstantByName(parsingMtString);	  
	  if (parsingMt != null) {
	    blueCycAccess.setParsingMt(parsingMt);
	  }
	} catch (IOException e) {
	  System.err.println("Blue: " + this + ": param " + parsingMtParamName + " error: " + parsingMtString + " not found");
	  Thread.currentThread().dumpStack();
	} catch (org.opencyc.api.CycApiException e) {
	  System.err.println("Blue: " + this + ": param " + parsingMtParamName + " error: " + parsingMtString + " not found");
	  Thread.currentThread().dumpStack();
	}
      }

      String generationMtParamName = "generation_mt";
      String generationMtString = getParameter(generationMtParamName);
      generationMt = BlueCycAccess.baseKB;
      //System.err.println(generationMtString);
      if (generationMtString != null) {
	try {
	  generationMt = blueCycAccess.getConstantByName(generationMtString);	  
	  if (generationMt != null) {
	      blueCycAccess.setGenerationMt(generationMt);
	  } else {
	    generationMt = blueCycAccess.getConstantByName("EnglishMt");  
	    if (generationMt != null) {
	      blueCycAccess.setGenerationMt(generationMt);
	    }
	  }
	} catch (IOException e) {
	  System.err.println("Blue: " + this + ": param " + generationMtParamName + " error: " + generationMtString + " not found");
	  Thread.currentThread().dumpStack();
	} catch (org.opencyc.api.CycApiException e) {
	  System.err.println("Blue: " + this + ": param " + generationMtParamName + " error: " + generationMtString + " not found");
	  Thread.currentThread().dumpStack();
	}
      }

      String signatureParamName = "signature";
      String signatureString = getParameter(signatureParamName);
      //System.err.println("signatureString = " + signatureString);
      if (signatureString != null) {
	signature = signatureString;
      } else {
	signature = null;
      }

      String heightParamName = "height";
      String heightString = getParameter(heightParamName);
      int height;
      try {
	height = Integer.parseInt(heightParamName);
      } catch (NumberFormatException e) {
	height = 465;
      }
      
      String widthParamName = "width";
      String widthString = getParameter(widthParamName);
      int width;
      try {
	width = Integer.parseInt(widthParamName);
      } catch (NumberFormatException e) {
	width = 625;
      }
      
      ////////////////////////////////////////////////////////////////////////////////
      // command manager thread and command executor
      applicationCommandExecutor = new ApplicationCommandExecutor(this);
      commandManager = new SimpleCommandManagerThread(this, applicationCommandExecutor);
      //commandManager.setPriority(3);
      commandManager.start();
      
      ////////////////////////////////////////////////////////////////////////////////
      // event service thread
      blueEventService = BlueEventService.instantiate();
      //new EventPrinterForThreadGroup(BlueApplicationEvent.class);

      ////////////////////////////////////////////////////////////////////////////////
      // the single applicationInternalFrame
      applicationInternalFrame = applicationCommandExecutor.newFrame(cycAccessInitializer, false, false, false, false);
	
      ////////////////////////////////////////////////////////////////////////////////
      // the cyc event listener thread
      if (signature != null) {
	//System.err.println("starting BlueCycEventListener");
	BlueCycEventListener blueCycEventListener = new BlueCycEventListener(applicationInternalFrame.getCommandManager(), signature, cycAccessInitializer);
	//blueCycEventListener.setPriority(3);
	blueCycEventListener.start();
      }

      ////////////////////////////////////////////////////////////////////////////////
      // config the internal frame (somewhat klugy)
      getContentPane().add(applicationInternalFrame.getComponent());
      JInternalFrame jInternalFrame = applicationInternalFrame.getJInternalFrame();
      if (jInternalFrame != null) {
	try {
	  jInternalFrame.setMaximum(true);
	} catch (java.beans.PropertyVetoException e) {
	  Thread.currentThread().dumpStack();
	}
      }

      ////////////////////////////////////////////////////////////////////////////////
      // set colors/sizes
      getContentPane().setVisible(true);
      getContentPane().setBackground(new Color(220,220,240));
      getParent().getParent().setBackground(new Color(217,217,217,0));
      //pack();
      setSize(width, height);

      ////////////////////////////////////////////////////////////////////////////////
      // keep CycAccess closed for this thread
      if (blueCycAccess != null) {
	blueCycAccess.close();
      }
    }
  }

  public void start() {
    commandManager.setPaused(false);
    if (blueCycEventListener != null) {
      blueCycEventListener.setPaused(false);
    }
  }

  public void stop() {
    commandManager.setPaused(true);
    if (blueCycEventListener != null) {
      blueCycEventListener.setPaused(true);
    }
  }
  public void destroy() {
    if (blueCycAccess != null) {
      blueCycAccess.close();
    }
    if (blueCycEventListener != null) {
      blueCycEventListener.disconnect();
    }
  }

  public String getAppletInfo() {
    return "Title: Blue Applet\n" + 
      "Author: jantos@cyc.com\n" +
      "Blue Applet with Cyc blue event listener.";
  }

  /* gets parameter information about the applet
   */
  public String[][] getParameterInfo() {
    String paramInfo[][] = {
      // Parameter Name     Kind of Value   Description
      {"PARAMETER",           "VALUE",        "NOTES"},
      {"signature",           "String",      "signature to use to get uia events"},
      {"domain_mt",           "CycFort",      "microtheory we're viewing the KB from"},
      {"parsing_mt",          "CycFort",      "microtheory we're parsing text from"},
      {"generation_mt",       "CycFort",      "microtheory we're generating text from"},
    };
    return paramInfo;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsApplicationFrame

  public void setRootPaneContainer(RootPaneContainer _frame) { }
  public RootPaneContainer getRootPaneContainer() { return this; }

  public IsApplicationInternalFrame getActiveInternalFrame() {
    return applicationInternalFrame;
  }

  public IsCommandManager getActiveInternalFrameCommandManager() {
    if (applicationInternalFrame != null) {
      if (DEBUG) { System.out.println("BlueApplet.getActiveInternalFrameCommandManager -> " + applicationInternalFrame.getCommandManager()); }
      return applicationInternalFrame.getCommandManager();
    } else {
      return null;
    }
  }

  public void addInternalFrame(IsApplicationInternalFrame _applicationInternalFrame) {
    getContentPane().add(_applicationInternalFrame.getComponent());
    //_applicationInternalFrame.getDesktopPane().setSelectedFrame(applicationInternalFrame);
    JInternalFrame jInternalFrame = _applicationInternalFrame.getJInternalFrame();
    if (jInternalFrame != null) {
      try {
	jInternalFrame.setSelected(true);
      } catch (java.beans.PropertyVetoException e) {
	Thread.currentThread().dumpStack();
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  
  /** Special setSize to allow javascript to resize the applet.
   * @param _width
   * @param _height
   */
  /* commented out since used with onLoad and onResize on a web page this cause mozilla to crash
  public void setSize(int _width, int _height) {
    Component lastObject = null;
    Component thisObject = this;
    Component nextObject = this;
    do {
      lastObject = thisObject;
      thisObject = nextObject;
      nextObject = thisObject.getParent();
    } while (nextObject != null);
    if (lastObject != null) {
      lastObject.setSize(_width,_height);
      lastObject.validate();
    } else {
      System.err.println("SingleGraphApplet[" + this + "].setSize(" + _width + ", " + _height + ") failed to find correct parent!");
    }
  }
  */

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsComponent

  public void setComponent(Component _component) { }
  public Component getComponent() { return this; }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsContainer

  public void setContainer(Container _container) { }
  public Container getContainer() { return this; }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsCommandable

  public void setCommandManager(IsCommandManager _commandManager) {
    if (_commandManager instanceof SimpleCommandManagerThread) {
      commandManager = (SimpleCommandManagerThread)_commandManager;
    } else {
      System.err.println("SingleGraphApplet[" + this + "].setCommandManager(" + _commandManager + ") failed: (_commandManager instanceof SimpleCommandManagerThread) is false.");
    }
  }
  public IsCommandManager getCommandManager() {
    return commandManager;
  }
  public void queueCommand(IsCommand _command) {
    getCommandManager().queueCommand(_command);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsCommandProcessor

  public void processCommand(IsCommand _command) {
    _command.process(this);
  }

}
