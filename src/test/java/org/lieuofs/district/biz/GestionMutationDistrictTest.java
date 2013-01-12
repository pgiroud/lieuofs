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
package org.lieuofs.district.biz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lieuofs.commune.IMutationCommune;
import org.lieuofs.commune.TypeMutationCommune;
import org.lieuofs.commune.biz.IGestionCommune;
import org.lieuofs.district.IMutationDistrict;
import org.lieuofs.district.TypeMutationDistrict;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/beans_lieuofs.xml")
public class GestionMutationDistrictTest {

    @Resource(name = "gestionDistrict")
    private IGestionDistrict gestionnaire;

    @Test
    public void redefinitionDistrictDansCanton() {
        IMutationDistrict mutation = gestionnaire.lireMutation(152);
        assertThat(mutation.getType()).isEqualTo(TypeMutationDistrict.Redefinition_de_districts_dans_le_canton);
    }

    @Test
    public void changementDeNom() {
        IMutationDistrict mutation = gestionnaire.lireMutation(127);
        assertThat(mutation.getType()).isEqualTo(TypeMutationDistrict.Changement_de_nom_du_district);
    }

    @Test
    public void renumerotation() {
        IMutationDistrict mutation = gestionnaire.lireMutation(130);
        assertThat(mutation.getType()).isEqualTo(TypeMutationDistrict.Renumérotation_de_districts);
    }

    @Test
    public void changementCanton() {
        IMutationDistrict mutation = gestionnaire.lireMutation(103);
        assertThat(mutation.getType()).isEqualTo(TypeMutationDistrict.Changement_d_appartenance_cantonale_du_district);
    }

}
