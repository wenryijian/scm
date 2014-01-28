<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<div style="padding:10px">
	<fieldset>
		<legend>查询符合条件的成绩列表</legend>
		<form id="query_form" method="post">
			<label for="id_slt_Grade">年级:</label>
			<input id="id_slt_Grade" name="nm_grade" class="easyui-combobox" style="width:75px" 
				data-options="valueField:'grade',textField:'text',url:'get_grade_list.htm'">&nbsp;</input>

			<label for="id_slt_Class">年级:</label>
			<input id="id_slt_Class" name="nm_class" class="easyui-combobox" style="width:75px"
				data-options="valueField:'class',textField:'text',url:'todo_get_data.htm'">&nbsp;</input>

			<label for="id_txt_Memo">备注关键字:</label>
			<input id="id_txt_Memo" name="nm_memo" type="text" style="width:150px"/>&nbsp;

			<label for="id_slt_begin">开始时间:</label>
			<input id="id_slt_begin" name="nm_begin" type="text", class="easyui-datebox" style="width:100px"/>&nbsp;

			<label for="id_slt_end">结束时间:</label>
			<input id="id_slt_end" name="nm_end" type="text", class="easyui-datebox" style="width:100px"/>&nbsp;&nbsp;

			<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search', plain:false">查询</a>
		</form>
	</fieldset>

    <table id="id_tbl_stu_score" class="easyui-datagrid" iconcls="icon-view" 
    	fit="true" fitColumns="true" rownumbers="true" 
    	pageSize="20" pagination="true" 
    	data-options="singleSelect:true" 
    	url="${ROOT }/todo_url.htm"
    	toolbar="#id_stu_score_toolbar">
    	<thread>
    		<tr>
    			<th field="id" data-options="hidden:true"></th>
    			<th field="grade" align="center" width="100px">年级</th>
    			<th field="class" align="center" width="100px">班级</th>
    			<th field="upload_time" align="center" width="100px">上传日期</th>
    			<th field="operation" align="center" width="200px" formatter="score_operation_formatter">操作</th>
    		</tr>
    	</thread>
    </table>
   	<div id="id_stu_score_toolbar">
		<div style="margin-bottom:5px">
			<a href="javascript:showOperationHistory();" class="easyui-linkbutton" iconCls="icon-tip">模板下载</a>
			<a href="javascript:newTask();" class="easyui-linkbutton" iconCls="icon-add">上传成绩</a>
			<a href="javascript:newTask();" class="easyui-linkbutton">刷新</a>
		</div>
   	</div>
</div>