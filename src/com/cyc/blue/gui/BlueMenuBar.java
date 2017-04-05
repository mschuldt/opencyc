/* $Id: BlueMenuBar.java,v 1.7 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import com.cyc.blue.command.ApplicationCommand_File_Close;
import com.cyc.blue.command.ApplicationCommand_File_Save;
import com.cyc.blue.command.GraphCommand_SelectAll;
import com.cyc.blue.command.IsCommand;
import com.cyc.blue.command.IsCommandManager;

/**
 * <description of this class or interface>
 *
 * @author John Jantos
 * @date 2001/10/18
 * @version $Id: BlueMenuBar.java,v 1.7 2002/05/23 22:30:14 jantos Exp $
 */

public class BlueMenuBar extends JMenuBar implements IsMenuBar {
  JMenu menu, subMenu;
  JMenuItem menuItem;
  JCheckBoxMenuItem cbMenuItem;
  JRadioButtonMenuItem rbMenuItem;
  IsCommandManager commandManager;
  IsApplicationFrame applicationFrame;

  public BlueMenuBar(IsApplicationFrame _applicationFrame) {
    super();
    applicationFrame = _applicationFrame;
    commandManager = _applicationFrame.getCommandManager();

    menu = new JMenu("File");
    menuItem = new JMenuItem("New"); 
    menuItem.setEnabled(false); 
//      menuItem.addActionListener(new ActionListener() {
//  	public void actionPerformed(ActionEvent ee) {
//  	  commandManager.queueCommand(new ApplicationCommand_File_New(applicationFrame));
//  	}
//        });
    menu.add(menuItem);
    menuItem = new JMenuItem("Open..."); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Close"); 
    menuItem.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ee) {
	  commandManager.queueCommand(new ApplicationCommand_File_Close(applicationFrame));
	}
      });
    menu.add(menuItem);
    menuItem = new JMenuItem("Save");
    menuItem.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ee) {
	  commandManager.queueCommand(new ApplicationCommand_File_Save(applicationFrame));
	}
      });
    menu.add(menuItem);
    menuItem = new JMenuItem("Save As..."); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Revert"); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Graph Info..."); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Clone"); menuItem.setEnabled(false); menu.add(menuItem);
    subMenu = new JMenu("Clone Source"); subMenu.setEnabled(false); menu.add(subMenu);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Page Setup..."); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Print..."); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Print Preview..."); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Send..."); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Preferences..."); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("[Recently Loaded Graph List]"); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Exit"); menuItem.setEnabled(false); menu.add(menuItem);
    add(menu);
    
    menu = new JMenu("Edit");
    menuItem = new JMenuItem("Undo [Command Name]"); menuItem.setEnabled(false);
//      menuItem.addActionListener(new ActionListener() {
//  	public void actionPerformed(ActionEvent ee) {
//  	  commandManager.queueCommand(new GraphCommand_Undo());
//  	}
//        });
    menu.add(menuItem);
    menuItem = new JMenuItem("Redo [Command Name]"); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Cut"); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Copy"); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Paste"); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Delete"); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Select All");
    menuItem.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ee) {
	  commandManager.processCommand(new GraphCommand_SelectAll());
	}
      });
    menu.add(menuItem);
    menuItem = new JMenuItem("Select None"); menuItem.setEnabled(false); menu.add(menuItem);
    subMenu = new JMenu("Select");
    menuItem = new JMenuItem("Reselect"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("Inverse"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menu.add(subMenu);
    subMenu = new JMenu("Modify Selection");
    menuItem = new JMenuItem("Widen..."); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("Contract..."); menuItem.setEnabled(false); subMenu.add(menuItem);
    menu.add(subMenu);
    add(menu);
    
    menu = new JMenu("Graph");
    menuItem = new JMenuItem("Undo [Command Name]"); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Redo [Command Name]"); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Pause and Branch"); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Resume Last Branch"); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Resume Branch..."); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Add Node..."); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    subMenu = new JMenu("Add genls");
    menuItem = new JMenuItem("1/0"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("2/0"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("3/0"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("1/1"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("2/1"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("3/1"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("4/1"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("1/2"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("2/2"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("3/2"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("Other..."); menuItem.setEnabled(false); subMenu.add(menuItem);
    menu.add(subMenu);
    subMenu = new JMenu("Add isa");
    menuItem = new JMenuItem("1/0"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("2/0"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("3/0"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("Other..."); menuItem.setEnabled(false); subMenu.add(menuItem);
    menu.add(subMenu);
    menuItem = new JMenuItem("Add Other..."); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    subMenu = new JMenu("Apply Macro");
    menu.add(subMenu);
    menuItem = new JMenuItem("Add Macro..."); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Edit Macros..."); menuItem.setEnabled(false); menu.add(menuItem);
    add(menu);
    
    menu = new JMenu("Layout");
    menuItem = new JMenuItem("Undo [Command Name]"); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Redo [Command Name]"); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Start/Stop??"); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Hide Unselected"); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Unhide All"); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    subMenu = new JMenu("Global Out Cutoff");
    menuItem = new JMenuItem("1"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("2"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("3"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("4"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("6"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("9"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("13"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("19"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("All"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menu.add(subMenu);
    subMenu = new JMenu("Global In Cutoff");
    menuItem = new JMenuItem("1"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("2"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("3"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("4"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menu.add(subMenu);
    menuItem = new JMenuItem("Custom Cutoffs..."); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    subMenu = new JMenu("Show Forces");
    menuItem = new JMenuItem("All"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("None"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("Selected"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("Visible"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menu.add(subMenu);
    menuItem = new JCheckBoxMenuItem("Show Forces on New Nodes"); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Edit Layout..."); menuItem.setEnabled(false); menu.add(menuItem);
    add(menu);
    
    menu = new JMenu("View");
    menuItem = new JMenuItem("Zoom In"); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Zoom Out"); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Fit In Window"); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JCheckBoxMenuItem("Auto Fit"); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    subMenu = new JMenu("Node Text");
    menuItem = new JMenuItem("CycL"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("English"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menu.add(subMenu);
    subMenu = new JMenu("Node Font");
    menuItem = new JMenuItem("[Standard Font Menu?]"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menu.add(subMenu);
    subMenu = new JMenu("Node Size");
    menuItem = new JMenuItem("Auto"); menuItem.setEnabled(false); subMenu.add(menuItem);
    subMenu.addSeparator(); // ------------------
    menuItem = new JMenuItem("5"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("6"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("7"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("8"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("9"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("10"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("11"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("12"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("14"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("18"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("21"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("24"); menuItem.setEnabled(false); subMenu.add(menuItem);
    subMenu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Other..."); menuItem.setEnabled(false); subMenu.add(menuItem);
    menu.add(subMenu);
    menuItem = new JMenuItem("Node Style..."); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    subMenu = new JMenu("Edge Text");
    menuItem = new JMenuItem("Bare"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("Symbol"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("English"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menu.add(subMenu);
    subMenu = new JMenu("Edge Font");
    menuItem = new JMenuItem("[Standard Font Menu?]"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menu.add(subMenu);
    subMenu = new JMenu("Edge Size");
    menuItem = new JMenuItem("Auto"); menuItem.setEnabled(false); subMenu.add(menuItem);
    subMenu.addSeparator(); // ------------------
    menuItem = new JMenuItem("4"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("5"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("6"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("7"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("8"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("9"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("10"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("11"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("12"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("14"); menuItem.setEnabled(false); subMenu.add(menuItem);
    subMenu.addSeparator(); // ------------------
    menuItem = new JMenuItem("Other..."); menuItem.setEnabled(false); subMenu.add(menuItem);
    menu.add(subMenu);
    subMenu = new JMenu("Edge Path");
    menuItem = new JMenuItem("Straight Line"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("Orthogonal Path"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menuItem = new JMenuItem("Spline Path"); menuItem.setEnabled(false); subMenu.add(menuItem);
    menu.add(subMenu);
    menuItem = new JMenuItem("Edge Style..."); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JCheckBoxMenuItem("Collapse Edges"); menuItem.setEnabled(false); menu.add(menuItem);
    add(menu);
    
    menu = new JMenu("Window");
    menuItem = new JMenuItem("Cascade"); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("Tile"); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JCheckBoxMenuItem("Info"); menuItem.setEnabled(false); menu.add(menuItem);
    menuItem = new JMenuItem("[any more global or graph level windows]"); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("[window list]"); menuItem.setEnabled(false); menu.add(menuItem);
    add(menu);
    
    menu = new JMenu("Help");
    menuItem = new JMenuItem("Help"); menuItem.setEnabled(false); menu.add(menuItem);
    menu.addSeparator(); // ------------------
    menuItem = new JMenuItem("About Blue..."); menuItem.setEnabled(false); menu.add(menuItem);
    add(menu);
 
  }

}



//    public BlueMenuBar() {
//      super();

//      //Build the first menu.
//      menu = new JMenu("A Menu");
//      menu.setMnemonic(KeyEvent.VK_F);
//      menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
//      add(menu);
    
//      //a group of JMenuItems
//      menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
//      menuItem.setAccelerator(KeyStroke.getKeyStroke(
//  						   KeyEvent.VK_1, ActionEvent.ALT_MASK));
//      menuItem.getAccessibleContext().setAccessibleDescription(
//  							     "This doesn't really do anything");
//      menu.add(menuItem);
    
//      menuItem = new JMenuItem("text");
//      menuItem.setMnemonic(KeyEvent.VK_B);
//      menu.add(menuItem);
    
//      menuItem = new JMenuItem();
//      menuItem.setMnemonic(KeyEvent.VK_D);
//      menu.add(menuItem);
    
//      //a group of radio button menu items
//      menu.addSeparator();
//      ButtonGroup group = new ButtonGroup();
//      rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
//      rbMenuItem.setSelected(true);
//      rbMenuItem.setMnemonic(KeyEvent.VK_R);
//      group.add(rbMenuItem);
//      menu.add(rbMenuItem);

//      rbMenuItem = new JRadioButtonMenuItem("Another one");
//      rbMenuItem.setMnemonic(KeyEvent.VK_O);
//      group.add(rbMenuItem);
//      menu.add(rbMenuItem);
    
//      //a group of check box menu items
//      menu.addSeparator();
//      cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
//      cbMenuItem.setMnemonic(KeyEvent.VK_C);
//      menu.add(cbMenuItem);
    
//      cbMenuItem = new JCheckBoxMenuItem("Another one");
//      cbMenuItem.setMnemonic(KeyEvent.VK_H);
//      menu.add(cbMenuItem);

//      //a submenu
//      menu.addSeparator();
//      submenu = new JMenu("A submenu");
//      submenu.setMnemonic(KeyEvent.VK_S);
    
//      menuItem = new JMenuItem("An item in the submenu");
//      menuItem.setAccelerator(KeyStroke.getKeyStroke(
//  						   KeyEvent.VK_2, ActionEvent.ALT_MASK));
//      submenu.add(menuItem);
    
//      menuItem = new JMenuItem("Another item");
//      submenu.add(menuItem);
//      menu.add(submenu);
    
//      //Build second menu in the menu bar.
//      menu = new JMenu("Another Menu");
//      menu.setMnemonic(KeyEvent.VK_N);
//      menu.getAccessibleContext().setAccessibleDescription(
//  							 "This menu does nothing");
//      add(menu);
    
//    }
