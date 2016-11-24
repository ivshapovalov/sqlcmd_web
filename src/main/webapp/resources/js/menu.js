$(window).load(function(){
    $.get("menu/content", function(elements) {
        $("#loading").hide(300, function() {
            $('#menu script[template="row"]').tmpl(elements).appendTo('#menu .container');
        });
    });
});