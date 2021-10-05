//import { data } from '/data.js'
//import * as cnt from './contentGenerator.js'

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
    timelineDiv.appendChild(addContainer(eventContent, left))
    left = !left
}

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

    //console.log(content)

    for (const row of content) {

        switch (row[0]) {
            case 'p':
                newContent.appendChild(addP(row[1]))
                break;
            case row[0].match(/h[1-6]/g):
                newContent.appendChild(addH(row[0]))
        }
    }

    newContainer.appendChild(newContent)
    return newContainer
}

function addTitle(text) {
    const newH = document.createElement("h2")
    newH.textContent = text
    return newH
}

 function addH(elem, text) {
    const newP = document.createElement(elem)
    newP.textContent = text
    return newP
}

function addP(text) {
    const newP = document.createElement("p")
    newP.textContent = text
    return newP
}

 function addVideo(src, local) {
    let newVideo;
    if (local) {
        newVideo = document.createElement("video")
        newVideo.src = src
        newVideo.controls = true
    } else {
        newVideo = document.createElement("iframe")
        newVideo.src = src
    }

    return newVideo
}

 function addImg(src, alt) {
    const newImg = document.createElement("img")
    newImg.src = src
    newImg.alt = alt
    return newImg
}
