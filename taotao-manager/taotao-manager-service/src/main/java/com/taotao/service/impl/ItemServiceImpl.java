package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;

	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;

	@Override
	public TbItem getItemById(long itemId) {
		// return itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		// add select criteria
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);

		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			TbItem item = list.get(0);
			return item;
		}
		return null;
	}

	@Override
	public EUDataGridResult getItemList(int page, int rows) {
		// 查询商品列表
		TbItemExample example = new TbItemExample();
		// 分页处理
		PageHelper.startPage(page, rows);
		List<TbItem> list = itemMapper.selectByExample(example);
		// 创建一个返回值对象
		EUDataGridResult result = new EUDataGridResult();
		result.setRows(list);
		// 取记录总条数
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		return result;

	}

	@Override
	public TaotaoResult createItem(TbItem item, String desc, String itemParams) throws Exception {
		// item completion
		// create id for the item
		Long itemId = IDUtils.genItemId();
		item.setId(itemId);
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// save in DB using mapper
		itemMapper.insert(item);
		// add item's description
		TaotaoResult result = insertItemDesc(itemId, desc);
		if (result.getStatus() != 200) {
			throw new Exception();
		}
		// add params of this item
		result = insertItemParamItem(itemId, itemParams);
		if (result.getStatus() != 200) {
			throw new Exception();
		}
		return TaotaoResult.ok();

	}

	private TaotaoResult insertItemDesc(Long itemId, String desc) {
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDescMapper.insert(itemDesc);
		return TaotaoResult.ok();
	}

	private TaotaoResult insertItemParamItem(Long itemId, String itemParams) {
		TbItemParamItem itemParamItem = new TbItemParamItem();
		itemParamItem.setCreated(new Date());
		itemParamItem.setUpdated(new Date());
		itemParamItem.setItemId(itemId);
		itemParamItem.setParamData(itemParams);

		itemParamItemMapper.insert(itemParamItem);
		return TaotaoResult.ok();
	}

}
