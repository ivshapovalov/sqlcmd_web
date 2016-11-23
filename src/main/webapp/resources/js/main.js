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
        window.location = ctx + '/connect' + '?fromPage=' + escape('/main#/' + fromPage);
    }

    var show = function(selector) {
        var component = $(selector);
        component.find('.container').children().not(':first').remove();
        component.show();
    }

    var initList = function() {
        isConnected("list", function() {
            show('#list');

            $.get(ctx + "/list/content", function(elements) {
                $("#loading").hide(300, function() {
                    $('#list script').tmpl(elements).appendTo('#list .container');
                });
            });
        });
    };

    var initTables = function(tableName) {
        isConnected("tables/" + tableName, function() {
            show('#tables');

            $.get(ctx + '/tables/' + tableName + '/content', function(elements) {
                $('#loading').hide(300, function() {
                    $('#tables script').tmpl(elements).appendTo('#tables .container');
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
        $('#list').hide();
        $('#tables').hide();
        $('#menu').hide();
        $('#help').hide();
    }

    var loadPage = function(data) {
        hideAllScreens();
        $("#loading").show();

        var page = data[0];
        if (page == 'list') {
           initList();
        } else if (page == 'tables') {
           initTables(data[1]);
        } else if (page == 'menu') {
           initMenu();
        } else if (page == 'help') {
           initHelp();
        } else {
           window.location.hash = "/menu";
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
