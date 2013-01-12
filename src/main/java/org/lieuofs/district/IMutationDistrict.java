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
package org.lieuofs.district;

import java.util.Date;
import java.util.List;

public interface IMutationDistrict {

    /**
     * Retourne le numéro de mutation définit par l'OFS.
     * Il s'agit d'un entier strictement positif.
     * @return le numéro de mutation.
     */
    int getNumero();

    /**
     * Retourne le type de mutation.
     * @return le type de mutation.
     */
    TypeMutationDistrict getType();

    /**
     * Retourne les districts à l'origine de la mutation.
     * @return les districts à l'origine de la mutation.
     */
    List<IDistrict> getOrigines();

    /**
     * Retourne les district résultants de la mutation.
     * @return les districts résultants de la mutation.
     */
    List<IDistrict> getCibles();

    /**
     * Fournit une description textuelle de la mutation à des fin d'affichage.
     * @return une description textuelle de la mutation.
     */
    String getDescription();

    /**
     * Retourne la date d'effet de la mutation.
     * @return la date d'effet de la mutation.
     */
    Date getDateEffet();

}
