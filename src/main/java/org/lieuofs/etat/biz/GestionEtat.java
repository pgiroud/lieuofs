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

import java.util.*;

import javax.annotation.PostConstruct;

import org.springframework.util.StringUtils;

import org.lieuofs.etat.EtatCritere;
import org.lieuofs.etat.IEtat;
import org.lieuofs.geo.territoire.biz.EtatTerritoireCritere;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoireDao;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoirePersistant;
import org.lieuofs.util.CodesOnuIso;
import org.lieuofs.util.InfosONUetISO3166;

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

    private boolean filtreDateReconnaissanceSuisse(IEtat etat, EtatCritere critere) {
        if (null != critere.getReconnuSuisse() && !critere.getReconnuSuisse()) {
            return (!etat.isReconnuParLaSuisse());
        }
        if (null == critere.getReconnuSuisseALaDate() || null == etat.getDateReconnaissanceParLaSuisse()) return true;
        int compare = new StrictDateComparator().compare(critere.getReconnuSuisseALaDate(), etat.getDateReconnaissanceParLaSuisse());
        return  compare >= 0;
    }

	private boolean accept(IEtat etat, EtatCritere critere) {
		if (!filtreBooleen(critere.getValide(),etat.isValide())) return false;
		if (!filtreBooleen(critere.getReconnuSuisse(),etat.isReconnuParLaSuisse())) return false;
        if (!filtreDateReconnaissanceSuisse(etat,critere)) return false;
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


    /**
     * Cette classe de comparateur permet de comparer 2 dates (java.util.Date) en ne considérant
     * que les parties jour, mois, année pour faire la comparaison.
     * <br><br>
     *
     * Ainsi, les parties heure / minute / seconde de l'une ou l'autre des dates sont ignorées.
     *
     *
     * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
     */
    private final static class StrictDateComparator implements Comparator<Date>
    {
        /**
         * Compare 2 dates en ne considérant que les informations jour, mois et année.
         * <br>
         *
         * Les paramètres doivent être des dates (java.util.Date) non nulles.
         *
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         * @see java.util.Date
         */
        public int compare(Date pO1, Date pO2)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(pO1);
            int date1 = cal.get(Calendar.YEAR)*10000 + cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DATE);
            cal.setTime(pO2);
            int date2 = cal.get(Calendar.YEAR)*10000 + cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DATE);
            return date1 - date2;
        }
    }

}
