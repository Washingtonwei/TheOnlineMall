package com.taotao.rest.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import com.taotao.rest.dao.JedisClient;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;

	@Value("${REDIS_ITEM_EXPIRE}")
	private Integer REDIS_ITEM_EXPIRE;

	@Override
	public TaotaoResult getItemBaseInfo(long itemId) {

		try {
			// try to get product info based on itemId from redis
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + "base");
			// check if we can get it from redis
			if (!StringUtils.isBlank(json)) {
				// convert json to pojo and return TaotaoResult
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return TaotaoResult.ok(tbItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// if there is no hit in redis, search based on itemId using mapper from
		// DB
		TbItem item = itemMapper.selectByPrimaryKey(itemId);

		try {
			// write info to redis, also set expire time
			jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + "base", JsonUtils.objectToJson(item));
			// set expire period
			jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + "base", REDIS_ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// encapsulate using TaotaoResult
		return TaotaoResult.ok(item);
	}

	@Override
	public TaotaoResult getItemDesc(long itemId) {
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + "desc");
			if (!StringUtils.isBlank(json)) {
				// convert json to pojo and return TaotaoResult
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return TaotaoResult.ok(tbItemDesc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);

		try {
			jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + "desc", JsonUtils.objectToJson(itemDesc));
			jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + "desc", REDIS_ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok(itemDesc);
	}

	@Override
	public TaotaoResult getItemParam(long itemId) {
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + "param");
			if (!StringUtils.isBlank(json)) {
				TbItemParamItem itemParamItem = JsonUtils.jsonToPojo(json, TbItemParamItem.class);
				return TaotaoResult.ok(itemParamItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemParamItemExample example = new TbItemParamItemExample();

		Criteria createCriteria = example.createCriteria();

		List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			TbItemParamItem tbItemParamItem = list.get(0);
			try {
				jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + "param", JsonUtils.objectToJson(tbItemParamItem));
				jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + "param", REDIS_ITEM_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return TaotaoResult.ok(tbItemParamItem);
		}
		return TaotaoResult.build(400, "No such product params, sorry!");

	}

}
