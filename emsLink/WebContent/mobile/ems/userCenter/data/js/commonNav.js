if (window.location.host == "h5.m.jia.com" || navigator.userAgent.indexOf("QIJIA") > -1||window.location.host == "zixun.m.jia.com") {
    document.querySelector('.newHeader').style.display = "none";
}
var cityNames;
var cityFlag = true;
function navCommonFn() {
    var navObject = {
        title: "",
        linkArr: [],
        txtArr: []
    };
    var cityObj = new Array("suzhou", "shanghai", "tianjin", "ningbo", "beijing", "wuxi", "chongqing", "kunming", "shijiazhuang", "changsha", "wuhan", "chengdu", "nanjing", "hangzhou", "shenyang", "kunshan", "qingdao", "dalian", "shenzhen", "nanchang", "changzhou", "nantong", "xian", "jinan", "jinhua", "fuzhou", "guangzhou", "haerbin", "hefei", "huzhou", "nanning", "fuzhou", "wulumuqi", "zhengzhou", "ahsz", "sdjz", "guiyang", "handan", "dandong", "sdwf", "jstz", "taiyuan", "jslyg", "changchun", "hbts", "jsha", "zjwz", "jljl", "hbxt", "sxjc", "sdhz", "qhxn", "gxlz", "sdzz", "jsxz", "hnyy", "jsyz", "sdly", "changji", "zjjx", "hbgbd", "gsqy", "hncd", "sxyc", "hbjm", "lanzhou", "hnxc", "ahaq", "zjtz", "scmy", "hbqhd", "gddg", "hblf", "ahfy", "sdwh", "sddy", "jsyc", "jxyt", "hnny", "scms", "hnyyz", "jssy", "scnc", "scga", "sdzb", "hncz", "hbxy", "zhangjiagang", "fjfq", "other");

    if(CookiesInfo.get('LOGIN_AREAFLAG')){
        cityNames = CookiesInfo.get('LOGIN_AREAFLAG');
        commonNavHandle();
    }else if(CookiesInfo.get('AREAFLAG_IP')){
        cityNames = CookiesInfo.get('AREAFLAG_IP');
        commonNavHandle();
    }else{
        cityFlag = false;
        $.get("/get_city.php",function(data){
            CookiesInfo.set("ZXAPPLY_IP",data,{expires:.25});
            cityNames = data;
            if(data=="other"){
                cityNames = "shanghai";
            }
            CookiesInfo.set("AREAFLAG_IP",cityNames,{expires:.25});
            commonNavHandle();
        });
    }

    if(!CookiesInfo.get('ZXAPPLY_IP')&&(window.location.pathname.indexOf("/tuku/")>-1)&&cityFlag){
        $.get("/get_city.php",function(data){
            CookiesInfo.set("ZXAPPLY_IP",data,{expires:.25});
        });
    }

    function commonNavFn(channel) {
        //底部文字
        if (channel == "tuku") {
            navObject.title = "美图分类";
            navObject.linkArr = [
                "http://m.jia.com/tuku/tag/list1-1198/", "http://m.jia.com/tuku/tag/list1-1208/", "http://m.jia.com/tuku/tag/list1-1199/", "http://m.jia.com/tuku/tags/list_a/list1-a2/", "http://m.jia.com/tuku/tags/list_a/list1-a11/",
                "http://m.jia.com/tuku/tags/list_a/list1-a28/", "http://m.jia.com/tuku/tag/list1-1164/", "http://m.jia.com/tuku/tag/list1-1169/", "http://m.jia.com/tuku/tag/list1-1170/"
            ];
            navObject.txtArr = [
                "简约", "中式", "田园", "客厅", "卧室", "卫生间", "小户型", "二居", "三居"
            ];
        } else if (channel == "ask") {
            navObject.title = "问答分类";
            navObject.linkArr = [
                "http://m.jia.com/ask/lista-1/", "http://m.jia.com/ask/lista-2/", "http://m.jia.com/ask/lista-3/", "http://m.jia.com/ask/lista-67/", "http://m.jia.com/ask/lista-4/", "http://m.jia.com/ask/lista-69/", "http://m.jia.com/ask/lista-70/"
            ];
            navObject.txtArr = [
                "装修流程", "家居产品", "装修材料", "家装设计", "公装设计", "技术专区", "其他"
            ];
        }else if (channel == "cpk") {
            navObject.title = "产品分类";
            navObject.linkArr = [
                "http://m.jia.com/product/plist-2/", "http://m.jia.com/product/plist-3/", "http://m.jia.com/product/plist-4/", "http://m.jia.com/product/plist-5/", "http://m.jia.com/product/plist-6/","http://m.jia.com/product/plist-138/"
            ];
            navObject.txtArr = [
                "建材", "家具", "家电", "家饰", "家纺", "卫浴"
            ];
        }else if (channel == "baike") {
            navObject.title = "百科分类";
            navObject.linkArr = [
                "http://m.jia.com/baike/blist-1456/", "http://m.jia.com/baike/blist-1485/", "http://m.jia.com/baike/blist-1565/", "http://m.jia.com/baike/blist-1648/", "http://m.jia.com/baike/blist-1666/","http://m.jia.com/baike/blist-1767/"
            ];
            navObject.txtArr = [
                "建材", "装修", "生活", "软装", "家电", "家具"
            ];
        }else if (channel == "xunxi") {
            navObject.title = "讯息分类";
            navObject.linkArr = [
                "http://m.jia.com/baike/xlist-560/", "http://m.jia.com/baike/xlist-561/", "http://m.jia.com/baike/xlist-562/", "http://m.jia.com/baike/xlist-563/", "http://m.jia.com/baike/xlist-564/","http://m.jia.com/baike/xlist-565/"
            ];
            navObject.txtArr = [
                "设计", "生活", "装修", "品牌", "商品", "家电"
            ];
        }
        //图标
        var commonNav = '';
        commonNav += '<div class="navs-wrap headers-wrap">';
        commonNav += '<section class="sub-navs-wrap">';
        commonNav += '<a href="javascript:;" class="close pr"></a>';
        commonNav += '<ul class="common-wrap-ul clearfix ' + channel + '">';
        commonNav += '<li class="common-wrap-li"><a href="http://m.jia.com/' + cityNames + '" tjjj="wap.nav.home"><i class="icon home"></i>首页</a></li>';
        if (channel == "mall") {
            commonNav += '<li class="common-wrap-li"><a href="http://m.jia.com/mall/order/cart/" tjjj="wap.nav.cart"><i class="icon cart"></i>购物车</a></li>';
            commonNav += '<li class="common-wrap-li"><a href="http://m.jia.com/mall/allcategory/" tjjj="wap.nav.category"><i class="icon category"></i>分类</a></li>';
            commonNav += '<li class="common-wrap-li"><a href="http://m.jia.com/member/myorder/" tjjj="wap.nav.order"><i class="icon order"></i>我的订单</a></li>';
        }
        commonNav += '<li class="common-wrap-li"><a href="http://m.jia.com/tuku/" tjjj="wap.nav.tuku"><i class="icon tuku"></i>看效果图</a></li>';
        commonNav += '<li class="common-wrap-li"><a href="http://m.jia.com/zx/page/ysbj/' + cityNames + '/" tjjj="wap.nav.zx"><i class="icon zx"></i>装修报价</a></li>';
        commonNav += '<li class="common-wrap-li"><a href="http://m.jia.com/zx/sheji/' + cityNames + '/" tjjj="wap.nav.design"><i class="icon zx_design"></i>户型设计</a></li>';
        if (channel != "mall") {
            commonNav += '<li class="common-wrap-li" id="zzfu"><a href="http://m.jia.com/weixin/ztjzg/" tjjj="wap.nav.zz"><i class="icon zz"></i>整装服务</a></li>';
            commonNav += '<li class="common-wrap-li"><a href="http://m.jia.com/mall/hd/ppr/" tjjj="wap.nav.brand"><i class="icon brand"></i>品牌特卖</a></li>';
        }
        commonNav += '<li class="common-wrap-li" id="nav_tg"><a href="http://m.jia.com/tg/' + cityNames + '" tjjj="wap.nav.tg" ><i class="icon tuangou"></i>现场团购</a></li>';
        commonNav += '<li class="common-wrap-li"><a href="http://m.jia.com/zixun/" tjjj="wap.nav.learn_zx"><i class="icon learn_zx"></i>学装修</a></li>';
        commonNav += '</ul>';
        if (navObject.linkArr && navObject.linkArr.length > 0) {
            commonNav += '<h3 class="more-title"><span class="more-text">' + navObject.title + '</span></h3>';
            commonNav += '<ul class="hot_zixun clearfix">';
            for (var i = 0; i < navObject.linkArr.length; i++) {
                commonNav += '<li><a href="' + navObject.linkArr[i] + '">' + navObject.txtArr[i] + '</a></li>';
            }
            commonNav += '</ul>';
        }
        commonNav += '</section>';
        commonNav += '</div>';
        $('body').append(commonNav);
        if (cityNames != "shanghai") {//除上海站外，其它分站不显示“整装服务”入口
            $("#zzfu").hide();
        }
        if (cityObj.indexOf(cityNames) > -1) {
            $("#nav_tg").show();
        } else {
            $("#nav_tg").hide();
        }
        if(window.location.pathname.indexOf("/mall/item/") == 0&&CookiesInfo.get("from_zdh")){
            $(".navs-wrap .cart").parents(".common-wrap-li").remove();
        }
    }

    function commonNavHandle(){
        if (window.location.pathname.indexOf("/ask") == 0) {//问答
            commonNavFn("ask");
        } else if (window.location.pathname.indexOf("/tuku") == 0) {//图库
            commonNavFn("tuku");
        } else if (window.location.pathname.indexOf("/zixun") == 0) {//资讯
            commonNavFn("zixun");
        } else if (window.location.pathname.indexOf("/mall") == 0||window.location.pathname.indexOf("/cuxiao") == 0||window.location.pathname.indexOf("/jiaju") == 0||window.location.pathname.indexOf("/jzj") == 0||window.location.pathname.indexOf("/createOrder") == 0||window.location.pathname.indexOf("/brand") == 0) {//商城
            commonNavFn("mall");
        } else if (window.location.pathname.indexOf("/product") == 0) {//产品库
            commonNavFn("cpk");
        } else if (window.location.pathname.indexOf("/baike/xunxi") == 0) {//讯息
            commonNavFn("xunxi");
        } else if (window.location.pathname.indexOf("/baike") == 0) {//百科
            commonNavFn("baike");
        } else {
            commonNavFn();
        }
        $(".newHeader .new_menu").live("click", function () {
            $(".navs-wrap").addClass("searchin");
            $(".navs-wrap").removeClass("searchout");
            //$("html,body").css({"position": "fixed", "width": "100%"});
        });
        $(".navs-wrap,.navs-wrap .sub-navs-ul li").live("click", function () {
            $(".navs-wrap").removeClass("searchin");
            $(".navs-wrap").addClass("searchout");
            setTimeout(function () {
                $(".navs-wrap").removeClass("searchout");
            }, 300);
            //$("html,body").css({"position": "relative", "width": "100%"});
        });
    }

    //更改newFooter固定在底部
    $(function(){
        fixNewFootFn();
    });
}

function fixNewFootFn(){//newFooter固定在底部执行函数
    if(window.location.host != "h5.m.jia.com" && window.location.pathname.indexOf("/member/") == 0 ){
        var footHei = $(".newFooter").height();
        $("body").addClass("fixedCss").css("padding-bottom",footHei);
        $(".newFooter").addClass("fixedCss");
    }
}

//解决商品详情页&&购物车页面zepto&wapCommon在底部加载问题
if(typeof commonPhoneReg == "object"){
    navCommonFn();
}else{
    var navTimer = null;
    navTimer = setInterval(function () {
        try {
            urlHrefObject;
            clearInterval(navTimer);
            navTimer = null;
            navCommonFn();
        } catch (e) {

        }
    }, 500);
}

    if(window.location.href.indexOf("zixun.m.jia.com/zixun/")>-1){
       $(function(){
        $(".tt_fixed_btn").remove();
        $(".detail-bmtg").parent().hide();
        $(".tuijian_tip").css("top", "0");
     });
    }

