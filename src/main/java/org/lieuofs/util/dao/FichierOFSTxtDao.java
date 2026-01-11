/**
 * This file is part of LieuOFS.
 *
 * LieuOFS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * LieuOFS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LieuOFS.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lieuofs.util.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


import org.lieuofs.Mutable;
import org.lieuofs.TypeMutation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public abstract class FichierOFSTxtDao {

	/**************************************************/
    /****************** Attributs *********************/
    /**************************************************/

	final Logger logger = LoggerFactory.getLogger(FichierOFSTxtDao.class);
	private final String fichierDansClasspathAvecCheminComplet;
	private final String charsetName;
	private DateFormat dateFmt;

    /**************************************************/
    /**************** Constructeurs *******************/
    /**************************************************/

	public FichierOFSTxtDao(String fichierAvecCheminComplet, String charset) {
		super();
		this.fichierDansClasspathAvecCheminComplet = fichierAvecCheminComplet;
		this.charsetName = charset;
		dateFmt = new SimpleDateFormat("dd.MM.yyyy");
		dateFmt.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
	}

	public FichierOFSTxtDao(String fichierAvecCheminComplet) {
		this(fichierAvecCheminComplet,"ISO-8859-1");
	}


	/**
	 * @return the dateFmt
	 */
	protected DateFormat getDateFmt() {
		return dateFmt;
	}

	/**************************************************/
    /******************* Méthodes *********************/
    /**************************************************/

	private ClassLoader getClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {}
		if (cl == null) {
			cl = FichierOFSTxtDao.class.getClassLoader();
			if (cl == null) {
				try {
					cl = ClassLoader.getSystemClassLoader();
				}
				catch (Throwable ex) {
				}
			}
		}
		return cl;
	}

	boolean exist() {
		try {
			ClassLoader cl = getClassLoader();
			InputStream is = (cl != null ? cl.getResourceAsStream(fichierDansClasspathAvecCheminComplet) : ClassLoader.getSystemResourceAsStream(fichierDansClasspathAvecCheminComplet));
			return null != is && new BufferedReader(new InputStreamReader(is,charsetName)).ready();
		} catch (IOException ioe) {
			logger.debug("Pas de lecture possible dans 'classpath:" + fichierDansClasspathAvecCheminComplet + "'",ioe);
		}
		return false;
	}

	private InputStream getInputStream() {
		ClassLoader cl = getClassLoader();
		URL resource = cl.getResource(fichierDansClasspathAvecCheminComplet);
		InputStream stream = (cl != null ? cl.getResourceAsStream(fichierDansClasspathAvecCheminComplet) : ClassLoader.getSystemResourceAsStream(fichierDansClasspathAvecCheminComplet));
		return stream;
	}

	protected abstract void traiterLigneFichier(String... tokens) throws ParseException;

	protected void chargerResource() {
		try (InputStream flux = getInputStream();
			 InputStreamReader lecteurFlux = new InputStreamReader(flux,charsetName);
			 BufferedReader lecteurAvecTampon = new BufferedReader(lecteurFlux)) {
			int numLigne = 1;
			String ligne = lecteurAvecTampon.readLine();
			while (null != ligne) {
				try {
					traiterLigneFichier(ligne.split("\t"));
				} catch (ParseException | NumberFormatException pe) {
					throw new  RuntimeException("Erreur de lecture dans la ressource " + fichierDansClasspathAvecCheminComplet + " à la ligne " + numLigne,pe);
				}
                ligne = lecteurAvecTampon.readLine();
				numLigne++;
			}
		} catch (Exception e) {
			logger.error("Lecture du fichier " + fichierDansClasspathAvecCheminComplet + " ");
		}
		;
	}

	private boolean vide(String texte) {
		return null == texte || texte.isBlank();
	}

	protected Mutable creerMutation(String numero, String typeMutation, String date, boolean radiation) throws ParseException {
		if (vide(numero) || vide(typeMutation) || vide(date)) return null;
		Integer num = Integer.decode(numero);
		TypeMutation type = TypeMutation.getParId(Long.decode(typeMutation));
		Date dtSuisse = getDateFmt().parse(date);
		Date dateMutation = radiation ? DateUtil.derniereMillisecondeDeLaJournee(dtSuisse)
				: DateUtil.premierMillisecondeDeLaJournee(dtSuisse);
		return creerMutation(num,type,dateMutation);
	}
	
	private Mutable creerMutation(Integer numero, TypeMutation typeMutation, Date date) {
		Mutation mutation = new Mutation();
		mutation.setNumero(numero);
		mutation.setMode(typeMutation);
		mutation.setDate(date);
		return mutation;
	}
	
	private class Mutation implements Mutable {

		int numero;
		TypeMutation mode;
		Date date;
		/**
		 * @return the numero
		 */
		public int getNumero() {
			return numero;
		}
		/**
		 * @param numero the numero to set
		 */
		public void setNumero(int numero) {
			this.numero = numero;
		}

		/**
		 * @return the date
		 */
		public Date getDate() {
			return date;
		}
		/**
		 * @param date the date to set
		 */
		public void setDate(Date date) {
			this.date = date;
		}
		@Override
		public TypeMutation getMode() {
			return mode;
		}
		/**
		 * @param mode the mode to set
		 */
		public void setMode(TypeMutation mode) {
			this.mode = mode;
		}
		
		
	}
	
}
