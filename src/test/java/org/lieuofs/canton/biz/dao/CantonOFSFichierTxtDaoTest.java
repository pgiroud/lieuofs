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

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

import org.lieuofs.canton.CantonCritere;
import org.lieuofs.canton.ICanton;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/beans_lieuofs.xml")
public class CantonOFSFichierTxtDaoTest {

	@Resource(name = "cantonDao")
	private CantonOFSFichierTxtDao dao;
	
	@Test
	public void lecture() {
		// Fribourg a l'id 10
		ICanton canton = dao.lire(Long.valueOf(10l));
		assertEquals("Code iso2 Fribourg","FR",canton.getCodeIso2());
	}
	
	@Test
	public void rechercherParCodeIso() {
		CantonCritere critere = new CantonCritere();
		critere.setCodeISO2("VS");
		List<ICanton> cantons = dao.rechercher(critere);
		assertEquals("Un seul canton : le Valais",1,cantons.size());
		assertEquals("Id du Valais",new Long(23l),cantons.get(0).getId());
	}
	
	@Test
	public void rechercherParNom() {
		// Recherche simple sur Genève
		CantonCritere critere = new CantonCritere();
		critere.setNom("Gen");
		List<ICanton> cantons = dao.rechercher(critere);
		assertEquals("Un seul canton : Genève",1,cantons.size());
		assertEquals("Id de Genève",new Long(25l),cantons.get(0).getId());
		
		// recherche avec plusieurs résultats
		critere = new CantonCritere();
		critere.setNom("Basel");
		cantons = dao.rechercher(critere);
		assertEquals("2 canton : Bâle-Ville et Bâle-Campagne",2,cantons.size());
		
		// Recherche sur nom en Allemand
		critere = new CantonCritere();
		critere.setNom("Frei");
		cantons = dao.rechercher(critere);
		assertEquals("Un seul canton : Fribourg",1,cantons.size());
		assertEquals("Id de Fribourg",new Long(10l),cantons.get(0).getId());
	}
	
}
