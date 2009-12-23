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
package ch.ge.afc.ofs.commune.biz;

import java.util.List;

import ch.ge.afc.ofs.commune.CommuneCritere;
import ch.ge.afc.ofs.commune.ICommuneSuisse;
import ch.ge.afc.ofs.commune.IMutationCommune;
import ch.ge.afc.ofs.commune.MutationCommuneCritere;

public interface IGestionCommune {
	/**
	 * Permet d'obtenir une commune suisse en fournissant son identifiant d'historisation.
	 * @param id l'identifiant d'historisation. Cet identzifiant doit être non nul.
	 * @return une commune suisse ou nul si aucune commune ne possède l'identifiant fournit en paramètre.
	 */
	ICommuneSuisse lire(Long id);
	
	/**
	 * Recherche une liste de commune suisse à partir d'un critère de recherche.
	 * @param critere le critère de recherche. Ne doit pas être nul.
	 * @return une liste de commune conforme au critère. Elle peut être éventuellement vide.
	 */
	List<ICommuneSuisse> rechercher(CommuneCritere critere);
	
	/**
	 * Permet d'obtenir une mutation à partir d'un numéro de mutation.
	 * @param numero le numéro de mutation
	 * @return une mutation ou nul si aucune mutation ne possède le numéro fournit en paramètre.
	 */
	IMutationCommune lireMutation(int numero);
	
	/**
	 * Recherche une liste de mutation à partir d'un critère de recherche.
	 * @param critere le critère de recherche. Ne doit pas être nul.
	 * @return une liste de mutation éventuellement vide si aucune mutation n'est conforme au critère.
	 */
	List<IMutationCommune> rechercherMutation(MutationCommuneCritere critere);
}
