package com.taotao.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
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
		//创建一连接
		SolrServer solrServer = new HttpSolrServer("http://10.0.61.26:8080/solr");
		//solrServer.deleteById("test001");
		solrServer.deleteByQuery("*:*");
		solrServer.commit();
	}

}
