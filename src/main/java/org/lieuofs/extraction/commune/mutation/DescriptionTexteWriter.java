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

package org.lieuofs.extraction.commune.mutation;

import java.util.EnumSet;

import org.lieuofs.commune.IMutationCommune;
import org.lieuofs.commune.TypeMutationCommune;

import static org.lieuofs.commune.TypeMutationCommune.*;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
class DescriptionTexteWriter implements MutationCommuneWriter {

	@Override
	public String ecrireMutation(IMutationCommune mutation) {
		EnumSet<TypeMutationCommune> typeEcrit = EnumSet.of(FUSION,INCLUSION,CHANGEMENT_NOM,ECHANGE_TERRITOIRE,CHANGEMENT_CANTON);
		if (typeEcrit.contains(mutation.getType())) return mutation.getDescription() + "\n";
		return null;
	}

}
