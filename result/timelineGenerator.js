
function dataToArray() {
    let timelineArray = []

    for (let i = 0; i < data.events.length; i++) {
        contentArray = [];
        contentToList(data.events[i].htmlContent)
        timelineArray.push(contentArray)
    }
    return timelineArray
}

function contentToList(content) {
    //If there is no content in the row then end with this event.
    if (content === '') return

    let closeIdx, textInRow, nextRow

    let firstStartIdx = content.indexOf('<')
    let firstCloseIdx = content.indexOf('>')

    let elem = content.substring(firstStartIdx + 1, firstCloseIdx).split(' ')[0];

    if (elem === 'img') {
        textInRow = ''
        nextRow =  content.substring(firstCloseIdx + 1)
    } else if (elem === 'br') {
        console.log(1)
    } else {
        closeIdx = content.indexOf('</' + elem + '>')
        textInRow = content.substring(firstCloseIdx + 1, closeIdx)
        nextRow =  content.substring(closeIdx + ('</' + elem + '>').length)
    }

    contentArray.push([elem, textInRow, extractParams(content.substring(firstStartIdx + elem.length + 1, firstCloseIdx))])

    contentToList(nextRow)
}

//Add to dict different parameters.
function extractParams(str) {
    let dict = {}

    const regex = /([a-z]*)="(.*?)"/g;
    let params = (str.match(regex))

    if (params) {
        for (let param of params) {
            let paramSplit = param.split("=")
            dict[paramSplit[0].replaceAll('"', '')] = paramSplit[1].replaceAll('"', '') //There are unnecessary quote marks
        }
    }
    return dict
}



//Create container for timeline content
const scriptContainer = document.querySelector(".timelineContainer");
const timelineDiv = document.createElement("div");
timelineDiv.classList.add("timeline")

//Data from dataToArray() is collected here.
let contentArray = [];
//Read data from data.js and put it into array.
let eventArray = dataToArray()

//Generate html
let left = true
for (const eventContent of eventArray) {
    timelineDiv.appendChild(addContainer(eventContent, left))
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

    for (const row of content) {
        //row[0] - elem; row[1] - text; row[2] - params
        let elem = row[0]
        let text = row[1]
        let params = row[2]

        switch (elem) {
            case 'p':
                newContent.appendChild(addP(text, params))
                break;
            case 'h1': ///h[1-6]/g.test(row[0]) returns boolean, if match
            case 'h2': case 'h3': case 'h4': case 'h5': case 'h6':
                newContent.appendChild(addH(elem, text))
                break;
            case 'img':
                newContent.appendChild(addImg(params))
                break;
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

function addP(text, params) {
    const newP = document.createElement("p")
    newP.textContent = text
    if (params.hasOwnProperty('style')) newP.style = params['style']
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

function addImg(params) {
    const newImg = document.createElement("img")

    if (params.hasOwnProperty('src')) newImg.src = params['src']
    if (params.hasOwnProperty('alt')) newImg.alt = params['alt']

    return newImg
}
