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
package org.lieuofs.extraction.commune;

import org.lieuofs.commune.CommuneCritere;
import org.lieuofs.commune.ICommuneSuisse;
import org.lieuofs.commune.biz.IGestionCommune;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;
import java.util.*;

public class ExtractionGeTax {

    @Resource(name="gestionCommune")
    private IGestionCommune gestionnaire;

    public void extraire() {
        CommuneCritere critere = new CommuneCritere();
        Calendar cal = Calendar.getInstance();
        cal.set(2012,Calendar.JANUARY,1);
        critere.setDateValiditeApres(cal.getTime());
        cal.set(2012,Calendar.DECEMBER,31);
        critere.setDateValiditeAvant(cal.getTime());
        List<ICommuneSuisse> communes = gestionnaire.rechercher(critere);
        Collections.sort(communes,new Comparator<ICommuneSuisse>() {
            @Override
            public int compare(ICommuneSuisse o1, ICommuneSuisse o2) {
                return o1.getNumeroOFS() - o2.getNumeroOFS();
            }
        });
        // Attention, le fichier est une iste historisée des communes.
        // Une commune peut donc figurer 2 fois dans le fichier
        Set<Integer> numOFS = new HashSet<Integer>(3000);
        int nbreCommune = 0;
        for (ICommuneSuisse commune : communes) {
            if (!numOFS.contains(commune.getNumeroOFS())) {
                nbreCommune++;
                numOFS.add(commune.getNumeroOFS());
                System.out.println(commune.getNumeroOFS() + " " + commune.getNomCourt());
            }
        }
        System.out.println("Nbre commune : " + nbreCommune);
    }




    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"beans_extraction.xml"});
        ExtractionGeTax extracteur = (ExtractionGeTax)context.getBean("extractionCommuneGeTax");
        extracteur.extraire();

    }
}
