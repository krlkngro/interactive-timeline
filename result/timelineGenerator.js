//Create container for timeline content
const scriptContainer = document.querySelector(".timelineContainer");
const timelineDiv = document.createElement("div");
timelineDiv.classList.add("timeline")

//Generate html
let left = true
for (const eventContent of data.events) {
    timelineDiv.appendChild(addContainer(eventContent.htmlContent, left))
    left = !left
}

//Add generated html to timeline div
scriptContainer.parentNode.insertBefore(timelineDiv, scriptContainer)


function addContainer(content ,left) {
    const newContainer = document.createElement("div");

    if (left) {
        newContainer.classList.add("container", "left")
    } else {
        newContainer.classList.add("container", "right")
    }

    const newContent = document.createElement("div");
    newContent.classList.add("content")
    newContent.insertAdjacentHTML('beforeend', content);
    newContainer.appendChild(newContent)

    return newContainer
}