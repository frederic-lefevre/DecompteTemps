/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.fl.decompteTemps.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fl.decompteTemps.core.Control;
import org.fl.decompteTemps.core.Entity;
import org.fl.decompteTemps.core.GroupEntity;

public class GestionGroupe {

	private static final Logger presenceLog = Control.getLogger();
	
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
			presenceLog.info("Choix groupe index " + whoIdx) ;
			
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
