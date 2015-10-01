package eu.legionpvp.legionapilib.objects;

/*
 * This file is part of LegionApiLib.
 *
 * LegionApiLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LegionApiLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LegionApiLib.  If not, see <http://www.gnu.org/licenses/>.
 */

public class Relation extends ApiObject{
	public Integer myUid, hisUid;
	public Integer level, requestedLevel;
	public Boolean isOut;
}