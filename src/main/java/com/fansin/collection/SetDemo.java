package com.fansin.collection;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by zhaofeng on 17-4-21.
 */
public class SetDemo {

    public static void main(String[] args) {
        //hashSet
        System.out.println("---------hashset-----------");
        HashSet hashSet = new HashSet();//默认为16,扩容为2倍
        hashSet.add("a");//先计算hashcode,再计算equals
        hashSet.add("a");//先计算hashcode,再计算equals
        hashSet.add("1");//先计算hashcode,再计算equals
        hashSet.add("2");//先计算hashcode,再计算equals
        hashSet.add(null);//null hash 为0
        hashSet.add(null);//
        System.out.println("重复值插入不了");
        Iterator iterator = hashSet.iterator();
        System.out.println("使用迭代器进行删除操作");
        while (iterator.hasNext()) {
            Object o = iterator.next();
            System.out.println(o);
            if (o != null && o.equals("1")) {
                iterator.remove();
            }
        }
        System.out.println("1还存在么?" + hashSet.contains("1"));

        System.out.println("---------Linkedhashset-----------");
        LinkedHashSet linkedHashSet = new LinkedHashSet();//默认 16
        linkedHashSet.add("1");
        linkedHashSet.add("2");
        linkedHashSet.add("3");
        linkedHashSet.add("3");
        linkedHashSet.add(null);
        linkedHashSet.add(null);
        System.out.println("不能重复");
        System.out.println("可以插入null");
        linkedHashSet.contains("1");
        linkedHashSet.add("1");
        System.out.println("输出有序!");
        Iterator linkedIterator = linkedHashSet.iterator();
        while (linkedIterator.hasNext()) {
            Object o = linkedIterator.next();
            System.out.println(o);
            if (o != null && o.equals("2")) {
                linkedIterator.remove();
            }
        }
        System.out.println("使用迭代器删除元素!2还在么?" + linkedHashSet.contains("2"));
        System.out.println("---------SortedSet-----------");
        System.out.println("---------NavigableSet-----------");
        System.out.println("---------TreeSet-----------");
        TreeSet treeSet = new TreeSet();//默认字符串
        treeSet.add("0");
        treeSet.add("a");
        treeSet.add("b");
        treeSet.add("c");
        try {

            treeSet.add(null);
        } catch (NullPointerException e) {
            System.out.println("不能插入null!");
        }
        treeSet.add("c");
        treeSet.add("1");
        treeSet.add("e");
        treeSet.add("2");
        Iterator treeIterator = treeSet.iterator();
        System.out.println("SortedSet 特性 排列有序!");
        while (treeIterator.hasNext()) {
            Object o = treeIterator.next();
            System.out.println(o);
            if (o.equals("c")) {
                treeIterator.remove();
            }
        }
        System.out.println("使用迭代器删除元素!c还在么?" + treeSet.contains("c"));
        TreeSet subSet = (TreeSet) treeSet.subSet("a", "e");
        System.out.println("subSet 不包含 上界");
        for (Object o : subSet) {
            System.out.println(o);
        }
        TreeSet subSet1 = (TreeSet) treeSet.subSet("a", true, "e", true);
        System.out.println("NaviagleSet 特性 : subSet 可以指定是否可以包含上下界");
        for (Object o : subSet1) {
            System.out.println(o);
        }
        TreeSet subSet2 = (TreeSet) treeSet.headSet("1");
        System.out.println("NaviagleSet 特性 : headSet < 1 的集合");
        for (Object o : subSet2) {
            System.out.println(o);
        }
        TreeSet subSet3 = (TreeSet) treeSet.tailSet("2");
        System.out.println("NaviagleSet 特性 : tailSet > 2 的集合");
        for (Object o : subSet3) {
            System.out.println(o);
        }
        System.out.println("NaviagleSet 特性 : ceiling >= 2 最近一个元素" + treeSet.ceiling("2"));

        Iterator descendingIterator = treeSet.descendingIterator();
        System.out.println("NaviagleSet 特性 逆序!");
        while (descendingIterator.hasNext()) {
            Object o = descendingIterator.next();
            System.out.println(o);
        }

        System.out.println("---------ConcurrentSkipListSet 用法与TreeSet基本一致,区别在于线程安全和底层实现 -----------");
        ConcurrentSkipListSet concurrentSkipListSet = new ConcurrentSkipListSet();
        concurrentSkipListSet.add("0");
        concurrentSkipListSet.add("a");
        concurrentSkipListSet.add("b");
        concurrentSkipListSet.add("c");
        try {

            concurrentSkipListSet.add(null);
        } catch (NullPointerException e) {
            System.out.println("不能插入null!");
        }
        concurrentSkipListSet.add("c");
        concurrentSkipListSet.add("1");
        concurrentSkipListSet.add("e");
        concurrentSkipListSet.add("2");
        Iterator Iterator0 = concurrentSkipListSet.iterator();
        System.out.println("SortedSet 特性 排列有序!");
        while (Iterator0.hasNext()) {
            Object o = Iterator0.next();
            System.out.println(o);
            if (o.equals("c")) {
                Iterator0.remove();
            }
        }
        System.out.println("使用迭代器删除元素!c还在么?" + concurrentSkipListSet.contains("c"));
        ConcurrentSkipListSet subSet00 = (ConcurrentSkipListSet) concurrentSkipListSet.subSet("a", "e");
        System.out.println("subSet 不包含 上界");
        for (Object o : subSet00) {
            System.out.println(o);
        }
        ConcurrentSkipListSet subSet10 = (ConcurrentSkipListSet) concurrentSkipListSet.subSet("a", true, "e", true);
        System.out.println("NaviagleSet 特性 : subSet 可以指定是否可以包含上下界");
        for (Object o : subSet10) {
            System.out.println(o);
        }
        ConcurrentSkipListSet subSet20 = (ConcurrentSkipListSet) concurrentSkipListSet.headSet("1");
        System.out.println("NaviagleSet 特性 : headSet < 1 的集合");
        for (Object o : subSet20) {
            System.out.println(o);
        }
        ConcurrentSkipListSet subSet30 = (ConcurrentSkipListSet) concurrentSkipListSet.tailSet("2");
        System.out.println("NaviagleSet 特性 : tailSet > 2 的集合");
        for (Object o : subSet30) {
            System.out.println(o);
        }
        System.out.println("NaviagleSet 特性 : ceiling >= 2 最近一个元素" + concurrentSkipListSet.ceiling("2"));

        Iterator descendingIterator0 = concurrentSkipListSet.descendingIterator();
        System.out.println("NaviagleSet 特性 逆序!");
        while (descendingIterator0.hasNext()) {
            Object o = descendingIterator0.next();
            System.out.println(o);
        }

    }
}
