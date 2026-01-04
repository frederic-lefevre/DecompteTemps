/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.fl.decompteTemps.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class PresenceAgenda {

	private static final Logger presenceLog = Logger.getLogger(PresenceAgenda.class.getName());
	
	private List<Period> periods;
	private long duration;

	public PresenceAgenda() {
		super();
		duration = 0;
		periods = new ArrayList<Period>();
	}

	public void addPresence(Period p) {
		periods.add(p);
		duration = duration + p.getDuration();
	}

	public long getDuration() {
		return duration;
	}

	public long getDuration(Date a, Date b) {

		if (a.after(b)) {
			presenceLog.severe("Begin is after end");
			throw new IllegalArgumentException("Begin is after end");
		}
		long res = 0;
		for (int i = 0; i < periods.size(); i++) {
			res = res + ((Period) (periods.get(i))).getDuration(a, b);
		}
		return res;
	}

	/**
	 * Get the periods of the presence agenda
	 * 
	 * @return the periods of the presence agenda
	 */
	public Period[] getPeriods() {

		Period[] result = new Period[periods.size()];
		return (Period[]) periods.toArray(result);
	}
}
