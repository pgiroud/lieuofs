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

package ch.ge.afc.ofs.etat.biz;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.util.StringUtils;

import ch.ge.afc.ofs.etat.EtatCritere;
import ch.ge.afc.ofs.etat.IEtat;
import ch.ge.afc.ofs.geo.territoire.biz.EtatTerritoireCritere;
import ch.ge.afc.ofs.geo.territoire.biz.dao.EtatTerritoireDao;
import ch.ge.afc.ofs.geo.territoire.biz.dao.EtatTerritoirePersistant;
import ch.ge.afc.ofs.util.CodesOnuIso;
import ch.ge.afc.ofs.util.InfosONUetISO3166;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class GestionEtat implements IGestionEtat {

    /**************************************************/
    /****************** Attributs *********************/
    /**************************************************/

	private EtatTerritoireDao dao;
	private final Map<Long,IEtat> mapParId = new HashMap<Long,IEtat>();
	
    /**************************************************/
    /********* Accesseurs / Mutateurs *****************/
    /**************************************************/

	public void setDao(EtatTerritoireDao dao) {
		this.dao = dao;
	}

	
	
	public GestionEtat() {
		super();
	}

    /**************************************************/
    /********************* Méthodes *******************/
    /**************************************************/



	@Override
	public IEtat lire(int numeroOFS) {
		return mapParId.get(Long.valueOf(numeroOFS));
	}

	private void stockerEtat(IEtat etat) {
		mapParId.put(etat.getId(), etat);
	}
	
	@PostConstruct
	private void populerCache() {
		EtatTerritoireCritere critere = new EtatTerritoireCritere();
		critere.setEstEtat(Boolean.TRUE);
		Set<EtatTerritoirePersistant> ensemble = dao.rechercher(critere);
		for (EtatTerritoirePersistant etatTerr : ensemble) {
			stockerEtat(new Etat(etatTerr));
		}
	}
	
	private boolean filtreBooleen(Boolean valeurCritere, boolean valeur) {
		if (null != valeurCritere
				&& valeurCritere.booleanValue() != valeur) return false;
		return true;
	}
	
	private boolean accept(IEtat etat, EtatCritere critere) {
		if (!filtreBooleen(critere.getValide(),etat.isValide())) return false;
		if (!filtreBooleen(critere.getReconnuSuisse(),etat.isReconnuParLaSuisse())) return false;
		if (!filtreBooleen(critere.getMembreONU(),etat.isMembreONU())) return false;
		InfosONUetISO3166 infosIsoCritere = critere.getInfosIsoOnu();
		if (null != infosIsoCritere) {
			CodesOnuIso infosIsoEtat = etat.getCodesOnuIso();
			if (null == infosIsoEtat) return false;
			if (StringUtils.hasText(infosIsoCritere.getCodeIsoAlpha2())) {
				if (!StringUtils.hasText(infosIsoEtat.getCodeIsoAlpha2()) 
						|| !infosIsoEtat.getCodeIsoAlpha2().equalsIgnoreCase(infosIsoCritere.getCodeIsoAlpha2())) return false;
			}
			if (StringUtils.hasText(infosIsoCritere.getCodeIsoAlpha3())) {
				if (!StringUtils.hasText(infosIsoEtat.getCodeIsoAlpha3()) 
						|| !infosIsoEtat.getCodeIsoAlpha3().equalsIgnoreCase(infosIsoCritere.getCodeIsoAlpha3())) return false;
			}
			if (0 < infosIsoCritere.getCodeNumeriqueONU()) {
				if (0 >= infosIsoEtat.getCodeNumeriqueONU() 
						|| infosIsoEtat.getCodeNumeriqueONU() != infosIsoCritere.getCodeNumeriqueONU()) return false;
			}
		}
		return true;
	}
	
	@Override
	public Set<IEtat> rechercher(EtatCritere critere) {
		Set<IEtat> ensemble = new HashSet<IEtat>();
		for (IEtat etat : mapParId.values()) {
			if (accept(etat,critere)) ensemble.add(etat);
		}
		return ensemble;
	}

}
