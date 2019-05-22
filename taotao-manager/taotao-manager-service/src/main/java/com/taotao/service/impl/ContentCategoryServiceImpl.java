package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.service.ContentCategoryService;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EUTreeNode> getCategoryList(long parentId) {
		// search based on parentId
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// execute search
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);

		ArrayList<EUTreeNode> resultList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			// create a EUTree node
			EUTreeNode node = new EUTreeNode(tbContentCategory.getId(), tbContentCategory.getName(),
					tbContentCategory.getIsParent() ? "closed" : "open");
			resultList.add(node);
		}

		return resultList;
	}

	@Override
	public TaotaoResult insertContentCategory(long parentId, String name) {
		TbContentCategory category = new TbContentCategory();

		category.setName(name);
		category.setIsParent(false);
		category.setStatus(1);
		category.setParentId(parentId);
		category.setSortOrder(1);
		category.setCreated(new Date());
		category.setUpdated(new Date());
		// save to DB
		contentCategoryMapper.insert(category);// since we already modified
												// TbContentCategoryMapper.xml,
												// id is returned automatically
												// to this pojo
		// let's check its parent node's isParent, make sure it is true,
		// otherwise, make it true
		TbContentCategory parentCat = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parentCat.getIsParent()) {
			parentCat.setIsParent(true);
			// update this node
			contentCategoryMapper.updateByPrimaryKey(parentCat);
		}
		//don't forget to attach the pojo in taotao result
		return TaotaoResult.ok(category);
	}

}
