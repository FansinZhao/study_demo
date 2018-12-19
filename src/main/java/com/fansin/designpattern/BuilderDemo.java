package com.fansin.designpattern;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by zhaofeng on 17-5-9.
 */
public class BuilderDemo {

    /**
     * 建造者模式:产品,建造者接口,具体建造者,指挥者
     * 应用场景,根据零部件组装玩具:
     * 产品:玩具:Toy 模型材料+外观+灯光+音乐
     * 建造者接口::ToyCreator 选择材料/外观/灯光/音乐
     * 具体建造者: XiYangYangToyCreator 接口实现
     * 指挥者:ToyManager:流水线
     * <p>
     * -----------门面比较-----------
     * 门面:对接口进行统一
     * 建造者:多部件对象统一创建过程
     */
    public static void main(String[] args) {
        ToyManager manager = new ToyManager(new XiYangYangToyCreator());
        Toy toy = manager.createToy();
        System.out.println("toy = " + toy);
    }
}

@Data
class Toy {

    private String material;
    private String appearance;
    private String light;
    private String music;
}

interface ToyCreator {

    void selectMaterial();

    void selectAppearance();

    void selectLight();

    void selectMusic();

    Toy toToy();
}

class XiYangYangToyCreator implements ToyCreator {

    private Toy toy = new Toy();

    @Override
    public void selectMaterial() {
        toy.setMaterial("塑料");
    }

    @Override
    public void selectAppearance() {
        toy.setAppearance("鸡年外形");
    }

    @Override
    public void selectLight() {
        toy.setLight("五彩");
    }

    @Override
    public void selectMusic() {
        toy.setMusic("过年发大财");
    }

    @Override
    public Toy toToy() {
        return toy;
    }
}

@AllArgsConstructor
class ToyManager {

    private ToyCreator creator;

    public Toy createToy() {
        creator.selectMaterial();
        creator.selectAppearance();
        creator.selectLight();
        creator.selectMusic();
        return creator.toToy();
    }
}
