package com.itheima.contrller;

import com.itheima.domain.Items;
import com.itheima.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/items")
public class ItemsContrller {

    @Autowired
    private ItemsService itemsService;

    @RequestMapping("/find")
    public String findTest(Model model){
       Items items = itemsService.findAll(1);
        model.addAttribute("item",items);
        return "itemDetail";
    }
}
