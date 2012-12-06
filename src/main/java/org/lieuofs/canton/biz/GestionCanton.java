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
package org.lieuofs.canton.biz;

import java.util.List;

import org.lieuofs.Identifiable;
import org.lieuofs.canton.CantonCritere;
import org.lieuofs.canton.ICanton;
import org.lieuofs.canton.biz.dao.CantonOFSDao;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class GestionCanton implements IGestionCanton {

	private CantonOFSDao dao;
	
	public void setDao(CantonOFSDao dao) {
		this.dao = dao;
	}
	
	@Override
	public ICanton lire(Identifiable identifiable) {
		return dao.lire(identifiable.getId());
	}

	@Override
	public List<ICanton> rechercher(CantonCritere critere) {
		return dao.rechercher(critere);
	}

}
