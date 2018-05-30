let tabBtn = document.querySelectorAll('.tabs-container button');
let tabPanel = document.querySelectorAll('.tab-panel');

function showPanel(panelIndex, colorCode) {
    tabBtn.forEach(function (node) {
        node.style.backgroundColor = "";
        node.style.color = "";
    });
    tabBtn[panelIndex].style.backgroundColor = colorCode;
    tabBtn[panelIndex].style.color = 'white';

    tabPanel.forEach(function (node) {
        node.style.display = "none";
    });
    tabPanel[panelIndex].style.display = 'block';
    tabPanel[panelIndex].style.backgroundColor = colorCode;
}
showPanel(0, 'red');