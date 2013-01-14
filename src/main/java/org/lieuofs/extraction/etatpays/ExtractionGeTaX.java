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
package org.lieuofs.extraction.etatpays;

import org.lieuofs.commune.biz.IGestionCommune;
import org.lieuofs.etat.EtatCritere;
import org.lieuofs.etat.IEtat;
import org.lieuofs.etat.biz.IGestionEtat;
import org.lieuofs.geo.territoire.biz.EtatTerritoireCritere;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoireDao;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoirePersistant;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: patrick
 * Date: 13/01/13
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public class ExtractionGeTaX {

    @Resource(name="gestionEtat")
    private IGestionEtat gestionnaire;


    public void extraire() throws IOException {
        EtatCritere critere = new EtatCritere();
        Calendar cal = Calendar.getInstance();
        cal.set(2012,Calendar.DECEMBER,31);
        critere.setReconnuSuisseALaDate(cal.getTime());
        Set<IEtat> etats = gestionnaire.rechercher(critere);
        List<IEtat> listeEtat = new ArrayList<IEtat>(etats);
        Collections.sort(listeEtat,new Comparator<IEtat>() {
            @Override
            public int compare(IEtat o1, IEtat o2) {
                return o1.getNumeroOFS() - o2.getNumeroOFS();
            }
        });
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("ExtractionEtatGETaX2012.csv")), Charset.forName("ISO8859-1")));

        for (IEtat etat : listeEtat) {
            writer.write(String.valueOf(etat.getNumeroOFS()));
            writer.write(";");
            writer.write(etat.getFormeCourte("fr"));
            writer.newLine();
        }
        writer.close();
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"beans_extraction.xml"});
        ExtractionGeTaX extracteur = (ExtractionGeTaX)context.getBean("extractionEtatGeTax");
        extracteur.extraire();


    }

}
