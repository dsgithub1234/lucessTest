package com.pinyougou.page.service.impl;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.*;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbItemExample.Criteria;

@Service
public class ItemPageServiceImpl implements ItemPageService {
	@Value("${pagedir}")
	private String pagedir;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Autowired
	private TbGoodsMapper goodsMapper;
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	@Autowired
	private TbItemMapper itemMapper;

	
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Override
	public boolean genItemHtml(Long goodsId) {

		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		try {
			Template template = configuration.getTemplate("item.ftl");
			//创建数据模型
			Map map=new HashMap();
			//商品主表数据
			TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
			map.put("goods",goods);
			//商品扩展表数据
			TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
			map.put("goodsDesc",goodsDesc);
			//商品分类

			String name1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
			String name2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
			String name3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
			map.put("name1",name1);
			map.put("name2",name2);
			map.put("name3",name3);
			//4.SKU 列表
			TbItemExample example=new TbItemExample();
			Criteria criteria = example.createCriteria();
			criteria.andStatusEqualTo("1");//状态为有效
			criteria.andGoodsIdEqualTo(goodsId);
			example.setOrderByClause("is_default desc");//按照状态降序，保证第一个为默认
			List<TbItem> itemList = itemMapper.selectByExample(example);
			map.put("itemList",itemList);


			Writer out=new FileWriter(pagedir+goodsId+".html");

			template.process(map,out);
			out.close();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteItemHtml(Long[] goodsIds) {
		try {
			for(Long goodsId:goodsIds){
				new File(pagedir+goodsId+".html").delete();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}


