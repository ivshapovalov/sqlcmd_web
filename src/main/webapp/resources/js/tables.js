$(window).load(function(){
    $.get("tables/content", function(elements) {
        $("#loading").hide(300, function() {
            $('#tables script[template="row"]').tmpl(elements).appendTo('#tables .container');
        });
    });
});