<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div style="padding:10px">
	<fieldset>
		<legend>查询符合条件的成绩列表</legend>
		<form id="query_form" method="post">
			<labe>年度:</labe>
			<select name="ss_year" class="easyui-combobox" style="width:110px">
				<option value="">所有</option>
				<c:forEach var="i" begin="0" end="10" step="1">
					<option value="${CURR_YEAR-i-1}-${CURR_YEAR-i}年">${CURR_YEAR-i-1}-${CURR_YEAR-i}年</option>
				</c:forEach>
			</select>

			<label>&nbsp;年级:</label>
			<select name="ss_grade" class="easyui-combobox" style="width:75px">
				<option value="">所有年级</option>
				<c:forEach var="i" begin="1" end="9" step="1">
					<option>${10-i}年</option>
				</c:forEach>
			</select>

			<label>&nbsp;班级:</label>
			<select name="ss_class" class="easyui-combobox" style="width:75px">
				<option value="">所有</option>
				<c:forEach var="i" begin="1" end="10" step="1">
					<option>${i}班</option>
				</c:forEach>
			</select>

			<label>&nbsp;关键字:</label>
			<input name="ss_name" type="text" style="width:150px"/>&nbsp;

			<label>&nbsp;开始时间:</label>
			<input name="ss_begin" type="text", class="easyui-datebox" style="width:100px"/>&nbsp;

			<label>-&nbsp;</label>
			<input name="ss_end" type="text", class="easyui-datebox" style="width:100px"/>&nbsp;&nbsp;

			<a href="#" class="easyui-linkbutton" iconCls="icon-search">查询</a>

		</form>
		<a href="javascript:showScoreModal();" class="easyui-linkbutton" iconCls="icon-add">新的考试</a>
		<a href="javascript:newTask();" class="easyui-linkbutton">某某统计1</a>
		<a href="javascript:newTask();" class="easyui-linkbutton">某某统计2</a>
		<a href="javascript:newTask();" class="easyui-linkbutton">某某统计3</a>
	</fieldset>

    <table id="id_tbl_stu_score" class="easyui-datagrid" style="width:auto"
    	fitColumns="true" rownumbers="true" 
    	data-options="singleSelect:true" 
    	pageSize="20" pagination="true" url="${ROOT}/getExamRecordPage.htm">
    	<thead>
    		<tr>
    			<th field="id", data-options="hidden:true"></th>
    			<th field="name", align="center" width="100px" >考试名称</th>
    			<th field="year" align="center" width="100px">年度</th>
    			<th field="grade" align="center" width="100px" data-options="formatter:function(value,row,index){if(row.grade=='all'){return '所有年级';}else{return row.grade;}}">年级</th>
    			<th field="class" align="center" width="100px" data-options="formatter:function(value,row,index){if(row.class=='all'){return '所有班级';}else{return row.grade;}}">班级</th>
    			<th field="subject" align="center" width="100px" data-options="formatter:function(value,row,index){if(row.subject=='all'){return '所有科目';}else{return row.grade;}}">科目</th>
    			<th field="timestamp" align="center" width="100px">创建时间</th>
    			<th field="operation" align="center" width="200px" formatter="score_operation_formatter">操作</th>
    		</tr>
    	</thead>
    </table>

	<div id="id_dialog_new_exam" class="easyui-dialog" title="&nbsp;&nbsp;补充考试信息"
		style="width:400px;height:300px;"
		data-options="iconCls:'icon-add',modal:true,closed:true,cache:false,
		buttons:[{
			text:'保存',
			handler:function(){ saveNewExamRecord(); },
		},{
			text:'取消',
			handler:function(){	$('#id_dialog_new_exam').dialog('close');}
		}]" >
		<form id="id_form_new_exam" style="padding-top:25px" class="fitem" method="post">
			<div>
				<label>考试名称:</label>
				<input name="exam_name" value=" " class="easyui-validatebox" required="true" missingMessage="填写本次考试的名称，或者描述信息"/>
			</div>
			<div>
				<label>年度:</label>
				<select name="exam_year" class="easyui-combobox" style="width:120px">
					<c:forEach var="i" begin="0" end="10" step="1">
						<option value="${CURR_YEAR-i-1}-${CURR_YEAR-i}年">${CURR_YEAR-i-1}-${CURR_YEAR-i}年</option>
					</c:forEach>
				</select>
			</div>
			<div>
				<label>年级:</label>
				<select name="exam_grade" class="easyui-combobox" style="width:120px">
					<option value="all">所有年级</option>
					<c:forEach var="i" begin="1" end="9" step="1">
						<option>${10-i}年</option>
					</c:forEach>
				</select>
			</div>
			<div>
				<label>班级:</label>
				<select name="exam_class" class="easyui-combobox" style="width:120px">
					<option value="all">所有班级</option>
					<c:forEach var="i" begin="1" end="10" step="1">
						<option>${i}班</option>
					</c:forEach>
				</select>
			</div>
			<div>
				<label>科目:</label>
				<select name="exam_subject" class="easyui-combobox" style="width:120px">
					<option value="all">所有科目</option>
					<c:forEach var="obj" items="${ALL_SUBJECT}">
						<option value="${obj.subject}">${obj.subject}</option>
					</c:forEach>
				</select>
			</div>
		</form>
	</div> 

    <script type="text/javascript">
    	function showScoreModal(){
			$("#id_dialog_new_exam").dialog('open');
    	}

    	function saveNewExamRecord(){
    		$('#id_form_new_exam').form('submit', {
    			url: '${ROOT}/newExamRecord.htm',
    			onSubmit: function(){
    				var name = $('#id_form_new_exam :input[name=exam_name]').val();
    				if ($.trim(name) == ''){
    					$('#id_form_new_exam :input[name=exam_name]').val('');
    					return $(this).form('validate');
    				}
    				// 打开进度条
    				$('#id_dialog_new_exam').dialog('close');
    				$.messager.progress({
    					msg:'处理中...',
    					interval: 500, //milliseconds
    				});
    			},
    			success: function(result){
    				// 关闭进度条
    				$.messager.progress('close');
    				// 检查结果
    				var result = eval('('+result+')');
    				if (result.success){
    					$('#id_tbl_stu_score').datagrid('reload');
    				}else{
    					$.messager.show({
    						title: '保存失败',
    						msg: result.msg,
    						width: 350,
    						height: 150,
    						showType: 'show'
    					});
    				}
    			}
    		});
    	}

    	function score_operation_formatter(value,row,index){
    		return '';
    	}
    </script>
</div>