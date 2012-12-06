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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.lieuofs.geo.territoire.biz.EtatTerritoireCritere;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoireDao;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoirePersistant;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class ExtractionPays {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
		        new String[] {"beans_lieuofs.xml"});
		EtatTerritoireCritere critere = new EtatTerritoireCritere();
		//critere.setEstEtat(Boolean.FALSE);
				
		EtatTerritoireDao dao = (EtatTerritoireDao)context.getBean("etatTerritoireDao");
		Set<EtatTerritoirePersistant> etats = dao.rechercher(critere);
		
		EtatWriter etatWriter = new NumOFSEtatWriter();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ExtractionPaysOFSReconnuRecemment.txt"),"UTF-8"));
		
		List<EtatTerritoirePersistant> listeTriee = new ArrayList<EtatTerritoirePersistant>(etats);
		Collections.sort(listeTriee, new Comparator<EtatTerritoirePersistant>() {

			@Override
			public int compare(EtatTerritoirePersistant o1,
					EtatTerritoirePersistant o2) {
				//return o1.getFormeCourte("fr").compareTo(o2.getFormeCourte("fr"));
				return o1.getNumeroOFS() - o2.getNumeroOFS();
			}
			
		});
		
		
		for (EtatTerritoirePersistant etat : filtre(listeTriee)) {
			String etatStr = etatWriter.ecrireEtat(etat);
			if (null != etatStr) {
				writer.append(etatStr);
			}
		}
		writer.close();
	}

    private static List<EtatTerritoirePersistant> filtre(List<EtatTerritoirePersistant> listeOriginal) {
        List<EtatTerritoirePersistant> listeFiltree = new ArrayList<EtatTerritoirePersistant>(); 
        for (EtatTerritoirePersistant etatTerritoire : listeOriginal) {
            // On sélectionne tous les états territoire reconnu il ya moins de 2 ans
            Calendar cal = Calendar.getInstance();
            cal.roll(Calendar.YEAR,-5);
            Date ilYa5an = cal.getTime();
            Date dateReconnaissance = etatTerritoire.getDateReconnaissance();
            if (null != dateReconnaissance
                    && dateReconnaissance.compareTo(ilYa5an) > 0) {
                listeFiltree.add(etatTerritoire);
            }
        }
        return listeFiltree;
    }
    
    
	private static interface EtatWriter {
		String ecrireEtat(EtatTerritoirePersistant etat);
	}

	private static class NumOFSEtatWriter implements EtatWriter {

		private boolean estPremier = true;
		
		@Override
		public String ecrireEtat(EtatTerritoirePersistant etat) {
			String nom = String.valueOf(etat.getFormeCourte("fr"));
			if (estPremier) {
				estPremier = false;
				return nom;
			}
			else return "\n" + nom;
		}
		
	}
}
