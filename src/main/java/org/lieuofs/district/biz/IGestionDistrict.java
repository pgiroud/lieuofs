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
package org.lieuofs.district.biz;

import java.util.List;

import org.lieuofs.commune.IMutationCommune;
import org.lieuofs.commune.MutationCommuneCritere;
import org.lieuofs.district.DistrictCritere;
import org.lieuofs.district.IDistrict;
import org.lieuofs.district.IMutationDistrict;
import org.lieuofs.district.MutationDistrictCritere;

public interface IGestionDistrict {
	IDistrict lire(Long id);
	List<IDistrict> rechercher(DistrictCritere critere);
    /**
     * Permet d'obtenir une mutation à partir d'un numéro de mutation.
     * @param numero le numéro de mutation
     * @return une mutation ou nul si aucune mutation ne possède le numéro fournit en paramètre.
     */
    IMutationDistrict lireMutation(int numero);

    /**
     * Recherche une liste de mutation à partir d'un critère de recherche.
     * @param critere le critère de recherche. Ne doit pas être nul.
     * @return une liste de mutation éventuellement vide si aucune mutation n'est conforme au critère.
     */
    List<IMutationDistrict> rechercherMutation(MutationDistrictCritere critere);


}
