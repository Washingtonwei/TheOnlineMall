package com.taotao.portal.service;

import com.taotao.common.pojo.SearchResult;

public interface SearchService {
	SearchResult search(String queryString, int page);
}
