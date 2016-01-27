package ia;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

/**
 * @author BOULUAD
 *
 */
public class Noeud {

	private Noeud pere = null;
	private int g = 0;
	private int f = 0;
	private int taille;
	private Configuration configuration;

	// les constructeurs
	public Noeud(int t, Icon[][] images) {
		taille = t;
		configuration = new Configuration(false, taille, images);
	}

	public Noeud(Configuration c, int t) {
		taille = t;
		configuration = c;
	}

	public Noeud(Configuration c, Noeud p) {
		configuration = c;
		pere = p;
		taille = c.getTaille();
	}

	// Cloner un noeude
	public Noeud cloner() {
		Noeud p = null;
		if (pere != null) {
			p = pere.cloner();
		}
		Noeud n = new Noeud(configuration.cloner(), p);
		n.setTaille(taille);
		n.setG(g);
		n.setF(f);
		return n;
	}

	// test si gagne
	public boolean gagnante() {
		return configuration.gagnante();
	}

	// return liste des fils
	public List<Noeud> fils() {
		List<Noeud> l = new ArrayList<>();
		List<Point> lc = new ArrayList<>();

		lc = configuration.voisines(configuration.caseVide());

		int t = lc.size();
		for (int i = 0; i < t; i++) {
			Configuration c = configuration.cloner();
			try {
				c.deplacerCaseVide(lc.get(i));
			} catch (ExceptionDeplacement e) {
				e.printStackTrace();
			}
			l.add(new Noeud(c, taille));
		}
		if (pere != null) {
			for (int i = 0; i < l.size();) {
				if (l.get(i).equals(pere)) {
					l.remove(i);
					continue;
				}
				i++;
			}
		}
		return l;
	}

	// coup de passage d'un noeud
	public int coupDePassageA(Noeud noeud) {
		List<Noeud> l = new ArrayList<>();
		l = fils();
		int t = l.size();

		for (int i = 0; i < t; i++) {
			if (noeud.equals(l.get(i))) {
				return 1;
			}
		}
		return 100;
	}

	// redefinir la methodes equals
	public boolean equals(Object noeud) {
		if (noeud instanceof Noeud) {
			return ((Noeud) noeud).configuration.equals(configuration);
		}
		return super.equals(noeud);
	}

	// Getters et Setters
	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}

	public Noeud getPere() {
		return pere;
	}

	public void setPere(Noeud pere) {
		this.pere = pere;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public int getTaille() {
		return taille;
	}

	public void setTaille(int taille) {
		this.taille = taille;
	}

	public String toString() {
		return "Noeud { " + configuration + " }";
	}

}
