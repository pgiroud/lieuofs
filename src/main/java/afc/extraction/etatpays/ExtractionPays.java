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

package afc.extraction.etatpays;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ch.ge.afc.ofs.geo.territoire.biz.EtatTerritoireCritere;
import ch.ge.afc.ofs.geo.territoire.biz.dao.EtatTerritoireDao;
import ch.ge.afc.ofs.geo.territoire.biz.dao.EtatTerritoirePersistant;

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
		        new String[] {"beans.xml"});
		EtatTerritoireCritere critere = new EtatTerritoireCritere();
		critere.setEstEtat(Boolean.FALSE);
				
		EtatTerritoireDao dao = (EtatTerritoireDao)context.getBean("etatTerritoireDao");
		Set<EtatTerritoirePersistant> etats = dao.rechercher(critere);
		
		EtatWriter etatWriter = new NumOFSEtatWriter();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ExtractionPaysOFS.txt"),"UTF-8"));
		
		List<EtatTerritoirePersistant> listeTriee = new ArrayList<EtatTerritoirePersistant>(etats);
		Collections.sort(listeTriee, new Comparator<EtatTerritoirePersistant>() {

			@Override
			public int compare(EtatTerritoirePersistant o1,
					EtatTerritoirePersistant o2) {
				//return o1.getFormeCourte("fr").compareTo(o2.getFormeCourte("fr"));
				return o1.getNumeroOFS() - o2.getNumeroOFS();
			}
			
		});
		
		
		for (EtatTerritoirePersistant etat : listeTriee) {
			String etatStr = etatWriter.ecrireEtat(etat);
			if (null != etatStr) {
				writer.append(etatStr);
			}
		}
		writer.close();
	}

	private static interface EtatWriter {
		String ecrireEtat(EtatTerritoirePersistant etat);
	}

	private static class NumOFSEtatWriter implements EtatWriter {

		private boolean estPremier = true;
		
		@Override
		public String ecrireEtat(EtatTerritoirePersistant etat) {
			String noOFS = String.valueOf(etat.getNumeroOFS());
			if (estPremier) {
				estPremier = false;
				return noOFS;
			}
			else return ", " + noOFS;
		}
		
	}
}
