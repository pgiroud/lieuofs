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

package org.lieuofs.geo.territoire;

import org.lieuofs.ILieuOFS;
import org.lieuofs.Changeable;
import org.lieuofs.Identifiable;
import org.lieuofs.etat.IEtat;
import org.lieuofs.geo.continent.Continent;
import org.lieuofs.geo.region.Region;
import org.lieuofs.util.CodesOnuIso;

/**
 * Un territoire est une dépendances, possessions, territoires autonomes, 
 * collectivités et territoires d'outre-mer non souverains.
 * 
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public interface ITerritoire extends ILieuOFS, Identifiable, Changeable {

	/**
	 * Retourne les codes ISO 3166 du territoire.
	 * Peut être nul lorsque le territoire n'est pas membre de l'ONU.
	 * @return les codes ISO du territoire ou nul.
	 */
	CodesOnuIso getCodesOnuIso();
	
	/**
	 * Retourne la forme courte (nom usuel) du territoire dans la langue fournie
	 * en paramètre.
	 * L'OFS fournit les noms dans 4 langues : le français, l'allemand, l'italien et l'anglais.
	 * Si on invoque la méthode avec un code de langue qui n'est pas une de celle précitée, la
	 * méthode renvoie la forme courte anglaise.
	 * @param codeIsoLangue le code de la langue sur 2 caractères latins non accentués [a-z][A-Z].
	 * @return La forme courte dans la langue. Ne peut pas être nulle. 
	 */
	String getFormeCourte(String codeIsoLangue);
	
	/**
	 * Retourne la désignation officielle (forme longue) du territoire dans la langue fournie
	 * en paramètre.
	 * Cette désignation n'est pas toujours existante : la méthode peut retourner null.
	 * Les désignations sont fournies dans l'une des 3 langues officielles suisses (français, allemand et italien).
	 * Si le code langue est celui d'une langue non précitée, retourne la désignation officielle française.
	 * @param codeIsoLangue le code de la langue sur 2 caractères latins non accentués [a-z][A-Z].
	 * @return la désignation officielle dans la langue.  
	 */
	String getDesignationOfficielle(String codeIsoLangue);
	
	Continent getContinent();
	Region getRegion();
	
	/**
	 * Retourne l'état souverain sur ce territoire.
	 * @return l'état souverain. Ne peut pas être nul.
	 */
	IEtat getEtat();

	String getRemarques(String langue);
	boolean isValide();
}
