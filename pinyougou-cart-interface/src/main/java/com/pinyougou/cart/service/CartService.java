package com.pinyougou.cart.service;

import com.pinyougou.pojo.group.Cart;

import java.util.List;

public interface CartService {

    public List<Cart>addGoodsToCartList(List<Cart>cartList,Long itemId,Integer num);

}
