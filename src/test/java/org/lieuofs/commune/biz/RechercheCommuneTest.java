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

import org.fest.assertions.api.IterableAssert;
import org.junit.runner.RunWith;
import org.lieuofs.commune.CommuneCritere;
import org.lieuofs.commune.ICommuneSuisse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.extractProperty;

import org.junit.Test;
import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/beans_lieuofs.xml")
public class RechercheCommuneTest {

    @Resource(name = "gestionCommune")
    private IGestionCommune gestionnaire;

    @Test
    public void rechercheValideAUneDateDonnee() {
        // La commune Collina d'Oro(5236) a absorbé la commune Carabietta(5169) le 01.04.2012
        CommuneCritere critere = new CommuneCritere();
        Calendar cal = Calendar.getInstance();
        cal.set(2012,Calendar.MAY,11);
        critere.setDateValidite(cal.getTime());
        List<ICommuneSuisse> communes = gestionnaire.rechercher(critere);
        assertThat(extractProperty("numeroOFS").from(communes)).contains(5236).doesNotContain(5169);
    }

    @Test
    public void recherche2012() {
        CommuneCritere critere = new CommuneCritere();
        Calendar cal = Calendar.getInstance();
        cal.set(2012,Calendar.JANUARY,1);
        critere.setDateValiditeApres(cal.getTime());
        cal.set(2012,Calendar.DECEMBER,31);
        critere.setDateValiditeAvant(cal.getTime());
        List<ICommuneSuisse> communes = gestionnaire.rechercher(critere);
        IterableAssert<Object> liste = assertThat(extractProperty("numeroOFS").from(communes));
        // La commune Collina d'Oro(5236) a absorbé la commune Carabietta(5169) le 01.04.2012
        // Les 2 communes ont donc existé en 2012
        liste.contains(5236, 5169);
        // La commune Estavayer-le-Lac(2015) a absorbé la commune Font(2017) le 01.01.2012
        liste.contains(2015).doesNotContain(2017);
        // La commune Val Terbi(6730) est née de la fusion des communes Montsevelier(6717), Vermes(6726), Vicques(6727) le 01.01.2013
        liste.contains(6717,6726,6727).doesNotContain(6730);
        // 2496 communes valides en 2012 (2 doublons de numOFS !!)
        liste.hasSize(2498);

    }

}
