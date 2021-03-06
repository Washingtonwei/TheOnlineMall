package com.taotao.portal.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.pojo.ItemInfo;

@Service
public class CartServiceImpl implements CartService {
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;

	@Value("${ITEM_INFO_URL}")
	private String ITEM_INFO_URL;

	@Override
	public TaotaoResult addCartItem(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {
		// Before we use HttpClientUtil, check the current cart to see if it
		// already exists
		// Get list of cartItems.

		CartItem cartItem = null;
		List<CartItem> cartItemList = getCartItemList(request);

		// search if the list has an item with itemId
		for (CartItem item : cartItemList) {
			if (item.getId() == itemId) {
				item.setNum(item.getNum() + num);// set a new number
				cartItem = item;
				break;
			}
		}
		if (cartItem == null) {// first time add to cart
			cartItem = new CartItem();
			String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, ItemInfo.class);
			if (taotaoResult.getStatus() == 200) {
				ItemInfo item = (ItemInfo) taotaoResult.getData();
				cartItem.setId(item.getId());
				cartItem.setImage(item.getImage() == null ? "" : item.getImages()[0]);
				cartItem.setTitle(item.getTitle());
				cartItem.setNum(num);
				cartItem.setPrice(item.getPrice());
			}
			cartItemList.add(cartItem);
		}
		// Write this list to cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartItemList), true);

		return TaotaoResult.ok();

	}

	// get cart items from cookies
	private List<CartItem> getCartItemList(HttpServletRequest request) {
		String cartJson = CookieUtils.getCookieValue(request, "TT_CART", true);
		if (cartJson == null) {
			return new ArrayList<>();
		}

		// 把json转换成商品列表
		try {
			List<CartItem> list = JsonUtils.jsonToList(cartJson, CartItem.class);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();

	}

	@Override
	public List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response) {
		List<CartItem> itemList = getCartItemList(request);
		return itemList;
	}

	@Override
	public TaotaoResult deleteCartItem(long itemId, HttpServletRequest request, HttpServletResponse response) {

		// 从cookie中取购物车商品列表
		List<CartItem> itemList = getCartItemList(request);
		// 从列表中找到此商品
		for (CartItem cartItem : itemList) {
			if (cartItem.getId() == itemId) {
				itemList.remove(cartItem);
				break;
			}

		}
		// 把购物车列表重新写入cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(itemList), true);

		return TaotaoResult.ok();

	}

}
