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
package org.lieuofs.etat.biz;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.lieuofs.etat.IEtat;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoirePersistant;
import org.lieuofs.util.CodesOnuIso;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class Etat implements IEtat {

	private final EtatTerritoirePersistant etatTerritoire;
	
	public Etat(EtatTerritoirePersistant etatTerritoire) {
		this.etatTerritoire = etatTerritoire;
	}

	//--------------- Implémentation de l'interface IEtat -------
	@Override
	public boolean isMembreONU() {
		return etatTerritoire.isMembreONU();
	}

	@Override
	public boolean isReconnuParLaSuisse() {
		return etatTerritoire.isReconnuSuisse();
	}

	@Override
	public CodesOnuIso getCodesOnuIso() {
		return etatTerritoire.getInfosISO();
	}

	@Override
	public Date getDateAdmissionONU() {
		return etatTerritoire.getDateEntreeONU();
	}

	@Override
	public Date getDateReconnaissanceParLaSuisse() {
		Date date = etatTerritoire.getDateReconnaissance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (1945 == cal.get(Calendar.YEAR) && Calendar.JANUARY == cal.get(Calendar.MONTH) && 1 == cal.get(Calendar.DATE)) {
			return null;
		}
		return date;
	}

	@Override
	public String getDesignationOfficielle(String langue) {
		return etatTerritoire.getDesignationOfficielle(langue);
	}

	@Override
	public String getFormeCourte(String langue) {
		return etatTerritoire.getFormeCourte(langue);
	}

	@Override
	public String getRemarques(String langue) {
		return etatTerritoire.getRemarque(langue);
	}

	@Override
	public boolean isValide() {
		return etatTerritoire.isValide();
	}

	@Override
	public Long getId() {
		return Long.valueOf(etatTerritoire.getNumeroOFS());
	}

	@Override
	public Date getDernierChangement() {
		return etatTerritoire.getDateDernierChangement();
	}

	@Override
	public String getNom() {
		return etatTerritoire.getFormeCourte(Locale.getDefault().getLanguage());
	}

	@Override
	public int getNumeroOFS() {
		return etatTerritoire.getNumeroOFS();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Etat)) return false;
		Etat etat = (Etat)obj;
		return this.getNumeroOFS() == etat.getNumeroOFS();
	}

	@Override
	public int hashCode() {
		return this.getNumeroOFS();
	}

	@Override
	public String toString() {
		return this.getNumeroOFS() + " : " + this.getFormeCourte("fr");
	}


}
