package org.fl.decompteTemps.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fl.decompteTemps.core.Control;
import org.fl.decompteTemps.core.Entity;
import org.fl.decompteTemps.core.GroupEntity;

public class GestionGroupe {

	private JPanel gestionGrp ;
	private JComboBox<String> choixGr;
	private JButton validerChoix ;
	private JButton reecriture ;
	private JButton relecture ;
	private Entity[] enfants ;
	
	public GestionGroupe() {
		
		gestionGrp = new JPanel() ;
		gestionGrp.setLayout(new GridLayout(2, 1, 5, 5)) ;
		
		// Choix groupe
		JPanel choixGroupe = new JPanel() ;
		choixGroupe.setLayout(new GridLayout(1, 3, 5, 5)) ;
		
		choixGroupe.add(new JLabel("Enfants: ")) ;
		
		choixGr = new JComboBox<String>() ;
		
		update() ;
		choixGroupe.add(choixGr) ;
		
		validerChoix = new JButton("Afficher") ;
		choixGroupe.add(validerChoix) ;
		
		gestionGrp.add(choixGroupe) ;
		
		// Lecture et ecriture des fichiers
		JPanel lectureEcriture = new JPanel() ;
		lectureEcriture.setLayout(new GridLayout(1, 2, 5, 5)) ;
		
		relecture = new JButton("Relecture de donn�es") ;
		lectureEcriture.add(relecture) ;
		
		reecriture = new JButton("R�ecrire les donn�es") ;
		lectureEcriture.add(reecriture) ;
		
		gestionGrp.add(lectureEcriture) ;
		
		validerChoix.addActionListener(new ChoixGrpValide()) ;
		reecriture.addActionListener(new ReecritureValide()) ;
		relecture.addActionListener(new RelectureValide()) ;
	}

	public JPanel getGestionGroupe() {
		return gestionGrp ;
	}
	
	public void update() {
		
		GroupEntity completeGroup = Control.getCompleteGroup() ;
		enfants = completeGroup.getEntities() ;
		
		choixGr.removeAllItems() ;
		for (int i=0; i < enfants.length; i++) {
			choixGr.addItem(enfants[i].getName()) ;
		}
		choixGr.addItem("Tous") ;
		
	}
	
	public class ChoixGrpValide implements ActionListener {
		
		public void actionPerformed(ActionEvent arg0) {
			
			GroupEntity completeGroup = Control.getCompleteGroup() ;
			GroupEntity gr ;

			int whoIdx = choixGr.getSelectedIndex() ;
			Control.presenceLog.info("Choix groupe index " + whoIdx) ;
			
			if (whoIdx < enfants.length) {
				gr = Control.getIndividualEntityAsGroups( enfants[whoIdx].getName()) ;
			} else {
			    gr = completeGroup ;
			}
			Control.setCurrentGroup(gr) ;
			
			DecompteTempsGui.updateDecompteTemps() ;
		}
	}
	
	public class RelectureValide implements ActionListener {
		
		public void actionPerformed(ActionEvent arg0) {
			
			Control.forceInit() ;
			DecompteTempsGui.updateDecompteTemps() ;
		}
	}

	public class ReecritureValide implements ActionListener {
		
		public void actionPerformed(ActionEvent arg0) {
			
			GroupEntity gr = Control.getCompleteGroup() ;

	        if (gr != null) {
	            Entity[] enfants = gr.getEntities();
	            if ((enfants != null) && (enfants.length > 0)) {
	                for (int i=0; i < enfants.length; i++) {
	                    enfants[i].storeEntity() ;
	                }
	            
	            }
	        } 
			DecompteTempsGui.updateDecompteTemps() ;
		}
	}
}
