package rescore.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import rescore.Captain;
import rescore.Owner;
import rescore.Yacht;
import rescore.YachtClass;
import rescore.models.ComboModel;

public class AddYachtDialog extends JDialog implements ActionListener {

	private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
	private ComboModel comboModel1;
	private ComboModel comboModel2;
	private ComboModel comboModel3;


	public void actionPerformed(ActionEvent e) {
		
	}
	
	public AddYachtDialog(){
		initComponents();
	}
	
	/** mostly generated code - better don't modify it manually */
    private void initComponents() {

    	 jLabel1 = new javax.swing.JLabel();
         jLabel2 = new javax.swing.JLabel();
         jLabel3 = new javax.swing.JLabel();
         jLabel4 = new javax.swing.JLabel();
         jLabel5 = new javax.swing.JLabel();
         jLabel6 = new javax.swing.JLabel();
         jLabel7 = new javax.swing.JLabel();
         jComboBox1 = new javax.swing.JComboBox();
         jTextField1 = new javax.swing.JTextField();
         jTextField2 = new javax.swing.JTextField();
         jTextField3 = new javax.swing.JTextField();
         jComboBox2 = new javax.swing.JComboBox();
         jComboBox3 = new javax.swing.JComboBox();
         jTextField4 = new javax.swing.JTextField();
         jButton1 = new javax.swing.JButton();
         jButton2 = new javax.swing.JButton();

         setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

         jLabel1.setText("Yacht Class:");

         jLabel2.setText("Name:");

         jLabel3.setText("Sail Number:");

         jLabel4.setText("Build Year:");

         jLabel5.setText("Captain:");

         jLabel6.setText("Owner:");

         jLabel7.setText("Sponsors:");


         jTextField1.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 jTextField1ActionPerformed(evt);
             }
         });

         comboModel1 = new ComboModel(getYachtClasses());
         jComboBox1.setModel(comboModel1);
         
         comboModel2 = new ComboModel(getCaptains());
         jComboBox2.setModel(comboModel2);
         
         comboModel3 = new ComboModel(getOwners());
         jComboBox3.setModel(comboModel3);

         jButton1.setText("Save");
         jButton1.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 jButton1ActionPerformed(evt);
             }
         });

         jButton2.setText("Cancel");
         jButton2.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 jButton2ActionPerformed(evt);
             }
         });
         
         javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
         getContentPane().setLayout(layout);
         layout.setHorizontalGroup(
             layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
             .addGroup(layout.createSequentialGroup()
                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(layout.createSequentialGroup()
                         .addGap(36, 36, 36)
                         .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addComponent(jLabel1)
                             .addComponent(jLabel2)
                             .addComponent(jLabel3)
                             .addComponent(jLabel4)
                             .addComponent(jLabel5)
                             .addComponent(jLabel6)
                             .addComponent(jLabel7))
                         .addGap(45, 45, 45)
                         .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                             .addComponent(jTextField4)
                             .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                             .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                             .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                             .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                             .addComponent(jTextField2)
                             .addComponent(jTextField3)))
                     .addGroup(layout.createSequentialGroup()
                         .addGap(100, 100, 100)
                         .addComponent(jButton1)
                         .addGap(40, 40, 40)
                         .addComponent(jButton2)))
                 .addContainerGap(45, Short.MAX_VALUE))
         );
         layout.setVerticalGroup(
             layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
             .addGroup(layout.createSequentialGroup()
                 .addGap(33, 33, 33)
                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jLabel1)
                     .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                 .addGap(37, 37, 37)
                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jLabel2)
                     .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                 .addGap(42, 42, 42)
                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jLabel3)
                     .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                 .addGap(45, 45, 45)
                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jLabel4)
                     .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                 .addGap(42, 42, 42)
                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jLabel5)
                     .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                 .addGap(45, 45, 45)
                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jLabel6)
                     .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                 .addGap(42, 42, 42)
                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jLabel7)
                     .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jButton2)
                     .addComponent(jButton1))
                 .addGap(38, 38, 38))
         );

         pack();
    	
    }


	public javax.swing.JTextField getjTextField1() {
		return jTextField1;
	}

	public javax.swing.JTextField getjTextField2() {
		return jTextField2;
	}
	
	public javax.swing.JTextField getjTextField3() {
		return jTextField3;
	}
	
	public javax.swing.JTextField getjTextField4() {
		return jTextField4;
	}
	
	/** performed when "Save" button is clicked */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
//        Yacht.create(sailNumber, yachtClass, name, year, captain, owner, sponsors)
        Yacht.create(getjTextField2().getText(), YachtClass.get(1), getjTextField1().getText(), Integer.valueOf(getjTextField3().getText()), Captain.get(1), Owner.get(1), getjTextField4().getText());
        setVisible(false);
    }
    
    /** performed when "Cancel" button is clicked */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
    	setVisible(false);
    }
    
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
	private static List getYachtClasses() {
		List<YachtClass> yachtTypes = YachtClass.getAll();
		List types = new ArrayList();
		for(YachtClass yachtType : yachtTypes) {
			types.add(yachtType.getName());
		}
		return types;
	}
	
	private static List getCaptains() {
		List<Captain> captains = Captain.getAll();
		List captains2 = new ArrayList();
		for(Captain captain : captains) {
			captains2.add(captain.getName());
		}
		return captains2;
	}
	
	private static List getOwners() {
		List<Owner> owners = Owner.getAll();
		List owners2 = new ArrayList();
		for(Owner owner : owners) {
			owners2.add(owner.getName());
		}
		return owners2;
	}
	
}
