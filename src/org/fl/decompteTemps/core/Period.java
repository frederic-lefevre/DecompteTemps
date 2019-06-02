package org.fl.decompteTemps.core;

import java.util.Date;

public class Period {
    
    private Date begin;
    private Date end;
    private String commentIn;
    private String commentOut;
    
    /**
     * @param b Begin of the period
     * @param e End of the period
     * @param ci Comment in
     * @param co Comment out
     */
    public Period(Date b, Date e, String ci,  String co) {
        commentIn = ci;
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
    
    /**
     * Get the period duration
     * @return The period duration in milliseconds
     */
    public long getDuration() {
        if (end == null) {
            return (Control.getEndDate().getTime() - begin.getTime()) ;
        } else {
            return (end.getTime() - begin.getTime()) ;
        }
    }
    
    /** Get the duration between date a and b
     * @param a
     * @param b
     * @return the duration between date a and b
     */
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
    /**
     * @return Returns the begin.
     */
    public Date getBegin() {
        return begin;
    }
    /**
     * @return Returns the end.
     */
    public Date getEnd() {
        return end;
    }
    /**
     * @return Returns the in comment.
     */
    public String getCommentIn() {
        return commentIn;
    }
    /**
     * @return Returns the out comment.
     */  
    public String getCommentOut() {
        return commentOut;
    }
}
