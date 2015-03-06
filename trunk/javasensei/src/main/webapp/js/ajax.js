function realizarAjax(url, data, method, done, fail) {
    var configAjax = {};
    configAjax.url = url;

    if (data)
        configAjax.data = data;

    if (!method)
        configAjax.type = "GET";

    if (!done)
        done = function () {};

    if (!fail)
        fail = function () {};

    $.ajax(configAjax)
            .done(done)
            .fail(fail);
}