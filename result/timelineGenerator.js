//Create container for timeline content
const scriptContainer = document.querySelector(".timelineScript");
const timelineDiv = document.createElement("div");
timelineDiv.classList.add("timeline")

//Generate html
for (const eventContent of data.events) {
    if (eventContent.queueNr === 1) {
        timelineDiv.appendChild(addContainer(eventContent.htmlContent))
        timelineDiv.appendChild(addCustomContainer('timelineCenter-line'))
        timelineDiv.appendChild(addCustomContainer('empty'))
        continue
    }
    timelineDiv.appendChild(addContainer(eventContent.htmlContent))
}

//Add generated html to timeline div
scriptContainer.parentNode.insertBefore(timelineDiv, scriptContainer)


function addContainer(htmlText) {
    const event = document.createElement("div");
    event.classList.add('timelineEvent')

    const eventBox = document.createElement('div')
    eventBox.classList.add('timelineEventBox')

    const section = document.createElement('section')
    section.classList.add('timelineSection')



    const content = document.createElement("div");
    content.classList.add("timelineContent")
    content.insertAdjacentHTML('beforeend', htmlText);

    section.append(addIcon())
    section.appendChild(content)
    eventBox.appendChild(section)
    event.appendChild(eventBox)

    return event
}

function addCustomContainer(className) {
    const newContainer = document.createElement("div");
    newContainer.classList.add(className);
    return newContainer
}

function addIcon() {
    const icon = document.createElement('i')
    icon.classList.add('icon')
    const text = document.createElement('p')
    text.textContent = '20.10.21 autumn'

    icon.appendChild(text)
    return icon
}
