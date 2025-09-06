package com.test.config;

import com.test.pojo.Cat;
import com.test.pojo.Mouse;
import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.StringUtils;

@Data
//当加载CartoonCatAndMouse类时将CartoonProperties类加载成bean，不用不加载，解耦合
@EnableConfigurationProperties(CartoonProperties.class)
public class CartoonCatAndMouse {
    private Cat cat;
    private Mouse mouse;

    private CartoonProperties cartoonProperties;

    public CartoonCatAndMouse(CartoonProperties cartoonProperties){
        this.cartoonProperties = cartoonProperties;
        cat = new Cat();
        cat.setName(cartoonProperties.getCat()!=null && StringUtils.hasText(cartoonProperties.getCat().getName()) ? cartoonProperties.getCat().getName() : "tom");
        cat.setAge(cartoonProperties.getCat()!=null && cartoonProperties.getCat().getAge()!=null ? cartoonProperties.getCat().getAge() : 3);
        mouse = new Mouse();
        mouse.setName(cartoonProperties.getMouse()!=null && StringUtils.hasText(cartoonProperties.getMouse().getName()) ? cartoonProperties.getMouse().getName() : "jerry");
        mouse.setAge(cartoonProperties.getMouse()!=null && cartoonProperties.getMouse().getAge()!=null ? cartoonProperties.getMouse().getAge() : 4);

    }

    public void play(){
        System.out.println(cat.getAge()+"岁的"+cat.getName()+"和"+ mouse.getAge()+"岁的"+mouse.getName()+"打起来了");
    }
}