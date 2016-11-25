function init(ctx) {

    var fromPage = null;

    var showFromPage = function () {
        window.location.hash = fromPage;
        fromPage = null;
    }

    var isConnected = function (url, onConnected) {
        $.get(ctx + "/connected", function (userName) {
            if (userName == "") {
                fromPage = url;
                window.location.hash = '/connect';
            } else {
                if (!!onConnected) {
                    onConnected(userName);
                }
            }
        });
    }

    var show = function (selector) {
        var component = $(selector);
        component.find('.container').children().not(':first').remove();
        component.show();
    }

    var initConnect = function (databaseName) {
        if (databaseName == null) {
            $("#databaseName").val("sqlcmd");
        } else {
            $("#databaseName").val(databaseName);
        }
        $("#userName").val("postgres");
        $("#password").val("postgres");
        $('#error').hide();
        $('#error').html("");
        $("#loading").hide(300, function () {
            $('#connecting-form').show();
        });
    };

    var disconnect = function () {
        isConnected("", function () {
            $.get(ctx + "/disconnect", function (elements) {
                showFromPage();
            });
        });
    };

    var dropDatabase = function (databaseName) {
        isConnected("databases", function () {
            $.ajax({
                url: ctx + "/dropdatabase/"+databaseName,
                type: 'GET',
                success: function (message) {
                    if (message == "" || message == null) {
                        showFromPage();
                    } else {
                        $('#error').html(message);
                        $('#error').show();
                    }
                }
            });
        });
    };

    var initActions = function () {
        isConnected("actions", function () {
            show('#actions');
            $.get(ctx + "/actions/content", function (elements) {
                $("#loading").hide(300, function () {
                    $('#actions script').tmpl(elements).appendTo('#actions .container');
                });
            });
        });
    };

    var initTables = function () {
        isConnected("tables", function () {
            show('#tables');
            $.get(ctx + "/tables/content", function (elements) {
                $("#loading").hide(300, function () {
                    $('#tables script').tmpl(elements).appendTo('#tables .container');
                });
            });
        });
    };

    var initDatabases = function () {
        isConnected("databases", function () {
            show('#databases');
            $.get(ctx + "/databases/content", function (elements) {
                $("#loading").hide(300, function () {
                    $('#databases script').tmpl(elements).appendTo('#databases .container');
                });
            });
        });
    };

    var initTable = function (tableName) {
        isConnected("table/" + tableName, function () {
            show('#table');
            $.get(ctx + '/table/' + tableName + '/content', function (elements) {
                $('#loading').hide(300, function () {
                    $('#table script').tmpl(elements).appendTo('#table .container');
                });
            });
        });
    };

    var initDatabase = function (databaseName) {
        isConnected("database/" + databaseName, function () {
            show('#database');
            $.get(ctx + '/database/' + databaseName + '/content', function (elements) {
                $('#loading').hide(300, function () {
                    $('#database script').tmpl(elements).appendTo('#database .container');
                });
            });
        });
    };

    var initRow = function (tableName, id) {
        isConnected("row/" + tableName + "/" + id, function () {
            show('#row');
            $.get(ctx + '/row/' + tableName + '/' + id + '/content', function (elements) {
                $('#loading').hide(300, function () {
                    $('#row script').tmpl(elements).appendTo('#row .container');
                });
            });
        });
    };

    var initMenu = function () {
        show('#menu');
        $.get(ctx + "/menu/content", function (elements) {
            $("#loading").hide(300, function () {
                $('#menu script').tmpl(elements).appendTo('#menu .container');
            });
        });
    };

    var initHelp = function () {
        show('#help');
        $.get(ctx + "/help/content", function (elements) {
            $("#loading").hide(300, function () {
                $('#help script').tmpl(elements).appendTo('#help .container');
            });
        });
    };

    var hideAllScreens = function () {
        $('#list').hide();
        $('#tables').hide();
        $('#menu').hide();
        $('#help').hide();
        $('#connecting-form').hide();
        $('#actions').hide();
        $('#table').hide();
        $('#row').hide();
        $('#databases').hide();
        $('#database').hide();

    }

    var loadPage = function (data) {
        hideAllScreens();
        $("#loading").show();

        var page = data[0];

        if (page == 'table') {
            initTable(data[1]);
        } else if (page == 'database') {
            initDatabase(data[1]);
        } else if (page == 'row') {
            initRow(data[1], data[2]);
        } else if (page == 'tables') {
            initTables();
        } else if (page == 'menu') {
            initMenu();
        } else if (page == 'help') {
            initHelp();
        } else if (page == 'databases') {
            initDatabases();
        } else if (page == 'disconnect') {
            disconnect();
        } else if (page == 'dropdatabase') {
            dropDatabase(data[1]);
        } else if (page == 'connect') {
            if (data.length > 1) {
                initConnect(data[1]);
            } else {
                initConnect(null);
            }
        } else if (page == 'actions') {
            initActions();
        } else {
            window.location.hash = "menu";
        }
    }

    var load = function () {
        var hash = window.location.hash.substring(1);
        var parts = hash.split('/');
        if (parts[0] == '') {
            parts.shift();
        }
        loadPage(parts);
    }

    $(window).bind('hashchange', function (event) {
        load();
    });

    $('#connect').click(function () {
        var connection = {};
        connection.databaseName = $("#databaseName").val();
        connection.userName = $("#userName").val();
        connection.password = $("#password").val();

        $.ajax({
            url: ctx + "/connect",
            data: connection,
            type: 'PUT',
            success: function (message) {
                if (message == "" || message == null) {
                    showFromPage();
                } else {
                    $('#error').html(message);
                    $('#error').show();
                }
            }
        });
    });

    load();
}

