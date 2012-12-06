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

package org.lieuofs.util;

/**
 * Interface fournissant les informations relatives aux données des <a href="http://www.iso.org/iso/fr/country_codes/iso_3166_code_lists.htm">normes ISO-3166</a>.
 * Le code numérique est défini par <a href="http://unstats.un.org/unsd/methods/m49/m49frnch.htm">la division statistique des Nations-Unies</a>.
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public interface CodesOnuIso {

	/**
	 * Retourne le code numérique de 3 chiffres.
	 * En 1981, le code numérique à trois chiffres à été ajouté à la norme ISO 3166 pour son utilité dans les applications qui exigent l'indépendance par rapport au type d'écriture (les langues écrites n'utilisent pas tous les caractères latins !).
	 * Ce <a href="http://unstats.un.org/unsd/methods/m49/m49chgef.htm">code</a> est élaboré par la Division de statistique de l'ONU. 
	 * @return le code numérique de 3 chiffres.
	 */
	int getCodeNumeriqueONU();
	
	
	/**
	 * Retourne le code ISO alphanumérique sur 2 positions.
	 * Le code alpha-2, conçu comme le code pays d'usage général recommandé pour l'échange international des biens et des informations.
	 * @return le code ISO alphanumérique sur 2 positions.
	 */
	String getCodeIsoAlpha2();
	
	/**
	 * Retourne le code ISO alphanumérique sur 3 positions.
	 * Le code alpha-3, permettant une meilleure association visuelle entre les codets et les noms de pays est envisagé pour les applications où cette propriété pourrait représenter un avantage.
	 * @return e code ISO alphanumérique sur 3 positions.
	 */
	String getCodeIsoAlpha3();	
}
