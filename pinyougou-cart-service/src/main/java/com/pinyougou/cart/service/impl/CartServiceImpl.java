package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.group.Cart;
import com.sun.jdi.LongValue;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private TbItemMapper itemMapper;
    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {

        //1.根据skuId得到商品对象
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if(item==null){
            throw new RuntimeException("商品不存在");
        }
        if(!item.getStatus().equals("1")){
            throw new RuntimeException("商品状态不合法");
        }
        //2.根据商品对象得到商家id
        String sellerId = item.getSellerId();
        //3.根据商家ID判断购物车列表中是否存在该商家的商品

        Cart cart = searchCartBySellerId(cartList, sellerId);


        if(cart==null){//4.如果购物车列表中不存在该商家的商品
            //4.1.构建新的购物车对象
            cart=new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(item.getSeller());
            List <TbOrderItem> orderItemList=new ArrayList<>();//创建购物车明细列表

            TbOrderItem orderItem = createOrderItem(item, num);
            orderItemList.add(orderItem);

            cart.setOrderItemList(orderItemList);
            //4.2.将购物车对象放入购物车列表中
            cartList.add(cart);


        }
        else {  //如果购物车列表中存在该商家的商品
            //判断该购物车对象是否存在该商品
            TbOrderItem orderItem = searchOrderByItemId(cart.getOrderItemList(), itemId);

            if(orderItem!=null){ //如果存在
                //更改商品数量，更改商品价格
                orderItem.setNum(orderItem.getNum()+num);
                orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum()));
                if(orderItem.getNum()<=0){
                    //当明细数量小于等于0时，移除该商品
                    cart.getOrderItemList().remove(orderItem);
                }
                if(cart.getOrderItemList().size()==0){
                    //当明细列表为0时，移除该购物车对象
                    cartList.remove(cart);
                }

            }else {//如果不存在

                //添加新的商品明细列表
                orderItem = createOrderItem(item, num);
                cart.getOrderItemList().add(orderItem);

            }


        }



        return null;
    }

    /**
     * 根据skuId查询明细列表中是否有该商品
     * @param orderItemList
     * @param itemId
     * @return
     */
    private TbOrderItem searchOrderByItemId(List<TbOrderItem>orderItemList,Long itemId){
        for (TbOrderItem orderItem : orderItemList) {
            if(orderItem.getItemId().longValue()==itemId.longValue()){
                return orderItem;
            }
        }
        return null;
    }

    /**
     * 添加商品到购物车列表中
     * @param cartList
     * @return
     */

    private Cart searchCartBySellerId(List<Cart> cartList,String sellerId){
        System.out.println(cartList);
            for (Cart cart : cartList) {
                System.out.println(cart.getSellerId());
                if (cart.getSellerId().equals(sellerId)) {

                    return cart;
                }
        }
        return null;
    }

    /**
     * 创建订单明细
     * @param item
     * @param num
     * @return
     */
    private TbOrderItem createOrderItem(TbItem item,Integer num){
        TbOrderItem orderItem=new TbOrderItem();
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setNum(num);
        orderItem.setItemId(item.getId());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setPicPath(item.getImage());
        orderItem.setTitle(item.getTitle());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setPrice(item.getPrice());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*num));
       return orderItem;

    }
}
