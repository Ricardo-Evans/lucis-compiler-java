package compiler.semantic;

import compiler.entity.BinaryData;

import javax.management.ObjectName;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by feaaaaaa.
 */
public class Serializer {
    BinaryData binaryData;

    public void serialize(Object serializeObject) throws Exception {
        int count=0;
        for(Field field:serializeObject.getClass().getDeclaredFields()){
            if(field.isAnnotationPresent(Serializable.class)){
                Object object=field.get(serializeObject);
                if(object instanceof Integer)
                    count+=countInt();
                else if(object instanceof String)
                    count+=countString((String) object);
                else if(object instanceof List<?>)
                    count+=countList((List<?>) object);
                else if(object instanceof HashMap<?,?>)
                    count+=countHashMap((HashMap<?, ?>) object);
                //todo
            }

        }


    }

    public void deserialize() {

    }

    //basic type
    private int countString(String s) {
        return s.getBytes().length;
    }

    private int countInt() {
        return 4;
    }

    //list
    private int countList(List<?> list) throws Exception {
        if (list.size() == 0) {
            System.out.println(" serializer count list size = 0");
            return 0;
        }

        Object listObject = list.get(0);
        int count = 0;

        if (listObject instanceof Integer)
            count += list.size() * countInt();
        else if (listObject instanceof String)
            for (Object o : list)
                count += countString((String) o);
        else if (listObject instanceof List<?>)
            for (Object o : list)
                count += countList((List<?>) o);
        else if (listObject instanceof HashMap<?, ?>)
            for (Object o : list)
                count += countHashMap((HashMap<?, ?>) o);
        else
            for (Object o : list)
                count += countObject(o);

        return count;
    }

    //hashmap
    private int countHashMap(HashMap<?, ?> hashMap) throws Exception {
        int size = hashMap.size();
        if (size == 0) {
            System.out.println(" serializer count hashmap size = 0");
            return 0;
        }

        Set<?> keySet = hashMap.keySet();
        int count = 0;
        Object keyObject = null, valueObject = null;
        for (Object key : keySet) {
            keyObject = key;
            valueObject = hashMap.get(key);
            break;
        }

        if (keyObject instanceof Integer)
            count += size * countInt();
        else if (keyObject instanceof String)
            for (Object key : keySet)
                count += countString((String) key);
        else if (keyObject instanceof List<?>)
            for (Object key : keySet)
                count += countList((List<?>) key);
        else if (keyObject instanceof HashMap<?, ?>)
            for (Object key : keySet)
                count += countHashMap((HashMap<?, ?>) key);
        else
            for (Object key : keySet)
                count += countObject(key);

        if (valueObject instanceof Integer)
            count += size * countInt();
        else if (valueObject instanceof String)
            for (Object key : keySet)
                count += countString((String) hashMap.get(key));
        else if (valueObject instanceof List<?>)
            for (Object key : keySet)
                count += countList((List<?>) hashMap.get(key));
        else if (valueObject instanceof HashMap<?, ?>)
            for (Object key : keySet)
                count += countHashMap((HashMap<?, ?>) hashMap.get(key));
        else
            for (Object key : keySet)
                count += countObject(hashMap.get(key));

        return count;
    }

    //object
    private int countObject(Object object) throws Exception {
        int count = 0;
        for (Field field : object.getClass().getDeclaredFields()) {
            Object fieldObject = field.get(object);
            if (fieldObject instanceof Integer)
                count += countInt();
            else if (fieldObject instanceof String)
                count += countString((String) field.get(object));
            else if (fieldObject instanceof List<?>)
                count += countList((List<?>) field.get(object));
            else if (fieldObject instanceof HashMap<?, ?>)
                count += countHashMap((HashMap<?, ?>) field.get(object));
            else throw new Exception("unhandled object field type");
        }
        return count;
    }


}
