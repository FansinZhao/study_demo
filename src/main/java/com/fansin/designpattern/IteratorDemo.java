package com.fansin.designpattern;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * Created by zhaofeng on 17-5-15.
 */
public class IteratorDemo {

    /*
    迭代子模式:
    又称游标模式,顺序访问对象,而不用暴露内部表象.
    应用场景:
    实现Iterator的Collection类

    基本类型
    Iterator
    具体的Iterator
    aggravate聚合
    具体聚合

    这里涉及了不少术语
    白盒迭代子/外禀迭代子/游标迭代子
    黑盒迭代子/內禀迭代子
    静态迭代子/动态迭代子
    fail fast
     */

    public static void main(String[] args) {

        //白盒
        System.out.println("---------白盒-----------");
        OutAggravate out = new OutAggravate(new int[] { 1, 2, 3, 4, 5 });
        OutIterator iterator = new OutIterator(out);
        while (iterator.hasNext()) {
            System.out.printf("%d ", iterator.next());
        }
        System.out.println("\n----------黑盒----------");
        AggravateIterator aggravateIterator = new AggravateIterator(new int[] { 1, 2, 3, 4, 5 });
        Iterator it = aggravateIterator.createIterator();
        while (it.hasNext()) {
            System.out.printf("%d ", it.next());
        }
        System.out.println("演示fast fail");
        it = aggravateIterator.createIterator();
        while (it.hasNext()) {
            System.out.printf("%d ", it.next());
            it.remove();
        }
        System.out.println("\n-----禁止在循环遍历时进行外部修改-------");
        it = aggravateIterator.createIterator();
        while (it.hasNext()) {
            System.out.printf("%d ", it.next());
            aggravateIterator.remove();
        }
    }
}

//白盒迭代子/外禀迭代子/游标迭代子
//使用jdk的Iterator
//具体迭代子
class OutIterator implements Iterator {

    private Aggravate aggravate;

    private int index = 0;
    private int size  = 0;

    public OutIterator(Aggravate aggravate) {
        this.aggravate = aggravate;
        this.size = aggravate.size();
    }

    @Override
    public boolean hasNext() {
        return index < size;
    }

    @Override
    public Object next() {
        return aggravate.getElement(index++);
    }

    @Override
    public void remove() {
        size--;
    }
}

abstract class Aggravate {

    abstract Iterator createIterator();

    abstract int size();

    abstract int getElement(int index);
}

class OutAggravate extends Aggravate {

    private int[] arr;

    public OutAggravate(int[] arr) {
        this.arr = arr;
    }

    @Override
    Iterator createIterator() {
        return new OutIterator(this);
    }

    @Override
    int size() {
        return arr.length;
    }

    @Override
    int getElement(int index) {
        if (index < arr.length) {
            return arr[index];
        } else {
            return -1;
        }
    }
}

//黑盒和fast fail
class AggravateIterator extends Aggravate {

    private int[] arr;

    private int modify = 0;

    @Override
    Iterator createIterator() {
        return new InnerIterator();
    }

    @Override
    int size() {
        return arr.length;
    }

    @Override
    int getElement(int index) {
        if (index < arr.length) {
            return arr[index];
        } else {
            return -1;
        }
    }

    public void remove() {
        modify++;
    }

    public AggravateIterator(int[] arr) {
        this.arr = arr;
    }

    private class InnerIterator implements Iterator {

        private int index  = 0;
        private int size   = 0;
        private int expect = modify;

        public InnerIterator() {
            this.size = arr.length;
        }

        @Override
        public boolean hasNext() {
            return index < this.size;
        }

        @Override
        public Object next() {

            if (expect != modify) {
                throw new ConcurrentModificationException("不能在外部修改聚集元素内容!");
            }
            return arr[index++];
        }

        @Override
        public void remove() {
            AggravateIterator.this.remove();
            expect = modify;
        }
    }

}
