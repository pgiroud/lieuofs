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

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lieuofs.geo.territoire.ITerritoire;
import org.lieuofs.geo.territoire.TerritoireCritere;
import org.lieuofs.geo.territoire.biz.IGestionTerritoire;

import static org.assertj.core.api.Assertions.assertThat;
import static org.lieuofs.ContexteTest.INSTANCE;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class GestionTerritoireTest {

	private IGestionTerritoire gestionnaire;

	@BeforeEach
	public void construireContexte() {
		gestionnaire = INSTANCE.constuireGestionTerritoire();
	}

	@Test
	public void territoireDeFrance() {
		TerritoireCritere critere = new TerritoireCritere();
		critere.setNoOFSEtat(8212);
		Set<ITerritoire> territoires = gestionnaire.rechercher(critere);
		assertThat(10 < territoires.size()).isTrue();
	}
}
