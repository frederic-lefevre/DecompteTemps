/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.fl.decompteTemps.core.Control;
import org.fl.decompteTemps.core.Entity;
import org.fl.decompteTemps.core.GroupEntity;
import org.fl.decompteTemps.util.AgendaFormat;
import org.fl.decompteTemps.util.StringFormat;

public class EntreeMouvement {

	private static final Logger presenceLog = Logger.getLogger(EntreeMouvement.class.getName());
	
	private JPanel entreeMove ;
	private JTextField jour ;
	private JTextField heure ;
	private JTextField minute ;
	private JComboBox<String> moveWho ;
	private JTextField comentaires ;
	private JButton validateMove ;
	private JLabel msg ;
	private GroupEntity completeGroup ;
	private Entity[] enfants ;
	
	private static String ENTREE = " Entrée" ;
	private static String SORTIE = " Sortie" ;
	
	public JPanel getEntreeMouvement() {
		return entreeMove;
	}

	public class entreeMvt implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			String dateFrancePattern = "EEEE dd MMMM yyyy à HH:mm" ;
			SimpleDateFormat dateFranceFormat = new SimpleDateFormat(dateFrancePattern, Locale.FRANCE);
			String startDate = StringFormat.nullToEmpty(jour.getText()).trim() ;
			String startHour = StringFormat.nullToEmpty(heure.getText()).trim() ;
			String startMin = StringFormat.nullToEmpty(minute.getText()).trim() ;
			GroupEntity gr = Control.getCurrentGroup() ;
			int whoIdx = moveWho.getSelectedIndex() ;
			String comment = comentaires.getText() ;
			Date d;
			try {
				d = AgendaFormat.getDate(startDate, startHour, startMin);
				String mvtDate = dateFranceFormat.format(d); 
				if (whoIdx < enfants.length) {
					if (enfants[whoIdx].getPresencePlace() == Entity.OUT) {
						enfants[whoIdx].addBeginDate(d, comment);
						msg.setText(enfants[whoIdx].getName() + " entré le " + mvtDate) ;
					} else {
						enfants[whoIdx].addEndDate(d, comment);
						msg.setText(enfants[whoIdx].getName() + " sorti le " + mvtDate) ;
					}
				} else {
					int presencePlace = gr.getPresencePlace();
	                if (presencePlace == GroupEntity.MIXED) {
	                	presenceLog.severe("Mouvement de groupe alors que le groupe n'est pas au même endroit") ;
	                } else if (presencePlace == GroupEntity.IN) {
	                	gr.addEndDate(d, comment);
	                	msg.setText("Tous sortis le " + mvtDate) ;
	                } else {
	                	gr.addBeginDate(d, comment);
	                	msg.setText("Tous entrés le " + mvtDate) ;
	                }
				}
			} catch (ParseException e) {
				presenceLog.severe("Exception when getting parsing date move " + e) ;
			} catch (IOException e) {
				presenceLog.severe("Exception when getting presence move " + e) ;
			}
			
			DecompteTempsGui.updateDecompteTemps() ;
		}
		
	}

	public EntreeMouvement(Date d) {
		
		String startDate = AgendaFormat.dayOfDateToString(d) ;
		
		entreeMove = new JPanel() ;
		entreeMove.setLayout(new GridLayout(5, 1, 5, 5)) ;
		
		// Entr�e date
		JPanel entreeDate = new JPanel() ;
		entreeDate.setLayout(new GridLayout(2, 3, 5, 5)) ;
		
		entreeDate.add(new JLabel("Jour/Mois/Année")) ;
		entreeDate.add(new JLabel("Heures")) ;
		entreeDate.add(new JLabel("Minutes")) ;
		
		jour = new JTextField(startDate , 10) ;
		heure = new JTextField(2) ;
		minute = new JTextField(2) ;
		
		entreeDate.add(jour) ;
		entreeDate.add(heure) ;
		entreeDate.add(minute) ;
		
		// type mouvement
		JPanel movePane = new JPanel() ;
		moveWho = new JComboBox<String>() ;
		
		// fill combobox
		update() ;
		
		validateMove = new JButton("Entrée mouvement") ;
		
		movePane.add(moveWho) ;
		movePane.add(validateMove) ;
		
		// Commentaires
		comentaires = new JTextField(10) ;
		
		// message
		msg = new JLabel("") ;
		
		entreeMove.add(entreeDate) ;
		entreeMove.add(movePane) ;
		entreeMove.add(new JLabel("Commentaires")) ;
		entreeMove.add(comentaires) ;
		entreeMove.add(msg) ;
		
		validateMove.addActionListener(new entreeMvt()) ;
		
	}
	
	public void update() {
		
		completeGroup = Control.getCurrentGroup() ;
		enfants = completeGroup.getEntities() ;
		
		moveWho.removeAllItems() ;
		int presencePlace;
		try {
			presencePlace = completeGroup.getPresencePlace();
		} catch (IOException e) {
			presencePlace = GroupEntity.IN ;
			 presenceLog.severe("Exception when getting presence place: " + e) ;
		}
		
		try {
			String lb ;
			for (int i=0; i < enfants.length; i++) {
				lb = enfants[i].getName() ;
				if (enfants[i].getPresencePlace() == Entity.OUT) {
					lb = lb + ENTREE ;
				} else {
				lb = lb + SORTIE ;
				}
				moveWho.addItem(lb.toString()) ;
			}
			if (presencePlace != GroupEntity.MIXED) {
				lb = "Tous" ;
				if (enfants[0].getPresencePlace() == Entity.OUT) {
					lb = lb + ENTREE ;
				} else {
					lb = lb + SORTIE ;
				}
				moveWho.addItem(lb) ;
			}
		} catch (IOException e) {
			presenceLog.severe("Exception when selecting direction " + e) ;
			e.printStackTrace();
		}
	}

}
