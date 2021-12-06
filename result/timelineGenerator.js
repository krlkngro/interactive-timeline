//Create container for timeline content
const scriptContainer = document.querySelector(".timelineScript");
const timelineDiv = document.createElement("div");
timelineDiv.classList.add("timeline")

//Necessary to close modal box by clicking outside of it.
let modalTemp;
let boolModalTemp;

const height = data.eventSpace
const contentHeight = data.eventSpace * 2 - 50
timelineDiv.style.gridAutoRows = height + 'px'


//Generate html
for (const eventContent of data.events) {
    if (eventContent.queueNr === 1) {
        timelineDiv.appendChild(addContainer(eventContent))
        timelineDiv.appendChild(addCustomContainer('timelineCenter-line'))
        timelineDiv.appendChild(addCustomContainer('empty'))
        continue
    }
    timelineDiv.appendChild(addContainer(eventContent))
}

//Add pack/unpack all cells button
timelineDiv.appendChild(addPackUnpackAllButton())

//Add generated html to timeline div
scriptContainer.parentNode.insertBefore(timelineDiv, scriptContainer)

//Add modal box for pictures
addModalBoxImg()

//To get size of element, the element need to be rendered in the Dom.
//Add button and hide overflowing content.
handleOverflowingContent()


/*document.querySelector("img").addEventListener("load", function ()
{
    console.log(document.querySelector("img").clientWidth)
})*/


//--------FUNCTIONS--------
function addContainer(content) {
    const event = document.createElement("div");
    event.classList.add('timelineEvent')

    const eventBox = document.createElement('div')
    eventBox.classList.add('timelineEventBox')

    const section = document.createElement('section')
    section.classList.add('timelineSection')


    const contentDiv = document.createElement("div");
    contentDiv.classList.add("timelineContent")
    contentDiv.insertAdjacentHTML('beforeend', content.htmlContent);

    section.appendChild(addIcon(content.label))

    section.appendChild(addPackUnpackButton(contentDiv, content.packed, content.htmlContent))

    section.appendChild(contentDiv)

    eventBox.appendChild(section)
    event.appendChild(eventBox)

    return event
}

//Adding  center line
function addCustomContainer(className) {
    const newContainer = document.createElement("div");
    newContainer.classList.add(className);
    return newContainer
}

function addIcon(label) {
    const icon = document.createElement('div')

    if (data.labelType.toUpperCase() === "TEXT" && label.length > 0) {
        const text = document.createElement('p')
        icon.classList.add('textIcon')
        text.textContent = label
        icon.appendChild(text)
    }
    else if (data.labelType.toUpperCase() === "LINE") {
        icon.classList.add('lineIcon')
    }
    else if (data.labelType.toUpperCase() === "DOT" || label.length === 0) {
        icon.classList.add('dotIcon')
    }

    return icon
}

function addPackUnpackButton(contentDiv, packed, htmlContent) {
    let packDiv = document.createElement("button");

    if (packed) {
        packDiv.textContent = '\u21D3'
        packDiv.classList.add("unpack")
    } else {
        packDiv.textContent = '\u21D1'
        packDiv.classList.add("pack")
    }

    packDiv.onclick = function () {
        packDiv = changePackUnpackStyle(packDiv, contentDiv, htmlContent)
    }

    return packDiv
}

function addPackUnpackAllButton() {
    let packDiv = document.createElement("button");
    packDiv.textContent = '\u21D1'
    packDiv.classList.add("pack")

    packDiv.onclick = function () {

        for (let [index, sectionDiv] of document.querySelectorAll(".timelineSection").entries()) {
            changePackUnpackStyle(sectionDiv.querySelector('button'), sectionDiv.querySelector(".timelineContent"), data.events[index].htmlContent, packDiv.className)
        }

        if (packDiv.className === 'pack') {
            packDiv.textContent = '\u21D3'
            packDiv.className = 'unpack'
        } else if (packDiv.className === 'unpack') {
            packDiv.textContent = '\u21D1'
            packDiv.className = 'pack'
        }
    }

    return packDiv
}

function changePackUnpackStyle(packDiv, contentDiv, htmlContent, className = packDiv.className) {

    if (className === 'pack') {
        contentDiv.style.height = 60 + 'px'
        packDiv.textContent = '\u21D3'
        packDiv.className = 'unpack'


        for (let child of contentDiv.children) {

            if (child.firstElementChild === null || child.firstElementChild.tagName !== 'img') {
                const text = contentDiv.firstElementChild
                text.outerHTML = '<h1>' + text.innerHTML + '</h1>'
                contentDiv.firstElementChild.classList.add('packedContent')
                if (contentDiv.parentElement.querySelector(".timelineReadMore")) {
                    contentDiv.parentElement.querySelector(".timelineReadMore").style.display = 'none'
                }
                break
            }
        }


    } else if (className === 'unpack') {
        if (contentDiv.classList.contains('overflow')) {
            contentDiv.style.height = contentHeight + 'px'
        } else {
            contentDiv.style.height = 'auto'
        }
        packDiv.textContent = '\u21D1'
        packDiv.className = 'pack'

        contentDiv.innerHTML = ""
        contentDiv.insertAdjacentHTML('beforeend', htmlContent)

        if (contentDiv.parentElement.querySelector(".timelineReadMore")) {
            contentDiv.parentElement.querySelector(".timelineReadMore").style.display = 'block'
        }
    }

    return packDiv
}

function addModalBoxImg() {
    for (let img of document.querySelectorAll("img")) {

        let modalBox = addModalBox(img, true)

        img.onclick = function () {
            modalBox.style.display = 'block'
            modalTemp = modalBox
            boolModalTemp = false
        }

        img.parentNode.insertBefore(modalBox, img)

    }
}


function handleOverflowingContent() {
    for (let [index, contentDiv] of document.querySelectorAll(".timelineContent").entries()) {
        if (contentDiv.scrollHeight > contentHeight) {
            //console.log(contentDiv.scrollHeight)
            contentDiv.parentNode.insertBefore(addReadMore(data.events[index].htmlContent), contentDiv.nextSibling)
            contentDiv.style.height = contentHeight + 'px'
            contentDiv.parentElement.parentElement.style.height = (contentHeight + 125) + 'px'
            contentDiv.classList.add('overflow');
        }
    }
}

function addReadMore(content) {
    const readMore = document.createElement('div')
    readMore.classList.add('timelineReadMore')

    const modalBoxButton = document.createElement('a')
    modalBoxButton.textContent = data.readMore
    modalBoxButton.id = 'event' + content.queueNr

    const modalBox = addModalBox(content)

    modalBoxButton.onclick = function () {
        modalBox.style.display = 'block'
        modalTemp = modalBox
        boolModalTemp = false
    }

    readMore.appendChild(modalBoxButton)
    readMore.appendChild(modalBox)
    return readMore
}

function addModalBox(content, img) {

    const modal = document.createElement('div')
    modal.classList.add('timelineModal')

    const modalContentDiv = document.createElement('div')
    modalContentDiv.classList.add('timelineModalContent')

    const span =  document.createElement('a')
    span.classList.add('closeX')
    span.textContent = 'X'

    let modalContent
    if (img) {
        modalContent =  document.createElement('img')
        modalContent.src = content.src
    } else {
        modalContent =  document.createElement('div')
        modalContent.insertAdjacentHTML('beforeend', content);
    }

    span.onclick = function() {
        modal.style.display = "none";
    }

    //Clicked outside modal box
    window.onclick = function(event) {
        if (!event.target.closest('.timelineModalContent') && boolModalTemp) {
            modalTemp.style.display = "none"
        }
        boolModalTemp=true
    }

    modalContentDiv.appendChild(span)
    modalContentDiv.appendChild(modalContent)
    modal.appendChild(modalContentDiv)
    return modal
}