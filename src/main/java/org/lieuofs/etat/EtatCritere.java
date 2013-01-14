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

package org.lieuofs.etat;


import org.lieuofs.util.InfosONUetISO3166;

import java.util.Date;


/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class EtatCritere {
	
	private String nom;
	private Boolean membreONU;
	private Boolean reconnuSuisse;
    private Date dateReconnuSuisse;
	private Boolean valide = Boolean.TRUE;
	private InfosONUetISO3166 infosIsoOnu;
	
	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	/**
	 * @return the membreONU
	 */
	public Boolean getMembreONU() {
		return membreONU;
	}
	/**
	 * @param membreONU the membreONU to set
	 */
	public void setMembreONU(Boolean membreONU) {
		this.membreONU = membreONU;
	}
	/**
	 * @return the reconnuSuisse
	 */
	public Boolean getReconnuSuisse() {
		return reconnuSuisse;
	}
	/**
	 * @param reconnuSuisse the reconnuSuisse to set
	 */
	public void setReconnuSuisse(Boolean reconnuSuisse) {
		this.reconnuSuisse = reconnuSuisse;
	}

    public void setReconnuSuisseALaDate(Date date) {
        this.reconnuSuisse = true;
        dateReconnuSuisse = date;
    }

    public Date getReconnuSuisseALaDate() {
        return dateReconnuSuisse;
    }

	public Boolean getValide() {
		return valide;
	}
	
	public void setValide() {
		this.valide = Boolean.TRUE;
	}

	public void setInvalide() {
		this.valide = Boolean.FALSE;
	}
	
	public void setValideEtInvalide() {
		this.valide = null;
	}
	
	public InfosONUetISO3166 getInfosIsoOnu() {
		return infosIsoOnu;
	}
	
	public void setCodeIsoAlpha2(String codeIsoAlpha2) {
		if (null == infosIsoOnu) infosIsoOnu = new InfosONUetISO3166();
		infosIsoOnu.setCodeIsoAlpha2(codeIsoAlpha2);
	}
	
	public void setCodeIsoAlpha3(String codeIsoAlpha3) {
		if (null == infosIsoOnu) infosIsoOnu = new InfosONUetISO3166();
		infosIsoOnu.setCodeIsoAlpha3(codeIsoAlpha3);
	}
	
	public void setCodeNumeriqueONU(int codeIsoNum3) {
		if (null == infosIsoOnu) infosIsoOnu = new InfosONUetISO3166();
		infosIsoOnu.setCodeNumeriqueONU(codeIsoNum3);
	}
}
