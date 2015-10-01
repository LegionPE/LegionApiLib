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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ApiObject{
	public static ApiObject fromObject(JSONObject object){
		String objectClass = object.getString("_meta_class");
		try{
			Class<? extends ApiObject> clazz = Class.forName(ApiObject.class.getPackage().getName() + "." + objectClass)
					.asSubclass(ApiObject.class);
			try{
				ApiObject out = clazz.getConstructor().newInstance();
				for(Field field : clazz.getFields()){
					Class<?> type = field.getType();
					try{
						if(type.equals(Integer.class)){
							field.setInt(out, object.getInt(field.getName()));
						}else if(type.equals(Double.class)){
							field.setDouble(out, object.getDouble(field.getName()));
						}else if(type.equals(Boolean.class)){
							field.setBoolean(out, object.getBoolean(field.getName()));
						}else if(type.equals(String.class)){
							field.set(out, object.getString(field.getName()));
						}else if(isSubclass(type, ApiObject.class)){
							field.set(out, fromObject(object.getJSONObject(field.getName())));
						}else if(type.isArray()){
							Class<?> componentType = type.getComponentType();
							JSONArray array = object.getJSONArray(field.getName());
							Object[] resultArray = (Object[]) Array.newInstance(componentType, array.length());
							int i = 0;
							for(Object element : array){
								if(element instanceof JSONObject){
									element = fromObject((JSONObject) element);
								}
								resultArray[i++] = element;
							}
							field.set(out, resultArray);
						}else if(type.equals(Map.class)){
							JSONObject json = object.getJSONObject(field.getName());
							Map<String, Object> map = new HashMap<>(json.length());
							for(String key : json.keySet()){
								Object value = json.get(key);
								if(value instanceof JSONObject){
									value = fromObject((JSONObject) value);
								}
								map.put(key, value);
							}
							field.set(out, map);
						}else{
							field.set(out, object.get(field.getName())); // throw exception?
						}
					}catch(JSONException e){

					}
				}
				return out;
			}catch(InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
				e.printStackTrace();
				return null;
			}
		}catch(ClassNotFoundException e){
			UnknownObject out = new UnknownObject();
			out.object = object;
			return out;
		}
	}

	private static boolean isSubclass(Class<?> subclass, Class<?> superclass){
		try{
			subclass.asSubclass(superclass);
			return true;
		}catch(ClassCastException e){
			return false;
		}
	}
}
