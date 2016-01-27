package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import ia.Configuration;
import ia.IA;
import ia.Noeud;

public class Fenetre extends JFrame implements ActionListener {

	private PnlDeJeu plateau = PnlDeJeu.INSTANCE;
	private JLabel lbl1 = new JLabel("               Joueur  :");
	private JLabel lbl2 = new JLabel("               Machine  :");

	private JButton btn1 = new JButton("Désordre");
	private JButton btn2 = new JButton("Résoudre");
	private JButton btn3 = new JButton(" Quitter");

	private Noeud noeud;
	private List<Noeud> list = new ArrayList<>();
	// temps d'execution
	private static int temps = 1000;
	private boolean enJeu = false;
	private int nbCoupJoueur = 0;
	private int nbCoupMachine = 0;
	private boolean win = false;
	private int suivant = 0;

	public Fenetre() {
		plateau.init(3);
		setLandF("Nimbus");

		Container contrainer = getContentPane();
		contrainer.setLayout(new BorderLayout());

		// cree les panels

		JPanel PnlHead = new JPanel();
		JPanel PnlCenter = new JPanel();
		JPanel PnlSouth = new JPanel();
		JPanel PnlLabel = new JPanel();
		JPanel PnlButton = new JPanel();

		// Header
		JLabel lblTitre = new JLabel();
		lblTitre.setIcon(new ImageIcon("src/images/title.png"));
		PnlHead.setBackground(new Color(0, 0, 0));
		PnlHead.add(Box.createRigidArea(new Dimension(-14, 80)), BorderLayout.NORTH);
		PnlHead.add(lblTitre, BorderLayout.CENTER);
		contrainer.add(PnlHead, BorderLayout.NORTH);

		// Centre

		PnlCenter.setLayout(new BorderLayout());
		PnlCenter.setPreferredSize(new Dimension(500, 500));
		PnlCenter.setBackground(new Color(25, 25, 25));

		PnlCenter.add(plateau, BorderLayout.CENTER);
		plateau.setPreferredSize(new Dimension(350, 350));
		PnlCenter.add(Box.createRigidArea(new Dimension(0, 40)), BorderLayout.NORTH);
		PnlCenter.add(Box.createRigidArea(new Dimension(0, 40)), BorderLayout.SOUTH);
		PnlCenter.add(Box.createRigidArea(new Dimension(40, 0)), BorderLayout.EAST);
		PnlCenter.add(Box.createRigidArea(new Dimension(40, 0)), BorderLayout.WEST);
		PnlCenter.setBorder(BorderFactory.createTitledBorder(""));

		// South
		PnlSouth.setPreferredSize(new Dimension(700, 215));
		PnlSouth.setBackground(new Color(0, 0, 0));
		PnlSouth.setLayout(new BoxLayout(PnlSouth, BoxLayout.Y_AXIS));
		PnlLabel.setPreferredSize(new Dimension(20, 2));

		PnlLabel.setBackground(new Color(0, 0, 0));

		lbl1.setForeground(Color.white);
		lbl1.setOpaque(true);
		lbl1.setBackground(new Color(25, 25, 25));
		lbl1.setPreferredSize(new Dimension(179, 40));
		lbl1.setBorder(BorderFactory.createTitledBorder(""));

		lbl2.setForeground(Color.white);
		lbl2.setOpaque(true);
		lbl2.setBackground(new Color(25, 25, 25));
		lbl2.setPreferredSize(new Dimension(179, 40));
		lbl2.setBorder(BorderFactory.createTitledBorder(""));

		PnlLabel.add(lbl1);
		PnlLabel.add(Box.createRigidArea(new Dimension(50, 30)));
		PnlLabel.add(lbl2);

		btn1.addActionListener(this);
		btn2.addActionListener(this);
		btn3.addActionListener(this);
		btn1.setPreferredSize(new Dimension(117, 40));
		btn2.setPreferredSize(new Dimension(117, 40));
		btn3.setPreferredSize(new Dimension(117, 40));

		PnlButton.setBackground(new Color(0, 0, 0));
		PnlButton.add(btn1);
		PnlButton.add(Box.createRigidArea(new Dimension(29, 40)));
		PnlButton.add(btn2);
		PnlButton.add(Box.createRigidArea(new Dimension(29, 40)));
		PnlButton.add(btn3);

		PnlLabel.add(Box.createRigidArea(new Dimension(-8, 80)), BorderLayout.NORTH);
		PnlSouth.add(PnlLabel);
		PnlSouth.add(PnlButton);

		contrainer.add(PnlSouth, BorderLayout.SOUTH);

		// Des panels pour regler l'interface
		JPanel PnlEast = new JPanel();
		JPanel PnlWest = new JPanel();
		PnlEast.setBackground(new Color(0, 0, 0));
		PnlWest.setBackground(new Color(0, 0, 0));
		PnlEast.setPreferredSize(new Dimension(140, 500));
		PnlWest.setPreferredSize(new Dimension(140, 500));

		// Ajouter les Panels au Fenetre
		contrainer.add(PnlCenter, BorderLayout.CENTER);
		contrainer.add(PnlEast, BorderLayout.EAST);
		contrainer.add(PnlWest, BorderLayout.WEST);

		// Proprietes de la fenetre
		setTitle(" ");
		contrainer.setBackground(new Color(0, 0, 0));
		setDefaultCloseOperation(3);
		setResizable(false);
		setSize(700, 700);
		setLocation(50, 20);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);

	}

	Timer t = new Timer(temps, new ActionListener() {
		public void actionPerformed(ActionEvent ev) {
			if (suivant < list.size()) {
				noeud = list.get(suivant);
				Configuration c = noeud.getConfiguration();
				plateau.setConfiguration(c);
				plateau.validate();
				suivant++;
				lbl2.setText("               Machine  :" + nbCoupMachine);
				nbCoupMachine++;
			} else {
				t.stop();
			}
		}
	});

	// joue le role d'un thread qui ecoute si gagne
	Timer ecouteGagne = new Timer(1, new ActionListener() {
		public void actionPerformed(ActionEvent ev) {
			if (enJeu && (plateau.getConfiguration()
					.equals(new Configuration(plateau.getConfiguration().getConfigurationFinale())))) {

				new JOptionPane().showMessageDialog(null,
						"\n\tVous avez gagné\n\n\tnombre de coups :" + (plateau.getNbDeplacement() - 1), "Information",
						JOptionPane.INFORMATION_MESSAGE);
				enJeu = false;
				ecouteGagne.stop();
			}
		}
	});

	public void actionPerformed(ActionEvent e) {

		Object s = e.getSource();
		t.stop();
		if ((s == btn2)) {
			t.setDelay(temps);
			enJeu = false;
			nbCoupMachine = 0;
			suivant = 0;
			lbl2.setText("               Machine  :" + nbCoupMachine);
			IA i = new IA();
			list = i.aEtoile(new Noeud(plateau.getConfiguration(), plateau.getConfiguration().getTaille()));
			System.out.println("liste  " + list.size());
			noeud = list.get(suivant);
			t.start();
			return;
		}

		if ((s == btn1)) {

			System.out.println("plateur get taille" + plateau.getTaille());
			plateau.setConfiguration(
					new Configuration(plateau.getTaille()).configurationAleatoire(plateau.getTaille()));
			plateau.validate();
			enJeu = true;
			plateau.setNbDeplacement(1);
			nbCoupMachine = 0;
			lbl2.setText("               Machine  :" + nbCoupMachine);
			lbl1.setText("               Joueur  :" + 0);
			System.out.println("desordre");
			boolean possible = new IA().isPossible(plateau.getConfiguration());
			System.out.println("Possible? " + possible);
			return;
		}
		if ((s == btn3)) {

			System.exit(0);
		}
		for (int i = 0; i < plateau.getTaille(); i++)
			for (int j = 0; j < plateau.getTaille(); j++) {
				if ((s == plateau.buttons[i][j]) && (enJeu)) {
					ecouteGagne.stop();
					lbl1.setText("               Joueur  :" + plateau.getNbDeplacement());
					ecouteGagne.start();
				}
			}

	}

	// changer Theme
	private void setLandF(String nomLf) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if (nomLf.equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {

			// L&F par défaut

			System.err.println("L&F intouvable");

		}
	}

}
