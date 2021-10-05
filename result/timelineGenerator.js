import { data } from '/data.js'
import * as cnt from './contentGenerator.js'

let contentArray = [];

function contentToList(content) {
    //If there is no
    if (content === '') return

    let params, closeIdx, textInRow, nextRow

    let firstStartIdx = content.indexOf('<')
    let firstCloseIdx = content.indexOf('>')

    let elem = content.substring(firstStartIdx + 1, firstCloseIdx).split(' ')[0];

    if (elem === 'img') {
        textInRow = ''
        nextRow =  content.substring(firstCloseIdx + 1)
    } else {
        closeIdx = content.indexOf('</' + elem + '>')
        textInRow = content.substring(firstCloseIdx + 1, closeIdx)
        nextRow =  content.substring(closeIdx + ('</' + elem + '>').length)
    }

    params = content.substring(firstStartIdx + elem.length + 1, firstCloseIdx)
    contentArray.push([elem, textInRow, params])

    contentToList(nextRow)
}

function dataToArray() {
    let timelineArray = []

    for (let i = 0; i < data.events.length; i++) {
        contentArray = [];
        contentToList(data.events[i].htmlContent)
        timelineArray.push(contentArray)
    }
    return timelineArray
}

//Create container for timeline content
const scriptContainer = document.querySelector(".timelineContainer");
const timelineDiv = document.createElement("div");
timelineDiv.classList.add("timeline")

//Read data from data.js and put it into array.
let eventArray = dataToArray()

//Generate html
let left = true
for (const eventContent of eventArray) {
    timelineDiv.appendChild(cnt.addContainer(eventContent, left))
    left = !left
}

scriptContainer.parentNode.insertBefore(timelineDiv, scriptContainer)


