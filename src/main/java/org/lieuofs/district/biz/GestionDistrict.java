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
package org.lieuofs.district.biz;

import java.util.List;

import org.lieuofs.Identifiable;
import org.lieuofs.canton.biz.IGestionCanton;
import org.lieuofs.district.DistrictCritere;
import org.lieuofs.district.IDistrict;
import org.lieuofs.district.biz.dao.DistrictOFSDao;
import org.lieuofs.district.biz.dao.PersistDistrict;

public class GestionDistrict implements IGestionDistrict {

	private DistrictOFSDao dao;
	private IGestionCanton gestionnaireCanton;
	
	/**
	 * @param dao the dao to set
	 */
	public void setDao(DistrictOFSDao dao) {
		this.dao = dao;
	}

	/**
	 * @param gestionnaireCanton the gestionnaireCanton to set
	 */
	public void setGestionnaireCanton(IGestionCanton gestionnaireCanton) {
		this.gestionnaireCanton = gestionnaireCanton;
	}

	@Override
	public IDistrict lire(Long id) {
		final PersistDistrict districtPersistant = dao.lire(id);
		District district = new District(districtPersistant);
		district.setCanton(gestionnaireCanton.lire(new Identifiable() {
			@Override
			public Long getId() {
				return districtPersistant.getIdCanton();
			}
		}));
		return district;
	}

	@Override
	public List<IDistrict> rechercher(DistrictCritere critere) {
		return null;
	}

}
