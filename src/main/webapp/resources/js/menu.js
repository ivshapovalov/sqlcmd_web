$(window).load(function(){
    $.get("menu/content", function(elements) {
        $("#loading").hide(300, function() {
            $('#menu row-template').tmpl(elements).appendTo('#menu .container');
        });
    });
});