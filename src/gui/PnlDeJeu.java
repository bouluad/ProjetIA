package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import ia.Configuration;
import ia.ExceptionDeplacement;
import utils.decouperImage;

public class PnlDeJeu extends JPanel implements ActionListener {
	
	public static final PnlDeJeu INSTANCE = new PnlDeJeu();

	int taille;
	private int nbDeplacement;
	private Configuration configuration;

	JButton[][] buttons;

	decouperImage di = new decouperImage();
	ImageIcon tabImages[][];

	public void init(int t) {
		taille = t;
		tabImages = di.cutImage("src/images/hassan.png", taille);
		buttons = new JButton[taille][taille];
		configuration = new Configuration(false, taille, tabImages);

		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].setIcon(tabImages[i][j]);
			}
		}

		setLayout(new GridLayout(taille, taille, 1, 1));
		setBackground(Color.white);
		setSize(300, 300);
		setBorder(new LineBorder(Color.BLACK, 3, true));
		nbDeplacement = 1;

		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				buttons[i][j].addActionListener(this);
				add(buttons[i][j]);
			}
		}
		validate();
	}

	public void setButtonColor(Color c) {
		for (int i = 0; i < taille; i++)
			for (int j = 0; j < taille; j++) {
				buttons[i][j].setBackground(c);
			}
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public int getNbDeplacement() {
		return nbDeplacement;
	}

	public void setNbDeplacement(int nbDeplacement) {
		this.nbDeplacement = nbDeplacement;
	}

	public JButton[][] getButtons() {
		return buttons;
	}

	public void setButtons(JButton[][] buttons) {
		this.buttons = buttons;
	}

	public void actionPerformed(ActionEvent ev) {

		Object s = ev.getSource();
		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				if (s == buttons[i][j]) {
					System.out.println(buttons[i][j].getText());
					try {
						configuration.deplacerCaseVide(new Point(i, j), true);
						nbDeplacement++;
						validate();
					} catch (ExceptionDeplacement e) {
						System.out.println("mmmm" + e.getMessage());
					}
				}
			}
		}
	}

	public void validate() {

		for (int i = 0; i < taille; i++)
			for (int j = 0; j < taille; j++) {
				buttons[i][j].setVisible(true);
			}

		int val;
		for (int i = 0; i < taille; i++)
			for (int j = 0; j < taille; j++) {
				Point p = new Point(i, j);
				val = configuration.getValeur(p);
				Icon image = configuration.getImage(p);
				buttons[i][j].setIcon(image);
				if (val == configuration.caseVide()) {
					buttons[i][j].setVisible(false);
				}
			}
		super.validate();
	}

	public int getTaille() {
		return taille;
	}

	public void setTaille(int taille) {
		this.taille = taille;
	}

	public JButton getButton(Point p) {
		return buttons[p.x][p.y];
	}

	public Icon[][] getTabImages() {
		return tabImages;
	}

}
