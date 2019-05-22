package com.taotao.rest.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.rest.dao.JedisClient;

@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${INDEX_CONTENT_REDIS_KEY}")
	private String INDEX_CONTENT_REDIS_KEY;

	@Override
	public List<TbContent> getContentList(long contentCid) {
		// get stuff from Redis
		try {
			String result = jedisClient.hget(INDEX_CONTENT_REDIS_KEY, contentCid + "");
			if (!StringUtils.isBlank(result)) {
				// convert string to list
				List<TbContent> resultList = JsonUtils.jsonToList(result, TbContent.class);
				return resultList;
			}
			// if it is blank, not a hit, go to DB
		} catch (Exception e) {
			e.printStackTrace();
		}

		// select category list based on contentCid
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(contentCid);

		// execute search
		List<TbContent> resultList = contentMapper.selectByExample(example);

		// insert (save) to Redis
		try {
			String cacheString = JsonUtils.objectToJson(resultList);// convert
																	// list to
																	// string

			jedisClient.hset("INDEX_CONTENT_REDIS_KEY", contentCid + "", cacheString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

}
