package com.taotao.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public CatResult getItemCatResult() {
		CatResult catResult = new CatResult();
		catResult.setData(getCatList(0));
		return catResult;
	}

	private List<?> getCatList(long parentId) {
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = itemCatMapper.selectByExample(example);

		List resultList = new ArrayList<>();
		for (TbItemCat tbItemCat : list) {
			if (tbItemCat.getIsParent()) {// if it is parent node
				CatNode catNode = new CatNode();
				if (parentId == 0) {// if this is first layer
					catNode.setName(
							"<a href='/products/" + tbItemCat.getId() + ".html'>" + tbItemCat.getName() + "</a>");
				} else {
					catNode.setName(tbItemCat.getName());
				}
				catNode.setUrl("/products/" + tbItemCat.getId() + ".html");
				catNode.setItem(getCatList(tbItemCat.getId()));
				resultList.add(catNode);
			} else {// if it is leaf node
				resultList.add("/products/" + tbItemCat.getId() + ".html|" + tbItemCat.getName());
			}
		}
		return resultList;

	}

}
