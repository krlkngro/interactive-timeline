//Create container for timeline content
const scriptContainer = document.querySelector(".timelineScript");
const timelineDiv = document.createElement("div");
timelineDiv.classList.add("timeline")

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

    section.append(addIcon(content.label))
    section.appendChild(contentDiv)
    eventBox.appendChild(section)
    event.appendChild(eventBox)

    return event
}

function addCustomContainer(className) {
    const newContainer = document.createElement("div");
    newContainer.classList.add(className);
    return newContainer
}

function addIcon(label) {
    const icon = document.createElement('i')
    icon.classList.add('icon')
    if (data.labelType === "TEXT") {
        const text = document.createElement('p')
        text.textContent = label
        icon.appendChild(text)
    }

    return icon
}
