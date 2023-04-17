package org.helloframework.codec.json;

import java.io.Serializable;
import java.util.*;

public final class JSONArray extends JSON implements List<Object>, Cloneable, RandomAccess, Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Object> dataList;

    public JSONArray() {
        this.dataList = new ArrayList<>();
    }

    public JSONArray(List<Object> list) {
        if (list == null)
            throw new IllegalArgumentException("list is null.");
        this.dataList = list;
    }

    public JSONArray(int initialCapacity) {
        this.dataList = new ArrayList<Object>(initialCapacity);
    }

    public int size() {
        return dataList.size();
    }

    @Override
    public boolean isEmpty() {
        return dataList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return dataList.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return dataList.iterator();
    }

    @Override
    public Object[] toArray() {
        return dataList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return dataList.toArray(a);
    }

    public boolean add(Object e) {
        return dataList.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return dataList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return dataList.containsAll(c);
    }

    @Override
    public boolean equals(Object obj) {
        return this.dataList.equals(obj);
    }

    @Override
    public boolean addAll(Collection<?> c) {
        return dataList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<?> c) {
        return dataList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return dataList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return dataList.retainAll(c);
    }

    @Override
    public void clear() {
        dataList.clear();
    }

    @Override
    public Object clone() {
        return new JSONArray(new ArrayList<>(dataList));
    }

    public int indexOf(Object o) {
        return dataList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return dataList.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return dataList.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return dataList.listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return dataList.subList(fromIndex, toIndex);
    }

    public Object get(int index) {
        return dataList.get(index);
    }

    @Override
    public int hashCode() {
        return this.dataList.hashCode();
    }

    @Override
    public Object set(int index, Object element) {
        if (index == -1) {
            dataList.add(element);
            return null;
        }

        if (dataList.size() <= index) {
            for (int i = dataList.size(); i < index; ++i) {
                dataList.add(null);
            }
            dataList.add(element);
            return null;
        }

        return dataList.set(index, element);
    }

    @Override
    public void add(int index, Object element) {
        dataList.add(index, element);
    }

    @Override
    public Object remove(int index) {
        return dataList.remove(index);
    }

    public JSONObject getJSONObject(int index) {
        Object value = dataList.get(index);
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }

        if (value instanceof Map) {
            return new JSONObject((Map) value);
        }
        return super.convertValue(value, JSONObject.class);
    }

    public JSONArray getJSONArray(int index) {
        Object value = dataList.get(index);

        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }

        if (value instanceof List) {
            return new JSONArray((List) value);
        }

        return super.convertValue(value, JSONArray.class);
    }

    public String getString(int index) {
        Object value = get(index);
        if (value == null)
            return null;
        return value.toString();
    }

    public <T> List<T> toJavaList(Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        for (Object obj : dataList) {
            resultList.add(super.convertValue(obj, clazz));
        }
        return resultList;
    }


}
