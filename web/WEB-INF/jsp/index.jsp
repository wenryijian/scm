<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>
<head>
	<title>学生成绩管理小工具</title>

    <link rel="stylesheet" type="text/css" href="${ROOT}/easyui/themes/bootstrap/easyui.css">
	<link rel="stylesheet" type="text/css" href="${ROOT}/easyui/themes/icon.css">
	
	<style>
		/*去掉a标签下划线*/
		a{text-decoration:none;}

		/*主页框架，左侧菜单*/
		#id_left_menu .sub_list{margin:0;padding:0}
		#id_left_menu .sub_list a{color:#000;text-decoration:none;}
		#id_left_menu .sub_list a li{list-style-type:none;padding:5px;}
		#id_left_menu .sub_list a li.cur{background-color:gray;color:#fff;font-weight:bold;}
		#id_left_menu .sub_list a li.over{font-weight:bold;}

		/*table的表格颜色*/
		table.view {border:1px solid #A8CFEB;border-collapse: collapse;margin-bottom:5px;}
        .view th {padding-left:10px;padding-right:5px;padding-top:5px;padding-bottom:5px;height:23px;width: 150px;border: 1px solid silver;background-color:#F1F6FF;}
        .view td {padding-left:10px;padding-right:5px;padding-top:5px;padding-bottom:5px;height:23px;width: 150px;border: 1px solid silver;background-color:#FAFCFF;}
	</style>
</head>

<body class="easyui-layout">
	<!-- 通栏标题 -->
	<div data-options="region:'north',split:false" style="overflow:hidden;padding:10px;height:45px;">
		<span style="padding-left:120px">
			<label style="font-size:large; font-weight:bold">小工具</label><label>&nbsp;&nbsp;v0.1</label>
		</span>
		<span style="float:right; padding-right:5px">
			当前帐号: ${user_email}&nbsp;|&nbsp;<a href = "${ROOT}/logout.htm">退出</a>
		</span>
		
	</div>

	<!-- 左侧菜单 -->
	<div data-options="region:'west',split:true" style="width:150px;padding:1px;overflow:hidden;" title="菜单">
		<div id="id_left_menu" class="easyui-accordion" data-options="fit:true,border:false,animate:false">
			<div title="主功能设置" style="padding:10px;overflow:auto;" selected="true">
				<ul class="sub_list">
					<a href="javascript:void(0);" onclick="selectFtn(this, '${ROOT}/todo_url.htm')"><li>成绩管理</li></a>
					<a href="javascript:void(0);" onclick="selectFtn(this, '${ROOT}/todo_url.htm')"><li>年级</li></a>
					<a href="javascript:void(0);" onclick="selectFtn(this, '${ROOT}/todo_url.htm')"><li>班级</li></a>
				</ul>
			</div>
			<div title="其他设置" style="padding:10px;overflow:auto;">
			</div>
		</div>
	</div>

	<!-- 中间内容 -->
	<div data-options="region:'center',split:false">
		<div id="id_tabs" class="easyui-tabs" fit="true" border="false" >
			<!-- 第一个tab -->
			<div title="成绩管理">
				<jsp:include page="student_score.jsp" flush="true"></jsp:include>
			</div>
			<!-- 其他tab -->
		</div>
	</div>

	<script type="text/javascript" src="${ROOT}/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="${ROOT}/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ROOT}/easyui/locale/easyui-lang-zh_CN.js"></script>
	
	<script type="text/javascript">
    	function selectFtn(obj, funURL) {
    		$("#content_iframe").attr('src',funURL);
    		$(".sub_list li").removeClass('cur');
    		$(obj).children("li").first().addClass('cur');
    	}
    	$(function(){
    		// 默认选中侧边栏的「成绩管理」这个tab
    		$(".sub_list li").first().addClass('cur');
    		// 侧边栏每个项目添加over效果
    		$(".sub_list a li").mouseover(function(){  
                $(this).addClass("over");  
            }).mouseout(function(){  
            	$(this).removeClass("over");  
            }); 
            
    	});

	</script>
</body>
</html>
