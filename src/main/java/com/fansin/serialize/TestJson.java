package com.fansin.serialize;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhaofeng on 17-4-2.
 */
public class TestJson {

    private int         num;
    private List        list;
    private String      id;
    private Map         map;
    private Set         set;
    private InnerJson[] inners;

    public TestJson() {
    }

    public Set getSet() {
        return set;
    }

    public void setSet(Set set) {
        this.set = set;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public InnerJson[] getInners() {
        return inners;
    }

    public void setInners(InnerJson[] inners) {
        this.inners = inners;
    }
}

class InnerJson {

    private String name;

    public InnerJson() {
    }

    public InnerJson(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
