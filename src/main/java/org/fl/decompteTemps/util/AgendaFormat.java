package org.fl.decompteTemps.util;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.fl.decompteTemps.core.Events;

public class AgendaFormat {

	private final static String 		  dayPattern 			= "dd/MM/yyyy" ;
	private final static SimpleDateFormat dayFormat  			= new SimpleDateFormat(dayPattern, Locale.US);
	private final static String 		  datePattern 			=  dayPattern + " HH:mm";
	private final static SimpleDateFormat dateFormat 			= new SimpleDateFormat(datePattern, Locale.US);
	private final static String 		  dateFrancePattern 	= "EEEE dd MMMM yyyy à HH:mm" ;
	private final static SimpleDateFormat dateFranceFormat 		= new SimpleDateFormat(dateFrancePattern, Locale.FRANCE);
	private final static String 		  dateFullFrancePattern = "EEEE dd MMMM yyyy � HH:mm:ss:SSS" ;
	private final static SimpleDateFormat dateFullFranceFormat  = new SimpleDateFormat(dateFullFrancePattern, Locale.FRANCE);

    static {
        dateFormat.setLenient(false) ;
        dayFormat.setLenient(false) ;
    }

	/**
	 * @param date
	 * @return The date as a string (precision: minute)
	 */
	public static String dayOfDateToString(Date date) {
		
		if (date == null) {
			return "";
		} else {
			return dayFormat.format(date) ;
		}
	}

	/**
	 * @param date
	 * @return The date as a string (precision: milisecond)
	 */
	public static String fullDateToString(Date date) {
		
		if (date == null) {
			return "";
		} else {
			return dateFullFranceFormat.format(date) ;
		}
	}

	/**
	 * @param duration
	 * @return The duration as a string (precision : heures)
	 */
	public static String durationToString(long duration) {
	    
	    long nbHeure 	 = duration / (1000 * 3600) ;
	    long nbDay 		 = nbHeure / 24 ;
	    long remainHeure = nbHeure % 24 ;
	    
	    return (Long.toString(nbDay) + " jours " + Long.toString(remainHeure) + " heures ") ;
	    
	}
	
	/**
	 * Get the date corresponding to the day/hour/min
	 * @param strDay
	 * @param strHour
	 * @param strMin
	 * @return the date corresponding to the day/hour/min
	 * @throws ParseException
	 */
	public static Date getDate(String strDay, String strHour, String strMin) throws ParseException {
	
	String hour = nullOrEmptyToZero(strHour);
	String min = nullOrEmptyToZero(strMin);
	if ((strDay == null) || (strDay.equals(""))) {
		// if day is not set, assume it is today
		strDay = dayFormat.format(new Date()) ;
	}
	return dateFormat.parse(strDay + " " + hour + ":" + min) ;
}

	/**
	 * @param events list of events
	 * @param out print destination
	 */
	public static void printEvents(Events[] events, PrintWriter out) {
	    
	    Date d ;
	    int evType ;
	    String[] evNames;
	    out.println("<table class=\"detail\"") ;
	    for (int i=0; i < events.length; i++) {
	        out.println("<tr>") ;
	        d = events[i].getDateEvents() ;
	        evNames = events[i].getNameEvents() ;
	        evType = events[i].getTypeEvents() ;
	        out.print("<td class=\"detdate\">" + dateFranceFormat.format(d) + "</td><td class=\"detname\">") ;
	        for (int j=0; j < evNames.length; j++) {
	            out.print(evNames[j] + " ") ;
	        }
	        if (evType == Events.IN) {
	            out.print("in") ;
	        } else if (evType == Events.OUT) {
	            out.print("out") ;
	        } else {
	            out.print(evType) ;
	        }
	        out.print("</td>") ;
	        out.println("<td class=\"detcom\">" + events[i].getComment() + "</td>") ;
	        out.println("</tr>") ;
	    }
	}
	
	private static String nullOrEmptyToZero(String t) {
		if ((t == null) || (t.equals(""))) {
			return "00";
		} else {
			return t;
		}
	}
	
	// get begin "month" date: first day of a month's date
	public static Date getBeginMonth(Date d) {
		
	    GregorianCalendar cal = new GregorianCalendar() ;
	    cal.setTime(d) ;
	    return getBeginMonth(cal) ;
	}
	
	// get begin "month" date: first day of a month's date
	private static Date getBeginMonth(GregorianCalendar cal) {
		
        cal.set(GregorianCalendar.DAY_OF_MONTH, cal.getActualMinimum(GregorianCalendar.DAY_OF_MONTH)) ;
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0) ;
        cal.set(GregorianCalendar.MINUTE, 0) ;
        cal.set(GregorianCalendar.SECOND, 0) ;
        cal.set(GregorianCalendar.MILLISECOND, 0) ;
        return cal.getTime() ;
	}
	
	// get end "month" date: last day of a month's date
	public static Date getEndMonth(Date d) {
	    GregorianCalendar cal = new GregorianCalendar() ;
	    cal.setTime(d) ;
	    return getEndMonth(cal) ;
	}
	
	// get end "month" date: last day of a month's date
	private static Date getEndMonth(GregorianCalendar cal) {
		
        cal.set(GregorianCalendar.DAY_OF_MONTH, cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)) ;
        cal.set(GregorianCalendar.HOUR_OF_DAY, 	23) ;
        cal.set(GregorianCalendar.MINUTE, 		59) ;
        cal.set(GregorianCalendar.SECOND, 	   	59) ;
        cal.set(GregorianCalendar.MILLISECOND, 	999) ;
        return cal.getTime() ;
	}

	public static int getNbJoursMois(GregorianCalendar cal) {
         return cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH) ;
	}
	
	public static int getDayNumber(Date d) {
        GregorianCalendar nowcal = new GregorianCalendar() ;
        nowcal.setTime(d) ;
        return nowcal.get(GregorianCalendar.DAY_OF_MONTH) ;
	}
	
	public static Date getBeginYear(Date d) {
	    GregorianCalendar cal = new GregorianCalendar() ;
	    cal.setTime(d) ;

		cal.set(GregorianCalendar.YEAR, cal.get(GregorianCalendar.YEAR) - 1) ;
		return cal.getTime() ;
	}
	
	// get the number of month between 2 dates (d1  > d2)
	public static int getNbMonthBetween(Date d1, Date d2) {
		
		GregorianCalendar currDate = new GregorianCalendar() ;
		currDate.setTime(d1) ;
		Date beginMonthPresence = AgendaFormat.getBeginMonth(d2) ;
		int res = 0 ;
		
		while (currDate.getTime().after(beginMonthPresence)) { 
			currDate.add(GregorianCalendar.MONTH, -1) ;
			res = res + 1 ;
		}
		return res ;
	}
}
