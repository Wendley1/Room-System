let codeForm = document.getElementById("code-form");
let warningText = document.getElementById("warning-text");

codeForm.addEventListener('submit', function (event) {
    event.preventDefault();
    
    let code = document.getElementById("code").value.toLocaleUpperCase();

    $.ajax({
        url: 'api/sala/existe',
        type: 'POST',
        contentType: 'text/plain',
        data: code,
        success: function (data) {
            if(data == 0){
                warningText.textContent = 'Sala não encontrada';
                return;
            }
            
            window.location.href = ("/sala/entrar/" + code)
        },
        error: function () {
            console.log("erro");
        }
    });
});
