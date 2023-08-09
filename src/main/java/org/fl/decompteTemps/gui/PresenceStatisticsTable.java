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

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.fl.decompteTemps.core.Control;
import org.fl.decompteTemps.core.GroupEntity;
import org.fl.decompteTemps.util.AgendaFormat;

public class PresenceStatisticsTable {

	private JTable 	   	tableMois ;
	private JScrollPane scrollPane ;
	
	public JScrollPane getPresenceStatTable() {
		return scrollPane;
	}

	public PresenceStatisticsTable(Date toDate)  {
		
		tableMois = new JTable();
		update(toDate) ;
		
		scrollPane = new JScrollPane(tableMois);
		tableMois.setFillsViewportHeight(true);
		tableMois.setMinimumSize(new Dimension(1500, 800)) ;
		tableMois.doLayout() ;
		
	}

	public void update(Date toDate) {
		
		GroupEntity gr = Control.getCurrentGroup() ;
		
		Date beginPresence = gr.getBeginPresence() ;
		
		Date beginMonthPresence = AgendaFormat.getBeginMonth(beginPresence) ;
		GregorianCalendar currDate = new GregorianCalendar() ;
	    currDate.setTime(toDate) ;
		
		String monthPattern = "MMMM yyyy" ;
	    SimpleDateFormat monthFormat = new SimpleDateFormat(monthPattern, Locale.FRANCE);
	    int line = 0 ;
	    
	    Object[][] result = new Object[AgendaFormat.getNbMonthBetween(toDate, beginPresence)][10] ;
	    
		while (currDate.getTime().after(beginMonthPresence)) { 
			Date begin = AgendaFormat.getBeginMonth(currDate.getTime()) ;
			if (begin.before(beginPresence)) {
	            begin = beginPresence ;
	        }
	        int nbJoursDansMois = AgendaFormat.getNbJoursMois(currDate) ;
	        Date end = AgendaFormat.getEndMonth(currDate.getTime()) ;
	         if (end.after(toDate)) {
	         	end = toDate ;
	         	nbJoursDansMois = AgendaFormat.getDayNumber(toDate) ;
	         }
	        long presPerEntityForMonth = gr.getPresenceDurationPerEntity(begin, end) ;
	        long presPerEntityCumul = gr.getPresenceDurationPerEntity(beginPresence, end) ;
	        long deltaPresence = (presPerEntityCumul * 2) - (end.getTime() - beginPresence.getTime()) ;
	        Date beginYear = AgendaFormat.getBeginYear(end) ;
	        if (beginYear.before(beginPresence)) {
	            beginYear = beginPresence ;
	        }
	        
			result[line][0] = monthFormat.format(currDate.getTime()) ;
			result[line][1] = nbJoursDansMois ;
			result[line][2] = AgendaFormat.durationToString(gr.getPresenceDurationTotal(begin, end));
			result[line][3] = AgendaFormat.durationToString(presPerEntityForMonth) ;
			result[line][4] = gr.getPresencePercentage(begin, end) ;
			result[line][5] = AgendaFormat.durationToString(gr.getPresenceDurationTotal(beginPresence, end)) ;
			result[line][6] = AgendaFormat.durationToString(presPerEntityCumul) ;
			result[line][7] = gr.getPresencePercentage(beginPresence, end) ;
			result[line][8] = gr.getPresencePercentage(beginYear, end) ;
			result[line][9] = AgendaFormat.durationToString(deltaPresence) ;
			
			line = line + 1 ;

			currDate.add(GregorianCalendar.MONTH, -1) ;  
		}
		
		tableMois.setModel(new DefaultTableModel(
				result,
				new String[] {
					"Mois", "Nb jours du mois", "Nb jours*enfants", "Nb jours", "% garde", "Nb jours*enfants cumul", 
					"Nb jours cumul", "% garde cumul", "% garde moyenne glissante sur 1 an", "Delta jours pr�sence"
				}
			));
	}
}
