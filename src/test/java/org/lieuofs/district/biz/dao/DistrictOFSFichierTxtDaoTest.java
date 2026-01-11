/*
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
package org.lieuofs.district.biz.dao;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.lieuofs.ContexteTest.INSTANCE;

public class DistrictOFSFichierTxtDaoTest {


	private DistrictOFSDao dao;

	@BeforeEach
	public void construireContexte() {
		dao = INSTANCE.construireDaoDistrict();
	}


	@Test
	public void lecture() {
		// District de la Sarine N° OFS 1004
		PersistDistrict district = dao.lire(10104L);
		assertThat(district.getNumero()).describedAs("N° OFS de la Sarine")
						.isEqualTo(1004);
	}
	
	
}
