package utils;

import java.awt.Image;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class decouperImage {
	
	String url="src/des.png";
	
	public decouperImage(){}
	/** La methode qui retourne un ImageIcon, 
	 * ou null si le path est invalide
	 */
	public ImageIcon createImageIcon(String path) {
	    java.net.URL imgURL = decouperImage.class.getResource(path);
	    if (imgURL != null) {
		return new ImageIcon(imgURL);
	    } 
            else {
		System.err.println("Couldn't find file: " + path);
		return null;
	    }
	}
	
    /* La methode permet de decouper comme il faut une image */
    public  ImageIcon[][] cutImage(String chemin, int taille){
	ImageIcon[][] partie;
	ImageIcon myImgIco=new ImageIcon(chemin);
	JFrame f = new JFrame();
	Image im = myImgIco.getImage();
	im	= im.getScaledInstance(306,258,Image.SCALE_DEFAULT);
	myImgIco.setImage(im);
	
	int hauteur = myImgIco.getIconHeight();
	int largeur = myImgIco.getIconWidth();
	int w = largeur/taille;//Largeur de l'image coupe
	int h = hauteur/taille;//Hauteur de l'image coupe
	partie = new ImageIcon[taille][taille];
	for (int i=0;i<taille;i++){
	    for(int j=0;j<taille;j++){
		ImageFilter filtre;
		int a = j * w;//X-axe du debut de nouveau image
		int b = i * h;//Y-axe du debut de nouveau image
		filtre = new CropImageFilter(a, b, w, h); 
		Image myImg = f.createImage(new FilteredImageSource((myImgIco.getImage()).getSource(), filtre));	
		partie[i][j] = new ImageIcon(myImg);		
	    }
	}
	return partie;
    }

}
