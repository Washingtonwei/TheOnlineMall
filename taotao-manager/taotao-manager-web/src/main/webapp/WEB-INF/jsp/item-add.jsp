<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link href="/js/kindeditor-4.1.11/themes/default/default.css"
	type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8"
	src="/js/kindeditor-4.1.11/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8"
	src="/js/kindeditor-4.1.11/lang/en.js"></script>
<div style="padding: 10px 10px 10px 10px">
	<form id="itemAddForm" class="itemForm" method="post">
		<table cellpadding="5">
			<tr>
				<td>Product Category:</td>
				<td><a href="javascript:void(0)"
					class="easyui-linkbutton selectItemCat">Select category</a> <input
					type="hidden" name="cid" style="width: 280px;"></input></td>
			</tr>
			<tr>
				<td>Product title:</td>
				<td><input class="easyui-textbox" type="text" name="title"
					data-options="required:true" style="width: 280px;"></input></td>
			</tr>
			<tr>
				<td>Product selling point:</td>
				<td><input class="easyui-textbox" name="sellPoint"
					data-options="multiline:true,validType:'length[0,150]'"
					style="height: 60px; width: 280px;"></input></td>
			</tr>
			<tr>
				<td>Product price:</td>
				<td><input class="easyui-numberbox" type="text"
					name="priceView"
					data-options="min:1,max:99999999,precision:2,required:true" /> <input
					type="hidden" name="price" /></td>
			</tr>
			<tr>
				<td>Product stock:</td>
				<td><input class="easyui-numberbox" type="text" name="num"
					data-options="min:1,max:99999999,precision:0,required:true" /></td>
			</tr>
			<tr>
				<td>Bar code:</td>
				<td><input class="easyui-textbox" type="text" name="barcode"
					data-options="validType:'length[1,30]'" /></td>
			</tr>
			<tr>
				<td>Product image:</td>
				<td><a href="javascript:void(0)"
					class="easyui-linkbutton picFileUpload">Upload pics</a> <input
					type="hidden" name="image" /></td>
			</tr>
			<tr>
				<td>Product description:</td>
				<td><textarea
						style="width: 800px; height: 300px; visibility: hidden;"
						name="desc"></textarea></td>
			</tr>
			<tr class="params hide">
				<td>Product params:</td>
				<td></td>
			</tr>
		</table>
		<input type="hidden" name="itemParams" />
	</form>
	<div style="padding: 5px">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			onclick="submitForm()">Submit</a> <a href="javascript:void(0)"
			class="easyui-linkbutton" onclick="clearForm()">Reset</a>
	</div>
</div>
<script type="text/javascript">
	var itemAddEditor;
	//页面初始化完毕后执行此方法
	$(function() {
		//创建富文本编辑器
		itemAddEditor = TAOTAO.createEditor("#itemAddForm [name=desc]");
		//初始化类目选择和图片上传器
		TAOTAO.init({
			fun : function(node) {
				//根据商品的分类id取商品 的规格模板，生成规格信息。第四天内容。
				TAOTAO.changeItemParam(node, "itemAddForm");
			}
		});
	});
	//提交表单
	function submitForm() {
		//有效性验证
		if (!$('#itemAddForm').form('validate')) {
			$.messager.alert('Reminder','Form is not finished yet!');
			return;
		}
		//取商品价格，单位为“分”
		$("#itemAddForm [name=price]").val(
				eval($("#itemAddForm [name=priceView]").val()) * 100);
		//同步文本框中的商品描述
		itemAddEditor.sync();
		//取商品的规格

		var paramJson = [];
		$("#itemAddForm .params li").each(function(i, e) {
			var trs = $(e).find("tr");
			var group = trs.eq(0).text();
			var ps = [];
			for (var i = 1; i < trs.length; i++) {
				var tr = trs.eq(i);
				ps.push({
					"k" : $.trim(tr.find("td").eq(0).find("span").text()),
					"v" : $.trim(tr.find("input").val())
				});
			}
			paramJson.push({
				"group" : group,
				"params" : ps
			});
		});
		//把json对象转换成字符串
		paramJson = JSON.stringify(paramJson);
		$("#itemAddForm [name=itemParams]").val(paramJson);

		//ajax的post方式提交表单
		//$("#itemAddForm").serialize()将表单序列号为key-value形式的字符串
		$.post("/item/save", $("#itemAddForm").serialize(), function(data) {
			if (data.status == 200) {
				$.messager.alert('Reminder','New product added!');
			}
		});
	}

	function clearForm() {
		$('#itemAddForm').form('reset');
		itemAddEditor.html('');
	}
</script>
