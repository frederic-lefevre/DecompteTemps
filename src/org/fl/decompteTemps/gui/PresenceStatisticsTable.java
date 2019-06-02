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

	private JTable tableMois ;
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
