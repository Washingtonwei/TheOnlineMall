import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;

public class TestTaotaoResult {
	@Test
	public void testTaotaoResult(){
		Map<String, String> param = new HashMap<>();
		param.put("q", "iPhone 8");
		param.put("page", "1");
	
		//调用服务
		String json = HttpClientUtil.doGet("http://localhost:8083/search/query", param);
		System.out.println(json);
		//把字符串转换成java对象
		TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, SearchResult.class);
	}
}
