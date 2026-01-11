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
package org.lieuofs.commune.biz;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lieuofs.commune.CommuneCritere;
import org.lieuofs.commune.ICommuneSuisse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.lieuofs.ContexteTest.INSTANCE;


public class GestionCommuneTest {

	private IGestionCommune gestionnaire;

	@BeforeEach
	public void construireContexte() throws IOException {
		gestionnaire = INSTANCE.construireGestionCommune();
	}

	@Test
	public void communeGivisiez() {
		ICommuneSuisse commune = gestionnaire.lire(12060L);
		assertThat(commune.getNumeroOFS()).describedAs("N° OFS de Givisiez").isEqualTo(2197);
		assertThat(commune.getNom()).describedAs("Nom Givisiez").isEqualTo("Givisiez");

		assertThat(commune.getDistrict().getNumeroOFS())
				.describedAs("N° OFS du district de la Sarine").isEqualTo(1004);
		assertThat(commune.getCanton().getCodeIso2())
				.describedAs("Code ISO 2 du canton de Fribourg").isEqualTo("FR");

	}

	@Test
	public void identifiantNegatif() {
		ICommuneSuisse commune = gestionnaire.lire(Long.valueOf(-3456l));
		assertThat(commune).isNull();
	}

	@Test
	public void lectureCommuneNulle() {
		assertThatExceptionOfType(IllegalArgumentException.class) .isThrownBy(
				() -> gestionnaire.lire(null)).describedAs("Identifiant nul -> commune nulle !").withMessage("L'identifiant d'historisation ne peut pas être nul !!");
	}

	
	@Test
	public void rechercheCommuneGenevoise() {
		CommuneCritere critere = new CommuneCritere();
		critere.setCodeCanton("GE");
		Calendar cal = Calendar.getInstance();
		cal.set(2025,Calendar.JANUARY,1);
		critere.setDateValidite(cal.getTime());
		List<ICommuneSuisse> communes = gestionnaire.rechercher(critere);
		assertThat(communes).hasSize(45);
	}
	
	@Test
	public void rechercheLaTourDeTreme() {
		// numéro OFS 2154 : la Tour de Trême a été incluse dans Bulle le 1er janvier 2006
		CommuneCritere critere = new CommuneCritere();
		critere.setCodeCanton("FR");
		Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
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
		assertThat(trouve).describedAs("Le 31 décembre 2005 la Tour-de-Trême est une commune fribourgeoise")
						.isTrue();
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
		assertThat(trouve).describedAs("Le 1er janvier 2006 la Tour-de-Trême n'est plus une commune fribourgeoise")
				.isFalse();
	}
	
	@Test
	public void testCommuneSansDistrict() {
		CommuneCritere critere = new CommuneCritere();
		critere.setCodeCanton("GE");
		List<ICommuneSuisse> communes = gestionnaire.rechercher(critere);
		ICommuneSuisse commune = communes.getFirst();
		assertThat(commune.getDistrict()).describedAs("Une commune genevoise n'a pas de district").isNull();
	}
	
	@Test
	public void rechercherCommuneNulle() {
		assertThatExceptionOfType(IllegalArgumentException.class) .isThrownBy(
				() -> gestionnaire.rechercher(null))
				.describedAs("Rechercher avec un critère null -> commune nulle")
				.withMessage("Le critère de recherche ne peut pas être nul !");
	}
	
	
}
