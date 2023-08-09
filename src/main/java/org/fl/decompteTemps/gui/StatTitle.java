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
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import org.fl.decompteTemps.core.Control;
import org.fl.decompteTemps.core.Entity;
import org.fl.decompteTemps.core.GroupEntity;

public class StatTitle {

	private StringBuilder sTitle ;
	private JLabel 		  jTitle ;
	
	public StatTitle(Date d) {
		
		jTitle = new JLabel() ;
		update(d) ;
		jTitle.setBackground(Color.orange) ;
		jTitle.setBorder(new LineBorder((Color.orange), 3)) ;
	}
	
	public void update(Date d) {
		GroupEntity gr = Control.getCurrentGroup() ;
		Entity[] enfants = gr.getEntities();
		String dateFrancePattern = "EEEE dd MMMM yyyy � HH:mm" ;
		SimpleDateFormat dateFranceFormat = new SimpleDateFormat(dateFrancePattern, Locale.FRANCE);
		
		sTitle = new StringBuilder("Statistiques pour ") ;
		for (int i = 0; i < enfants.length; i++) { 
			sTitle.append(enfants[i].getName()) ;
			sTitle.append(" ") ;
		} 
		sTitle.append(dateFranceFormat.format(d)) ;
		jTitle.setText(sTitle.toString()) ;
		Font font = new Font("Verdana", Font.BOLD, 14);
		jTitle.setFont(font);
		jTitle.setForeground(Color.BLUE);
	}
	
	public JLabel getStatTitle() {
		
		return jTitle ;
	}

}
