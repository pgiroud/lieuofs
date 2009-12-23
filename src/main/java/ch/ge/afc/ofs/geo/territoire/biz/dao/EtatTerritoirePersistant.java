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
package ch.ge.afc.ofs.geo.territoire.biz.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.ge.afc.ofs.util.InfosONUetISO3166;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class EtatTerritoirePersistant {
	
	private int numeroOFS;
	private InfosONUetISO3166 infosISO;
	private Map<String,String> formesCourtes = new HashMap<String,String>();
	private Map<String,String> designationsOfficielles;
	private int numContinent;
	private int numRegion;
	private boolean etat;
	private int numEtatRattachement;
	
	private boolean membreONU;
	private Date dateEntreeONU;
	
	private boolean reconnuSuisse;
	private Date dateReconnaissance;
	
	private Map<String,String> remarques;
	
	private boolean valide;
	private Date dateDernierChangement;
	/**
	 * @return the numeroOFS
	 */
	public int getNumeroOFS() {
		return numeroOFS;
	}
	/**
	 * @param numeroOFS the numeroOFS to set
	 */
	public void setNumeroOFS(int numeroOFS) {
		this.numeroOFS = numeroOFS;
	}
	/**
	 * @return the infosISO
	 */
	public InfosONUetISO3166 getInfosISO() {
		return infosISO;
	}
	/**
	 * @param infosISO the infosISO to set
	 */
	public void setInfosISO(InfosONUetISO3166 infosISO) {
		this.infosISO = infosISO;
	}

	
	/**
	 * @return the numContinent
	 */
	public int getNumContinent() {
		return numContinent;
	}
	/**
	 * @param numContinent the numContinent to set
	 */
	public void setNumContinent(int numContinent) {
		this.numContinent = numContinent;
	}
	/**
	 * @return the numRegion
	 */
	public int getNumRegion() {
		return numRegion;
	}
	/**
	 * @param numRegion the numRegion to set
	 */
	public void setNumRegion(int numRegion) {
		this.numRegion = numRegion;
	}
	/**
	 * @return the etat
	 */
	public boolean isEtat() {
		return etat;
	}
	/**
	 * @param etat the etat to set
	 */
	public void setEtat(boolean etat) {
		this.etat = etat;
	}
	/**
	 * @return the numEtatRattachement
	 */
	public int getNumEtatRattachement() {
		return numEtatRattachement;
	}
	/**
	 * @param numEtatRattachement the numEtatRattachement to set
	 */
	public void setNumEtatRattachement(int numEtatRattachement) {
		this.numEtatRattachement = numEtatRattachement;
	}
	/**
	 * @return the membreONU
	 */
	public boolean isMembreONU() {
		return membreONU;
	}
	/**
	 * @param membreONU the membreONU to set
	 */
	public void setMembreONU(boolean membreONU) {
		this.membreONU = membreONU;
	}
	/**
	 * @return the dateEntreeONU
	 */
	public Date getDateEntreeONU() {
		return dateEntreeONU;
	}
	/**
	 * @param dateEntreeONU the dateEntreeONU to set
	 */
	public void setDateEntreeONU(Date dateEntreeONU) {
		this.dateEntreeONU = dateEntreeONU;
	}
	/**
	 * @return the reconnuSuisse
	 */
	public boolean isReconnuSuisse() {
		return reconnuSuisse;
	}
	/**
	 * @param reconnuSuisse the reconnuSuisse to set
	 */
	public void setReconnuSuisse(boolean reconnuSuisse) {
		this.reconnuSuisse = reconnuSuisse;
	}
	/**
	 * @return the dateReconnaissance
	 */
	public Date getDateReconnaissance() {
		return dateReconnaissance;
	}
	/**
	 * @param dateReconnaissance the dateReconnaissance to set
	 */
	public void setDateReconnaissance(Date dateReconnaissance) {
		this.dateReconnaissance = dateReconnaissance;
	}

	
	/**
	 * @return the valide
	 */
	public boolean isValide() {
		return valide;
	}
	/**
	 * @param valide the valide to set
	 */
	public void setValide(boolean valide) {
		this.valide = valide;
	}
	/**
	 * @return the dateDernierChangement
	 */
	public Date getDateDernierChangement() {
		return dateDernierChangement;
	}
	/**
	 * @param dateDernierChangement the dateDernierChangement to set
	 */
	public void setDateDernierChangement(Date dateDernierChangement) {
		this.dateDernierChangement = dateDernierChangement;
	}
	
	public void ajouterFormeCourte(String codeIsoLangue, String formeCourte) {
		formesCourtes.put(codeIsoLangue, formeCourte);
	}
	
	public String getFormeCourte(String codeIsoLangue) {
		return formesCourtes.get(codeIsoLangue);
	}
	
	public void ajouterDesignationOfficielle(String codeIsoLangue, String designation) {
		if (null == designationsOfficielles) designationsOfficielles = new HashMap<String,String>();
		designationsOfficielles.put(codeIsoLangue, designation);
	}
	
	public String getDesignationOfficielle(String codeIsoLangue) {
		if (null == designationsOfficielles) return null;
		return designationsOfficielles.get(codeIsoLangue);
	}
	
	public void ajouterRemarque(String codeIsoLangue, String remarque) {
		if (null == remarques) remarques = new HashMap<String,String>();
		remarques.put(codeIsoLangue, remarque);
	}
	
	public String getRemarque(String codeIsoLangue) {
		if (null == remarques) return null;
		return remarques.get(codeIsoLangue);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EtatTerritoirePersistant)) return false;
		EtatTerritoirePersistant etatTerr = (EtatTerritoirePersistant)obj;
		return this.getNumeroOFS() == etatTerr.getNumeroOFS();
	}
	
	@Override
	public int hashCode() {
		return this.getNumeroOFS();
	}
	
	
}
