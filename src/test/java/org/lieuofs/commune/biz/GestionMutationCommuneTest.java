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
package org.lieuofs.commune.biz;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.fest.assertions.api.Assertions.assertThat;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.lieuofs.commune.IMutationCommune;
import org.lieuofs.commune.MutationCommuneCritere;
import org.lieuofs.commune.TypeMutationCommune;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/beans_lieuofs.xml")
public class GestionMutationCommuneTest {

	@Resource(name = "gestionCommune")
	private IGestionCommune gestionnaire;

	@Test
	public void exclusion() {
		// Mutation N° 1565
		IMutationCommune mutation = gestionnaire.lireMutation(1565);
        assertThat(mutation.getType()).isEqualTo(TypeMutationCommune.EXCLUSION);
        assertThat(mutation.getCommunesOrigines()).hasSize(1);
        assertThat(mutation.getCommunesCibles().size()).isGreaterThan(1);
	}
	
	@Test
	public void inclusion() {
		// Mutation N° 2328 : Bulle + La-Tour-de-Trême --> Bulle
		IMutationCommune mutation = gestionnaire.lireMutation(2328);
		String description = mutation.getDescription();
        assertThat(description.length()).isGreaterThan(20);
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
        assertThat(mutations.size()).isGreaterThan(40);
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
        assertThat(mutations).hasSize(1);
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
        assertThat(mutations).hasSize(82);
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
        int i = 0;
		for (IMutationCommune mut : mutations) {
            System.out.println("Mutation " + i);
			descriptions.add(mut.getDescription());
            i++;
		}
        assertThat(mutations.size()).isGreaterThan(100);
	}
}
