<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
    <script type="text/javascript">
      var msg = "${msg}";
      var url = "${url}";
      if(msg != ""){
        alert(msg);
      }
      if(url != ""){
        location.href=url;
      }
    </script>
</body>
</html>
