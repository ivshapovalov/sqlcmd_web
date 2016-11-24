$(window).load(function(){
    $.get("databases/content", function(elements) {
        $("#loading").hide(300, function() {
            $('#databases script[template="row"]').tmpl(elements).appendTo('#databases .container');
        });
    });
});