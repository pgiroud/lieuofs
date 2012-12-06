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

package org.lieuofs.extraction.commune.historisation;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.lieuofs.commune.CommuneCritere;
import org.lieuofs.commune.ICommuneSuisse;
import org.lieuofs.commune.biz.IGestionCommune;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class ExtractionNumHistorisationCommune {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
		        new String[] {"beans_lieuofs.xml"});
		CommuneCritere critere = new CommuneCritere();
		IGestionCommune gestionnaire = (IGestionCommune)context.getBean("gestionCommune");
		List<ICommuneSuisse> communes = gestionnaire.rechercher(critere);
		Collections.sort(communes, new Comparator<ICommuneSuisse>(){
			public int compare(ICommuneSuisse com1, ICommuneSuisse com2) {
				return com1.getNumeroOFS() - com2.getNumeroOFS();
			}
		});
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("NumeroHistorisationCommune.sql"),"Windows-1252"));
		NumeroHistorisationCommuneWriter commWriter = new NumeroHistorisationRefonteSQLWriter();
		for (ICommuneSuisse commune : communes) {
			String numHistStr = commWriter.ecrireNumero(commune);
			if (null != numHistStr) {
				writer.append(numHistStr);
			}
		}
		writer.close();
		
	}
	
}
