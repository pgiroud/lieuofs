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
package ch.ge.afc.ofs.commune.biz;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.ge.afc.ofs.commune.IMutationCommune;
import ch.ge.afc.ofs.commune.MutationCommuneCritere;
import ch.ge.afc.ofs.commune.TypeMutationCommune;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/beans_lieuofs.xml")
public class GestionMutationCommuneTest {

	@Resource(name = "gestionCommune")
	private IGestionCommune gestionnaire;

	@Test
	public void exclusion() {
		// Mutation N° 1565
		IMutationCommune mutation = gestionnaire.lireMutation(1565);
		assertEquals("Type",TypeMutationCommune.EXCLUSION,mutation.getType());
		assertEquals("Nbre commune source",1,mutation.getCommunesOrigines().size());
		assertTrue("Nbre commune cible",1 < mutation.getCommunesCibles().size());
	}
	
	@Test
	public void inclusion() {
		// Mutation N° 2328 : Bulle + La-Tour-de-Trême --> Bulle
		IMutationCommune mutation = gestionnaire.lireMutation(2328);
		String description = mutation.getDescription();
		assertTrue("longue description",20 < description.length());
	}
	
	@Test
	public void testRechercherMutationFribourg() {
		// Recherche des mutations sur le canton de Fribourg
		MutationCommuneCritere critere = new MutationCommuneCritere();
		critere.setCodeCanton("FR");
		Calendar cal = Calendar.getInstance();
		cal.set(2000, Calendar.JANUARY,1);
		critere.setDateDebut(cal.getTime());
		List<IMutationCommune> mutations = gestionnaire.rechercherMutation(critere);
		List<String> descriptions = new ArrayList<String>();
		for (IMutationCommune mut : mutations) {
			descriptions.add(mut.getDescription());
		}
		assertTrue("Beaucoup de mutation", 40 < mutations.size());
	}
	
	@Test
	public void testRechercherMutationBernoise() {
		// Recherche des mutations sur le canton de Berne
		MutationCommuneCritere critere = new MutationCommuneCritere();
		critere.setCodeCanton("BE");
		Calendar cal = Calendar.getInstance();
		cal.set(1979, Calendar.DECEMBER,1);
		critere.setDateDebut(cal.getTime());
		cal.add(Calendar.YEAR, 2);
		critere.setDateFin(cal.getTime());
		List<IMutationCommune> mutations = gestionnaire.rechercherMutation(critere);
		List<String> descriptions = new ArrayList<String>();
		for (IMutationCommune mut : mutations) {
			descriptions.add(mut.getDescription());
		}
		assertEquals("Une seule mutation", 1, mutations.size());
	}
	
	@Test
	public void testRechercherMutationJurassienne() {
		// Recherche des mutations sur le canton du Jura
		// Lors de la création du canton le 01.01.1979, 82 communes ont migré du canton de Berne
		// dans le canton du Jura.
		MutationCommuneCritere critere = new MutationCommuneCritere();
		critere.setCodeCanton("BE");
		Calendar cal = Calendar.getInstance();
		cal.set(1978, Calendar.DECEMBER,31);
		critere.setDateDebut(cal.getTime());
		cal.add(Calendar.DATE, 1);
		critere.setDateFin(cal.getTime());
		List<IMutationCommune> mutations = gestionnaire.rechercherMutation(critere);
		List<String> descriptions = new ArrayList<String>();
		for (IMutationCommune mut : mutations) {
			descriptions.add(mut.getDescription());
		}
		assertEquals("82 mutations", 82, mutations.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void rechercherMutationNulle() {
		gestionnaire.rechercherMutation(null);
	}
	
	@Test
	public void testRechercherMutationDepuisDate() {
		MutationCommuneCritere critere = new MutationCommuneCritere();
		Calendar cal = Calendar.getInstance();
		cal.set(2006, Calendar.JANUARY,1);
		critere.setDateDebut(cal.getTime());
		List<IMutationCommune> mutations = gestionnaire.rechercherMutation(critere);
		List<String> descriptions = new ArrayList<String>();
		for (IMutationCommune mut : mutations) {
			descriptions.add(mut.getDescription());
		}
		assertTrue("Beaucoup de mutation", 100 < mutations.size());
	}
}
