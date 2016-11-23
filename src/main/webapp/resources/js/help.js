
$(window).load(function(){
    $.get("help/content", function(elements) {
        $("#loading").hide(300, function() {
            var container = $("#commands");
            $('#descriptionRow').tmpl(elements).appendTo('#commands');
        });
    });
});