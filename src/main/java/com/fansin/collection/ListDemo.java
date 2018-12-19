package com.fansin.collection;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by zhaofeng on 17-4-19.
 */
public class ListDemo {

    /*
     * Collection
     * --List
     * ----ArrayList
     * ----Vector
     * ----LinkedList
     *
     * */

    public static void main(String[] args) {

        ArrayList arrayList = new ArrayList();//默认大小是10,可以指定大小
        //1 有序
        arrayList.add("3");
        arrayList.add("2");
        arrayList.add("1");
        //2 允许重复
        arrayList.add("0");
        arrayList.add("0");
        arrayList.add(null);
        arrayList.add(null);
        arrayList.add(null);
        // 3 起始索引为0
        System.out.println(arrayList.get(0));
        //4 迭代器 ListIterator
        ListIterator listIterator = arrayList.listIterator();
        while (listIterator.hasNext()) {
            int index = listIterator.nextIndex();
            Object value = listIterator.next();
            System.out.println("迭代器:" + index + " " + value);
            if (index == 6) {// 迭代器删除,状态模式,修改会立即生效
                listIterator.remove();
                listIterator.add("66");
                /*
                 *相对于foreach来说,
                 * 每次对对象增加删除,都会做一次赋值
                 * expectedModCount = modCount;
                 *
                 *
                 */
            }
        }
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println("fori " + i + " " + arrayList.get(i));
            //发现上面的已经添加进入了 66
        }

        System.out.println("开始:" + arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            /*
             * 每次循环都会重新计算一次大小
             *
             * */
            if (i == 5) {
                arrayList.remove(arrayList.get(i));
            }
            System.out.println("删除3 " + i + " " + arrayList.get(i));
        }
        System.out.println("结束:" + arrayList.size());

        try {
            //5 遍历方式 基于快速失败
            for (Object o : arrayList) {
                /*
                 * foreach 会先生成Iterator,同时记录修改 modCount 数量和期望数量 expectedModCount
                 * 当执行remove时,修改数量+1,当前循环并不会出现问题,在下一次循环时,modCount != expectedModCount,从而报错
                 *
                 * **/
                arrayList.remove("3");///使用快速失败迭代
                System.out.println("foreach 遍历对象:" + o);
            }

        } catch (ConcurrentModificationException e) {
            System.out.println(e.toString());
        }
        /*
         *调用remove时,modCount+1,并没有校验,所以不会报错
         *
         */
        arrayList.remove("3");
        System.out.println(arrayList.contains("3"));

        //6 扩容,每次扩容为原来的1.5倍
        //可以追踪grow方法,查看capacity,注意不是list的size
        //7 集合添加
        ArrayList list = new ArrayList();
        list.add("aaa");
        list.add("bbb");
        list.add("abc");
        arrayList.addAll(1, list);//不是add
        HashSet map = new HashSet();
        map.add("m01");
        map.add("m02");
        map.add("m03");
        arrayList.addAll(1, map);
        for (Object o : arrayList) {
            System.out.println(o);
        }

        System.out.println("-----------------vector-----------------");
        Vector vector = new Vector();
        vector.add("v1");
        vector.add("v1");
        vector.add("v2");
        vector.add(null);
        vector.add(null);
        vector.add("v3");
        vector.addElement("obj1");
        vector.addElement("obj2");

        for (Object o : vector) {
            System.out.println(o);
        }

        System.out.println("-----------------LinkedList-----------------");
        LinkedList linkedList = new LinkedList();
        //栈 FILO先进后出
        linkedList.push("stack1");
        linkedList.push("stack2");
        linkedList.push("stack3");
        System.out.println("栈:先进后出");
        System.out.println(linkedList.pop());
        System.out.println(linkedList.pop());
        System.out.println(linkedList.pop());
        //队列,先进先出
        System.out.println("队列,先进先出");
        linkedList.add(0, "queue1");
        linkedList.add("queue2");
        linkedList.offer("queue3");
        System.out.println(linkedList.poll());
        System.out.println(linkedList.remove());
        System.out.println(linkedList.removeLast());
        //双向队列
        System.out.println("双向队列,两头都可以进或出");
        linkedList.addFirst("head1");
        linkedList.offerFirst("head2");
        linkedList.addLast("tail1");
        linkedList.offerLast("tail2");
        System.out.println(linkedList.getFirst());
        System.out.println(linkedList.getLast());
        System.out.println(linkedList.getFirst());
        System.out.println(linkedList.getLast());

        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        copyOnWriteArrayList.add("aaa");
        copyOnWriteArrayList.add("bbb");
        copyOnWriteArrayList.add("abc");
        arrayList.addAll(1, list);//不是add
        HashSet map1 = new HashSet();
        map1.add("m01");
        map1.add("m02");
        map1.add("m03");
        copyOnWriteArrayList.addAll(1, map1);
        for (int i = 0; i < copyOnWriteArrayList.size(); i++) {
            String s = (String) copyOnWriteArrayList.get(i);
            System.out.println("s = " + s);
        }

    }

}
