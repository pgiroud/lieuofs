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
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.lieuofs.Mutable;
import org.lieuofs.TypeMutation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.util.StringUtils;


/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public abstract class FichierOFSTxtDao {

	/**************************************************/
    /****************** Attributs *********************/
    /**************************************************/

	final Logger logger = LoggerFactory.getLogger(FichierOFSTxtDao.class);
	private Resource fichier;
	private String charsetName;
	private DateFormat dateFmt;

    /**************************************************/
    /**************** Constructeurs *******************/
    /**************************************************/

	public FichierOFSTxtDao() {
		super();
		dateFmt = new SimpleDateFormat("dd.MM.yyyy");
		dateFmt.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
	}
	
	/**************************************************/
    /******* Accesseurs / Mutateurs *******************/
    /**************************************************/
	
	public void setFichier(Resource fichier) {
		this.fichier = fichier;
	}
	
	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
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

	protected abstract void traiterLigneFichier(String... tokens) throws ParseException;
	
	@PostConstruct
	public void chargerResource() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(fichier.getInputStream(),charsetName));
		int numLigne = 1;
		String ligne = reader.readLine(); 
		while (null != ligne) {
			try {
				traiterLigneFichier(ligne.split("\t"));
			} catch (ParseException pe) {
				throw new TypeMismatchDataAccessException("Erreur de lecture dans la ressource " + fichier.getFilename() + " à la ligne " + numLigne,pe);
			} catch (NumberFormatException nfe) {
				throw new TypeMismatchDataAccessException("Erreur de lecture dans la ressource " + fichier.getFilename() + " à la ligne " + numLigne,nfe);
			}
			ligne = reader.readLine();
			numLigne++;
		}
		reader.close();
	}
	
	protected Mutable creerMutation(String numero, String typeMutation, String date, boolean radiation) throws ParseException {
		if (!StringUtils.hasText(numero) || !StringUtils.hasText(typeMutation) || !StringUtils.hasText(date)) return null;
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
