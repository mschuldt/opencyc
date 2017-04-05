/* BlueFrame.java,v 1.20 2002/04/11 21:46:34 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.RootPaneContainer;
import com.cyc.blue.command.ApplicationCommandExecutor;
import com.cyc.blue.command.IsCommand;
import com.cyc.blue.command.IsCommandable;
import com.cyc.blue.command.IsCommandExecutor;
import com.cyc.blue.command.IsCommandManager;
import com.cyc.blue.command.SimpleCommandManagerThread;
import com.cyc.blue.cyc.BlueCycAccess;
import com.cyc.blue.cyc.CycAccessInitializer;
import com.cyc.blue.cyc.CycGraphCommand_AddGraph;
import com.cyc.blue.event.BlueEventService;
import com.cyc.blue.event.BlueApplicationEvent;
import com.cyc.event.EventPrinterForThreadGroup;
import com.cyc.event.IsEventService;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycConstant;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycSymbol;

/**
 * An implementation of IsApplicationFrame for use with BlueGraphFrames.  This
 * class could probably be split into BlueApplicationFrame and ApplicationFrame
 * (such that BlueApplicationFrame inherits the more general functionality from
 * ApplicationFrame.)
 *
 * @author John Jantos
 * @date 2001/08/06
 * @version $Id: BlueFrame.java,v 1.26 2002/05/23 22:30:14 jantos Exp $
 */

public class BlueFrame extends JFrame implements IsApplicationFrame {
  private static final boolean DEBUG = false;

  private BlueCycAccess blueCycAccess = null;
  private CycAccessInitializer cycAccessInitializer;
  private BlueMenuBar menubar;
  private JDesktopPane desktopPane;
  private SimpleCommandManagerThread commandManagerThread;
  //private ApplicationCommandExecutor applicationCommandExecutor;
  private IsEventService blueEventService;

  public BlueFrame(CycAccessInitializer _cycAccessInitializer, int _numInitialHardcodedGraphs)  {
    this(_cycAccessInitializer);
    for (int i=0; i < _numInitialHardcodedGraphs; i++) {
      createHardcodedGraphFrame();
    }
  }

  public BlueFrame(CycAccessInitializer _cycAccessInitializer)  {
    super("Blue Grapher");
    
//      int inset = 50;
//      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//      setBounds(inset, inset, screenSize.width/2 + inset, screenSize.height/2 + inset);
    //setSize(new Dimension(800, 600));
    
    // set system look and feel
//      String laf = UIManager.getSystemLookAndFeelClassName();
//      try {
//        //UIManager.setLookAndFeel(laf);
//        // If you want the Cross Platform L&F instead, comment out the above line and
//        // uncomment the following:
//        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//      } catch (UnsupportedLookAndFeelException exc) {
//        System.err.println("Warning: UnsupportedLookAndFeel: " + laf);
//      } catch (Exception exc) {
//        System.err.println("Error loading " + laf + ": " + exc);
//      }
    
    cycAccessInitializer = _cycAccessInitializer;
    
    /*** COMMAND THREAD ***/
    commandManagerThread = new SimpleCommandManagerThread(this, new ApplicationCommandExecutor(this));
    commandManagerThread.setPriority(Thread.NORM_PRIORITY);
    commandManagerThread.start();

    //applicationCommandExecutor = new ApplicationCommandExecutor(this);
    
    /*** EVENT THREAD ***/
    blueEventService = BlueEventService.instantiate();
    ((Thread)blueEventService).start();
    new EventPrinterForThreadGroup(BlueApplicationEvent.class);

    /*** MENUBAR ***/
    menubar = new BlueMenuBar(this);
    setJMenuBar(menubar);

    addWindowListener(new WindowAdapter() {
	public void windowClosing(WindowEvent e) {
	  System.exit(0);
	}
      });
    
    /*** DESKTOP ***/
    desktopPane = new JDesktopPane();
    desktopPane.setPreferredSize(new Dimension(800,575)); // 575 = 800 - 25(menubar)
    setContentPane(desktopPane);
    
    setTitle("Blue");
    setVisible(true);
    //setSize(800,600);
    pack();
    //setLocation(100,100);

    //Make dragging faster: (doesn't work)
    //desktopPane.putClientProperty("JDesktopPane.dragMode", "outline");

//      setBackground(new Color(255,255,128));
//      setForeground(new Color(128,255,128));
    validate();
  }

  /**
   * cleans up the CycAccess connection if any.
   */
  protected void finalize() throws Throwable { // throws Throwable because java.awt.Frame does
    super.finalize();
    if (blueCycAccess != null) {
      blueCycAccess.close();
    }
  }


  ////////////////////////////////////////////////////////////////////////////////
  // IsApplicationFrame
  
  public void setRootPaneContainer(RootPaneContainer _frame) { }
  public RootPaneContainer getRootPaneContainer() { return this; }

  public IsApplicationInternalFrame getActiveInternalFrame() {
    return (IsApplicationInternalFrame)desktopPane.getSelectedFrame(); // dangerous??
  }

  public void addInternalFrame(IsApplicationInternalFrame _applicationInternalFrame) {
    //System.out.println("--> BlueFrame.addInternalFrame[" + this + "].addInternalFrame(" + _frame + ")");
    //System.out.println("--- _frame.getComponent() = " + _frame.getComponent());
    desktopPane.add(_applicationInternalFrame.getComponent());
    //blueFrame.getDesktop().setSelectedFrame(frame);
    JInternalFrame jInternalFrame = _applicationInternalFrame.getJInternalFrame();
    if (jInternalFrame != null) {
      try {
	jInternalFrame.setSelected(true);
      } catch (java.beans.PropertyVetoException e) {}
    }
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsCommandProcessor

  public void processCommand(IsCommand _command) {
    getCommandManager().queueCommand(_command);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsCommandable

  public void setCommandManager(IsCommandManager _commandManager) {
    //commandManagerThread = _commandManager;
  }

  public IsCommandManager getCommandManager() {
    return commandManagerThread;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsContainer

  public void setContainer(Container _container) { }
  public Container getContainer() { return this; }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsComponent

  public void setComponent(Component _component) { }
  public Component getComponent() { return this; }


  ////////////////////////////////////////////////////////////////////////////////
  // misc

  private void createHardcodedGraphFrame() {
    try {      
      if (blueCycAccess == null) {
	blueCycAccess = new BlueCycAccess(cycAccessInitializer);
      }

      //    getContentPane().add(jGraph); 
      BlueGraphFrame frame = (BlueGraphFrame)((ApplicationCommandExecutor)getCommandManager().getCommandExecutor()).newFrame(cycAccessInitializer, true, true, true, true);
      //      CycGraph cycGraph = new CycGraph();
      //      CycGraphFrame frame = new CycGraphFrame(_cycAccessInitializer, cycGraph, true, true, true, true);
      frame.setApplicationFrame(this); //@toso: put into newFrame.
      //addInternalFrame(frame);
      
      HashSet defaultFilterFns = new HashSet();
      defaultFilterFns.add(new CycSymbol("BFF-ARBITRARY-UNIONS"));
      defaultFilterFns.add(new CycSymbol("BFF-CYC-KB-SUBSET-COLLECTIONS"));
      //defaultFilterFns.add("BFF-RKF-IRRELEVANT-TERMS");
      /*      
      HashSet preds = new HashSet();
      preds.add(blueCycAccess.getConstantByName("genls"));
      //preds.add("isa");
      //preds.add("genlPreds");
      //preds.add("genlMt");
      
      
      //blueCommandManager.execute(new CycGraphCommand_AddNode("Animal"));
      //CycConstant c1 = blueCycAccess.getConstantByName("Animal");
      //CycNode n1 = CycNode.instantiate(c1,c1.toString());
      //graph.addNode(n1);
      
      //commandManager.execute(new CycGraphCommand_AddNode("Person"));
      
      HashSet predSet = new HashSet();
      predSet.add(blueCycAccess.getConstantByName("containsInformation"));
      predSet.add(blueCycAccess.getConstantByName("subEvents"));
      predSet.add(blueCycAccess.getConstantByName("actors"));
      predSet.add(blueCycAccess.getConstantByName("acquaintedWith"));
      predSet.add(blueCycAccess.getConstantByName("possesses"));

      HashSet forts = new HashSet();
      //forts.add("ELExpression");
      //forts.add(blueCycAccess.getConstantByName("Thing"));
      //forts.add(blueCycAccess.getConstantByName("Animal"));
      //forts.add(blueCycAccess.getConstantByName("Tank-Vehicle"));
      forts.add(blueCycAccess.getConstantByName("Dog"));
      //      forts.add("Cat");
      //      forts.add("GymHorse");
      //      forts.add("GymPommelHorse");
      //        forts.add("Heroin");
      //forts.add("Horse");
      //        forts.add("Horse-Domesticated");
      //        forts.add("SawHorse");
      //	forts.add("AddictiveSubstance");
      
      //forts.add("BaseKB");
      Iterator fortsIterator = forts.iterator();
      while (fortsIterator.hasNext()) {
	CycFort fort = (CycFort)fortsIterator.next();
	//frame.processCommand(new CycGraphCommand_AddNode(frame.getGraph(), fort));
	//commandManagerThread.enqueueCommand(new CycGraphCommand_AddMinForwardTrue(cycAccessInitializer, "genls", fortString, "BPVI-QueryMt", defaultFilterFns, 2));
	//commandManagerThread.enqueueCommand(new CycGraphCommand_AddMinForwardTrue(cycAccessInitializer, "isa", fortString, "BPVI-QueryMt", defaultFilterFns, 1));
	//commandManagerThread.enqueueCommand(new CycGraphCommand_AddMinBackwardTrue(cycAccessInitializer, "genls", fortString, "BPVI-QueryMt", defaultFilterFns, 3));

      CycList argList = new CycList();
      argList.add(blueCycAccess.getConstantByName("WTCTerroristAttackSept2001"));
      argList.add(new CycList(predSet));
      argList.add(new Integer(5));
      argList.add(blueCycAccess.getConstantByName("TKBDemonstrationSpindleHeadMt"));
      //argList.add(_params);
      CycList quoteList = new CycList();
      quoteList.add(new CycSymbol("QUOTE"));
      quoteList.add(argList);
      CycList paramList = new CycList();
      paramList.add(new CycSymbol("BBF-RTV-ALL-EDGES-FROM-NODE"));
      paramList.add(quoteList);
*/
      HashSet predSet = new HashSet();
      predSet.add(blueCycAccess.getConstantByName("genls"));
      HashSet fortSet = new HashSet();
      fortSet.add(blueCycAccess.getConstantByName("Dog"));
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
      argList.add(new Integer(5));
      argList.add(new CycSymbol(":MT"));
      argList.add(blueCycAccess.getConstantByName("BiologyMt"));
      //argList.add(_params);

      CycList quoteList = new CycList();
      quoteList.add(new CycSymbol("QUOTE"));
      quoteList.add(argList);

      CycList paramList = new CycList();
      paramList.add(new CycSymbol("BBF-MIN-FORWARD-TRUE"));
      paramList.add(quoteList);

      frame.processCommand(new CycGraphCommand_AddGraph(paramList));
      // and again..
      //frame.processCommand(new CycGraphCommand_AddGraph(paramList));
      //      }
      //commandManager.enqueueCommand(new CycGraphCommand_AddMinForwardTrue(cycAccessInitializer, "isa", fortString, "RKF-TKCP-DemoEnvironmentMt", defaultFilterFns, 1));
     
    } catch (Exception e) {
      System.err.println("BlueFrame[" + this + "]: error w/ BlueCycAccess: " + e);
    }
      
    
    //commandManager.execute(new CycGraphCommand_AddMinCeilingsForwardTrue(cycAccessInitializer, "genls", forts, "RKF-TKCP-DemoEnvironmentMt", defaultFilterFns, 1));
    
    
    //CycRelation r1 = new CycRelation(blueCycAccess.getConstantByName("genls"), RelationDirection.OUT);
    //CycInterest interest1 = new CycInterest(r1, 20);
    //commandManager.addInterestAtNode(interest1, n1); // assume thread running
    
    //        CycConstant c2 = blueCycAccess.getConstantByName("WildAnimal");
    //        CycNode n2 = CycNode.instantiate(c2);
    //        graph.addNode(n2);
    //        CycRelation r2 = new CycRelation(blueCycAccess.getConstantByName("genls"), RelationDirection.IN);
    //        CycInterest interest2 = new CycInterest(r2, 10);
    //        commandManager.addInterestAtNode(interest2, n2);
    
  }
  


}

