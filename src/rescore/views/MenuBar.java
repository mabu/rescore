package rescore.views;

import javax.swing.*;
import java.awt.*;

public class MenuBar {

    public static JMenuBar prepareMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menuRegatta = new JMenu("Regatta");
        menuBar.add(menuRegatta);

        JMenuItem menuItem1 = new JMenuItem("Create New Regatta");
        JMenuItem menuItem2 = new JMenuItem("Open File");
        JMenuItem menuItem3 = new JMenuItem("Regatta Settings");
        JMenuItem menuItem4 = new JMenuItem("Export Regatta Report");
        menuRegatta.add(menuItem1);
        menuRegatta.add(menuItem2);
        menuRegatta.addSeparator();
        menuRegatta.add(menuItem3);
        menuRegatta.add(menuItem4);

        JMenu menuGroup = new JMenu("Group");
        menuBar.add(menuGroup);
        
        JMenu menuLeg = new JMenu("Leg");
        menuBar.add(menuLeg);

        JMenu menuSettings = new JMenu("Settings");
        menuBar.add(menuSettings);

        JMenu menuHelp = new JMenu("Help");
        menuBar.add(menuHelp);

        return menuBar;
    }
    

}
