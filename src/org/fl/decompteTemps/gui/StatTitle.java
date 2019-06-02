package org.fl.decompteTemps.gui;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import org.fl.decompteTemps.core.Control;
import org.fl.decompteTemps.core.Entity;
import org.fl.decompteTemps.core.GroupEntity;

public class StatTitle {

	private StringBuilder sTitle ;
	private JLabel 		  jTitle ;
	
	public StatTitle(Date d) {
		
		jTitle = new JLabel() ;
		update(d) ;
		jTitle.setBackground(Color.orange) ;
		jTitle.setBorder(new LineBorder((Color.orange), 3)) ;
	}
	
	public void update(Date d) {
		GroupEntity gr = Control.getCurrentGroup() ;
		Entity[] enfants = gr.getEntities();
		String dateFrancePattern = "EEEE dd MMMM yyyy � HH:mm" ;
		SimpleDateFormat dateFranceFormat = new SimpleDateFormat(dateFrancePattern, Locale.FRANCE);
		
		sTitle = new StringBuilder("Statistiques pour ") ;
		for (int i = 0; i < enfants.length; i++) { 
			sTitle.append(enfants[i].getName()) ;
			sTitle.append(" ") ;
		} 
		sTitle.append(dateFranceFormat.format(d)) ;
		jTitle.setText(sTitle.toString()) ;
		Font font = new Font("Verdana", Font.BOLD, 14);
		jTitle.setFont(font);
		jTitle.setForeground(Color.BLUE);
	}
	
	public JLabel getStatTitle() {
		
		return jTitle ;
	}

}
