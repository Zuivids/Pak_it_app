$(document).ready(function () {
    $("#loginForm").on("submit", function (e) {
        e.preventDefault();

        const csrfHeader = $("meta[name='_csrf_header']").attr("content");
        const csrfValue = $("meta[name='_csrf']").attr("content");

        const request = {
            username: $("#username").val() || null,
            password: $("#password").val() || null,
            totp: $("#totp").val() || null
        };

        fetch("/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                [csrfHeader]: csrfValue
            },
            body: JSON.stringify(request),
        })
        .then(response => {
            if (!response.ok) {
                response.json().then(handleErrorResponse);
            } else {
                alert("Pieslēgšanās veiksmīga!");
                window.location.href = "/home";
            }
        })
        .catch(error => console.log(error));

        const handleErrorResponse = (errorResponse) => {
            $(".error-message").text("");
            if (errorResponse.fieldErrors) {
                handleFieldErrors(errorResponse.fieldErrors);
            } else {
                handleErrorMessage(errorResponse.errorMessage);
            }
        };

        const handleFieldErrors = (fieldErrors) => {
            for (let field in fieldErrors) {
                $(`#${field}-error`).text(fieldErrors[field]);
            }
        };

        const handleErrorMessage = (errorMessage) => {
            $("#errorMessage").text(errorMessage);
        };
    });
});
