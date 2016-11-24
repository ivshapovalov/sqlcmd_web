$(window).load(function(){
    $.get("tables/content", function(elements) {
        $("#loading").hide(300, function() {
            $('#tables row-template').tmpl(elements).appendTo('#tables .container');
        });
    });
});