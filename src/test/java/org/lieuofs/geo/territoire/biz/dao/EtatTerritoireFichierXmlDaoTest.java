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
package org.lieuofs.geo.territoire.biz.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.lieuofs.util.InfosONUetISO3166;

import java.util.Calendar;
import static org.assertj.core.api.Assertions.assertThat;
import static org.lieuofs.ContexteTest.INSTANCE;

public class EtatTerritoireFichierXmlDaoTest {


	private EtatTerritoireDao dao;

	@BeforeEach
	public void contexte() {
		dao = INSTANCE.construireDaoEtatTerritoire();
	}


	@Test
	public void infosISOSuisse() {
		EtatTerritoirePersistant suisse = dao.lire(8100);
		assertThat(suisse.isMembreONU()).isTrue();
		InfosONUetISO3166 infos = suisse.getInfosISO();
		assertThat(infos).isNotNull();
		assertThat(infos.getCodeNumeriqueONU()).isEqualTo(756);
		assertThat(infos.getCodeIsoAlpha2()).isEqualTo("CH");
		assertThat(infos.getCodeIsoAlpha3()).isEqualTo("CHE");


	}

	@Test
	public void entreeSuisseOnu() {
		EtatTerritoirePersistant suisse = dao.lire(8100);
		assertThat(suisse.isMembreONU()).isTrue();
		Calendar cal = Calendar.getInstance();
		cal.setTime(suisse.getDateEntreeONU());
		assertThat(cal.get(Calendar.DATE)).isEqualTo(10);
		assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.SEPTEMBER);
		assertThat(cal.get(Calendar.YEAR)).isEqualTo(2002);
	}

	@Test
	public void positionnementGeographiqueSuisse() {
		EtatTerritoirePersistant suisse = dao.lire(8100);
		assertThat(suisse.getNumContinent()).isEqualTo(1);
		assertThat(suisse.getNumRegion()).isEqualTo(3);
	}

	@Test
	public void designationCourteDeLaSuisse() {
		EtatTerritoirePersistant suisse = dao.lire(8100);
		assertThat(suisse.getFormeCourte("de")).as("Forme courte allemande").isEqualTo("Schweiz");
		assertThat(suisse.getFormeCourte("fr")).as("Forme courte française").isEqualTo("Suisse");
		assertThat(suisse.getFormeCourte("it")).as("Forme courte italienne").isEqualTo("Svizzera");
		assertThat(suisse.getFormeCourte("en")).as("Forme courte anglaise").isEqualTo("Switzerland");
	}

	@Test
	public void designationOfficielleDeLaSuisse() {
		EtatTerritoirePersistant suisse = dao.lire(8100);
		assertThat(suisse.getDesignationOfficielle("de")).as("Désignation allemande")
				.isEqualTo("Schweizerische Eidgenossenschaft");
		assertThat(suisse.getDesignationOfficielle("fr")).as("Désignation française")
				.isEqualTo("Confédération suisse");
		assertThat(suisse.getDesignationOfficielle("it")).as("Désignation italienne")
				.isEqualTo("Confederazione svizzera");
	}

	@Test
	public void laSuisseNestPasReconnueParElleMeme() {
		EtatTerritoirePersistant suisse = dao.lire(8100);
		assertThat(suisse.isReconnuSuisse()).isFalse();
		assertThat(suisse.getDateReconnaissance()).isNull();
	}

	@Test
	public void pasDeRemarquesPourLaSuisse() {
		EtatTerritoirePersistant suisse = dao.lire(8100);
		assertThat(suisse.getRemarque("de")).isNull();
		assertThat(suisse.getRemarque("fr")).isNull();
		assertThat(suisse.getRemarque("it")).isNull();
	}

	@Test
	public void dernierChangementpourLaSuisse() {
		EtatTerritoirePersistant suisse = dao.lire(8100);
		Calendar cal = Calendar.getInstance();
		cal.setTime(suisse.getDateDernierChangement());
		assertThat(cal.get(Calendar.DATE)).isEqualTo(1);
		assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.JANUARY);
		assertThat(cal.get(Calendar.YEAR)).isEqualTo(2008);
	}

	@Test
	public void suissePresente() {
		EtatTerritoirePersistant suisse = dao.lire(8100);
		assertThat(suisse.getNumeroOFS()).isEqualTo(8100);
		assertThat(suisse.isEtat()).isTrue();
		assertThat(suisse.getNumEtatRattachement()).isEqualTo(0);
		assertThat(suisse.isValide()).isTrue();

	}

	@Test
	public void berlinOuestPresente() {
		EtatTerritoirePersistant berlinOuest = dao.lire(8209);
		assertThat(berlinOuest.getNumeroOFS()).isEqualTo(8209);
		assertThat(berlinOuest.isEtat()).isFalse();
	}

	@Test
	public void berlinOuestNestPasUnEtat() {
		EtatTerritoirePersistant berlinOuest = dao.lire(8209);
		// "Berlin ouest est rattaché à l'Allemagne"
		assertThat(berlinOuest.getNumEtatRattachement()).isEqualTo(8207);
	}

	@Test
	public void berlinOuestNestPasReconnuONU() {
		EtatTerritoirePersistant berlinOuest = dao.lire(8209);
		InfosONUetISO3166 infos = berlinOuest.getInfosISO();
		assertThat(infos).isNull();
		assertThat(berlinOuest.isMembreONU()).isFalse();
		assertThat(berlinOuest.getDateEntreeONU()).isNull();
	}

	@Test
	public void berlinOuestNestPasReconnuActuellementParLaSuisse() {
		EtatTerritoirePersistant berlinOuest = dao.lire(8209);
		assertThat(berlinOuest.isReconnuSuisse()).isFalse();
		assertThat(berlinOuest.getDateReconnaissance()).isNull();
	}

	@Test
	public void berlinOuestNaPlusDeRaisonDexisterSuiteChuteDuMur() {
		EtatTerritoirePersistant berlinOuest = dao.lire(8209);
		assertThat(berlinOuest.isValide()).isFalse();
		Calendar cal = Calendar.getInstance();
		cal.setTime(berlinOuest.getDateDernierChangement());
		assertThat(cal.get(Calendar.DATE)).isEqualTo(5);
		assertThat(cal.get(Calendar.MONTH)).isEqualTo(Calendar.MARCH);
		assertThat(cal.get(Calendar.YEAR)).isEqualTo(2008);
	}

	@Test
	public void designationCourteBerlinOuest() {
		EtatTerritoirePersistant berlinOuest = dao.lire(8209);
		assertThat(berlinOuest.getFormeCourte("de")).as("Forme courte allemande").isEqualTo("Westberlin");
		assertThat(berlinOuest.getFormeCourte("fr")).as("Forme courte française").isEqualTo("Berlin ouest");
		assertThat(berlinOuest.getFormeCourte("it")).as("Forme courte italienne").isEqualTo("Berlino Ovest");
		assertThat(berlinOuest.getFormeCourte("en")).as("Forme courte anglaise").isEqualTo("West Berlin");
	}

	@Test
	public void plusDeDesignationOfficielleBerlinOuest() {
		EtatTerritoirePersistant berlinOuest = dao.lire(8209);
		assertThat(berlinOuest.getDesignationOfficielle("de")).as("Désignation allemande").isNull();
		assertThat(berlinOuest.getDesignationOfficielle("fr")).as("Désignation française").isNull();
		assertThat(berlinOuest.getDesignationOfficielle("it")).as("Désignation italienne").isNull();
	}

	@Test
	public void existenceDeRemarquesPourBerlinOuest() {
		EtatTerritoirePersistant berlinOuest = dao.lire(8209);
		assertThat(berlinOuest.getRemarque("de")).as("Remarque en allemand").isNotNull();
		assertThat(berlinOuest.getRemarque("fr")).as("Remarque en français").isNotNull();
		assertThat(berlinOuest.getRemarque("it")).as("Remarque en italien").isNotNull();
	}

	@Test
	public void positionnementGeographiqueBerlinOuest() {
		EtatTerritoirePersistant berlinOuest = dao.lire(8209);
		assertThat(berlinOuest.getNumContinent()).isEqualTo(1);
		assertThat(berlinOuest.getNumRegion()).isEqualTo(3);
	}
}
