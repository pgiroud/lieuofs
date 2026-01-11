/*
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
package org.lieuofs.util.dao;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;


public class DateUtilTest {

	private void premierMillisecondeDeLaJournee(Calendar cal, int jour, int mois, int annee) {
		cal.set(annee, mois, jour);
		Date date = DateUtil.premierMillisecondeDeLaJournee(cal.getTime());
		cal.setTime(date);
		assertThat(cal.get(Calendar.YEAR)).isEqualTo(annee);
		assertThat(cal.get(Calendar.MONTH)).isEqualTo(mois);
		assertThat(cal.get(Calendar.DATE)).isEqualTo(jour);
		assertThat(cal.get(Calendar.HOUR_OF_DAY)).isEqualTo(0);
		assertThat(cal.get(Calendar.MINUTE)).isEqualTo(0);
		assertThat(cal.get(Calendar.SECOND)).isEqualTo(0);
		assertThat(cal.get(Calendar.MILLISECOND)).isEqualTo(0);
	}
	
	@Test
	public void premierMillisecondeDeLaJournee() {
		Calendar cal = Calendar.getInstance(Locale.of("fr","CH"));
		premierMillisecondeDeLaJournee(cal,25,Calendar.JANUARY,1971);
		premierMillisecondeDeLaJournee(cal,1,Calendar.JANUARY,2008);
	}
	
	
	private void derniereMillisecondeDeLaJournee(Calendar cal, int jour, int mois, int annee) {
		cal.set(annee, mois, jour);
		Date date = DateUtil.derniereMillisecondeDeLaJournee(cal.getTime());
		cal.setTime(date);
		assertThat(cal.get(Calendar.YEAR)).isEqualTo(annee);
		assertThat(cal.get(Calendar.MONTH)).isEqualTo(mois);
		assertThat(cal.get(Calendar.DATE)).isEqualTo(jour);
		assertThat(23 <= cal.get(Calendar.HOUR_OF_DAY)).isTrue();

		assertThat(59 <= cal.get(Calendar.MINUTE)).isTrue();
		assertThat(59 <= cal.get(Calendar.SECOND)).isTrue();
		assertThat(999 <= cal.get(Calendar.MILLISECOND)).isTrue();
	}
	
	@Test
	public void derniereMillisecondeDeLaJournee() {
		Calendar cal = Calendar.getInstance(Locale.of("fr","CH"));
		derniereMillisecondeDeLaJournee(cal,25,Calendar.JANUARY,1971);
		// la dernière minute du 31 décembre 2005 contenait 61 secondes
		derniereMillisecondeDeLaJournee(cal,31,Calendar.DECEMBER,2005);
		// la dernière minute du 31 décembre 2008 contenait 61 secondes
		derniereMillisecondeDeLaJournee(cal,31,Calendar.DECEMBER,2008);
		
	}
	
	
}
