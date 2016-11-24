$(window).load(function(){
    $.get("databases/content", function(elements) {
        $("#loading").hide(300, function() {
            $('#databases row-template').tmpl(elements).appendTo('#databases .container');
        });
    });
});