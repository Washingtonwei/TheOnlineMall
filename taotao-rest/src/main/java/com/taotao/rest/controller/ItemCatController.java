package com.taotao.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.utils.JsonUtils;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;

@Controller
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;

	@RequestMapping(value = "/itemcat/list", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	@ResponseBody
	public String getItemCatList(String callBack) {
		CatResult catResult = itemCatService.getItemCatResult();
		// convert pojo to Json string and add call back function
		String json = JsonUtils.objectToJson(catResult);
		String result = callBack + "(" + json + ");";
		return result;

	}
}
