function initRows(ctx) {
    var url = window.location.href;
    var parts = url.split('/');
    var tableName = parts[parts.length - 1];

    $.get(ctx + '/table/' + tableName + '/content', function(elements) {
        $('#loading').hide(300, function() {
            $('#rows script[template="row"]').tmpl(elements).appendTo('#rows .container');
        });
    });
}