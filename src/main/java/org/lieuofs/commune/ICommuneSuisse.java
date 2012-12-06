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
package org.lieuofs.commune;

import org.lieuofs.Changeable;
import org.lieuofs.ILieuOFS;
import org.lieuofs.Identifiable;
import org.lieuofs.Mutable;
import org.lieuofs.canton.ICanton;
import org.lieuofs.district.IDistrict;

public interface ICommuneSuisse extends ILieuOFS, Changeable, Identifiable {
	IDistrict getDistrict();
	ICanton getCanton();
	String getNomCourt();
	TypeCommune getTypeEnregistrement();
	boolean isProvisoire();
	Mutable getInscription();
	Mutable getRadiation();

}
