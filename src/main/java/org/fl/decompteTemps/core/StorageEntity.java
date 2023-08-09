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

package org.fl.decompteTemps.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageEntity {

	private static final Logger presenceLog = Control.getLogger();
	
    private final Path   fileEntity ;
    private final String name ;
    
    private final static String datePattern = "dd/MM/yyyy HH:mm" ;
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
    private final static String commentSeparator = "-" ;
    private final static String inMark = "In" ;
    private final static String outMark = "Out" ;
    
    static {
        dateFormat.setLenient(false) ;
    }
    
    public StorageEntity(Path f) {
        super();
        fileEntity = f ;
        name = getName() ;
    }

    public StorageEntity(Path f, String n) {
        super();
        fileEntity = f ;
        name = n ;
    }

    public PresenceAgenda getPresenceAgenda() {
        
        String inputLine = "" ;
        PresenceAgenda agd = new PresenceAgenda() ;
        // Line number
        int lineNumber = 1 ;
        
     	try (BufferedReader in = Files.newBufferedReader(fileEntity)) {
       
    		// read first line : name
    		inputLine = in.readLine() ;
        
    		// one line
    		String[] inFields ;
    		String[] outFields ;
        
    		while ((inputLine = in.readLine()) != null) {
             
    			lineNumber = lineNumber + 2 ;
    			// read in line
    			inFields = inputLine.split(commentSeparator) ;
    			presenceLog.finer("Find presence in date " + inputLine) ;
            
    			// read  out line
    			if ((inputLine = in.readLine()) != null) {
    				outFields = inputLine.split(commentSeparator) ;
    				presenceLog.finer("Find presence out date " + inputLine) ;
    			} else {
    				outFields = null ;
    			}
            
    			if ((! isIn(inFields)) || (isIn(outFields))) {
    				in.close();
    				throw (new ParseException("Malformed period: " + fileEntity, lineNumber)) ;
    			}
    			agd.addPresence(new Period(getFieldDate(inFields), getFieldDate(outFields), getFieldComment(inFields), getFieldComment(outFields))) ;
            
    		}
      
    	} catch (ParseException e) {
    		presenceLog.log(Level.SEVERE, "Parse entity file error, line " + lineNumber, e) ;
    		presenceLog.severe("File " + fileEntity) ;
    	} catch (IOException e) {
    		presenceLog.log(Level.SEVERE, "IO exception reading entity File, line " + lineNumber, e) ;
    		presenceLog.severe("File " + fileEntity) ;
    	}  catch (Exception e) {
    		presenceLog.log(Level.SEVERE, "Exception reading entity File, line " + lineNumber, e) ;
    		presenceLog.severe("File " + fileEntity) ;
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
    
    public String getName() {
        
        String inputLine = "" ;
        try (BufferedReader in = Files.newBufferedReader(fileEntity)) {
            inputLine = in.readLine() ;
        } catch (FileNotFoundException e) {
            presenceLog.log(Level.SEVERE, "Entity File not found ", e) ;
            presenceLog.severe("File " + fileEntity) ;
        } catch (IOException e) {
            presenceLog.log(Level.SEVERE, "IO exception reading entity File", e) ;
            presenceLog.severe("File " + fileEntity) ;
        }
        presenceLog.finer("Load entity name : " + inputLine) ;
        return inputLine ;
    }

    public void storeEntity(PresenceAgenda agd) {
        
        // (Re)Create the file
    	if (Files.exists(fileEntity)) {
    		try {
    			Files.delete(fileEntity) ;
    		} catch (Exception e) {
    			presenceLog.severe("Cannot delete entity file: " + fileEntity) ;
    		}
    	}
        try (BufferedWriter out = Files.newBufferedWriter(fileEntity)){
                         
                // store the name in the file
                out.append(name) ;
                out.newLine() ;
            
                // store the presence agenda in the file
                Period[] periods = agd.getPeriods() ;
                for (int i=0; i < periods.length; i++) {
                    addDate(out, periods[i].getBegin(), periods[i].getCommentIn(), inMark) ;
                    if (periods[i].getEnd() != null) {
                        addDate(out, periods[i].getEnd(), periods[i].getCommentOut(), outMark) ;
                    }
                }
                out.close() ;
               
          
        } catch (IOException e) {
            presenceLog.log(Level.SEVERE, "IO exception writing entity File", e) ;
            presenceLog.severe("File " + fileEntity) ;
        }
    }
    
    public void addPresence(Period p) {

    	try (BufferedWriter out = Files.newBufferedWriter(fileEntity)){

    		// store the name in the file
    		out.append(name) ;
    		out.newLine() ;

    		addDate(out, p.getBegin(), p.getCommentIn(), inMark) ;
    		if (p.getEnd() != null) {
    			addDate(out, p.getEnd(), p.getCommentOut(), outMark) ;
    		}
    		out.close() ;

    	} catch (IOException e) {
    		presenceLog.log(Level.SEVERE, "IO exception writing entity File", e);
    		presenceLog.severe("File " + fileEntity);
    	}

    }
    
    public void addEndDate(Date d, String comment) {
        
        presenceLog.finer("Add end date " + dateFormat.format(d) + " to storage entity " + name) ;
        try (BufferedWriter out = Files.newBufferedWriter(fileEntity)){
            // Create the file if it does not exists
            if (Files.exists(fileEntity)) {
               
                Period lastPeriod = getLastPeriod() ;
                if (lastPeriod.getEnd() != null) {
                	 out.close() ;
                    throw new IllegalArgumentException("Trying to write a end date instead of begin") ;                   
                }
            } else {
            	out.close() ;
                throw new IllegalArgumentException("Trying to write an end date to a empty file") ;             
            }
            addDate(out, d, comment, outMark) ;
            
        } catch (IOException e) {
            presenceLog.log(Level.SEVERE, "IO exception writing entity File", e);
            presenceLog.severe("File " + fileEntity);
        }         
    }
    
    public void addBeginDate(Date d, String comment) {
        
        presenceLog.finer("Add begin date " + dateFormat.format(d) + " to storage entity " + name) ;
        try (BufferedWriter out = Files.newBufferedWriter(fileEntity)){

        	// Create the file if it does not exists
        	if (! Files.exists(fileEntity)) {

        		// store the name in the file
        		out.write(name) ;
        		out.newLine() ;
        	}
            Period lastPeriod = getLastPeriod() ;
            if (lastPeriod.getEnd() == null) {
            	out.close() ;
                throw new IllegalArgumentException("Trying to write a begin date instead of end") ;
            }
            addDate(out, d, comment, inMark) ;
           
        } catch (IOException e) {
            presenceLog.log(Level.SEVERE, "IO exception writing entity File", e);
            presenceLog.severe("File " + fileEntity);
        }         
    }
    
    public Period getLastPeriod() throws IOException {
        // WARNING : works if there is at least 2 lines (one complete period)
        
    	Period p = null;
        try (BufferedReader in = Files.newBufferedReader(fileEntity)) {
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
	        
	        try { 
	            if (isIn(beforeLastFields)) {
	                // 2 last lines are in out
	                p = new Period(getFieldDate(beforeLastFields), getFieldDate(lastFields), getFieldComment(beforeLastFields), getFieldComment(lastFields)) ;
	            } else {
	                // 2 last line are out in
	                p = new Period(getFieldDate(lastFields), null, getFieldComment(lastFields), "") ;
	            }
	        } catch(ParseException e) {
	            presenceLog.log(Level.SEVERE, "Parse entity file error ", e) ;
	            presenceLog.severe("File " + fileEntity) ;
	        }
	
	        if (p != null) {
	        	presenceLog.finer("GetLastPeriod: begin=" + dateFormat.format(p.getBegin())) ;
	            if (p.getEnd() != null)   presenceLog.finer(" End=" + dateFormat.format(p.getEnd())) ;
	        }
        }
        return p;
    }
    
    private void addDate(BufferedWriter out, Date d, String c, String mark) throws IOException {
        
        out.write(mark) ;
        out.write(commentSeparator) ;
        out.write(dateFormat.format(d)) ;
        out.write(commentSeparator) ;
        out.write(c) ;
        out.newLine() ;
        
    }
}
