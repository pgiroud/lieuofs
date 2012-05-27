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

package afc.extraction.commune.mutation;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ch.ge.afc.ofs.commune.IMutationCommune;
import ch.ge.afc.ofs.commune.MutationCommuneCritere;
import ch.ge.afc.ofs.commune.biz.IGestionCommune;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class ExtracteurMutation {

	@Resource(name="gestionCommune")
	private IGestionCommune gestionnaire;
	
	@Resource(name="mutationWriter")
	private MutationCommuneWriter mutWriter;
	
	public void extraireMutation(Date depuis, Writer writer) throws IOException {
		extraireMutation(depuis,null,writer);
	}

	public void extraireMutation(Date depuis, Date jusqua, Writer writer) throws IOException {
		MutationCommuneCritere critere = new MutationCommuneCritere();
		if (null != depuis) {
			critere.setDateDebut(depuis);
		}
		if (null != jusqua) {
			critere.setDateFin(jusqua);
		}
		List<IMutationCommune> mutations = gestionnaire.rechercherMutation(critere);
		for (IMutationCommune mut : mutations) {
			String mutStr = mutWriter.ecrireMutation(mut);
			if (null != mutStr) {
				writer.append(mutStr);
			}
		}
		writer.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
		        new String[] {"beans_extraction.xml"});
		ExtracteurMutation extracteur = (ExtracteurMutation)context.getBean("extracteurMutation");
		Calendar cal = Calendar.getInstance();
		cal.set(2011, Calendar.JANUARY,1);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("MutationCommune2011.txt"),"Windows-1252"));
		extracteur.extraireMutation(cal.getTime(), writer);
	}	
	
	
}
