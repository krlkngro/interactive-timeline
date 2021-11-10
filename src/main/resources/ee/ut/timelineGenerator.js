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


//Add generated html to timeline div
scriptContainer.parentNode.insertBefore(timelineDiv, scriptContainer)


//To get size of element, the element need to be rendered in the Dom.
//Add button and hide overflowing content.
handleOverflowingContent()


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
    icon.classList.add('icon')
    if (data.labelType === "TEXT") {
        const text = document.createElement('p')
        text.textContent = label
        icon.appendChild(text)
    }

    return icon
}


function handleOverflowingContent() {
    for (let [index, contentDiv] of document.querySelectorAll(".timelineContent").entries()) {
        if (contentDiv.scrollHeight > contentHeight) {
            console.log(contentDiv.scrollHeight)
            contentDiv.parentNode.insertBefore(addReadMore(data.events[index]), contentDiv.nextSibling)
            contentDiv.style.height = contentHeight + 'px'
            contentDiv.parentElement.parentElement.style.height = (contentHeight + 125) + 'px'
            contentDiv.style.overflow = "hidden"
        }
    }
}

function addReadMore(content) {
    const readMore = document.createElement('div')
    readMore.classList.add('timelineReadMore')

    const modalBoxButton = document.createElement('button')
    modalBoxButton.textContent = 'Read more ...'
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

function addModalBox(content) {
    const modal = document.createElement('div')
    modal.classList.add('timelineModal')

    const modalContent = document.createElement('div')
    modalContent.classList.add('timelineModalContent')

    const span =  document.createElement('button')
    span.textContent = 'X'

    const text =  document.createElement('p')
    text.insertAdjacentHTML('beforeend', content.htmlContent);

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

    modalContent.appendChild(span)
    modalContent.appendChild(text)
    modal.appendChild(modalContent)
    return modal
}