/* global $,window */
var quizMaster = (function () {
    var name;
    var data;
    var loaded = false;
    var displayDom;
    var successCbAlias;

    function nextHandler(e) {
        e.preventDefault();

        var status = getUserStatus();

        //if we aren't on the intro, then we need to ensure you picked something
        if (status.question >= 0) {
            var checked = $("input[type=radio]:checked", displayDom);
            if (checked.length === 0) {
                //for now, an ugly alert
                window.alert("Â¡Por favor, responde la pregunta!");
                return;
            } else {
                status.answers[status.question] = checked.val();
            }
        }
        status.question++;
        storeUserStatus(status);
        displayQuiz(successCbAlias);
    }

    function displayQuiz(successCb) {

        //We copy this out so our event can use it later. This feels wrong
        successCbAlias = successCb;
        var current = getQuiz();
        var html;

        if (current.state === "introduction") {
            html = "<h2>IntroducciÃ³n</h2><p>" + current.introduction + "</p>" + nextButton();
            displayDom.html(html).trigger('create');
        } else if (current.state === "inprogress") {

            html = "<h2>" + current.question.question + "</h2><form><div data-role='fieldcontain'><fieldset data-role='controlgroup'>";
            for (var i = 0; i < current.question.answers.length; i++) {
                html += "<input type='radio' name='quizMasterAnswer' id='quizMasterAnswer_" + i + "' value='" + i + "'/><label for='quizMasterAnswer_" + i + "'>" + current.question.answers[i] + "</label>";
            }
            html += "</fieldset></div></form>" + nextButton();
            displayDom.html(html).trigger('create');
        } else if (current.state === "complete") {
            html = "<h2>Completo!!</h2><p>El examen ha finalizado. Has obtenido " + current.correct + " respuestas correctas de " + data.questions.length + ".</p>";
            displayDom.html(html).trigger('create');
            removeUserStatus();
            successCb(current);
        }


        //Remove previous if there...
        //Note - used click since folks will be demoing in the browser, use touchend instead
        displayDom.off("click", ".quizMasterNext", nextHandler);
        //Then restore it
        displayDom.on("click", ".quizMasterNext", nextHandler);

    }

    function getKey() {
        return "quizMaster_" + name;
    }

    function getQuestion(x) {
        return data.questions[x];
    }

    function getQuiz() {
        //Were we taking the quiz already?
        var status = getUserStatus();
        if (!status) {
            status = {question: -1, answers: []};
            storeUserStatus(status);
        }
        //If a quiz doesn't have an intro, just go right to the question
        if (status.question === -1 && !data.introduction) {
            status.question = 0;
            storeUserStatus(status);
        }

        var result = {
            currentQuestionNumber: status.question
        };

        if (status.question == -1) {
            result.state = "introduction";
            result.introduction = data.introduction;
        } else if (status.question < data.questions.length) {
            result.state = "inprogress";
            result.question = getQuestion(status.question);
        } else {
            result.state = "complete";
            result.correct = 0;
            for (var i = 0; i < data.questions.length; i++) {
                if (data.questions[i].correct == status.answers[i]) {
                    result.correct++;
                }
            }
        }
        return result;
    }

    function getUserStatus() {
        var existing = window.sessionStorage.getItem(getKey());
        if (existing) {
            return JSON.parse(existing);
        } else {
            return null;
        }
    }

    function nextButton() {
        return "<a href='' class='quizMasterNext' data-role='button'>Siguiente</a>";
    }

    function removeUserStatus(s) {
        window.sessionStorage.removeItem(getKey());
    }

    function storeUserStatus(s) {
        window.sessionStorage.setItem(getKey(), JSON.stringify(s));
    }

    return {
        execute: function (url, dom, cb) {
            //We cache the ajax load so we can do it only once 
            //if(!loaded) {

            $.get(url, function (res, code) {
                //Possibly do validation here to ensure basic stuff is present
                name = url;
                data = $.parseJSON(res);
                displayDom = $(dom);
                //console.dir(res);
                loaded = true;
                displayQuiz(cb);
            });

            /*} else {
             displayQuiz(cb);
             }*/
        }
    };
}());