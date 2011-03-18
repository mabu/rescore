package rescore.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.CardLayout;

public class MainWindow implements ActionListener {

	final static String REGATTAS_PANEL = "RegattasList";
    final static String GROUPS_PANEL = "GroupsList";
    final static String LEGS_PANEL = "LegsList";
    final static String YACHTS_PANEL = "YachtsList";
    final static String CLASSES_PANEL = "ClassesList";
	JPanel cards;
    
	public JFrame frame;

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("rescore");
		frame.setBounds(100, 100, 850, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Regatta");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("List Regattas");
		mnNewMenu.add(mntmNewMenuItem_2);
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				listRegattasActionPerformed(ae);
			}
		});
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Create New Regatta");
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Import File");
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenu mnNewMenu_1 = new JMenu("Group");
		menuBar.add(mnNewMenu_1);
		
		JMenu mntmNewMenu_2 = new JMenu("Leg");
		menuBar.add(mntmNewMenu_2);

		JMenu mnYachts = new JMenu("Yachts");
		menuBar.add(mnYachts);
		
		JMenuItem mntmListYachts = new JMenuItem("List Yachts");
		mnYachts.add(mntmListYachts);
		mntmListYachts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				listYachtsActionPerformed(ae);
			}
		});
		
		JMenuItem mntmAddNewYacht = new JMenuItem("Add New Yacht");
		mnYachts.add(mntmAddNewYacht);
		mntmAddNewYacht.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	addNewYachtActionPerformed(ae);
            }
        });
		
		JMenuItem mntmExportYachtList = new JMenuItem("Export Yacht List");
		mnYachts.add(mntmExportYachtList);
		
		JMenu mnYachtClasses = new JMenu("Yacht Classes");
		menuBar.add(mnYachtClasses);
		
		JMenuItem mntmListYachtClasses = new JMenuItem("List Yacht Classes");
		mnYachtClasses.add(mntmListYachtClasses);
		mntmListYachtClasses.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	listClassesActionPerformed(ae);
            }
        });
		
		JMenuItem mntmAddNewYacht_1 = new JMenuItem("Add New Yacht Class");
		mnYachtClasses.add(mntmAddNewYacht_1);
		mntmAddNewYacht_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	addNewYachtClassActionPerformed(ae);
            }
        });
		
		
		JPanel regattasCard = new JPanel();
        regattasCard.add(new JTextField("TextField1", 20));
        
        JPanel groupsCard = new JPanel();
        groupsCard.add(new JTextField("TextField2", 20));
        
        JPanel legsCard = new JPanel();
        legsCard.add(new JTextField("TextField3", 20));
        
        JPanel yachtsCard = new JPanel();
        yachtsCard.add(new YachtListView());
        
        JPanel modelsCard = new JPanel();
        modelsCard.add(new ClassListView());
        
        cards = new JPanel(new CardLayout());
        cards.add(regattasCard, REGATTAS_PANEL);
        cards.add(groupsCard, GROUPS_PANEL);
        cards.add(legsCard, LEGS_PANEL);
        cards.add(yachtsCard, YACHTS_PANEL);
        cards.add(modelsCard, CLASSES_PANEL);
        
        frame.getContentPane().add(cards, BorderLayout.CENTER);
	}
	
	 private void addNewYachtActionPerformed(ActionEvent ae){
	    AddYachtDialog ayDialog = new AddYachtDialog();
	    ayDialog.setVisible(true);
	 }

	 private void addNewYachtClassActionPerformed(ActionEvent ae){
		    AddYachtClassDialog aycDialog = new AddYachtClassDialog();
		    aycDialog.okButton.addActionListener(this);
		    aycDialog.setVisible(true);
		 }
	 
	 private void listYachtsActionPerformed(ActionEvent ae){
		 CardLayout cl = (CardLayout)(cards.getLayout());
		 cl.show(cards, YACHTS_PANEL);
	 }
	 
	 private void listRegattasActionPerformed(ActionEvent ae){
		 CardLayout cl = (CardLayout)(cards.getLayout());
		 cl.show(cards, REGATTAS_PANEL);
	 }
	 
	 private void listClassesActionPerformed(ActionEvent ae){
		 CardLayout cl = (CardLayout)(cards.getLayout());
		 cl.show(cards, CLASSES_PANEL);
	 }

	public void actionPerformed(ActionEvent e) {
		System.out.println("Ok Button in child window pressed");
		
	}
}
