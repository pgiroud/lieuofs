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

package org.lieuofs.geo.territoire.biz;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class EtatTerritoireCritere {
	private Boolean valide;
	private Boolean membreONU;
	private Boolean reconnuSuisse;
	private Boolean estEtat;
	private Integer noOFSEtatRattachement;
	
	public Boolean getMembreONU() {
		return membreONU;
	}
	public void setMembreONU(Boolean membreONU) {
		this.membreONU = membreONU;
	}
	public Boolean getReconnuSuisse() {
		return reconnuSuisse;
	}
	public void setReconnuSuisse(Boolean reconnuSuisse) {
		this.reconnuSuisse = reconnuSuisse;
	}
	public Boolean getEstEtat() {
		return estEtat;
	}
	public void setEstEtat(Boolean estEtat) {
		this.estEtat = estEtat;
	}
	public Integer getNoOFSEtatRattachement() {
		return noOFSEtatRattachement;
	}
	public void setNoOFSEtatRattachement(int noOFSEtatRattachement) {
		this.noOFSEtatRattachement = noOFSEtatRattachement;
	}
	public Boolean getValide() {
		return valide;
	}
	public void setValide(Boolean valide) {
		this.valide = valide;
	}
	
	
	
}
