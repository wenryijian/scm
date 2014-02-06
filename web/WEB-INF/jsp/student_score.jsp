<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="id_tbl_exam_record_toolbar" style="padding:10px;">
	<form id="id_exam_record_query_form" method="post">
		<labe>年度:</labe>
		<select name="ss_year" class="easyui-combobox" style="width:110px">
			<option value="">所有年度</option>
			<c:forEach var="i" begin="0" end="10" step="1">
				<option value="${CURR_YEAR-i-1}-${CURR_YEAR-i}年">${CURR_YEAR-i-1}-${CURR_YEAR-i}年度</option>
			</c:forEach>
		</select>

		<label>&nbsp;年级:</label>
		<select name="ss_grade" class="easyui-combobox" style="width:75px">
			<option value="">所有年级</option>
			<c:forEach var="i" begin="1" end="9" step="1">
				<option>${10-i}年级</option>
			</c:forEach>
		</select>

		<label>&nbsp;班级:</label>
		<select name="ss_class" class="easyui-combobox" style="width:75px">
			<option value="">所有班级</option>
			<c:forEach var="i" begin="1" end="10" step="1">
				<option>${i}班</option>
			</c:forEach>
		</select>

		<label>&nbsp;关键字:</label>
		<input name="ss_name" type="text" style="width:150px"/>&nbsp;

		<!-- <label>&nbsp;开始时间:</label>
		<input name="ss_begin" type="text", class="easyui-datebox" style="width:100px"/>&nbsp;

		<label>-&nbsp;</label>
		<input name="ss_end" type="text", class="easyui-datebox" style="width:100px"/>&nbsp;&nbsp; -->

		<a href="javascript:query_exam_record();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	</form>
	<a href="javascript:open_score_template_dialog();" class="easyui-linkbutton" iconCls="icon-add">新的考试</a>
	<a href="javascript:newTask();" class="easyui-linkbutton">某某统计1</a>
</div>


<table id="id_tbl_exam_record" class="easyui-datagrid" style="width:auto"
	fitColumns="true" rownumbers="true" nowrap="false" fit="true" toolbar="#id_tbl_exam_record_toolbar"
	pageSize="20" pagination="true" url="${ROOT}/getExamRecordPage.htm">
	<thead>
		<tr>
			<th field="ck", data-options="checkbox:'true'"></th>
			<th field="id", data-options="hidden:true"></th>
			<th field="name", align="center" width="200px" >考试名称</th>
			<th field="year" align="center" width="75px">年度</th>
			<th field="grade" align="center" width="45px">年级</th>
			<th field="class" align="center" width="50px" data-options="formatter:function(value,row,index){if(row.class=='all'){return '所有班级';}else{return row.class;}}">班级</th>
			<th field="subject" align="center" width="150px">科目</th>
			<th field="timestamp" align="center" width="100px" data-options="formatter:function(value,row,index){return row.timestamp.replace(/\.0/, '');}">创建时间</th>
			<th field="operation" align="center" width="200px" formatter="score_operation_formatter">操作</th>
		</tr>
	</thead>
</table>


<div id="id_dialog_new_exam" class="easyui-dialog" title="&nbsp;&nbsp;补充考试信息"
	style="width:400px;height:300px;"
	data-options="iconCls:'icon-add',modal:true,closed:true,cache:false,
	buttons:[{
		text:'保存',
		iconCls:'icon-ok',
		handler:function(){ saveNewExamRecord(); },
	},{
		text:'取消',
		iconCls:'icon-cancel',
		handler:function(){	$('#id_dialog_new_exam').dialog('close');}
	}]" >
	<form id="id_form_new_exam" style="padding-top:25px" class="fitem" method="post">
		<div>
			<label>考试名称:</label>
			<input name="exam_name" value=" " class="easyui-validatebox" required="true" missingMessage="填写本次考试的名称，或者描述信息" onclick="javascript:$('#id_form_new_exam :input[name=exam_name]').val('');" />
		</div>
		<div>
			<label>年度:</label>
			<select name="exam_year" class="easyui-combobox" style="width:120px" data-options="editable:false">
				<c:forEach var="i" begin="0" end="10" step="1">
					<option value="${CURR_YEAR-i-1}-${CURR_YEAR-i}年">${CURR_YEAR-i-1}-${CURR_YEAR-i}年度</option>
				</c:forEach>
			</select>
		</div>
		<div>
			<label>年级:</label>
			<select name="exam_grade" class="easyui-combobox" style="width:120px" data-options="editable:false" required="true" missingMessage="选择一个年级">
				<option value=""></option>
				<c:forEach var="i" begin="1" end="9" step="1">
					<option>${10-i}年级</option>
				</c:forEach>
			</select>
		</div>
		<div>
			<label>班级:</label>
			<select name="exam_class" class="easyui-combobox" style="width:120px" data-options="editable:false">
				<option value="all">所有班级</option>
				<c:forEach var="i" begin="1" end="10" step="1">
					<option>${i}班</option>
				</c:forEach>
			</select>
		</div>
		<div>
			<label>科目:</label>
			<select name="exam_subject" id="id_exam_subject" class="easyui-combobox" style="width:120px" data-options="multiple:true,editable:false,panelHeight:'auto',
				onSelect: function(rec){
					if (rec.value == 'all'){
						$('#id_exam_subject').combobox('clear');
						$('#id_exam_subject').combobox('setValue', 'all');
					}else{
						$('#id_exam_subject').combobox('unselect', 'all');
					}
				}">
				<option value="all">所有科目</option>
				<c:forEach var="obj" items="${ALL_SUBJECT}">
					<option value="${obj.subject}">${obj.subject}</option>
				</c:forEach>
			</select>
		</div>
	</form>
</div> 


<div id="id_dialog_upload_exam" class="easyui-dialog" title="&nbsp;&nbsp;上传考试成绩"
	style="width:400px;height:200px;"
	data-options="iconCls:'icon-add',modal:true,closed:true,cache:false,
	buttons:[{
		text:'保存',
		iconCls:'icon-ok',
		handler:function(){ upload_exam_score(); },
	},{
		text:'取消',
		iconCls:'icon-cancel',
		handler:function(){	$('#id_dialog_upload_exam').dialog('close');}
	}]" >
	<form id="id_form_new_score" style="padding-top:30px" class="fitem" method="post" enctype="multipart/form-data">
		<div>
			<label style="padding-left:8%">选择excel文件:</label>
			<input type="file" name="file" />
		</div>
		<input name="id" type="hidden" />
	</form>
</div> 


<script type="text/javascript">
	function query_exam_record(){
		$("#id_tbl_exam_record").datagrid('reload', {
			ss_year: $("#id_exam_record_query_form :input[name=ss_year]").val(),
			ss_grade: $("#id_exam_record_query_form :input[name=ss_grade]").val(),
			ss_class: $("#id_exam_record_query_form :input[name=ss_class]").val(),
			ss_name: $("#id_exam_record_query_form :input[name=ss_name]").val(),
			// ss_begin: $("#id_exam_record_query_form :input[name=ss_begin]").val(),
			// ss_end: $("#id_exam_record_query_form :input[name=ss_end]").val(),
		});
	}

	function open_score_template_dialog(){
		$("#id_dialog_new_exam").dialog('open');
	}

	function saveNewExamRecord(){
		$('#id_form_new_exam').form('submit', {
			url: '${ROOT}/newExamRecord.htm',
			onSubmit: function(param){
				var name = $('#id_form_new_exam :input[name=exam_name]').val();
				if ($.trim(name) == ''){
					$('#id_form_new_exam :input[name=exam_name]').val('');
					return $(this).form('validate');
				}

				var selected_subjects = $('#id_exam_subject').combobox('getValues');
				if (selected_subjects == 'all'){
					// 获取所有的科目列表，以逗号分割
					selected_subjects = "";
					<c:forEach var="obj" items="${ALL_SUBJECT}">
						selected_subjects += "${obj.subject}" + ',';
					</c:forEach>
				}
				$('#id_exam_subject').combobox('setValue', selected_subjects);
				
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
					$('#id_tbl_exam_record').datagrid('reload');
				}else{
					$.messager.show({
						title: '保存失败',
						msg: result.errmsg,
						width: 350,
						height: 150,
						showType: 'show'
					});
				}
			}
		});
	}

	function score_operation_formatter(value,row,index){
		var html = "";
		html += "<a href='javascript:download_score_template(\" " + row.id + " \");'>下载模板</a>";
		html += "&nbsp;&nbsp;<a href='javascript:open_upload_dialog(\" " + row.id + " \");'>上传成绩表</a>";
		html += "&nbsp;&nbsp;<a href='javascript:open_exam_record(\" " + row.id + "\", \"" + index + " \");'>查看</a>";
		html += "&nbsp;&nbsp;<a href='javascript:delete_exam_record(\" " + row.id + " \");'>删除</a>";
		return html;
	}

	function download_score_template(id){
		var url = "${ROOT}/downloadScoreTemplate.htm?id=" + id;
		window.open(url);
	}

	function delete_exam_record(id){
		$.messager.confirm("删除确认", "确定要删除？", function(confirm){
			if (confirm){
				$.post('${ROOT}/deleteExamRecord.htm', {id:id}, function(result){
					if (result.success){
	    				$('#id_tbl_exam_record').datagrid('reload');
    				}else{
    					$.messager.show({
    						title: '删除失败',
    						msg: result.errmsg,
    						width: 350,
    						height: 150,
    						showType: 'show'
    					});
    				}
				}, 'json');
			}
		});
	}

	function open_upload_dialog(id){
		$("#id_form_new_score :input[name=id]").val(id);
		$("#id_dialog_upload_exam").dialog('open');
	}

	function upload_exam_score(){
		$("#id_form_new_score").form('submit', {
			url: '${ROOT}/uploadExamScore.htm',
			onSubmit: function(){
				if ($("#id_form_new_score :input[name=file]").val() == ""){
					alert("没有选择文件");
					return false;
				}
				if ($("#id_form_new_score :input[name=id]").val() == ""){
					alert("找不到id");
					return false;
				}
				// 打开进度条
				$('#id_dialog_upload_exam').dialog('close');
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
					alert("上传成功");
				}else{
					$.messager.show({
						title: '保存失败',
						msg: result.errmsg,
						width: 350,
						height: 150,
						showType: 'show'
					});
				}
			}
		});
	}

	function open_exam_record(id, index){
		var title = "查看成绩";
		if ($("#id_tabs").tabs('exists', title)){
			$("#id_tabs").tabs('close', title);
		}
		
		$("#id_tabs").tabs('add', {
			title: title,
			href: '${ROOT}/openExamScore.htm?examid='+ $.trim(id),
			closable: true,
		});
	}

</script>