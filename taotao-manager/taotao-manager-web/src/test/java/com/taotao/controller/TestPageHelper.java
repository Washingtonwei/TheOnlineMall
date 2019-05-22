package com.taotao.controller;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

public class TestPageHelper {

	@Test
	public void testPageHelper() {
		// create a Spring container
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-*.xml");
		// get Mapper's object
		TbItemMapper mapper = applicationContext.getBean(TbItemMapper.class);
		// execute select and page
		TbItemExample example = new TbItemExample();
		PageHelper.startPage(2, 10);

		List<TbItem> list = mapper.selectByExample(example);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			TbItem tbItem = (TbItem) iterator.next();
			System.out.println(tbItem.getTitle());
		}
		// get paging info
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		System.out.println("There are total: " + total);

	}
}
