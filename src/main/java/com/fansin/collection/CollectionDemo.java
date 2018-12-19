package com.fansin.collection;

import java.util.*;

/**
 * Created by zhaofeng on 17-4-6.
 */
public class CollectionDemo {

    /*
     * hashmap和hashtable比较
     * 1 继承
     * hashtable继承Dictionary,hashmap继承AbstractMap
     * 2 遍历方式
     * hashtable 多一个Enumeration,两者都有Iterator
     * 3 空值key-value
     * hashtable 不能含有空key值,hashmap 可以有一个空key值,多个空value
     * 4 hash算法
     * hashtable 直接使用hashcode,hashmap 对hash进行了重新计算
     * 5 初始值和扩容机制不同
     * hashtable 默认值11,扩容old*2+1;hashmap 默认16,扩容2^n
     * 6 安全性
     * hashtable 存值使用synchronized;hashmap 没有同步
     *
     * */
    public static void main(String[] args) {

        System.out.println(5 >>> 1);
        System.out.println("-------treeset--------");
        //treeset
        treeset();
        System.out.println("-------treemap--------");
        //treemap
        try {
            treemap();
        } catch (Exception e) {

        }
        System.out.println("-------linkedlist------");
        //Linkedlist
        linkedlist();
        System.out.println("------linkedhashmap-------");
        //linked
        linkedhashmap();
        //hashset
        System.out.println("-----hashset-----");
        hashset();
        //linkedhashset
        System.out.println("-----linkedhashset-----");
        linkedhashset();
        hashSet();
    }

    public static void hashSet() {
        HashSet<String> set = new HashSet<>();
        System.out.println(set.add(null));
        System.out.println(set.add(null));
        System.out.println(set.add("z"));
        System.out.println(set.add("0"));
        System.out.println(set.add("a"));
        System.out.println(set.add("a"));
        set.remove("a");
        for (String s : set) {
            System.out.println("set value:" + s);
        }

    }

    public static void linkedhashset() {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        linkedHashSet.add("a");
        linkedHashSet.add("b");
        linkedHashSet.add("b");
        for (Object o : linkedHashSet) {
            System.out.println(o);
        }
    }

    public static void hashset() {
        HashSet hashSet = new HashSet();
        hashSet.add("a");
        hashSet.add("a");//不会重复插入
        hashSet.add("e");
        hashSet.add("b");
        for (Object o : hashSet) {
            System.out.println(o);//根据key值显示
        }
    }

    public static void linkedhashmap() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("0", "0");
        linkedHashMap.put(new Tree("00", 12), "0000");
        linkedHashMap.put("a", "a");
        linkedHashMap.put("d", "d");
        linkedHashMap.put("c", "c");
        linkedHashMap.put("b", "sc");
        linkedHashMap.put("b", "b");
        linkedHashMap.remove("c");
        for (Object o : linkedHashMap.keySet()) {
            System.out.println(linkedHashMap.get(o));
        }

        System.out.println(">>>>>>>");
        HashMap map = new HashMap();
        map.put("a", "a");
        map.put("d", "d");
        map.put("c", "c");
        for (Object o : map.keySet()) {
            System.out.println(o + ">>" + map.get(o));
        }
    }

    public static void linkedlist() {

        System.out.println("10右移一位" + (10 >> 1));

        LinkedList linkedList = new LinkedList();
        linkedList.add("aa");
        linkedList.add("bb");
        linkedList.add("cc");
        linkedList.add(1, "ab");
        linkedList.addFirst("00");
        linkedList.addLast("99");
        linkedList.remove(3);//删除bb

        for (Object o : linkedList) {
            System.out.println(o);
        }

    }

    public static void treemap() {
        TreeMap treeMap = new TreeMap();
        treeMap.put("c", "c");
        treeMap.put("a", "a");
        treeMap.put("2", "2");
        treeMap.put(new Tree("bb", 20), "ssss");
        treeMap.put(new Tree("aa", 23), "ssss");

        //类型不一致,会报错 使用泛型定义
        for (Object key : treeMap.keySet()) {
            if (key instanceof Tree) {
                Tree treeKey = (Tree) key;
                System.out.println(treeKey + ">>" + treeMap.get(treeKey));
            } else {
                String strKey = (String) key;
                System.out.println(strKey + ">>" + treeMap.get(strKey));
            }
        }

    }

    public static void treeset() {
        TreeSet treeSet = new TreeSet();
        treeSet.add("aaa");
        treeSet.add("caa");
        treeSet.add("0caa");
        treeSet.add("abc");
        treeSet.add(new Tree("edd", 0));//对象必须实现comparable
        treeSet.add(new Tree("ddd", 0));

//        Iterator iterator = treeSet.iterator();
        for (Object o : treeSet) {
            System.out.println(o);
        }
    }

    static class Tree implements Comparable {

        private String node;
        private int    id;

        public Tree(String node, int id) {
            this.node = node;
            this.id = id;
        }

        public String getNode() {
            return node;
        }

        public void setNode(String node) {
            this.node = node;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        /**
         * Compares this object with the specified object for order.  Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         * <p>
         * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
         * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
         * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
         * <tt>y.compareTo(x)</tt> throws an exception.)
         * <p>
         * <p>The implementor must also ensure that the relation is transitive:
         * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
         * <tt>x.compareTo(z)&gt;0</tt>.
         * <p>
         * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
         * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
         * all <tt>z</tt>.
         * <p>
         * <p>It is strongly recommended, but <i>not</i> strictly required that
         * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
         * class that implements the <tt>Comparable</tt> interface and violates
         * this condition should clearly indicate this fact.  The recommended
         * language is "Note: this class has a natural ordering that is
         * inconsistent with equals."
         * <p>
         * <p>In the foregoing description, the notation
         * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
         * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
         * <tt>0</tt>, or <tt>1</tt> according to whether the value of
         * <i>expression</i> is negative, zero or positive.
         *
         * @param o the object to be compared.
         * @return a negative integer, zero, or a positive integer as this object
         * is less than, equal to, or greater than the specified object.
         * @throws NullPointerException if the specified object is null
         * @throws ClassCastException   if the specified object's type prevents it
         *                              from being compared to this object.
         */
        @Override
        public int compareTo(Object o) {
            if (o instanceof Tree) {
                Tree tree = (Tree) o;
                return node.compareTo(tree.node);
            } else {
                return node.compareTo((String) o);
            }
        }

        @Override
        public String toString() {
            return "node=" + node + " id=" + id;
        }
    }
}

