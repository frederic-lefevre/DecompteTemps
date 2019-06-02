/*
 * Created on Nov 5, 2005
 *
 */
package org.fl.decompteTemps.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * @author Fr�d�ric Lef�vre
 *
 */
public class StorageEntity {

    private File fileEntity ;
    private String name ;
    private static String datePattern = "dd/MM/yyyy HH:mm" ;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
    private static String commentSeparator = "-" ;
    private static String inMark = "In" ;
    private static String outMark = "Out" ;
    
    static {
        dateFormat.setLenient(false) ;
    }
    
    /**
     * Create a storage entity with a existant file
     * @param f File
     */
    public StorageEntity(File f) {
        super();
        fileEntity = f ;
        name = getName() ;
    }

    /**
     * Create a storage entity with a non existant file
     * @param f File
     * @param n Name
     */
    public StorageEntity(File f, String n) {
        super();
        fileEntity = f ;
        name = n ;
    }

    /**
     * Get the presence agenda
     * @return The presence agenda
     */
    public PresenceAgenda getPresenceAgenda() {
        
        String inputLine = "" ;
        PresenceAgenda agd = new PresenceAgenda() ;
        // Line number
        int lineNumber = 1 ;
        BufferedReader in ;
        
        try {
        	in = new BufferedReader( new FileReader(fileEntity));
        
        	try {
           
        		// read first line : name
        		inputLine = in.readLine() ;
            
        		// one line
        		String[] inFields ;
        		String[] outFields ;
            
        		while ((inputLine = in.readLine()) != null) {
                 
        			lineNumber = lineNumber + 2 ;
        			// read in line
        			inFields = inputLine.split(commentSeparator) ;
        			Control.presenceLog.finer("Find presence in date " + inputLine) ;
                
        			// read  out line
        			if ((inputLine = in.readLine()) != null) {
        				outFields = inputLine.split(commentSeparator) ;
        				Control.presenceLog.finer("Find presence out date " + inputLine) ;
        			} else {
        				outFields = null ;
        			}
                
        			if ((! isIn(inFields)) || (isIn(outFields))) {
        				in.close();
        				throw (new ParseException("Malformed period: " + fileEntity.getAbsolutePath(), lineNumber)) ;
        			}
        			agd.addPresence(new Period(getFieldDate(inFields), getFieldDate(outFields), getFieldComment(inFields), getFieldComment(outFields))) ;
                
        		}
          
        	} catch (ParseException e) {
        		Control.presenceLog.log(Level.SEVERE, "Parse entity file error, line " + lineNumber, e) ;
        		Control.presenceLog.severe("File " + fileEntity.getAbsolutePath()) ;
        	} catch (IOException e) {
        		Control.presenceLog.log(Level.SEVERE, "IO exception reading entity File, line " + lineNumber, e) ;
        		Control.presenceLog.severe("File " + fileEntity.getAbsolutePath()) ;
        	}
        	
        	  // close file
            in.close();
        } catch (FileNotFoundException e) {
    		Control.presenceLog.log(Level.SEVERE, "Entity File not found ", e) ;
    		Control.presenceLog.severe("File " + fileEntity.getAbsolutePath()) ;
        }catch (IOException e) {
    		Control.presenceLog.log(Level.SEVERE, "IO exception closing entity File ", e) ;
    		Control.presenceLog.severe("File " + fileEntity.getAbsolutePath()) ;
    	}
       return agd ;
    }

    private boolean isIn(String[] f) {
        if ((f != null) && (f.length > 0)) {
            return f[0].equals(inMark) ;
        } else {
            return false ;
        }       
    }
    
    private Date getFieldDate(String[] f) throws ParseException {
        if ((f != null) && (f.length > 1)) {
            return dateFormat.parse(f[1]) ;
        } else {
            return null ;
        }
    }
    
    private String getFieldComment(String[] f) {
        if ((f != null) && (f.length > 2)) {
            return f[2] ;
        } else {
            return "";
        }
    }
    
    /**
     * Get entity name
     * @return The name of the entity
     */
    public String getName() {
        
        String inputLine = "" ;
        try {
            BufferedReader in = new BufferedReader( new FileReader(fileEntity));
            inputLine = in.readLine() ;
            in.close();
        } catch (FileNotFoundException e) {
            Control.presenceLog.log(Level.SEVERE, "Entity File not found ", e) ;
            Control.presenceLog.severe("File " + fileEntity.getAbsolutePath()) ;
        } catch (IOException e) {
            Control.presenceLog.log(Level.SEVERE, "IO exception reading entity File", e) ;
            Control.presenceLog.severe("File " + fileEntity.getAbsolutePath()) ;
        }
        Control.presenceLog.finer("Load entity name : " + inputLine) ;
        return inputLine ;
    }

    /**
     * Store the entity
     * @param agd the presence agenda
     */
    public void storeEntity(PresenceAgenda agd) {
        
        // (Re)Create the file
        if (fileEntity.exists()) {
            if (! fileEntity.delete()) {
                Control.presenceLog.severe("Cannot delete entity file: " + fileEntity.getAbsolutePath()) ;
            }
        }
        try {
            if (fileEntity.createNewFile()) {
                
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileEntity)));
                
                // store the name in the file
                out.println(name) ;
            
                // store the presence agenda in the file
                Period[] periods = agd.getPeriods() ;
                for (int i=0; i < periods.length; i++) {
                    addDate(out, periods[i].getBegin(), periods[i].getCommentIn(), inMark) ;
                    if (periods[i].getEnd() != null) {
                        addDate(out, periods[i].getEnd(), periods[i].getCommentOut(), outMark) ;
                    }
                }
                out.close() ;
               
            }
        } catch (IOException e) {
            Control.presenceLog.log(Level.SEVERE, "IO exception writing entity File", e) ;
            Control.presenceLog.severe("File " + fileEntity.getAbsolutePath()) ;
        }
    }
    
    /**
     * Add a presence
     * @param p The period
     */
    public void addPresence(Period p) {

        PrintWriter out;
        try {
            // Create the file if it does not exists
            if (fileEntity.exists()) {
                out = new PrintWriter(new BufferedWriter(new FileWriter(fileEntity, true)));
            } else {
                if (fileEntity.createNewFile()) {
                    out = new PrintWriter(new BufferedWriter(new FileWriter(fileEntity)));
                    
                    // store the name in the file
                    out.println(name) ;
                } else {
                    throw new IOException("IO exception creating entity file") ;
                }
            }
            addDate(out, p.getBegin(), p.getCommentIn(), inMark) ;
            if (p.getEnd() != null) {
                addDate(out, p.getEnd(), p.getCommentOut(), outMark) ;
            }
            out.close() ;

        } catch (IOException e) {
            Control.presenceLog.log(Level.SEVERE, "IO exception writing entity File", e);
            Control.presenceLog.severe("File " + fileEntity.getAbsolutePath());
        }

    }
    
    /**
     * Add a end date
     * @param d Date
     * @param comment
     */
    public void addEndDate(Date d, String comment) {
        
        PrintWriter out;
        Control.presenceLog.finer("Add end date " + dateFormat.format(d) + " to storage entity " + name) ;
        try {
            // Create the file if it does not exists
            if (fileEntity.exists()) {
               
                out = new PrintWriter(new BufferedWriter(new FileWriter(fileEntity, true)));
                Period lastPeriod = getLastPeriod() ;
                if (lastPeriod.getEnd() != null) {
                	 out.close() ;
                    throw new IllegalArgumentException("Trying to write a end date instead of begin") ;                   
                }
            } else {
                throw new IllegalArgumentException("Trying to write an end date to a empty file") ;             
            }
            addDate(out, d, comment, outMark) ;

            out.close() ;
        } catch (IOException e) {
            Control.presenceLog.log(Level.SEVERE, "IO exception writing entity File", e);
            Control.presenceLog.severe("File " + fileEntity.getAbsolutePath());
        }         
    }
    
    /**
     * Add a begin date
     * @param d Date
     * @param comment
     */
    public void addBeginDate(Date d, String comment) {
        
        PrintWriter out;
        Control.presenceLog.finer("Add begin date " + dateFormat.format(d) + " to storage entity " + name) ;
        try {
            // Create the file if it does not exists
            if (fileEntity.exists()) {
               
                out = new PrintWriter(new BufferedWriter(new FileWriter(fileEntity, true)));
                Period lastPeriod = getLastPeriod() ;
                if (lastPeriod.getEnd() == null) {
                	out.close() ;
                    throw new IllegalArgumentException("Trying to write a begin date instead of end") ;
                }
            } else {
                if (fileEntity.createNewFile()) {
                    out = new PrintWriter(new BufferedWriter(new FileWriter(fileEntity)));

                    // store the name in the file
                    out.println(name);
                } else {
                    throw new IOException("IO exception creating entity file");
                }
            }
            addDate(out, d, comment, inMark) ;
           
            out.close() ;
        } catch (IOException e) {
            Control.presenceLog.log(Level.SEVERE, "IO exception writing entity File", e);
            Control.presenceLog.severe("File " + fileEntity.getAbsolutePath());
        }         
    }
    
    /**
     * Get the last period
     * @return the last period
     * @throws IOException
     */
    public Period getLastPeriod() throws IOException {
        // WARNING : works if there is at least 2 lines (one complete period)
        
        BufferedReader in = new BufferedReader( new FileReader(fileEntity));
        String inputLine = "" ;
        String lastInputLine = null;
        String beforeLastInputLine = null;
        
        // read all lines
        while ((inputLine = in.readLine()) != null) {
            beforeLastInputLine = lastInputLine ;
            lastInputLine = inputLine ;
        }
        
        // analyse before last line
        String[] beforeLastFields = beforeLastInputLine.split(commentSeparator) ;
        
        // analyse last line
        String[] lastFields = lastInputLine.split(commentSeparator) ;
        
        Period p = null;
        try { 
            if (isIn(beforeLastFields)) {
                // 2 last lines are in out
                p = new Period(getFieldDate(beforeLastFields), getFieldDate(lastFields), getFieldComment(beforeLastFields), getFieldComment(lastFields)) ;
            } else {
                // 2 last line are out in
                p = new Period(getFieldDate(lastFields), null, getFieldComment(lastFields), "") ;
            }
        } catch(ParseException e) {
            Control.presenceLog.log(Level.SEVERE, "Parse entity file error ", e) ;
            Control.presenceLog.severe("File " + fileEntity.getAbsolutePath()) ;
        }

        // close file
        in.close();
        if (p != null) {
            Control.presenceLog.finer("GetLastPeriod: begin=" + dateFormat.format(p.getBegin())) ;
            if (p.getEnd() != null)   Control.presenceLog.finer(" End=" + dateFormat.format(p.getEnd())) ;
        }
        return p;
    }
    
    /**
     * @param out printwoiter to write to
     * @param d date
     * @param c comment
     * @param mark in or out mark
     */
    private void addDate(PrintWriter out, Date d, String c, String mark) {
        
        out.print(mark) ;
        out.print(commentSeparator) ;
        out.print(dateFormat.format(d)) ;
        out.print(commentSeparator) ;
        out.println(c) ;
        
    }
}
