$(window).load(function(){
    $.get("help/content", function(elements) {
        $("#loading").slideUp(300, function () {
            var container = $("#commands");
            for (var index in elements) {
                var element = elements[index];
                container.append('<tr><td>' + element.command + '</td><td>' + element.description + '</td></tr>');
            }
        })
    });
});