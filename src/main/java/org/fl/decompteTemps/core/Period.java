package org.fl.decompteTemps.core;

import java.util.Date;

public class Period {
    
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
            Control.presenceLog.severe("Begin is after end") ;
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
            Control.presenceLog.severe("Begin is after end") ;
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
