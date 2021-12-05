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

    section.appendChild(addPackUnpackButton(contentDiv, content.packed))

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

function addPackUnpackButton(contentDiv, packed) {
    const packDiv = document.createElement("button");

    if (packed) {
        packDiv.textContent = '\u21D1'
        packDiv.classList.add("pack")
    } else {
        packDiv.textContent = '\u21D3'
        packDiv.classList.add("unpack")
    }

    packDiv.onclick = function () {
        if (packDiv.className === 'pack') {
            contentDiv.style.height = 45 + 'px'
            packDiv.className = 'unpack'
            packDiv.textContent = '\u21D3'
        } else if (packDiv.className === 'unpack') {
            if (contentDiv.classList.contains('overflow')) {
                contentDiv.style.height = contentHeight + 'px'
            } else {
                contentDiv.style.height = 'auto'
            }

            packDiv.className = 'pack'
            packDiv.textContent = '\u21D1'
            console.log(contentDiv.classList)
        }

    }

    return packDiv
}

function addPackUnpackAllButton() {
    const packDiv = document.createElement("button");
    packDiv.textContent = '\u21D1'
    packDiv.classList.add("pack")

    packDiv.onclick = function () {

        for (let sectionDiv of document.querySelectorAll(".timelineSection")) {

            if (packDiv.className === 'pack') {
                sectionDiv.querySelector(".timelineContent").style.height = 45 + 'px'
                sectionDiv.querySelector("button").textContent = '\u21D3'
                sectionDiv.querySelector("button").className = 'unpack'

            } else if (packDiv.className === 'unpack') {
                if (sectionDiv.querySelector(".timelineContent").classList.contains('overflow')) {
                    sectionDiv.querySelector(".timelineContent").style.height = contentHeight + 'px'
                } else {
                    sectionDiv.querySelector(".timelineContent").style.height = 'auto'
                }
                sectionDiv.querySelector("button").textContent = '\u21D1'
                sectionDiv.querySelector("button").className = 'pack'
            }

        }

        if (packDiv.className === 'pack') {
            packDiv.className = 'unpack'
            packDiv.textContent = '\u21D3'
        }
        else {
            packDiv.className = 'pack'
            packDiv.textContent = '\u21D1'
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