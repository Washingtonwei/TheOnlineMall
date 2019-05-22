package com.taotao.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrJTest {
	@Test
	public void addDocument() throws SolrServerException, IOException {
		// create a connection
		SolrServer solrServer = new HttpSolrServer("http://10.0.61.26:8080/solr");
		// create a document object
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		solrInputDocument.addField("id", "test001");
		solrInputDocument.addField("item_title", "product1");
		solrInputDocument.addField("item_price", 5689);
		// write this object to index db
		solrServer.add(solrInputDocument);
		solrServer.commit();
	}

	@Test
	public void deleteDocument() throws Exception {
		// 创建一连接
		SolrServer solrServer = new HttpSolrServer("http://10.0.61.26:8080/solr");
		// solrServer.deleteById("test001");
		solrServer.deleteByQuery("*:*");
		solrServer.commit();
	}

	@Test
	public void queryDocument() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://10.0.61.26:8080/solr");
		// Create a query object
		SolrQuery query = new SolrQuery();
		// Set query
		query.setQuery("*:*");
		query.setStart(20);
		query.setRows(50);
		// Execute query
		QueryResponse response = solrServer.query(query);
		// Get results
		SolrDocumentList solrDocumentList = response.getResults();
		System.out.println("Query results: " + solrDocumentList.getNumFound());
		// Every element is a SolrDocument
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));

		}
	}

}
