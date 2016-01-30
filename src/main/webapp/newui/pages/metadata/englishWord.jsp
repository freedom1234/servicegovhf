<%--
  Created by IntelliJ IDEA.
  User: vincentfxz
  Date: 15/6/23
  Time: 17:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String ctx = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<%@ include file="/ui/pages/common/common.jsp" %>
<body class="skin-blue sidebar-mini">
<div class="wrapper">
    <%@ include file="/ui/pages/common/header.jsp" %>
    <!-- Left side column. contains the logo and sidebar -->
    <%@ include file="/ui/pages/common/aside.jsp" %>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">

        <section class="content-header">
            <h1>
                元数据管理
                <small>英文单词与缩写管理</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i>元数据管理</a></li>
                <li><a href="#">英文单词及缩写管理</a></li>
            </ol>

        </section>

        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body">
                            <table id="englishWordTable" class="table table-bordered table-striped">
                                <tfoot>
                                <tr>
                                    <th>Rendering engine</th>
                                    <th>Browser</th>
                                    <th>Platform(s)</th>
                                    <th>Engine version</th>
                                    <th>CSS grade</th>
                                </tr>
                                </tfoot>
                            </table>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </div><!-- /.col -->
            </div><!-- /.row -->
        </section>
    </div>
    <!-- /.content-wrapper -->
    <%@ include file="/ui/pages/common/footer.jsp" %>
    <!-- Control Sidebar -->
    <%@ include file="/ui/pages/common/controlSideBar.jsp" %>
    <!-- Add the sidebar's background. This div must be placed
         immediately after the control sidebar -->
</div>
<!-- ./wrapper -->
</body>

<script src="/ui/dist/js/common/dataTableCommon.js"></script>
<script src="/ui/dist/js/metadata/englishWordLayout.js"></script>
<script src="/ui/dist/js/metadata/englishWord.js"></script>


</html>

