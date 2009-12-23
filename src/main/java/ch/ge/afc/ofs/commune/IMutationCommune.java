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
package ch.ge.afc.ofs.commune;

import java.util.Date;
import java.util.List;

public interface IMutationCommune {
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
	TypeMutationCommune getType();
	
	/**
	 * Retourne la liste des communes à l'origine de la mutation.
	 * Par exemple, pour une fusion c.-à-d. {@literal commune A + commune B --> commune C},
	 * la liste contient les communes A et B. 
	 * Cette liste contient au moins un élément.
	 * @return la liste des commune à l'origine de la mutation.
	 */
	List<ICommuneSuisse> getCommunesOrigines();
	
	/**
	 * Retourne la liste des communes résultantes de la mutation.
	 * Par exemple, pour une fusion c.-à-d. {@literal commune A + commune B --> commune C},
	 * la liste contient la commune C. 
	 * Cette liste contient au moins un élément.
	 * @return la liste des communes résultantes de la mutation.
	 */
	List<ICommuneSuisse> getCommunesCibles();
	
	/**
	 * Fournit une description textuelle de la mutation à des fin d'affichage.
	 * @return une description textuelle de la mutation.
	 */
	String getDescription();
	
	/**
	 * Retourne la date d'effet de la mutation i.e. la date à partir de laquelle
	 * les nouvelles communes sont crées et les anciennes radiées.
	 * @return la date d'effet de la mutation.
	 */
	Date getDateEffet();
}
