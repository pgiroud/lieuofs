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
package org.lieuofs.etat;

import java.util.Date;

import org.lieuofs.Changeable;
import org.lieuofs.ILieuOFS;
import org.lieuofs.Identifiable;
import org.lieuofs.util.CodesOnuIso;

/**
 * Liste des états définis par l'OFS.
 * 
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public interface IEtat extends ILieuOFS, Identifiable, Changeable {
	/**
	 * Retourne les codes ISO 3166 de l'état.
	 * Peut être nul lorsque l'état n'est pas membre de l'ONU.
	 * @return les codes ISO de l'état ou nul.
	 */
	CodesOnuIso getCodesOnuIso();
	
	/**
	 * Retourne la forme courte (nom usuel) de l'état 
	 * dans la langue fournie en paramètre.
	 * L'OFS fournit les noms dans 4 langues : le français, l'allemand, l'italien et l'anglais.
	 * Si on invoque la méthode avec un code de langue qui n'est pas une de celle précitée, la
	 * méthode renvoie la forme courte anglaise.
	 * @param langue le code de la langue sur 2 caractères latins non accentués [a-z][A-Z].
	 * @return La forme courte dans la langue. Ne peut pas être nulle. 
	 */
	String getFormeCourte(String langue);
	
	/**
	 * Retourne la désignation officielle (forme longue) de l'état
	 * dans la langue fournie en paramètre.
	 * Cette désignation n'est pas toujours existante : la méthode peut retourner null.
	 * Les désignations sont fournies dans l'une des 3 langues officielles suisses (français, allemand et italien).
	 * Si le code langue est celui d'une langue non précitée, retourne la désignation officielle française.
	 * @param langue le code de la langue sur 2 caractères latins non accentués [a-z][A-Z].
	 * @return la désignation officielle dans la langue.  
	 */
	String getDesignationOfficielle(String langue);
	
	/**
	 * Indique si l'état est un membre de l'ONU. 
	 * L'information se base sur <a href="http://www.un.org/fr/members/index.shtml">la liste des membres</a>.
	 * @return vrai si l'état est membre de l'ONU.
	 */
	boolean isMembreONU();
	
	/**
	 * Retourne la date d'admission à l'ONU. 
	 * Si l'état n'est pas membre de l'ONU retourne null.
	 * @return la date d'admission à l'ONU.
	 */
	Date getDateAdmissionONU();
	
	/**
	 * Indique si l'état est reconnu par la Suisse. 
	 * La Suisse, comme la plupart des autres Etats, reconnaît un nouvel Etat si celui-ci
	 * réunit trois conditions :
	 * <ol>
	 * 	<li>un peuple bien défini;</li>
	 * 	<li>un territoire délimité;</li>
	 * 	<li>une autorité publique qui est en mesure de mettre en oeuvre de manière effective
	 * la souveraineté étatique tant à l'intérieur que vers l'extérieur.</li>
	 * </ol>
	 * @return vrai si l'état est reconnu par la Suisse.
	 */
	boolean isReconnuParLaSuisse();
	
	/**
	 * Retourne la date à laquelle a été reconnu l'état par la Suisse. 
	 * Dans le cas où l'état n'est pas reconnu, retourne null.
	 * @return retourne la date de reconnaissance par la Suisse. Peut être null. 
	 */
	Date getDateReconnaissanceParLaSuisse();

	/**
	 * Retourne les remarques dans la langue fournie en paramètre.
	 * Il n'existe pas toujours une remarque : la méthode peut retourner null.
	 * Les remarques sont fournies dans l'une des 3 langues officielles suisses (français, allemand et italien).
	 * Si le code langue est celui d'une langue non précitée, retourne les remarques en français.
	 * @param langue le code de la langue sur 2 caractères latins non accentués [a-z][A-Z].
	 * @return les remarques dans la langue fournie en paramètre. Peut être null.
	 */
	String getRemarques(String langue);
	
	/**
	 * Si un enregistrement n'est pas valable: l'état n'existe plus, mais sera conservé 
	 * dans le répertoire des pays pour des raisons d'historisation. 
	 * @return vrai si l'état existe.
	 */
	boolean isValide();

}
