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

import java.awt.Color;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.fl.decompteTemps.core.Control;
import org.fl.decompteTemps.core.Entity;
import org.fl.decompteTemps.core.GroupEntity;
import org.fl.decompteTemps.util.AgendaFormat;

public class StatGlobaleTable {

	private JTable 		statGlobale ;
	private JScrollPane scrollStat ;
	
	public JScrollPane getStatGlobale() {
		return scrollStat;
	}
	
	public StatGlobaleTable() {
		
		statGlobale = new JTable() ;
		update() ;
		
		scrollStat = new JScrollPane(statGlobale);
		statGlobale.setFillsViewportHeight(true);
		statGlobale.setGridColor(Color.cyan) ;
		statGlobale.doLayout() ;
		
	}
	
	public void update() {
		
		Date endDate 	   = Control.getEndDate() ;
		GroupEntity gr 	   = Control.getCurrentGroup() ;
		Entity[] enfants   = gr.getEntities();
		Date beginPresence = gr.getBeginPresence() ;
		long presPerEntity = gr.getPresenceDurationPerEntity(beginPresence, endDate) ;
		long totalTime 	    = endDate.getTime() - beginPresence.getTime() ;
		Object[][] result   = new Object[enfants.length+2][4] ;
		
		for (int i = 0; i < enfants.length; i++) { 
		
			result[i][0] = enfants[i].getName() ;
			result[i][1] = "-" ;
			result[i][2] = AgendaFormat.durationToString(enfants[i].getPresenceDuration()) ;
			result[i][3] = enfants[i].getPresencePercentage(beginPresence, endDate) ;
		} 
		result[enfants.length][0] = "Pr�sence" ;
		result[enfants.length][1] = AgendaFormat.durationToString(gr.getPresenceDurationTotal(beginPresence, endDate)) ;
		result[enfants.length][2] = AgendaFormat.durationToString(presPerEntity) ;
		result[enfants.length][3] = gr.getPresencePercentage(beginPresence, endDate) ;
			
		result[enfants.length+1][0]	= "Total" ;
		result[enfants.length+1][1]	= AgendaFormat.durationToString(totalTime*enfants.length) ;
		result[enfants.length+1][2]	= AgendaFormat.durationToString(totalTime) ;
		result[enfants.length+1][3]	= "100%" ;
		
		statGlobale.setModel(new DefaultTableModel(
				result,
				new String[] {"", "Nb jours*enfants", "Nombre de jours", "Pourcentage"}
			));
		
	}

}
