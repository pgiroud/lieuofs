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


import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.lieuofs.commune.CommuneCritere;
import org.lieuofs.commune.ICommuneSuisse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/beans_lieuofs.xml")
public class GestionCommuneTest {

	@Resource(name = "gestionCommune")
	private IGestionCommune gestionnaire;
	
	@Test
	public void lecture() {
		ICommuneSuisse commune = gestionnaire.lire(12060l);
		assertEquals("N° OFS de Givisiez",2197,commune.getNumeroOFS());
		assertEquals("Nom Givisiez","Givisiez",commune.getNom());
		assertEquals("N° OFS de la Sarine",1004,commune.getDistrict().getNumeroOFS());
		assertEquals("Canton de Fribourg","FR",commune.getCanton().getCodeIso2());
		
		commune = gestionnaire.lire(Long.valueOf(-3456l));
		assertNull("Lecture avec identifiant négatif",commune);
	}	
	
	@Test(expected=IllegalArgumentException.class)
	public void lectureCommuneNulle() {
		gestionnaire.lire(null);
	}

	
	@Test
	public void rechercheCommuneGenevoise() {
		CommuneCritere critere = new CommuneCritere();
		critere.setCodeCanton("GE");
		List<ICommuneSuisse> communes = gestionnaire.rechercher(critere);
		assertEquals("Nbre commune",45,communes.size());
	}
	
	@Test
	public void rechercheLaTourDeTreme() {
		// numéro OFS 2154 : la Tour de Trême a été incluse dans Bulle le 1er janvier 2006
		CommuneCritere critere = new CommuneCritere();
		critere.setCodeCanton("FR");
		Calendar cal = Calendar.getInstance();
		cal.set(2005, Calendar.DECEMBER, 31);
		critere.setDateValidite(cal.getTime());
		List<ICommuneSuisse> communes = gestionnaire.rechercher(critere);
		boolean trouve = false;
		for (ICommuneSuisse commune : communes) {
			if (2154 == commune.getNumeroOFS()) {
				trouve = true;
				break;
			}
		}
		assertTrue("Le 31 décembre 2005 la Tour-de-Trême est une commune fribourgeoise",trouve);
		cal.add(Calendar.DATE, 1);
		critere.setDateValidite(cal.getTime());
		communes = gestionnaire.rechercher(critere);
		trouve = false;
		for (ICommuneSuisse commune : communes) {
			if (2154 == commune.getNumeroOFS()) {
				trouve = true;
				break;
			}
		}
		assertTrue("Le 1er janvier 2006 la Tour-de-Trême n'est plus une commune fribourgeoise",!trouve);
	}
	
	@Test
	public void testCommuneSansDistrict() {
		CommuneCritere critere = new CommuneCritere();
		critere.setCodeCanton("GE");
		List<ICommuneSuisse> communes = gestionnaire.rechercher(critere);
		ICommuneSuisse commune = communes.get(0);
		assertNull("Une commune genevoise n'a pas de district",commune.getDistrict());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void rechercherCommuneNulle() {
		gestionnaire.rechercher(null);
	}
	
	
}
