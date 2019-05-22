package com.taotao.search.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.search.dao.SearchDao;
import com.taotao.common.pojo.SearchResult;

@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
	private SearchDao searchDao;

	@Override
	public SearchResult search(String queryString, int page, int rows) throws Exception {
		// create a new query
		SolrQuery query = new SolrQuery();
		query.setQuery(queryString);// queryString is not empty
		query.setStart((page - 1) * rows);
		query.setRows(rows);
		query.set("df", "item_keywords");
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");

		SearchResult search = searchDao.search(query);

		// fill up some fields in search object and the return

		// calculate total page num
		long recordCount = search.getRecordCount();
		long pageCount = recordCount / rows;
		if (recordCount % rows > 0) {
			pageCount++;
		}
		search.setPageCount(pageCount);
		search.setCurPage(page);

		return search;
	}

}
