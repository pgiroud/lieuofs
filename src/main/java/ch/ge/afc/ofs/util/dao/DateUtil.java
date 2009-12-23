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
package ch.ge.afc.ofs.util.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class DateUtil {

	public static Date premierMillisecondeDeLaJournee(Date date) {
		Calendar cal = Calendar.getInstance(new Locale("fr", "CH"));
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date derniereMillisecondeDeLaJournee(Date date) {
		Calendar cal = Calendar.getInstance(new Locale("fr", "CH"));
		cal.setTime(premierMillisecondeDeLaJournee(date));
		// Pour être certain d'obtenir la dernière milliseconde
		// du jour, on ajoute 1 jour et on retranche une milliseconde.
		cal.add(Calendar.DATE, 1);
		cal.add(Calendar.MILLISECOND, -1);
		return cal.getTime();
	}
}
