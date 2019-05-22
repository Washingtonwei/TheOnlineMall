package com.taotao.search.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.search.service.SearchService;

@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult search(@RequestParam("q") String queryString, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "60") Integer rows) {
		// query cannot be empty
		if (StringUtils.isBlank(queryString)) {
			return TaotaoResult.build(400, "Query cannot be empty!!! :(");
		}

		SearchResult searchResult = null;
		try {
			queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
			searchResult = searchService.search(queryString, page, rows);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return TaotaoResult.ok(searchResult);

	}

}
