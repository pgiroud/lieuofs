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

package org.lieuofs.geo.territoire.biz;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.lieuofs.etat.IEtat;
import org.lieuofs.etat.biz.IGestionEtat;
import org.lieuofs.geo.territoire.ITerritoire;
import org.lieuofs.geo.territoire.TerritoireCritere;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoireDao;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoirePersistant;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class GestionTerritoire implements IGestionTerritoire {

    /**************************************************/
    /****************** Attributs *********************/
    /**************************************************/

	private EtatTerritoireDao dao;
	private IGestionEtat gestionnaireEtat;
	private Map<Integer,Territoire> mapParId = new HashMap<Integer,Territoire>();
	
    /**************************************************/
    /********* Accesseurs / Mutateurs *****************/
    /**************************************************/

	public void setDao(EtatTerritoireDao dao) {
		this.dao = dao;
	}

	public void setGestionnaireEtat(IGestionEtat gestionnaireEtat) {
		this.gestionnaireEtat = gestionnaireEtat;
	}

	
    /**************************************************/
    /********************* Méthodes *******************/
    /**************************************************/

	@PostConstruct
	private void populerCache() {
		EtatTerritoireCritere critere = new EtatTerritoireCritere();
		Set<EtatTerritoirePersistant> ensemble = dao.rechercher(critere);
		for (EtatTerritoirePersistant etatTerr : ensemble) {
			int noOFSEtat = etatTerr.isEtat() ? etatTerr.getNumeroOFS() : etatTerr.getNumEtatRattachement();
			IEtat etat = gestionnaireEtat.lire(noOFSEtat);
			stockerTerritoire(new Territoire(etatTerr,etat));
		}
	}
	
	private void stockerTerritoire(Territoire territoire) {
		mapParId.put(territoire.getNumeroOFS(), territoire);
	}
	
	
	//------------------- Implémentation de l'interface IGestionTerritoire
	
	@Override
	public ITerritoire lire(int noOFS) {
		return mapParId.get(noOFS);
	}

	private boolean accept(Territoire terr, TerritoireCritere critere) {
		if (null != critere.getNoOFSEtat()) {
			if (null == terr.getEtat()) return false;
			if (terr.getNumeroOFS() != critere.getNoOFSEtat() && terr.getEtat().getNumeroOFS() != critere.getNoOFSEtat()) return false;
		}
		return true;
	}
	
	@Override
	public Set<ITerritoire> rechercher(TerritoireCritere critere) {
		Set<ITerritoire> ensemble = new HashSet<ITerritoire>();
		for (Territoire terr : mapParId.values()) {
			if (accept(terr,critere)) ensemble.add(terr);
		}
		return ensemble;
	}

}
