package com.taotao.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.service.ContentService;

@Controller
public class IndexController {

	@Autowired
	ContentService contentService;

	@RequestMapping("/index")
	public String showIndex(Model model) {
		String adJson = contentService.getContentList();
		// the ad1 must match the jsp page: var data = ${ad1};
		model.addAttribute("ad1", adJson);

		return "index";
	}

	// @RequestMapping("/index")
	// public String showIndex() {
	// return "index";
	// }

	@RequestMapping(value = "/httpclient/post", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult testPost(String username, String password) {
		String result = "username: " + username + "\tpassword:" + password;
		System.out.println(result);
		return TaotaoResult.ok();
	}
}
