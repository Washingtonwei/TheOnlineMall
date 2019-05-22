package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public List<EUTreeNode> getCatList(long parentID) {

		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentID);
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		// convert to treenodelist
		List<EUTreeNode> resultList = new ArrayList<> ();
		for (TbItemCat tbItemCat : list) {
			EUTreeNode node = new EUTreeNode(tbItemCat.getId(), tbItemCat.getName(),
					tbItemCat.getIsParent() ? "closed" : "open");
			resultList.add(node);
		}
		return resultList;
	}

}
