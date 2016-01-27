package ia;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.JButton;

import gui.PnlDeJeu;

/**
 * @author BOULUAD
 * 
 *         La classe metier
 *
 */

public class Configuration {

	// les attributs
	public int taille;
	int[][] tab; 
	Icon[][] tabImages;
	private int[][] CONFIGURATION_FINALE;
	public int[][] getConfigurationFinale() {
		return CONFIGURATION_FINALE;
	}

	public void configurationFinale() {
		CONFIGURATION_FINALE = new int[taille][taille];
		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				CONFIGURATION_FINALE[i][j] = taille * i + j + 1;
			}
		}
	}

	public int caseVide() {
		return taille * taille;
	}

	public Configuration(int t) {
		taille = t;
		tab = new int[taille][taille];
		configurationFinale();
		setTabImages(PnlDeJeu.INSTANCE.getTabImages());
	}

	// Constructeur pour cree une configuration aleatoir si aleatoire=true
	public Configuration(boolean aleatoire, int t, Icon[][] images) {
		this(t);
		if (!aleatoire) {
			setTabImages(images);
			for (int i = 0; i < taille; i++) {
				for (int j = 0; j < taille; j++) {
					tab[i][j] = taille * i + j + 1;
					System.out.println(tab[i][j]);
				}
			}
		}
	}

	public Configuration configurationAleatoire(int t) {
		Configuration c = new Configuration(false, t, tabImages);
		Random random = new Random();
		List<Point> v = new ArrayList<>();
		int indice;
		for (int i = 0; i < taille * 33; i++) {
			v = c.voisines(caseVide());
			indice = random.nextInt(v.size());
			try {
				c.deplacerCaseVide(v.get(indice), true);
			} catch (ExceptionDeplacement e) {
				e.printStackTrace();
			}
		}
		return c;
	}

	// Constructeur pour cree une configuration apartir d'un tableau donnée
	public Configuration(int[][] t) {
		configurationFinale();
		tab = new int[taille][taille];
		for (int i = 0; i < taille; i++)
			for (int j = 0; j < taille; j++) {
				tab[i][j] = t[i][j];
			}
	}

	// methode pour cloner une configuration
	public Configuration cloner() {
		Configuration c = new Configuration(taille);
		c.setTab(getTab());
		c.setTabImages(getTabImages());
		return c;
	}

	private void setTabImages(Icon[][] images) {
		tabImages = new Icon[taille][taille];
		for (int i = 0; i < images.length; i++) {
			for (int j = 0; j < images.length; j++) {
				tabImages[i][j] = images[i][j];
			}
		}
	}

	// methode return position d'une point sous forme d'un objet point si'l
	// existe sinon (return -1,-1)
	public Point getPosition(int n) {
		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				if (tab[i][j] == n) {
					return new Point(i, j);
				}
			}
		}
		return null;
	}

	// methode return une liste des points voisines d'un point donnée(la valeur
	// du point)
	public List<Point> voisines(Point p) {
		List<Point> list = new ArrayList<>();

		if (p.x > 0) {
			list.add(new Point(p.x - 1, p.y));
		}

		if (p.x < taille - 1) {
			list.add(new Point(p.x + 1, p.y));
		}

		if (p.y > 0) {
			list.add(new Point(p.x, p.y - 1));
		}

		if (p.y < taille - 1) {
			list.add(new Point(p.x, p.y + 1));
		}

		return list;
	}

	public List<Point> voisines(int n) {
		return voisines(getPosition(n));
	}

	// pour modifier la valeur d'une point
	public void setValeur(Point p, int n) {
		Point np = null;
		try {
			np = getPosition(n);
			tab[p.x][p.y] = n;
			Icon tmp = getImage(p);
			tabImages[p.x][p.y] = getImage(np);
			tabImages[np.x][np.y] = tmp;
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("NullPointerException: " + n + " " + np);
		}
	}

	// methode qui return la valeur d'une point donnée
	public int getValeur(Point p) {
		return tab[p.x][p.y];
	}

	// methode qui return la valeur d'une case on donnent leur coord
	public int getValeur(int x, int y) {
		return tab[x][y];
	}

	public void deplacerCaseVide(Point p, boolean image) throws ExceptionDeplacement {
		List<Point> list = new ArrayList<>();
		list = voisines(p);
		Optional<Point> vide = list.stream().filter(point -> getValeur(point) == caseVide()).findFirst();
		if (vide.isPresent()) {
			Point caseVide = vide.get();
			/*
			 * if (image) { JButton buttonP = PnlDeJeu.INSTANCE.getButton(p);
			 * JButton buttonVide = PnlDeJeu.INSTANCE.getButton(caseVide); Icon
			 * buttonPicon = buttonP.getIcon();
			 * buttonP.setIcon(buttonVide.getIcon());
			 * buttonVide.setIcon(buttonPicon); }
			 */
			switcherValeur(p, caseVide(), caseVide, getValeur(p));
		}
	}

	private void switcherValeur(Point p, int vide, Point caseVide, int valeur) {
		tab[p.x][p.y] = vide;
		tab[caseVide.x][caseVide.y] = valeur;
		
		Icon tmp = getImage(p);
		tabImages[p.x][p.y] = getImage(caseVide);
		tabImages[caseVide.x][caseVide.y] = tmp;
	}

	public void deplacerCaseVide(Point p) throws ExceptionDeplacement {
		deplacerCaseVide(p, false);
	}

	public boolean equals(Object c) {
		if (c instanceof Configuration) {
			int[][] d = ((Configuration) c).getTab();
			for (int i = 0; i < taille; i++)
				for (int j = 0; j < taille; j++) {
					if (tab[i][j] != d[i][j])
						return false;
				}
			return true;
		}
		return super.equals(c);
	}

	// methode pour test si gagne ou non
	public boolean gagnante() {
		int t[][] = CONFIGURATION_FINALE;
		for (int i = 0; i < taille; i++)
			for (int j = 0; j < taille; j++) {
				if (tab[i][j] != t[i][j]) {
					return false;
				}
			}
		return true;
	}

	// Getters et Setters

	public int[][] getTab() {
		return tab;
	}

	public void setTab(int[][] tab) {
		for (int i = 0; i < tab.length; i++) {
			for (int j = 0; j < tab.length; j++) {
				this.tab[i][j] = tab[i][j];
			}
		}
	}

	public int getTaille() {
		return taille;
	}

	public void setTaille(int taille) {
		this.taille = taille;
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				s += tab[i][j] + ", ";
			}
		}
		return "Configuration { " + s + " }";
	}

	public Icon getImage(Point p) {
		return tabImages[p.x][p.y];
	}

	public Icon[][] getTabImages() {
		return tabImages;
	}

}
