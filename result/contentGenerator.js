export function addContainer(content ,left) {
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

export function addTitle(text) {
    const newH = document.createElement("h2")
    newH.textContent = text
    return newH
}

export function addH(elem, text) {
    const newP = document.createElement(elem)
    newP.textContent = text
    return newP
}

export function addP(text) {
    const newP = document.createElement("p")
    newP.textContent = text
    return newP
}

export function addVideo(src, local) {
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

export function addImg(src, alt) {
    const newImg = document.createElement("img")
    newImg.src = src
    newImg.alt = alt
    return newImg
}