<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <ul id="contentCategory" class="easyui-tree">
    </ul>
</div>
<!--right click menu  -->
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">Add</div>
    <div data-options="iconCls:'icon-remove',name:'rename'">Rename</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">Delete</div>
</div>
<!-- when the page is initialized, the function will be called -->
<script type="text/javascript">
$(function(){
	$("#contentCategory").tree({
		url : '/content/category/list',
		animate: true,
		method : "GET",
		onContextMenu: function(e,node){
            e.preventDefault();
            $(this).tree('select',node.target);
            $('#contentCategoryMenu').menu('show',{
                left: e.pageX,
                top: e.pageY
            });
        },
        onAfterEdit : function(node){
        	var _tree = $(this);
        	if(node.id == 0){
        		// create a new node
        		$.post("/content/category/create",{parentId:node.parentId,name:node.text},function(data){
        			if(data.status == 200){//success! data is TaotaoResult pojo.
        				_tree.tree("update",{
            				target : node.target,
            				id : data.data.id//update id
            			});
        			}else{
        				$.messager.alert('Reminder','Create'+node.text+' category failure!');
        			}
        		});
        	}else{
        		// rename
        		$.post("/content/category/update",{id:node.id,name:node.text});
        	}
        }
	});
});
function menuHandler(item){
	var tree = $("#contentCategory");
	var node = tree.tree("getSelected");
	if(item.name === "add"){
		tree.tree('append', {
            parent: (node?node.target:null),
            data: [{
                text: 'New Category',
                id : 0,
                parentId : node.id
            }]
        }); 
		var _node = tree.tree('find',0);
		tree.tree("select",_node.target).tree('beginEdit',_node.target);
	}else if(item.name === "rename"){
		tree.tree('beginEdit',node.target);
	}else if(item.name === "delete"){
		$.messager.confirm('Confirm','Confirm to delete '+node.text+' category?',function(r){
			if(r){
				$.post("/content/category/delete/",{parentId:node.parentId,id:node.id},function(){
					tree.tree("remove",node.target);
				});	
			}
		});
	}
}
</script>