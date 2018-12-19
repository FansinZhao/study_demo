package com.fansin.collection;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by zhaofeng on 17-4-21.
 */
public class MapDemo {

    public static void main(String[] args) {
        System.out.println("--------HashMap-----------");
        System.out.println("默认大小为16,扩容为2倍");
        System.out.println("必要时重写equals和hashcode,保证其良好性");
        HashMap hashMap = new HashMap();//
        hashMap.put("1", "1");
        hashMap.put("2", "2");
        hashMap.put("3", "3");
        hashMap.put("3", "333");
        System.out.println("key重复,value会被覆盖");
        System.out.println("键值和value都可以为null");
        hashMap.put(null, null);
        hashMap.put(null, "ssss");
        hashMap.put("a", null);
        hashMap.put("b", null);
        hashMap.put("c", null);
        Set keySet = hashMap.keySet();
        System.out.println("键集遍历! 无序");
        for (Object o : keySet) {
            System.out.println(o + "->" + hashMap.get(o));
        }
        System.out.println("值集遍历!无序");
        Collection values = hashMap.values();
        for (Object o : values) {
            System.out.println(o);
        }
        System.out.println("KV集遍历!无序");
        Set<Map.Entry> entrySet = hashMap.entrySet();
        for (Map.Entry entry : entrySet) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "->" + value);
        }
        System.out.println("只能使用迭代器去删除");
        Iterator<Map.Entry> iterable = entrySet.iterator();
        while (iterable.hasNext()) {
            Map.Entry entry = iterable.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key != null && key.equals("b")) {
                iterable.remove();
            }
        }
        System.out.println("还有key=b?" + hashMap.containsKey("b"));
        System.out.println("--------HashTable 使用基本与hashMap一致-----------");
        System.out.println("默认大小为11,扩容为2倍+1");
        System.out.println("必要时重写equals和hashcode,保证其良好性");
        Hashtable hashtable = new Hashtable();//
        hashtable.put("1", "1");
        hashtable.put("2", "2");
        hashtable.put("3", "3");
        hashtable.put("3", "333");
        System.out.println("key重复,value会被覆盖");
        try {

            hashtable.put(null, "aaaa");
            hashtable.put("a", null);
            hashtable.put("b", null);
            hashtable.put("c", null);
        } catch (NullPointerException e) {
            System.out.println("key和value 都不能为null");
        }
        Set keySet0 = hashtable.keySet();
        System.out.println("键集遍历! 无序");
        for (Object o : keySet0) {
            System.out.println(o + "->" + hashtable.get(o));
        }
        System.out.println("值集遍历!无序");
        Collection values0 = hashtable.values();
        for (Object o : values0) {
            System.out.println(o);
        }
        System.out.println("KV集遍历!无序");
        Set<Map.Entry> entrySet0 = hashtable.entrySet();
        for (Map.Entry entry : entrySet0) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "->" + value);
        }
        System.out.println("只能使用迭代器去删除");
        Iterator<Map.Entry> iterable0 = entrySet0.iterator();
        while (iterable0.hasNext()) {
            Map.Entry entry = iterable0.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key != null && key.equals("2")) {
                iterable0.remove();
            }
        }
        System.out.println("还有key=2?" + hashtable.containsKey("2"));

        System.out.println("--------IdentityHashMap-----------");
        IdentityHashMap identityHashMap = new IdentityHashMap();//
        System.out.println("初始化为32,扩容2倍,期望大小为容量的2/3");
        identityHashMap.put("1", "1");
        identityHashMap.put("a", "a");
        identityHashMap.put("b", "b");
        identityHashMap.put("1", "1");
        identityHashMap.put(new String("1"), "1");
        System.out.println("key值比较,采用的是==");
        identityHashMap.put(null, null);
        identityHashMap.put(null, "1");
        identityHashMap.put("2", null);
        System.out.println("允许key和value值为null");
        System.out.println("遍历和迭代方式同hashMap");
        Set keySet01 = identityHashMap.keySet();
        System.out.println("键集遍历! 无序");
        for (Object o : keySet01) {
            System.out.println(o + "->" + identityHashMap.get(o));
        }
        System.out.println("值集遍历!无序");
        Collection values01 = identityHashMap.values();
        for (Object o : values01) {
            System.out.println(o);
        }
        System.out.println("KV集遍历!无序");
        Set<Map.Entry> entrySet01 = identityHashMap.entrySet();
        for (Map.Entry entry : entrySet01) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "->" + value);
        }
        System.out.println("只能使用迭代器去删除");
        Iterator<Map.Entry> iterable01 = entrySet01.iterator();
        while (iterable01.hasNext()) {
            Map.Entry entry = iterable01.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key != null && key.equals("b")) {
                iterable01.remove();
            }
        }
        System.out.println("还有key=b?" + identityHashMap.containsKey("b"));

        System.out.println("--------IdentityHashMap-----------");
        System.out.println("默认大小为16,扩容2倍");
        System.out.println("允许key或value为null");
        System.out.println("value使用WeakReference包装,不要引用key");
        System.out.println("迭代方式同hashmap");
        System.out.println("迭代遍历结果无序");
        WeakHashMap weakHashMap = new WeakHashMap();
        String key1 = "a";
        weakHashMap.put(key1, new WeakReference<String>("a"));
        weakHashMap.put("b", new WeakReference<String>("b"));
        weakHashMap.put("c", new WeakReference<String>("c"));
        weakHashMap.put("c", new WeakReference<String>("c"));
        weakHashMap.put(null, null);
        weakHashMap.put(null, new WeakReference<String>("c"));
        weakHashMap.put("e", null);
        System.out.println("KV集遍历!无序");
        key1 = null;
        Set<Map.Entry> entrySet11 = weakHashMap.entrySet();
        for (Map.Entry entry : entrySet11) {
            Object key = entry.getKey();
            Object value = null;
            if (entry.getValue() != null) {
                value = ((WeakReference<String>) entry.getValue()).get();
            }
            System.out.println(key + "->" + value);
        }

        key1 = null;
        System.gc();
        System.out.println("gcgc");
        System.out.println("KV集遍历!无序");
        Set<Map.Entry> entrySet12 = weakHashMap.entrySet();
        for (Map.Entry entry : entrySet12) {
            Object key = entry.getKey();
            Object value = null;
            if (entry.getValue() != null) {
                value = ((WeakReference<String>) entry.getValue()).get();
            }
            System.out.println(key + "->" + value);
        }

        System.out.println("--------LinkedHashMap-----------");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        System.out.println("集成hashmap,默认大小16,扩容2倍");
        linkedHashMap.put("1", "1");
        linkedHashMap.put("2", "2");
        linkedHashMap.put("2", "2");
        linkedHashMap.put("a", "a");
        linkedHashMap.put("b", "b");
        linkedHashMap.put("3", "3");
        linkedHashMap.put("4", "4");
        linkedHashMap.put(null, null);
        linkedHashMap.put(null, "null");
        linkedHashMap.put("null", null);
        System.out.println("允许空值,不能重复");

        System.out.println("有序实现原理,是在原hashmap的entry基础上,添加前一个和下一个的entry的引用");
        System.out.println("插入顺序,更新值不会发生结构修改");
        Set keySet010 = linkedHashMap.keySet();
        System.out.println("键集遍历! 有序");
        for (Object o : keySet010) {
            System.out.println(o + "->" + linkedHashMap.get(o));
        }
        System.out.println("值集遍历!有序");
        Collection values010 = linkedHashMap.values();
        for (Object o : values010) {
            System.out.println(o);
        }
        System.out.println("KV集遍历!有序");
        Set<Map.Entry> entrySet010 = linkedHashMap.entrySet();
        for (Map.Entry entry : entrySet010) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "->" + value);
        }
        System.out.println("只能使用迭代器去删除");
        Iterator<Map.Entry> iterable010 = entrySet010.iterator();
        while (iterable010.hasNext()) {
            Map.Entry entry = iterable010.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key != null && key.equals("b")) {
                iterable010.remove();
            }
        }
        System.out.println("还有key=b?" + linkedHashMap.containsKey("b"));
        System.out.println("默认是插入顺序!");
        System.out.println("访问顺序模式用来实现LRU最近最少使用Least Recently Used!");
        LinkedHashMap lru = new LinkedHashMap(5, .75f, true) {

            private static final long serialVersionUID = -6088304723264830398L;

            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > 5;
            }
        };
        lru.put("a", "a");
        lru.put("b", "b");
        lru.put("c", "c");
        lru.put("d", "d");
        lru.put("a", "a");
        lru.put("e", "e");
        lru.put("0", "0");

        System.out.println("LRU算法!有序");
        System.out.println("b被删除,因为a在b后面又被放入一次,发生了结构修改");
        lru.get("b");
        System.out.println("b被get一次,没有发生了结构修改");

        Set<Map.Entry> entrySet0100 = lru.entrySet();
        for (Map.Entry entry : entrySet0100) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "->" + value);
        }

        System.out.println("--------TreeMap-----------");
        TreeMap treeMap = new TreeMap();
        treeMap.put("a", "a");
        treeMap.put("0", "aaa");
        treeMap.put("2", "a");
        treeMap.put("1", "a");
        treeMap.put("b", "a");
        treeMap.put("a", "ab");
        try {
            treeMap.put(null, null);
            treeMap.put(null, "b");
            treeMap.put("d", null);

        } catch (NullPointerException e) {
            System.out.println("不允许空值存在");
        }
        System.out.println("排序输出");
        Set<Map.Entry> entrySet01001 = treeMap.entrySet();
        for (Map.Entry entry : entrySet01001) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "->" + value);
        }
        System.out.println("navigable属性:");
        NavigableMap treeMap1 = treeMap.subMap("0", true, "2", true);
        System.out.println("可以对上下界进行限定");
        Set<Map.Entry> treeMapEntry = treeMap1.entrySet();
        for (Map.Entry entry : treeMapEntry) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "->" + value);
        }
        System.out.println("第一个:" + treeMap.firstEntry());
        System.out.println("也是第一个:" + treeMap.pollFirstEntry());
        System.out.println("最后一个:" + treeMap.lastEntry());
        System.out.println("也是最后一个:" + treeMap.pollLastEntry());
        System.out.println("<= 2 的最近的entry:" + treeMap.floorEntry("2"));

        System.out.println("--------ConcurrentHashMap-----------");
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        System.out.println("默认容量为16,扩容为2倍");
        concurrentHashMap.put("b", "b");
        concurrentHashMap.put("a", "a");
        concurrentHashMap.put("0", "b");
        System.out.println("只有当值为null才能put,否则,返回已存在的值");
        System.out.println("返回上一个键值对的value:" + concurrentHashMap.putIfAbsent("a", "b"));
        System.out.println("返回上一个键值对的value:" + concurrentHashMap.putIfAbsent("e", "e"));

        try {
            concurrentHashMap.put(null, null);
            concurrentHashMap.put(null, "b");
            concurrentHashMap.put("d", null);

        } catch (NullPointerException e) {
            System.out.println("不允许空值存在");
        }
        concurrentHashMap.put("c", "c");
        concurrentHashMap.put("d", "d");
        System.out.println("无序");
        Set<Map.Entry> entrySet1 = concurrentHashMap.entrySet();
        for (Map.Entry entry : entrySet1) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "->" + value);
        }
        System.out.println("还有很多java8特性待发掘......");
        System.out.println("--------ConcurrentHashMap-----------");
        ConcurrentSkipListMap concurrentSkipListMap = new ConcurrentSkipListMap();
        concurrentSkipListMap.put("a", "a");
        concurrentSkipListMap.put("0", "0");
        concurrentSkipListMap.put("2", "2");
        concurrentSkipListMap.put("1", "1");
        concurrentSkipListMap.put("b", "b");
        try {
            concurrentSkipListMap.put(null, null);
            concurrentSkipListMap.put(null, "b");
            concurrentSkipListMap.put("d", null);

        } catch (NullPointerException e) {
            System.out.println("不允许空值存在");
        }

        System.out.println("只有当值为null才能put,否则,返回已存在的值");
        System.out.println("返回上一个键值对的value:" + concurrentSkipListMap.putIfAbsent("a", "b"));
        System.out.println("返回上一个键值对的value:" + concurrentSkipListMap.putIfAbsent("e", "e"));

        System.out.println("排序");
        Set<Map.Entry> entrySet10 = concurrentSkipListMap.entrySet();
        for (Map.Entry entry : entrySet10) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "->" + value);
        }

        System.out.println("navigable属性:");
        NavigableMap treeMap10 = concurrentSkipListMap.subMap("0", true, "2", true);
        System.out.println("可以对上下界进行限定");
        Set<Map.Entry> treeMapEntry0 = treeMap10.entrySet();
        for (Map.Entry entry : treeMapEntry0) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "->" + value);
        }
        System.out.println("第一个:" + concurrentSkipListMap.firstEntry());
        System.out.println("也是第一个:" + concurrentSkipListMap.pollFirstEntry());
        System.out.println("最后一个:" + concurrentSkipListMap.lastEntry());
        System.out.println("也是最后一个:" + concurrentSkipListMap.pollLastEntry());
        System.out.println("<= 2 的最近的entry:" + concurrentSkipListMap.floorEntry("2"));
    }
}
