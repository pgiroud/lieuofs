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

package ch.ge.afc.ofs.geo.territoire.biz;

import java.util.Date;
import java.util.Locale;

import ch.ge.afc.ofs.etat.IEtat;
import ch.ge.afc.ofs.geo.continent.Continent;
import ch.ge.afc.ofs.geo.region.Region;
import ch.ge.afc.ofs.geo.territoire.ITerritoire;
import ch.ge.afc.ofs.geo.territoire.biz.dao.EtatTerritoirePersistant;
import ch.ge.afc.ofs.util.CodesOnuIso;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class Territoire implements ITerritoire {

	private final EtatTerritoirePersistant persistant;
	private final IEtat etat;
	
	public Territoire(EtatTerritoirePersistant persistant, IEtat etat) {
		this.persistant = persistant;
		this.etat = etat;
	}
	
	// -------- Implémentation de l'interface ITerritoire 

	@Override
	public int getNumeroOFS() {
		return persistant.getNumeroOFS();
	}

	@Override
	public Long getId() {
		return Long.valueOf(getNumeroOFS());
	}

	@Override
	public String getNom() {
		return this.getFormeCourte(Locale.getDefault().getLanguage());
	}

	
	@Override
	public CodesOnuIso getCodesOnuIso() {
		return persistant.getInfosISO();
	}

	@Override
	public String getFormeCourte(String langue) {
		return persistant.getFormeCourte(langue);
	}

	@Override
	public String getDesignationOfficielle(String langue) {
		return persistant.getDesignationOfficielle(langue);
	}

	@Override
	public Continent getContinent() {
		return Continent.getParId(Long.valueOf(persistant.getNumContinent()));
	}

	@Override
	public Region getRegion() {
		return Region.getParId(Long.valueOf(persistant.getNumRegion()));
	}

	@Override
	public IEtat getEtat() {
		return etat;
	}

	@Override
	public String getRemarques(String langue) {
		return persistant.getRemarque(langue);
	}

	@Override
	public boolean isValide() {
		return persistant.isValide();
	}

	@Override
	public Date getDernierChangement() {
		return persistant.getDateDernierChangement();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Territoire)) return false;
		Territoire territoire = (Territoire)obj;
		return this.getNumeroOFS() == territoire.getNumeroOFS();
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
