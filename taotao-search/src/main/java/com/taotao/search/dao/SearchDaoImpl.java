package com.taotao.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.border.TitledBorder;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.common.pojo.Item;
import com.taotao.common.pojo.SearchResult;

@Repository
public class SearchDaoImpl implements SearchDao {

	@Autowired
	SolrServer solrServer;

	@Override
	public SearchResult search(SolrQuery query) throws Exception {

		SearchResult searchResult = new SearchResult();

		// search based on query
		QueryResponse queryResponse = solrServer.query(query);
		// get result
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		// get total num found
		searchResult.setRecordCount(solrDocumentList.getNumFound());
		// get item list
		List<Item> itemList = new ArrayList<>();
		// get highlight from queryResponse
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		for (SolrDocument solrDocument : solrDocumentList) {
			Item item = new Item();
			item.setId((String) solrDocument.get("id"));

			// highlighting
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");

			String title = "";
			// tell if the list is empty
			if (list != null && list.size() > 0) {
				title = list.get(0);// highlighting info
			} else {// if the title doesn't contain search keyword
				title = (String) solrDocument.get("item_title");
			}
			item.setTitle(title);
			
			item.setImage((String) solrDocument.get("item_image"));
			item.setPrice((long) solrDocument.get("item_price"));
			item.setSell_point((String) solrDocument.get("item_sell_point"));
			item.setCategory_name((String) solrDocument.get("item_category_name"));
			// add to item list
			itemList.add(item);
		}

		searchResult.setItemList(itemList);

		return searchResult;
	}

}
