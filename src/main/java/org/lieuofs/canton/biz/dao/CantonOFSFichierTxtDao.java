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
package org.lieuofs.canton.biz.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import org.lieuofs.canton.CantonCritere;
import org.lieuofs.canton.ICanton;
import org.lieuofs.canton.biz.Canton;
import org.lieuofs.util.dao.FichierOFSTxtDao;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class CantonOFSFichierTxtDao extends FichierOFSTxtDao implements CantonOFSDao {

    /**************************************************/
    /****************** Attributs *********************/
    /**************************************************/

	private Map<Long,Canton> mapParId = new HashMap<Long,Canton>(26);
	private Map<String,Canton> mapParCodeIso = new HashMap<String,Canton>(26);
	
    /**************************************************/
    /**************** Constructeurs *******************/
    /**************************************************/

	public CantonOFSFichierTxtDao() {
		super();
	}
	
    /**************************************************/
    /******************* Méthodes *********************/
    /**************************************************/

	@Override
	protected void traiterLigneFichier(String... tokens) throws ParseException  {
		Canton canton = new Canton();
		canton.setId(Long.decode(tokens[0].trim()));
		canton.setCodeISO2(tokens[1].trim());
		canton.setNom(tokens[2].trim());
		canton.setDernierChangement(getDateFmt().parse(tokens[3].trim()));
		stockerCanton(canton);
	}
	
	private void stockerCanton(Canton canton) {
		mapParId.put(canton.getId(), canton);
		mapParCodeIso.put(canton.getCodeIso2(), canton);
	}
	
	/* (non-Javadoc)
	 * @see org.lieuofs.canton.biz.dao.CantonOFSDao#lire(java.lang.Long)
	 */
	@Override
	public ICanton lire(Long id) {
		return mapParId.get(id);
	}

	/* (non-Javadoc)
	 * @see org.lieuofs.canton.biz.dao.CantonOFSDao#rechercher(org.lieuofs.canton.CantonCritere)
	 */
	@Override
	public List<ICanton> rechercher(CantonCritere critere) {
		if (null == critere) return Collections.emptyList();
		if (null != critere.getCodeISO2()) {
			return Collections.<ICanton>singletonList(mapParCodeIso.get(critere.getCodeISO2().toUpperCase()));
		}
		if (StringUtils.hasText(critere.getNom())) {
			String critereNom = critere.getNom().trim();
			List<ICanton> liste = new ArrayList<ICanton>();
			for (Canton canton : mapParId.values()) {
				int longueurChaine = critereNom.length();
				String[] nomCantons = canton.getNom().split("/");
				boolean nonTrouve = true;
				for (String nomCanton : nomCantons) {
					String nomCantonTrim = nomCanton.trim();
					if (nonTrouve && nomCantonTrim.length() >= longueurChaine) {
						if (critereNom.toUpperCase().equals(nomCantonTrim.substring(0,longueurChaine).toUpperCase())) {
							liste.add(canton);
							nonTrouve = false;
						}
					}
				}
			}
			return liste;
		}
		return Collections.emptyList();
	}

}
