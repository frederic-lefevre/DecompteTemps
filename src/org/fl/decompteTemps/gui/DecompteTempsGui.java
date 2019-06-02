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
	
	private static StatTitle 				st ;
	private static GestionGroupe 			gestGrp ;
	private static StatGlobaleTable 		tStat ;
	private static MouvementTable 			tMove ;
	private static PresenceStatisticsTable 	tMois ;
	private static EntreeMouvement 			inMove ;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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

	public static void updateDecompteTemps() {
		
		DecompteTempsGui.st.update(Control.getEndDate()) ;
		DecompteTempsGui.gestGrp.update() ;
		DecompteTempsGui.tMove.update() ;
		DecompteTempsGui.tMois.update(Control.getEndDate()) ;
		DecompteTempsGui.inMove.update() ;
		DecompteTempsGui.tStat.update() ;
	}
	
	/**
	 * Create the application.
	 */
	public DecompteTempsGui() {
		
		Control.init() ;
		Date endDate = Control.getEndDate() ;
		
		setBounds(50, 50, 1600, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("D�compte temps") ;
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		// Titre de la statistique
		st = new StatTitle(endDate) ;
		getContentPane().add(st.getStatTitle());
		
		JPanel p1 = new JPanel() ;
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		p1.setPreferredSize(new Dimension(1500,300)) ;
		
		JPanel p2  = new JPanel() ;
		p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
		
		// Formulaire gestion groupe
		gestGrp = new GestionGroupe() ;
		p2.add(gestGrp.getGestionGroupe()) ;
		
		// Formulaire entr�e mouvement
		inMove = new EntreeMouvement(endDate) ;
		p2.add(inMove.getEntreeMouvement()) ;
		
		// Statistiques globales
		tStat = new StatGlobaleTable();
		JScrollPane statGlob = tStat.getStatGlobale() ;
		p2.add(statGlob) ;
		
		p1.add(p2) ;
		
		// Table des mouvements
		tMove = new MouvementTable();
		JScrollPane tableMouv = tMove.getMouvementTable() ;
		p1.add(tableMouv) ;
		
		getContentPane().add(p1) ;
		
		// Statistiques d�taill�es par mois
		tMois = new PresenceStatisticsTable(endDate);
		JScrollPane tableMois = tMois.getPresenceStatTable() ;
		tableMois.setMinimumSize(new Dimension(1500, 600)) ;
		
		getContentPane().add(tableMois) ;
		
	}	
}
