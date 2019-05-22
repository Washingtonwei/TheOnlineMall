package com.taotao.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbContent;

@Service
public class ContentServiceImpl implements ContentService {

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;

	@Value("${REST_INDEX_AD_URL}")
	private String REST_INDEX_AD_URL;

	@Override
	public String getContentList() {
		String result = HttpClientUtil.doGet(REST_BASE_URL + REST_INDEX_AD_URL);
		// convert the string result to TaotaoResult
		try {
			TaotaoResult taotaoResult = TaotaoResult.formatToList(result, TbContent.class);
			// get content list
			List<TbContent> list = (List<TbContent>) taotaoResult.getData();
			// create a pojo list required by jsp page, or we can use map if you
			// don't want to create pojo
			List<Map> resultList = new ArrayList<>();
			for (TbContent tbContent : list) {
				Map map = new HashMap<>();
				map.put("src", tbContent.getPic());
				map.put("height", 240);
				map.put("width", 670);
				map.put("srcB", tbContent.getPic2());
				map.put("heightB", 240);
				map.put("widthB", 550);
				map.put("href", tbContent.getUrl());
				map.put("alt", tbContent.getSubTitle());
				resultList.add(map);
			}
			return JsonUtils.objectToJson(resultList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
