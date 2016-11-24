function init(ctx) {

    var isConnected = function(fromPage, onConnected) {
        $.get(ctx + "/connected", function(isConnected) {
            if (isConnected) {
                if (!!onConnected) {
                    onConnected();
                }
            } else {
                gotoConnectPage(fromPage);
            }
        });
    }

    var gotoConnectPage = function(fromPage) {
        window.location = ctx + '/connect' + '?fromPage=' + escape('/main#' + fromPage);
    }

    var show = function(selector) {
        var component = $(selector);
        component.find('.container').children().not(':first').remove();
        component.show();
    }

    var initTables = function() {
        isConnected("tables", function() {
            show('#tables');
            $.get(ctx + "/tables/content", function(elements) {
                $("#loading").hide(300, function() {
                    $('#tables script').tmpl(elements).appendTo('#tables .container');
                });
            });
        });
    };

    var initDatabases = function() {
        isConnected("databases", function() {
            show('#databases');
            $.get(ctx + "/databases/content", function(elements) {
                $("#loading").hide(300, function() {
                    $('#databases script').tmpl(elements).appendTo('#databases .container');
                });
            });
        });
    };

    var initTable = function(tableName) {
        isConnected("table/" + tableName, function() {
            show('#table');
            $.get(ctx + '/table/' + tableName + '/content', function(elements) {
                $('#loading').hide(300, function() {
                    $('#table script').tmpl(elements).appendTo('#table .container');
                });
            });
        });
    };

    var initMenu = function() {
        show('#menu');
        $.get(ctx + "/menu/content", function(elements) {
            $("#loading").hide(300, function() {
                $('#menu script').tmpl(elements).appendTo('#menu .container');
            });
        });
    };

    var initHelp = function() {
        show('#help');
        $.get(ctx + "/help/content", function(elements) {
            $("#loading").hide(300, function() {
                $('#help script').tmpl(elements).appendTo('#help .container');
            });
        });
    };

    var hideAllScreens = function() {
        $('#tables').hide();
        $('#table').hide();
        $('#menu').hide();
        $('#help').hide();
        $('#databases').hide();
    }

    var loadPage = function(data) {
        hideAllScreens();
        $("#loading").show();

        var page = data[0];
        if (page == 'table') {
           initTable(data[1]);
        } else if (page == 'tables') {
           initTables();
        } else if (page == 'menu') {
           initMenu();
        } else if (page == 'help') {
           initHelp();
        } else if (page == 'databases') {
            initDatabases();
        } else {
           window.location.hash = "menu";
        }
    }

    var load = function() {
        var hash = window.location.hash.substring(1);
        var parts = hash.split('/');
        if (parts[0] == '') {
            parts.shift();
        }
        loadPage(parts);
    }

    $(window).bind('hashchange', function(event) {
        load();
    });

    load();
}
