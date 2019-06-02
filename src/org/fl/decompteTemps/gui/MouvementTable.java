package org.fl.decompteTemps.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.fl.decompteTemps.core.Control;
import org.fl.decompteTemps.core.Events;
import org.fl.decompteTemps.core.GroupEntity;

public class MouvementTable {

	private JTable tableMouvement ;
	private JScrollPane scrollMove ;
	
	public JScrollPane getMouvementTable() {
		return scrollMove;
	}
	
	public MouvementTable() {

		tableMouvement = new JTable() ;
		update() ;
		scrollMove = new JScrollPane(tableMouvement);
		
		tableMouvement.setFillsViewportHeight(true);
		tableMouvement.doLayout() ;
	}

	public void update() {
		
		String dateFrancePattern = "EEEE dd MMMM yyyy � HH:mm" ;
		SimpleDateFormat dateFranceFormat = new SimpleDateFormat(dateFrancePattern, Locale.FRANCE);
		Date d ;
		int evType ;
		String comment;
		String[] evNames;
		GroupEntity gr = Control.getCurrentGroup() ;
		Events[] events = gr.getEventsAgenda().getEvents() ;
		Object[][] result = new Object[events.length][2] ;
		StringBuilder mvt ;
		
    	for (int i=0; i < events.length; i++) {
    		d = events[i].getDateEvents() ;
    		evNames = events[i].getNameEvents() ;
    		evType = events[i].getTypeEvents() ;
    		comment = events[i].getComment() ;
    
    		result[i][0] = dateFranceFormat.format(d); 
    		mvt = new StringBuilder("") ;
    		
     		for (int j=0; j < evNames.length; j++) { 
     			mvt.append(evNames[j]) ; 
     			mvt.append(" ") ;	
     		} 
     	 	if (evType == Events.IN) {	
     	 		mvt.append("in") ;
    		} else if (evType == Events.OUT) { 
    			mvt.append("out") ;
    		} else { 
    			mvt.append(evType) ; 
    		} 
     	 	if ((comment != null) && (comment.length() > 0)) { 
     	 		mvt.append(" - ") ;
     	 		mvt.append(comment) ; 
     	 	} 
     	 	result[i][1] = mvt ;
    	} 
    	
    	tableMouvement.setModel(new DefaultTableModel(
				result,
				new String[] {"Date", "Mouvement"}
			));

    	
	}
	
}
