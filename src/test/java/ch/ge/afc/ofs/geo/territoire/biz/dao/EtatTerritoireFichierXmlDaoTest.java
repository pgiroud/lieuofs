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
package ch.ge.afc.ofs.geo.territoire.biz.dao;

import java.util.Calendar;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.ge.afc.ofs.util.InfosONUetISO3166;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/beans_lieuofs.xml")
public class EtatTerritoireFichierXmlDaoTest {

	@Resource(name = "etatTerritoireDao")
	private EtatTerritoireDao dao;
	
	@Test
	public void lireSuisse() {
		EtatTerritoirePersistant suisse = dao.lire(8100);
		assertEquals("N° OFS Suisse",8100,suisse.getNumeroOFS());
		InfosONUetISO3166 infos = suisse.getInfosISO();
		assertNotNull("Infos Onu Iso non nulles",infos);
		assertEquals("Info Onu numérique",756,infos.getCodeNumeriqueONU());
		assertEquals("Info ISO alpha 2","CH",infos.getCodeIsoAlpha2());
		assertEquals("Info ISO alpha 3","CHE",infos.getCodeIsoAlpha3());
		assertEquals("Forme courte allemande","Schweiz",suisse.getFormeCourte("de"));
		assertEquals("Forme courte française","Suisse",suisse.getFormeCourte("fr"));
		assertEquals("Forme courte italienne","Svizzera",suisse.getFormeCourte("it"));
		assertEquals("Forme courte anglaise","Switzerland",suisse.getFormeCourte("en"));
		assertEquals("Désignation allemande","Schweizerische Eidgenossenschaft",suisse.getDesignationOfficielle("de"));
		assertEquals("Désignation française","Confédération suisse",suisse.getDesignationOfficielle("fr"));
		assertEquals("Désignation italienne","Confederazione svizzera",suisse.getDesignationOfficielle("it"));
		assertEquals("Continent",1,suisse.getNumContinent());
		assertEquals("Région",3,suisse.getNumRegion());
		assertTrue("Est un état",suisse.isEtat());
		assertEquals("La Suisse est un état : pas de rattachement",0,suisse.getNumEtatRattachement());
		assertTrue("Est membre de l'ONU",suisse.isMembreONU());
		Calendar cal = Calendar.getInstance();
		cal.setTime(suisse.getDateEntreeONU());
		assertEquals("Jour entrée ONU",10,cal.get(Calendar.DATE));
		assertEquals("mois entrée ONU",Calendar.SEPTEMBER,cal.get(Calendar.MONTH));
		assertEquals("année entrée ONU",2002,cal.get(Calendar.YEAR));
		assertFalse("Non reconnu par la Suisse",suisse.isReconnuSuisse());
		assertNull("Date reconnaissance Suisse",suisse.getDateReconnaissance());
		assertNull("Remarque en allemand",suisse.getRemarque("de"));
		assertNull("Remarque en français",suisse.getRemarque("fr"));
		assertNull("Remarque en italien",suisse.getRemarque("it"));
		assertTrue("valide",suisse.isValide());
		cal.setTime(suisse.getDateDernierChangement());
		assertEquals("Jour dernier changement",1,cal.get(Calendar.DATE));
		assertEquals("mois dernier changement",Calendar.JANUARY,cal.get(Calendar.MONTH));
		assertEquals("année dernier changement",2008,cal.get(Calendar.YEAR));
	}
	
	@Test
	public void lireBerlinOuest() {
		EtatTerritoirePersistant berlinOuest = dao.lire(8209);
		assertEquals("N° OFS Suisse",8209,berlinOuest.getNumeroOFS());
		InfosONUetISO3166 infos = berlinOuest.getInfosISO();
		assertNull("Infos Onu Iso non nulles",infos);
		assertEquals("Forme courte allemande","Westberlin",berlinOuest.getFormeCourte("de"));
		assertEquals("Forme courte française","Berlin ouest",berlinOuest.getFormeCourte("fr"));
		assertEquals("Forme courte italienne","Berlino Ovest",berlinOuest.getFormeCourte("it"));
		assertEquals("Forme courte anglaise","West Berlin",berlinOuest.getFormeCourte("en"));
		assertNull("Désignation allemande",berlinOuest.getDesignationOfficielle("de"));
		assertNull("Désignation française",berlinOuest.getDesignationOfficielle("fr"));
		assertNull("Désignation italienne",berlinOuest.getDesignationOfficielle("it"));
		assertEquals("Continent",1,berlinOuest.getNumContinent());
		assertEquals("Région",3,berlinOuest.getNumRegion());
		assertFalse("Est un état",berlinOuest.isEtat());
		assertEquals("Berlin ouest est rattaché à l'Allemagne",8207,berlinOuest.getNumEtatRattachement());
		assertFalse("Est membre de l'ONU",berlinOuest.isMembreONU());
		assertNull("Pas de date d'entrée car pas membre",berlinOuest.getDateEntreeONU());
		assertFalse("Pas reconnu par la Suisse",berlinOuest.isReconnuSuisse());
		assertNull("Date reconnaissance Suisse",berlinOuest.getDateReconnaissance());
		assertNotNull("Remarque en allemand",berlinOuest.getRemarque("de"));
		assertNotNull("Remarque en français",berlinOuest.getRemarque("fr"));
		assertNotNull("Remarque en italien",berlinOuest.getRemarque("it"));
		assertFalse("valide",berlinOuest.isValide());
		Calendar cal = Calendar.getInstance();
		cal.setTime(berlinOuest.getDateDernierChangement());
		assertEquals("Jour dernier changement",5,cal.get(Calendar.DATE));
		assertEquals("mois dernier changement",Calendar.MARCH,cal.get(Calendar.MONTH));
		assertEquals("année dernier changement",2008,cal.get(Calendar.YEAR));
	}
}
