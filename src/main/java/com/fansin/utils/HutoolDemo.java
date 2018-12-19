package com.fansin.utils;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhaofeng on 17-5-5.
 */
public class HutoolDemo {

    public static class Person {

        private String name;

        public Person() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        Person person = new Person();
        person.setName("test");
        JSON json = JSONUtil.parse(person);
        System.out.println(json.toString());
        System.out.println("fastjson:" + JSONObject.toJSONString(person));
        System.out.println((JSONUtil.parse(person)).toString());
        System.out.println(JSONUtil.toJsonStr(JSONUtil.parse(person)));

    }
}

