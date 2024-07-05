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

package org.fl.decompteTemps.core;

import java.util.Date;
import java.util.logging.Logger;

public class Period {
    
	private static final Logger presenceLog = Logger.getLogger(Period.class.getName());
	
    private Date   begin;
    private Date   end;
    private String commentIn;
    private String commentOut;
    

    public Period(Date b, Date e, String ci,  String co) {
        commentIn  = ci;
        commentOut = co;
        if (b == null) {
            throw new IllegalArgumentException("Begin is null") ;
        }
        if ((e != null) && (b.after(e))) {
            presenceLog.severe("Begin is after end") ;
            throw new IllegalArgumentException("Begin is after end") ;
        }
       begin = b;
       end = e ;
    }
    
    public long getDuration() {
        if (end == null) {
            return (Control.getEndDate().getTime() - begin.getTime()) ;
        } else {
            return (end.getTime() - begin.getTime()) ;
        }
    }
    
    public long getDuration(Date a, Date b) {
        if (a.after(b)) {
            presenceLog.severe("Begin is after end") ;
            throw new IllegalArgumentException("Begin is after end") ;
        }
        long start = Math.max(a.getTime(), begin.getTime()) ;
        
        long stop ;
        if (end == null) {
            stop = Math.min(b.getTime(), Control.getEndDate().getTime()) ;
        } else {
            stop = Math.min(b.getTime(), end.getTime()) ;
        }
        
        return Math.max(stop - start, 0) ;
    }

    public Date   getBegin() 	  { return begin	  ; }
    public Date   getEnd() 		  { return end		  ; }
    public String getCommentIn()  { return commentIn  ; }
    public String getCommentOut() { return commentOut ; }
}
