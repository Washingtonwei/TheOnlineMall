package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.pojo.TbItemParamExample.Criteria;
import com.taotao.service.ItemParamService;

@Service
public class ItemParamServiceImpl implements ItemParamService {

	@Autowired
	TbItemParamMapper itemParamMapper;

	@Override
	public TaotaoResult getItemParamByCid(long cid) {
		TbItemParamExample example = new TbItemParamExample();
		Criteria critiera = example.createCriteria();
		critiera.andItemCatIdEqualTo(cid);
		// List<TbItemParam> list = itemParamMapper.selectByExample(example);
		List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);
		// tell if we get sth from DB
		if (list != null && list.size() > 0) {
			return TaotaoResult.ok(list.get(0));
		}
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult insertItemParam(TbItemParam itemParam) {
		// complete pojo
		itemParam.setCreated(new Date());
		itemParam.setUpdated(new Date());
		itemParamMapper.insert(itemParam);
		return TaotaoResult.ok();
	}

}
