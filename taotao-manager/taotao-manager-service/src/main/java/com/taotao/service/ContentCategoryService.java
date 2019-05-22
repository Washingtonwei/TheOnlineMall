package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;

public interface ContentCategoryService {
	public List<EUTreeNode> getCategoryList(long parentId);
	public TaotaoResult insertContentCategory(long parentId, String name);
}
