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
package org.lieuofs.commune.biz.dao;


import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lieuofs.Mutable;
import org.lieuofs.TypeMutation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.lieuofs.ContexteTest.INSTANCE;
import static org.lieuofs.TypeMutation.*;


public class CommuneOFSDaoTest {

	private CommuneOFSDao dao;

	final Logger logger = LoggerFactory.getLogger(CommuneOFSDaoTest.class);

	@BeforeEach
	public void construireContexte() throws IOException {
		dao = INSTANCE.construireDaoCommune();
	}


	private List<PersistCommune> getCommunesRadiees(List<PersistCommune> communes, int numMutation) {
		List<PersistCommune> communesFiltrees = new ArrayList<PersistCommune>();
		for (PersistCommune commune : communes) {
			Mutable radiation = commune.getRadiation();
			if (null != radiation && numMutation == radiation.getNumero()) communesFiltrees.add(commune);
		}
		return communesFiltrees;
	}
	
	private List<PersistCommune> getCommunesCrees(List<PersistCommune> communes, int numMutation) {
		List<PersistCommune> communesFiltrees = new ArrayList<PersistCommune>();
		for (PersistCommune commune : communes) {
			Mutable inscription = commune.getInscription();
			if (numMutation == inscription.getNumero()) communesFiltrees.add(commune);
		}
		return communesFiltrees;
	}

	private void typeRadiationIn(Set<TypeMutation> types, List<PersistCommune> communes) {
		boolean test = true;
		for (PersistCommune commune : communes) if (!types.contains(commune.getRadiation().getMode())) test = false;
		assertThat(test).isTrue();
	}

	private void typeInscriptionIn(Set<TypeMutation> types, List<PersistCommune> communes) {
		boolean test = true;
		for (PersistCommune commune : communes) if (!types.contains(commune.getInscription().getMode())) test = false;
		assertThat(test).isTrue();
	}
	
	
	@Test
	public void inclusionCommune() {
		// Mutation N° 2328 : Bulle + La-Tour-de-Trême --> Bulle
		int numMutation = 2328;
		List<PersistCommune> communes = dao.getMutation(numMutation);
		assertThat(communes).hasSize(3);

		List<PersistCommune> communesRadiees = getCommunesRadiees(communes,numMutation);
		assertThat(communesRadiees).hasSize(2);
		typeRadiationIn(EnumSet.of(MODIF_TERR, RADIATION),communesRadiees);
		
		List<PersistCommune> communesInscrites = getCommunesCrees(communes,numMutation);
		assertThat(communesInscrites).hasSize(1);
		typeInscriptionIn(EnumSet.of(MODIF_TERR),communesInscrites);
		
		// Mutation N° 1510 : Altavilla + Murten --> Murten
		numMutation = 1510;
		communes = dao.getMutation(numMutation);
		assertThat(communes).hasSize(3);

		communesRadiees = getCommunesRadiees(communes,numMutation);
		assertThat(communesRadiees).hasSize(2);
		typeRadiationIn(EnumSet.of(MODIF_TERR, RADIATION),communesRadiees);
		
		communesInscrites = getCommunesCrees(communes,numMutation);
		assertThat(communesInscrites).hasSize(1);
		typeInscriptionIn(EnumSet.of(MODIF_TERR),communesInscrites);	
	}
	
	@Test
	public void fusionCommune() {
		// Mutation N° 2162 : Albeuve + Montbovon + Neirivue + Lessoc --> Haut-Intyamon
		int numMutation = 2162;
		List<PersistCommune> communes = dao.getMutation(numMutation);
		assertThat(communes).hasSize(5);

		List<PersistCommune> communesRadiees = getCommunesRadiees(communes,numMutation);
		assertThat(communesRadiees).hasSize(4);
		typeRadiationIn(EnumSet.of(RADIATION),communesRadiees);
		
		List<PersistCommune> communesInscrites = getCommunesCrees(communes,numMutation);
		assertThat(communesInscrites).hasSize(1);
		typeInscriptionIn(EnumSet.of(CREATION),communesInscrites);
		
		// Mutation N° 1562 : Lohn + Ammannsegg --> Lohn-Ammannsegg
		numMutation = 1562;
		communes = dao.getMutation(numMutation);
		assertThat(communes).hasSize(3);

		communesRadiees = getCommunesRadiees(communes,numMutation);
		assertThat(communesRadiees).hasSize(2);
		typeRadiationIn(EnumSet.of(RADIATION),communesRadiees);
		
		communesInscrites = getCommunesCrees(communes,numMutation);
		assertThat(communesInscrites).hasSize(1);
		typeInscriptionIn(EnumSet.of(CREATION),communesInscrites);		
	}
	
	
	
	@Test
	public void scissionCommune() {
		// Mutation N° 1481 : canton AG   Arni-Islisberg --> Arni + Islisberg
		int numMutation = 1481;
		List<PersistCommune> communes = dao.getMutation(numMutation);
		assertThat(communes).hasSize(3);

		List<PersistCommune> communesRadiees = getCommunesRadiees(communes,numMutation);
		assertThat(communesRadiees).hasSize(1);
		typeRadiationIn(EnumSet.of(RADIATION),communesRadiees);
		
		List<PersistCommune> communesInscrites = getCommunesCrees(communes,numMutation);
		assertThat(communesInscrites).hasSize(2);
		typeInscriptionIn(EnumSet.of(CREATION),communesInscrites);		
	}
	
	@Test
	public void exclusionCommune() {
		// Mutation N° 1483 : canton BE Bolligen --> Bolligen + Ittigen + Ostermundigen
		int numMutation = 1483;
		List<PersistCommune> communes = dao.getMutation(numMutation);
		assertThat(communes).hasSize(4);

		List<PersistCommune> communesRadiees = getCommunesRadiees(communes,numMutation);
		assertThat(communesRadiees).hasSize(1);
		typeRadiationIn(EnumSet.of(MODIF_TERR),communesRadiees);
		
		List<PersistCommune> communesInscrites = getCommunesCrees(communes,numMutation);
		assertThat(communesInscrites).hasSize(3);
		typeInscriptionIn(EnumSet.of(MODIF_TERR,CREATION),communesInscrites);

		// Mutation N° 1565 : Rubigen --> Allmendigen + Rubigen + Trimstein
		numMutation = 1565;
		communes = dao.getMutation(numMutation);
		assertThat(communes).hasSize(4);

		communesRadiees = getCommunesRadiees(communes,numMutation);
		assertThat(communesRadiees).hasSize(1);
		typeRadiationIn(EnumSet.of(MODIF_TERR),communesRadiees);
		
		communesInscrites = getCommunesCrees(communes,numMutation);
		assertThat(communesInscrites).hasSize(3);
		typeInscriptionIn(EnumSet.of(MODIF_TERR,CREATION),communesInscrites);		
	}
	
	@Test
	public void echangeTerritoire() {
		// Mutation N° 1909 : canton TG : Gachnang + Frauenfeld --> Gachnang + Frauenfeld
		int numMutation = 1909;
		List<PersistCommune> communes = dao.getMutation(numMutation);
		assertThat(communes).hasSize(4);

		List<PersistCommune> communesRadiees = getCommunesRadiees(communes,numMutation);
		assertThat(communesRadiees).hasSize(2);
		typeRadiationIn(EnumSet.of(MODIF_TERR),communesRadiees);
		
		List<PersistCommune> communesInscrites = getCommunesCrees(communes,numMutation);
		assertThat(communesInscrites).hasSize(2);
		typeInscriptionIn(EnumSet.of(MODIF_TERR),communesInscrites);	
		
		// Mutation N° 1858 : Opfershofen + Sulgen --> Opfershofen + Sulgen
		numMutation = 1858;
		communes = dao.getMutation(numMutation);
		assertThat(communes).hasSize(4);

		communesRadiees = getCommunesRadiees(communes,numMutation);
		assertThat(communesRadiees).hasSize(2);
		typeRadiationIn(EnumSet.of(MODIF_TERR),communesRadiees);
		
		communesInscrites = getCommunesCrees(communes,numMutation);
		assertThat(communesInscrites).hasSize(2);
		typeInscriptionIn(EnumSet.of(MODIF_TERR),communesInscrites);		
	}
	
	@Test
	public void changementNom() {
		// Mutation N° 1075 : canton FR Vully-le-Haut --> Haut-Vully
		int numMutation = 1075;
		List<PersistCommune> communes = dao.getMutation(numMutation);
		assertThat(communes).hasSize(2);

		List<PersistCommune> communesRadiees = getCommunesRadiees(communes,numMutation);
		assertThat(communesRadiees).hasSize(1);
		typeRadiationIn(EnumSet.of(CHGT_NOM_COMM),communesRadiees);
		
		List<PersistCommune> communesInscrites = getCommunesCrees(communes,numMutation);
		assertThat(communesInscrites).hasSize(1);
		typeInscriptionIn(EnumSet.of(CHGT_NOM_COMM),communesInscrites);	
		
		// Mutation N° 1477 : Yverdon --> Yverdon-les-Bains
		numMutation = 1477;
		communes = dao.getMutation(numMutation);
		assertThat(communes).hasSize(2);

		communesRadiees = getCommunesRadiees(communes,numMutation);
		assertThat(communesRadiees).hasSize(1);
		typeRadiationIn(EnumSet.of(CHGT_NOM_COMM),communesRadiees);
		
		communesInscrites = getCommunesCrees(communes,numMutation);
		assertThat(communesInscrites).hasSize(1);
		typeInscriptionIn(EnumSet.of(CHGT_NOM_COMM),communesInscrites);			
	}
	
	@Test
	public void changementAppartenanceDistrictCanton() {
		// Mutation N° 2375 : canton VD Cudrefin
		int numMutation = 2375;
		List<PersistCommune> communes = dao.getMutation(numMutation);
		assertThat(communes).hasSize(2);

		List<PersistCommune> communesRadiees = getCommunesRadiees(communes,numMutation);
		assertThat(communesRadiees).hasSize(1);
		typeRadiationIn(EnumSet.of(RATT),communesRadiees);
		
		List<PersistCommune> communesInscrites = getCommunesCrees(communes,numMutation);
		assertThat(communesInscrites).hasSize(1);
		typeInscriptionIn(EnumSet.of(RATT),communesInscrites);	
		
		// Mutation N° 1890 : la commune de Vellerat est passée du canton de Berne au canton du Jura
		numMutation = 1890;
		communes = dao.getMutation(numMutation);
		assertThat(communes).hasSize(2);

		communesRadiees = getCommunesRadiees(communes,numMutation);
		assertThat(communesRadiees).hasSize(1);
		typeRadiationIn(EnumSet.of(RATT),communesRadiees);
		
		communesInscrites = getCommunesCrees(communes,numMutation);
		assertThat(communesInscrites).hasSize(1);
		typeInscriptionIn(EnumSet.of(RATT),communesInscrites);		
	}
	
	@Test
	public void renumerotationFormelle() {
		// Mutation N° 1372 : canton BE Belp
		int numMutation = 1372;
		List<PersistCommune> communes = dao.getMutation(numMutation);
		assertThat(communes).hasSize(2);

		List<PersistCommune> communesRadiees = getCommunesRadiees(communes,numMutation);
		assertThat(communesRadiees).hasSize(1);
		typeRadiationIn(EnumSet.of(RENUMEROTATION_FORMELLE),communesRadiees);
		
		List<PersistCommune> communesInscrites = getCommunesCrees(communes,numMutation);
		assertThat(communesInscrites).hasSize(1);
		typeInscriptionIn(EnumSet.of(RENUMEROTATION_FORMELLE),communesInscrites);		
	}

}
