package com.taotao.search.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.search.mapper.ItemMapper;
import com.taotao.common.pojo.Item;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	ItemMapper itemMapper;

	@Autowired
	SolrServer solrServer;

	@Override
	public TaotaoResult importAllItems() {
		List<Item> list = itemMapper.getItemList();
		// write list to solr
		for (Item item : list) {
			// create a SolrInputDocument object
			SolrInputDocument document = new SolrInputDocument();
			document.setField("id", item.getId());
			document.setField("item_title", item.getTitle());
			document.setField("item_sell_point", item.getSell_point());
			document.setField("item_price", item.getPrice());
			document.setField("item_image", item.getImage());
			document.setField("item_category_name", item.getCategory_name());
			document.setField("item_desc", item.getItem_des());
			// write to solr using solr server
			try {
				solrServer.add(document);
				// commit
				solrServer.commit();
			} catch (Exception e) {
				e.printStackTrace();
				return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));

			}
		}

		return TaotaoResult.ok();
	}

}
