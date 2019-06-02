package com.pinyougou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;
@Component
public class ItemSearchListener implements MessageListener {
    @Autowired
    private ItemSearchService itemSearchService;
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage=(TextMessage)message;

        try {
            System.out.println("监听接收到消息...");
            String text = textMessage.getText();
            List<TbItem> items = JSON.parseArray(text, TbItem.class);
            itemSearchService.importList(items);
            System.out.println("成功导入到索引库");
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
