/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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
import java.awt.EventQueue;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.fl.decompteTemps.core.Control;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class DecompteTempsGui extends JFrame {
	
	private static final long serialVersionUID = -3275605158047047933L;
	
	private static final String DEFAULT_PROP_FILE = "presence.properties";
	
	private static StatTitle st;
	private static GestionGroupe gestGrp;
	private static StatGlobaleTable tStat;
	private static MouvementTable tMove;
	private static PresenceStatisticsTable tMois;
	private static EntreeMouvement inMove;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		Control.init(DEFAULT_PROP_FILE);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DecompteTempsGui window = new DecompteTempsGui();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static String getPropertyFile() {
		return DEFAULT_PROP_FILE;
	}
	
	public static void updateDecompteTemps() {
		
		DecompteTempsGui.st.update(Control.getEndDate());
		DecompteTempsGui.gestGrp.update();
		DecompteTempsGui.tMove.update();
		DecompteTempsGui.tMois.update(Control.getEndDate());
		DecompteTempsGui.inMove.update();
		DecompteTempsGui.tStat.update();
	}
	
	/**
	 * Create the application.
	 */
	public DecompteTempsGui() {
		
		Date endDate = Control.getEndDate();
		
		setBounds(50, 50, 1600, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Décompte temps") ;
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		// Titre de la statistique
		st = new StatTitle(endDate);
		getContentPane().add(st.getStatTitle());
		
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		p1.setPreferredSize(new Dimension(1500,300)) ;
		
		JPanel p2  = new JPanel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
		
		// Formulaire gestion groupe
		gestGrp = new GestionGroupe();
		p2.add(gestGrp.getGestionGroupe());

		// Formulaire entrée mouvement
		inMove = new EntreeMouvement(endDate);
		p2.add(inMove.getEntreeMouvement());

		// Statistiques globales
		tStat = new StatGlobaleTable();
		JScrollPane statGlob = tStat.getStatGlobale();
		p2.add(statGlob);

		p1.add(p2);

		// Table des mouvements
		tMove = new MouvementTable();
		JScrollPane tableMouv = tMove.getMouvementTable();
		p1.add(tableMouv);

		getContentPane().add(p1);

		// Statistiques détaillées par mois
		tMois = new PresenceStatisticsTable(endDate);
		JScrollPane tableMois = tMois.getPresenceStatTable();
		tableMois.setMinimumSize(new Dimension(1500, 600));

		getContentPane().add(tableMois);
		
	}	
}
