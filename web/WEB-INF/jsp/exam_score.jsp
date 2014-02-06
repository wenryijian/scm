<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="id_tbl_exam_score_toolbar" style="padding-top:10px;padding-left:10px">
	<form id="query_form" method="post">
		<!-- 其他过滤 -->
		<a href="javascript:down_current_exam_score();" class="easyui-linkbutton" iconCls="icon-filesave">下载当前排名表</a>
	</form>
</div>

<div id="id_tbl_exam_score">
加载中...
</div>


<script type="text/javascript">
	$(function(){
		$("#id_tbl_exam_score").datagrid({
			url: '${ROOT}/scm/getExamScoreDetailPage.htm?examid=' + "${EXAMID}",
			columns:[[
				{field:'rank', title: '排名', width:35, align:'center'},
				{field:'class', title: '班级', width:35, align:'center'},
				{field:'name', title: '姓名', width:65, align:'center'},
				{field:'seat', title: '座位号', width:35},
				<c:forEach var="obj" items="${CURRENT_EXAM_SUBJECTS}">
					{field: '${obj.subject}', title: '${obj.subject}', width:55},
				</c:forEach>
				{field:'all_score', title: '总分', width:45},
			]],
			singleSelect:true,
			fitColumns:true,
			pagination:true,
			pageSize:20,
			pageList:[20,50,100],
			toolbar: '#id_tbl_exam_score_toolbar'
		});
	});

	function down_current_exam_score(){
		var url = "${ROOT}/scm/downCurrentSortedScore.htm?examid=${EXAMID}";
		window.open(url);
	}
</script>