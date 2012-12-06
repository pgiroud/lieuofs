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

package org.lieuofs.geo.territoire.biz.dao;


import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.lieuofs.geo.territoire.ITerritoire;
import org.lieuofs.geo.territoire.TerritoireCritere;
import org.lieuofs.geo.territoire.biz.IGestionTerritoire;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/beans_lieuofs.xml")
public class GestionTerritoireTest {

	@Resource(name = "gestionTerritoire")
	private IGestionTerritoire gestionnaire;
	
	@Test
	public void territoireDeFrance() {
		TerritoireCritere critere = new TerritoireCritere();
		critere.setNoOFSEtat(8212);
		Set<ITerritoire> territoires = gestionnaire.rechercher(critere);
		assertTrue("Nombre territoires > 10", 10 < territoires.size());
	}
}
