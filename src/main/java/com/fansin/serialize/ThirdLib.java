package com.fansin.serialize;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by zhaofeng on 17-4-2.
 */
public class ThirdLib {

    public static void main(String[] args) {

        TestJson jo = new TestJson();
        jo.setId("aaa");
        jo.setNum(20);
        Map map = new ConcurrentHashMap();
        map.put("a", "1");
        map.put("b", "2");
        jo.setMap(map);
        List list = new CopyOnWriteArrayList();
        list.add("cow1");
        list.add("cow2");
        jo.setList(list);
        Set set = new HashSet();
        set.add("set");
        jo.setSet(set);
        InnerJson json = new InnerJson("fansin");
        jo.setInners(new InnerJson[] { json });

        String jstr = "";
        TestJson tmp = null;

        //fastjson
        jstr = JSONObject.toJSONString(jo);
        System.out.println("fastjson->json:" + jstr);
        tmp = JSONObject.parseObject(jstr, TestJson.class);
        System.out.println("fastjson->obj:" + tmp.getId());
        jstr = "";
        tmp = null;
        //gson
        Gson gson = new Gson();
        jstr = gson.toJson(jo);
        System.out.println("gson->json:" + jstr);
        tmp = gson.fromJson(jstr, TestJson.class);
        System.out.println("fastjson->obj:" + tmp.getId());
        jstr = "";
        tmp = null;
//jack
        ObjectMapper mapper = new ObjectMapper();
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        try {
            jstr = mapper.writeValueAsString(jo);
            System.out.println("jackson->json:" + jstr);
            tmp = mapper.readValue(jstr, TestJson.class);
            System.out.println("jackson->obj:" + tmp.getId());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
