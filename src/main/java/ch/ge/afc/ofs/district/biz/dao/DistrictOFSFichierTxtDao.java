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
package ch.ge.afc.ofs.district.biz.dao;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.ge.afc.ofs.district.DistrictCritere;
import ch.ge.afc.ofs.district.TypeDistrict;
import ch.ge.afc.ofs.util.dao.FichierOFSTxtDao;

public class DistrictOFSFichierTxtDao extends FichierOFSTxtDao implements DistrictOFSDao {

    /**************************************************/
    /****************** Attributs *********************/
    /**************************************************/

	private Map<Long,PersistDistrict> mapParId = new HashMap<Long,PersistDistrict>();

    /**************************************************/
    /**************** Constructeurs *******************/
    /**************************************************/

	public DistrictOFSFichierTxtDao() {
		super();
	}
	
	/**************************************************/
    /******************* Méthodes *********************/
    /**************************************************/

	/* (non-Javadoc)
	 * @see ch.ge.afc.ofs.util.dao.FichierOFSTxtDao#traiterLigneFichier(java.lang.String[])
	 */
	@Override
	protected void traiterLigneFichier(String... tokens) throws ParseException {
		PersistDistrict district = new PersistDistrict();
		district.setId(Long.decode(tokens[0].trim()));
		district.setIdCanton(Long.decode(tokens[1].trim()));
		district.setNumero(Integer.decode(tokens[2].trim()));
		district.setNom(tokens[3].trim());
		district.setNomCourt(tokens[4].trim());
		district.setTypeEnregistrement(TypeDistrict.getParId(Long.decode(tokens[5].trim())));
		district.setInscription(creerMutation(tokens[6].trim(),tokens[7].trim(),tokens[8].trim(),false));
		district.setRadiation(creerMutation(tokens[9].trim(),tokens[10].trim(),tokens[11].trim(),true));
		district.setDernierChangement(getDateFmt().parse(tokens[12].trim()));
		stockerDistrict(district);
	}

	private void stockerDistrict(PersistDistrict district) {
		mapParId.put(district.getId(), district);
	}
	
	@Override
	public PersistDistrict lire(Long id) {
		return mapParId.get(id);
	}

	/* (non-Javadoc)
	 * @see ch.ge.afc.ofs.district.biz.dao.DistrictOFSDao#rechercher(ch.ge.afc.ofs.district.DistrictCritere)
	 */
	@Override
	public List<PersistDistrict> rechercher(DistrictCritere critere) {
		// TODO Auto-generated method stub
		return null;
	}


}
