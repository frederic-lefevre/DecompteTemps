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
		
		String dateFrancePattern = "EEEE dd MMMM yyyy à HH:mm" ;
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
