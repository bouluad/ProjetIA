package ia;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IA {

	// attributs
	final static int coup = 1;

	// les listes fermées et les listes ouvertes
	public static List<Noeud> P = new ArrayList<>();
	public static List<Noeud> Q = new ArrayList<>();

	public IA() {

	}

	public boolean isPossible(Configuration configuration) {
		int even = 0;
		int taille = configuration.getTaille();
		for (int i = 0; i < taille * taille - 1; i++) {
			int v1 = configuration.getValeur((int) (i / taille), Math.abs(i % taille - (taille - 1)));
			if (v1 == configuration.caseVide()) {
				continue;
			}
			
			for (int j = i + 1; j < taille * taille; j++) {
				int v2 = configuration.getValeur((int) (j / taille), Math.abs(j % taille - (taille - 1)));
				System.out.print(v2 + " ");
				if (v1 > v2) {
					even++;
				}
			}
			System.out.println();
		}
		System.out.println("ODD? " + even);
		return even % 2 == 0;
	}

	// implementation de A*
	public List<Noeud> aEtoile(Noeud noeud) {
		List<Noeud> listP = new ArrayList<>();
		List<Noeud> listQ = new ArrayList<>();
		List<Noeud> fils = new ArrayList<>();
		List<Noeud> res = new ArrayList<>();
		Noeud n1 = noeud.cloner();
		Noeud n2;

		listP.add(n1);

		while ((listP.size() > 0) && (!n1.gagnante())) {
			listQ.add(n1);
			listP.remove(0);
			fils = n1.fils();

			int t = fils.size();
			for (int i = 0; i < t; i++) {
				n2 = fils.get(i);
				if ((!appartient(n2, listP) && !appartient(n2, listQ)) || (n2.getG() > n1.getG() + k(n1, n2))) {
					n2.setG(n1.getG() + n1.coupDePassageA(n2));
					n2.setF(n2.getG() + heuristique(n1));
					n2.setPere(n1);
					inserer(n2, listP);
				}
			}
			if (listP.size() > 0) {
				n1 = listP.get(0);
			}
			// (new Scanner(System.in)).nextLine();
		}
		if (n1.gagnante()) {
			System.out.println("N1 gagnant!");
			System.out.println("Add: " + n1);
			res.add(n1);
			while (n1.getPere() != null) {
				System.out.println("Add: " + n1.getPere());
				res.add(0, n1.getPere());
				n1 = n1.getPere();
			}
		}

		P = new ArrayList<>(listP);
		Q = new ArrayList<>(listQ);

		return res;
	}

	public int heuristique(Noeud n) {
		int h = 0;
		Point p = new Point();
		Configuration c = n.getConfiguration();
		int taille = c.getTaille();
		int[][] config_finale = c.getConfigurationFinale();

		for (int i = 1; i <= taille; i++)
			for (int j = 1; j <= taille; j++) {
				p = c.getPosition(config_finale[i - 1][j - 1]);
				h += (Math.abs(p.x - i + 1) + Math.abs(p.y - j + 1));
			}
		return h;
	}

	// methode verifier si un noeud donnée existe dans une liste
	private static boolean appartient(Noeud n, List<Noeud> l) {
		int t = l.size();
		for (int i = 0; i < t; i++) {
			if (l.get(i).equals(n))
				return true;
		}
		return false;
	}

	private static int k(Noeud n1, Noeud n2) {
		return coup;
	}

	// la fonction calcule H
	private static int h(Noeud n) {
		int h1 = 0;
		int taille = n.getConfiguration().getTaille();
		for (int i = 0; i < taille; i++)
			for (int j = 0; j < taille; j++) {
				if (n.getConfiguration().getTab()[i][j] == (taille * i + j + 1))
					h1++;
			}
		return h1;
	}

	// methode pour inserer les noeuds dans une liste avec des valeurs
	// croissante de F
	private static void inserer(Noeud n, List<Noeud> l) {
		Noeud noeud = n.cloner();
		int t = l.size();

		if (t == 0) {
			l.add(n);
			return;
		}
		int i = 0;
		while (i < t) {
			noeud = l.get(i);
			i++;
			if (noeud.getF() > n.getF()) {
				l.add(i - 1, n);
				return;
			}
			if (noeud.getF() == n.getF()) {
				i--;
				break;
			}
		}
		if (i == t) {
			l.add(i, n);
			return;
		}
		while (noeud.getF() == n.getF()) {
			if (noeud.getG() <= n.getG()) {
				l.add(i, n);
				return;
			}
			i++;
			if (i < t) {
				noeud = l.get(i);
			} else {
				break;
			}
		}
		if (i == t) {
			l.add(i, n);
			return;
		}
		if (noeud.getF() != n.getF()) {
			l.add(i - 1, n);
			return;
		}
	}

}
