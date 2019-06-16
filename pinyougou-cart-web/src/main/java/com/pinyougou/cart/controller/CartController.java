package com.pinyougou.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.pojo.group.Cart;

import entity.Result;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Reference(timeout=6000)
	private CartService cartService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;
	
	@RequestMapping("/findCartList")
	public List<Cart> findCartList(){

		String cartListString = util.CookieUtil.getCookieValue(request, "cartList", "UTF-8");
		if(cartListString==null || cartListString.equals("")){
			cartListString="[]";
		}
		List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
		System.out.println(cartList_cookie);
			return cartList_cookie;

	}
	
	@RequestMapping("/addGoodsToCartList")
	public Result addGoodsToCartList(Long itemId,Integer num){

		try {
			//提取购物车
			List<Cart> cartList = findCartList();
			//调用服务方法操作购物车
			cartList = cartService.addGoodsToCartList(cartList, itemId, num);
				String cartListString = JSON.toJSONString(cartList);
				util.CookieUtil.setCookie(request, response, "cartList", cartListString, 3600*24, "UTF-8");
				System.out.println("向cookie存储购物车");
			System.out.println(cartList);
			return new Result(true, "存入购物车成功");
		     } catch (Exception e) {
		       	e.printStackTrace();
			    return new Result(false, "存入购物车失败");
		}
		
		
	}
	
	
}
