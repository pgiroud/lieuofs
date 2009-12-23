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

package ch.ge.afc.ofs.geo.territoire.biz;

import java.util.Set;

import ch.ge.afc.ofs.geo.territoire.ITerritoire;
import ch.ge.afc.ofs.geo.territoire.TerritoireCritere;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public interface IGestionTerritoire {
	ITerritoire lire(int noOFS);
	Set<ITerritoire> rechercher(TerritoireCritere critere);
}
