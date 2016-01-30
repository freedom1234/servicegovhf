<%--
  Created by IntelliJ IDEA.
  User: vincentfxz
  Date: 15/7/2
  Time: 14:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
  <meta charset="UTF-8">
  <title>服务治理平台 | 登录</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
  <!-- Bootstrap 3.3.4 -->
  <link href="../../newui/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
  <!-- Font Awesome Icons -->
  <!-- Theme style -->
  <link href="../../newui/dist/css/AdminLTE.min.css" rel="stylesheet" type="text/css" />
  <!-- iCheck -->
  <link href="../../newui/plugins/iCheck/square/blue.css" rel="stylesheet" type="text/css" />

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <![endif]-->
  <script type="text/javascript">
            var errMsg = '${errMsg}';
            if(errMsg != null && errMsg!= ""){
              alert(errMsg);
            }
            var topFlag = '${topFlag}';
            if(topFlag){
                top.location.href="/login/?topFlag=true" ;
            }


  </script>
</head>
<body class="login-page">
<div class="login-box">
  <div class="login-logo">
    <%--<a href="../../newui/index2.html"><b>服务治理平台</b>SG</a>--%>
      <b>服务治理平台</b>SG
  </div><!-- /.login-logo -->
  <div class="login-box-body">
    <p class="login-box-msg">登入</p>
    <form action="/login/" method="post">
      <div class="form-group has-feedback">
        <input name="username" type="user" class="form-control" placeholder="用户名"/>
        <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
      </div>
      <div class="form-group has-feedback">
        <input name="password" type="password" class="form-control" placeholder="密码"/>
        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
      </div>
      <div class="row">
        <div class="col-xs-4" style="width:100%">
          <button type="submit" class="btn btn-primary btn-block btn-flat">登 录</button>
        </div><!-- /.col -->
      </div>
    </form>

    <div class="social-auth-links text-center">
      <!--
      <p>- OR -</p>
      <!--
      <a href="#" class="btn btn-block btn-social  btn-flat"><i class="fa"></i> 游客登录 </a>
      -->
    </div><!-- /.social-auth-links -->

  </div><!-- /.login-box-body -->
</div><!-- /.login-box -->

<!-- jQuery 2.1.4 -->
<script src="../../newui/plugins/jQuery/jQuery-2.1.4.min.js"></script>
<!-- Bootstrap 3.3.2 JS -->
<script src="../../newui/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<!-- iCheck -->
<script src="../../newui/plugins/iCheck/icheck.min.js" type="text/javascript"></script>
<script>
  $(function () {
    $('input').iCheck({
      checkboxClass: 'icheckbox_square-blue',
      radioClass: 'iradio_square-blue',
      increaseArea: '20%' // optional
    });
  });
</script>
</body>
</html>
