/**
 * Created by vincentfxz on 15/6/23.
 */
var englishWordManager = {
    "add" : function(data, callBack) {
        $.ajax({
            "type" : "POST",
            "contentType" : "application/json; charset=utf-8",
            "url" : "/englishWord/add",
            "data" : JSON.stringify(data),
            "dataType" : "json",
            "success" : function(result) {
                callBack(result);
            }
        });
    },
    "modify" : function(data, callBack) {
        $.ajax({
            "type" : "POST",
            "contentType" : "application/json; charset=utf-8",
            "url" : "/englishWord/modify",
            "data" : JSON.stringify(data),
            "dataType" : "json",
            "success" : function(result) {
                callBack(result);
            }
        });
    },
    "deleteById" : function(id, callBack) {
        $.ajax({
            "type" : "DELETE",
            "contentType" : "application/json; charset=utf-8",
            "url" : "/englishWord/delete/" + id,
            "dataType" : "json",
            "success" : function(result) {
                callBack(result);
            }
        });
    },
    "getByChineseWord" : function(value, callBack) {
        this.getByParam("ChineseWord", value, callBack);
    },
    "getByEnglishWord" : function(value, callBack) {
        this.getByParam("EnglishWord", value, callBack);
    },
    "getByWordAb" : function(value, callBack) {
        this.getByParam("WordAb", value, callBack);
    },

    "getByParam" : function(key, value, callBack) {
        $.ajax({
            "type" : "GET",
            "contentType" : "application/json; charset=utf-8",
            "url" : "/englishWord/getBy" + key + "/" + value,
            "dataType" : "json",
            "success" : function(result) {
                callBack(result);
            }
        });
    },
    "getByParams" : function(param, callBack) {
        var url = "/englishWord/get";
        url += "/EnglishWord/" +param.englishWord;
        url += "/ChineseWord/" +param.chineseWord;
        url += "/WordAb/" +param.wordAb;
        $.ajax({
            "type" : "GET",
            "contentType" : "application/json; charset=utf-8",
            "url" : url,
            "dataType" : "json",
            "success" : function(result) {
                callBack(result);
            }
        });
    },
    "query" : function(data,callBack){
            $.ajax({
                "type" : "POST",
                "contentType" : "application/json;charset=utf-8",
                "url" : "/englishWord/query",
                "data": JSON.stringify(data),
                "dataType": "json",
                "success": function(result) {
                    callBack(result);
                }
            });
        }

};
